package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.DTO.EstabilidadeClinicaDTO;
import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.EstabilidadeClinica;
import br.com.vittasync.vittasync.Repository.EstabilidadeClinicaRepository;
import br.com.vittasync.vittasync.Repository.ContatoEmergenciaRepository;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class EstabilidadeClinicaService {

    private final EstabilidadeClinicaRepository estabilidadeClinicaRepository;
    private final ContatoEmergenciaRepository contatoEmergenciaRepository;
    private final NotificacaoService notificacaoService;
    private final SinaisVitaisRepository sinaisVitaisRepository;
    private final HabitosRepository habitosRepository;

    private static final Logger logger = LoggerFactory.getLogger(EstabilidadeClinicaService.class);

    private boolean usarProcedure = true;

    private static final int minimoRegistros = 3;
    private static final int minimoFatoresIndiceGeral = 3;
    private static final int sonoMinimoSaudavel = 7;
    private static final int sonoMaximoSaudavel = 9;
    private static final int exercicioMinimoSaudavel = 60;
    private static final int semExercicioCriticoDias = 10;

    public EstabilidadeClinicaService(
            EstabilidadeClinicaRepository estabilidadeClinicaRepository,
            ContatoEmergenciaRepository contatoEmergenciaRepository,
            NotificacaoService notificacaoService,
            SinaisVitaisRepository sinaisVitaisRepository,
            HabitosRepository habitosRepository
    ) {
        this.estabilidadeClinicaRepository = estabilidadeClinicaRepository;
        this.contatoEmergenciaRepository = contatoEmergenciaRepository;
        this.notificacaoService = notificacaoService;
        this.sinaisVitaisRepository = sinaisVitaisRepository;
        this.habitosRepository = habitosRepository;
    }

    // índices do paciente com todos os sinais vitais e os hábitos dos últimos 30 dias
    public List<EstabilidadeClinicaDTO> consultarIndices(Integer pacienteId) {
        List<SinaisVitais> sinais = sinaisVitaisRepository.findByPacienteIdOrderByDataRegistroAsc(pacienteId);
        List<Habitos> habitos = habitosRepository.findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(
                pacienteId,
                LocalDate.now().minusDays(30),
                LocalDate.now()
        );

        return calcularIndices(pacienteId, sinais, habitos);
    }

    public void verificarMudancaEstabilidade(Integer pacienteId) {
        List<EstabilidadeClinicaDTO> indices = null;

        if (usarProcedure) {
            try {
                indices = calcularIndicesPorMedias(estabilidadeClinicaRepository.buscarMediasEstabilidade(pacienteId).get(0));
            } catch (RuntimeException e) {
                usarProcedure = false;
                logger.warn("Procedure sp_medias_estabilidade indisponível, os índices serão calculados no Java");
            }
        }

        if (indices == null) {
            indices = calcularIndices(pacienteId,
                    sinaisVitaisRepository.findByPacienteId(pacienteId),
                    habitosRepository.findByPacienteId(pacienteId));
        }

        registrarMudanca(pacienteId, indices);
    }

    public void verificarMudancaEstabilidade(Integer pacienteId,
                                             List<SinaisVitais> sinais,
                                             List<Habitos> habitos) {
        registrarMudanca(pacienteId, calcularIndices(pacienteId, sinais, habitos));
    }

    private void registrarMudanca(Integer pacienteId, List<EstabilidadeClinicaDTO> indices) {
        EstabilidadeClinicaDTO geralNovo = indices.stream()
                .filter(i -> "geral".equals(i.getTipo()))
                .findFirst()
                .orElse(new EstabilidadeClinicaDTO("geral", null, "n/a", 1.0, LocalDateTime.now()));

        String categoriaAnterior = estabilidadeClinicaRepository.findUltimaCategoriaGeral(pacienteId);
        String categoriaNova = geralNovo.getCategoria();

        if (categoriaAnterior == null || !categoriaAnterior.equals(categoriaNova)) {
            dispararAlertasDetalhados(pacienteId, indices, geralNovo);

            EstabilidadeClinica registro = new EstabilidadeClinica();
            registro.setPacienteId(pacienteId);
            registro.setTipo("geral");
            registro.setIndice(geralNovo.getIndice());
            registro.setCategoria(categoriaNova);
            registro.setDataCalculo(LocalDateTime.now());
            estabilidadeClinicaRepository.save(registro);
        }
    }

    /**
     * Calcula todos os índices de estabilidade clínica (sinais vitais + hábitos).
     */
    public List<EstabilidadeClinicaDTO> calcularIndices(Integer pacienteId,
                                                        List<SinaisVitais> sinais,
                                                        List<Habitos> habitos) {
        List<EstabilidadeClinicaDTO> indices = new ArrayList<>();

        indices.add(calcularIndiceSinal("fc_bpm", sinais.stream().map(SinaisVitais::getFcBpm).toList(), 1.0));
        indices.add(calcularIndiceSinal("fr_rpm", sinais.stream().map(SinaisVitais::getFrRpm).toList(), 1.0));
        indices.add(calcularIndicePressao(sinais, 1.0));
        indices.add(calcularIndiceSinal("temp_celcius", sinais.stream().map(SinaisVitais::getTempCelcius).toList(), 1.0));
        indices.add(calcularIndiceSinal("spo2", sinais.stream().map(SinaisVitais::getSpo2Porcento).toList(), 1.0));
        indices.add(calcularIndiceSinal("peso", sinais.stream().map(SinaisVitais::getPeso).toList(), 1.0));
        indices.add(calcularIndiceSono(habitos));
        indices.add(calcularIndiceExercicio(habitos));

        return adicionarIndiceGeral(indices);
    }

    private List<EstabilidadeClinicaDTO> calcularIndicesPorMedias(Object[] m) {
        List<EstabilidadeClinicaDTO> indices = new ArrayList<>();

        indices.add(calcularIndiceSinal("fc_bpm", quantidade(m[0]), media(m[1]), 1.0));
        indices.add(calcularIndiceSinal("fr_rpm", quantidade(m[2]), media(m[3]), 1.0));
        indices.add(combinarPressao(
                calcularIndiceSinal("pa_sistolica", quantidade(m[4]), media(m[5]), 1.0),
                calcularIndiceSinal("pa_diastolica", quantidade(m[6]), media(m[7]), 1.0),
                1.0));
        indices.add(calcularIndiceSinal("temp_celcius", quantidade(m[8]), media(m[9]), 1.0));
        indices.add(calcularIndiceSinal("spo2", quantidade(m[10]), media(m[11]), 1.0));
        indices.add(calcularIndiceSinal("peso", quantidade(m[12]), media(m[13]), 1.0));
        indices.add(calcularIndiceSono(quantidade(m[14]), media(m[15])));
        indices.add(calcularIndiceExercicio(quantidade(m[16]), quantidade(m[17]), media(m[18])));

        return adicionarIndiceGeral(indices);
    }

    private long quantidade(Object valor) {
        return valor == null ? 0 : ((Number) valor).longValue();
    }

    private Double media(Object valor) {
        return valor == null ? null : ((Number) valor).doubleValue();
    }

    private List<EstabilidadeClinicaDTO> adicionarIndiceGeral(List<EstabilidadeClinicaDTO> indices) {
        Integer indiceGeral = calcularIndiceGeral(indices);
        EstabilidadeClinicaDTO geral = new EstabilidadeClinicaDTO(
                "geral",
                indiceGeral,
                classificar(indiceGeral),
                1.0,
                LocalDateTime.now()
        );
        indices.add(geral);

        return indices;
    }

    private Integer calcularIndiceGeral(List<EstabilidadeClinicaDTO> indices) {
        long qtdValidos = indices.stream().filter(i -> i.getIndice() != null).count();
        double somaPesos = indices.stream().filter(i -> i.getIndice() != null).mapToDouble(EstabilidadeClinicaDTO::getPeso).sum();
        double somaValores = indices.stream().filter(i -> i.getIndice() != null).mapToDouble(i -> i.getIndice() * i.getPeso()).sum();

        return qtdValidos >= minimoFatoresIndiceGeral && somaPesos > 0 ? (int) Math.round(somaValores / somaPesos) : null;
    }

    private void dispararAlertasDetalhados(Integer pacienteId,
                                           List<EstabilidadeClinicaDTO> indices,
                                           EstabilidadeClinicaDTO geral) {
        List<ContatoEmergencia> contatos = contatoEmergenciaRepository.findByPacienteIdOrderByDataRegistroAsc(pacienteId);

        for (ContatoEmergencia contato : contatos) {
            Map<String, Map<String, List<EstabilidadeClinicaDTO>>> agrupadosPorCategoria = new HashMap<>();

            for (EstabilidadeClinicaDTO indice : indices) {
                if (indice.getIndice() == null) continue;

                String tipo = indice.getTipo();
                String categoria = indice.getCategoria().toLowerCase();

                boolean deveNotificar = verificarFlags(contato, tipo, categoria);

                if (deveNotificar) {
                    agrupadosPorCategoria
                            .computeIfAbsent(categoria, k -> new HashMap<>())
                            .computeIfAbsent(tipo, k -> new ArrayList<>())
                            .add(indice);
                }
            }

            for (var entry : agrupadosPorCategoria.entrySet()) {
                String categoria = entry.getKey();
                Map<String, List<EstabilidadeClinicaDTO>> tipos = entry.getValue();

                StringBuilder mensagem = new StringBuilder("Alterações de estabilidade detectadas:\n");

                tipos.forEach((tipo, lista) -> {
                    mensagem.append("- ").append(tipo).append(": ");
                    mensagem.append(lista.stream()
                            .map(i -> i.getIndice() + " (" + i.getCategoria() + ")")
                            .collect(Collectors.joining(", ")));
                    mensagem.append("\n");
                });

                mensagem.append("\nEstabilidade geral: ")
                        .append(geral.getIndice())
                        .append(" (").append(geral.getCategoria()).append(")");

                notificacaoService.enviarAlertaEmergencia(contato, mensagem.toString(), categoria);
            }
        }
    }

    private boolean verificarFlags(ContatoEmergencia contato, String tipo, String categoria) {
        return switch (tipo) {
            case "fc_bpm", "fr_rpm", "pressao", "temp_celcius", "spo2", "peso" -> switch (categoria) {
                case "saudavel" -> Boolean.TRUE.equals(contato.getReceberAlertaSinaisVitaisSaudavel());
                case "moderado" -> Boolean.TRUE.equals(contato.getReceberAlertaSinaisVitaisModerado());
                case "critico" -> Boolean.TRUE.equals(contato.getReceberAlertaSinaisVitaisCritico());
                default -> false;
            };
            case "sono", "exercicio" -> switch (categoria) {
                case "saudavel" -> Boolean.TRUE.equals(contato.getReceberAlertaHabitosSaudavel());
                case "moderado" -> Boolean.TRUE.equals(contato.getReceberAlertaHabitosModerado());
                case "critico" -> Boolean.TRUE.equals(contato.getReceberAlertaHabitosCritico());
                default -> false;
            };
            case "geral" -> switch (categoria) {
                case "saudavel" -> Boolean.TRUE.equals(contato.getReceberAlertaGeralSaudavel());
                case "moderado" -> Boolean.TRUE.equals(contato.getReceberAlertaGeralModerado());
                case "critico" -> Boolean.TRUE.equals(contato.getReceberAlertaGeralCritico());
                default -> false;
            };
            default -> false;
        };
    }

    private EstabilidadeClinicaDTO calcularIndicePressao(List<SinaisVitais> sinais, double peso) {
        EstabilidadeClinicaDTO sistolica = calcularIndiceSinal(
                "pa_sistolica",
                sinais.stream().map(SinaisVitais::getPaSistolica).toList(),
                peso
        );
        EstabilidadeClinicaDTO diastolica = calcularIndiceSinal(
                "pa_diastolica",
                sinais.stream().map(SinaisVitais::getPaDiastolica).toList(),
                peso
        );

        return combinarPressao(sistolica, diastolica, peso);
    }

    private EstabilidadeClinicaDTO combinarPressao(EstabilidadeClinicaDTO sistolica,
                                                   EstabilidadeClinicaDTO diastolica,
                                                   double peso) {
        if (sistolica.getIndice() == null || diastolica.getIndice() == null) {
            return new EstabilidadeClinicaDTO("pressao", null, "n/a", peso, LocalDateTime.now());
        }

        int indice = Math.min(sistolica.getIndice(), diastolica.getIndice());
        return new EstabilidadeClinicaDTO("pressao", indice, classificar(indice), peso, LocalDateTime.now());
    }

    private EstabilidadeClinicaDTO calcularIndiceSinal(String tipo, List<? extends Number> valores, double peso) {
        List<? extends Number> preenchidos = valores.stream().filter(v -> v != null).toList();
        Double media = preenchidos.isEmpty() ? null
                : preenchidos.stream().mapToDouble(Number::doubleValue).average().getAsDouble();

        return calcularIndiceSinal(tipo, preenchidos.size(), media, peso);
    }

    private EstabilidadeClinicaDTO calcularIndiceSinal(String tipo, long quantidade, Double media, double peso) {
        if (quantidade < minimoRegistros || media == null) {
            return new EstabilidadeClinicaDTO(tipo, null, "n/a", peso, LocalDateTime.now());
        }

        int indice = normalizar(media, tipo);
        return new EstabilidadeClinicaDTO(tipo, indice, classificar(indice), peso, LocalDateTime.now());
    }

    private EstabilidadeClinicaDTO calcularIndiceSono(List<Habitos> habitos) {
        List<Integer> horas = habitos.stream().map(Habitos::getHorasSono).filter(h -> h != null).toList();
        Double media = horas.isEmpty() ? null : horas.stream().mapToDouble(Integer::doubleValue).average().getAsDouble();

        return calcularIndiceSono(horas.size(), media);
    }

    private EstabilidadeClinicaDTO calcularIndiceSono(long quantidade, Double mediaSono) {
        if (quantidade < minimoRegistros || mediaSono == null) {
            return new EstabilidadeClinicaDTO("sono", null, "n/a", 1.0, LocalDateTime.now());
        }

        int indice = (mediaSono >= sonoMinimoSaudavel && mediaSono <= sonoMaximoSaudavel) ? 9 :
                (mediaSono < 5 || mediaSono > 11) ? 3 : 6;

        return new EstabilidadeClinicaDTO("sono", indice, classificar(indice), 1.0, LocalDateTime.now());
    }

    private EstabilidadeClinicaDTO calcularIndiceExercicio(List<Habitos> habitos) {
        List<Integer> minutos = habitos.stream().map(Habitos::getMinutosExercicio).filter(m -> m != null).toList();
        long diasSemExercicio = minutos.stream().filter(m -> m == 0).count();
        Double media = minutos.isEmpty() ? null : minutos.stream().mapToDouble(Integer::doubleValue).average().getAsDouble();

        return calcularIndiceExercicio(minutos.size(), diasSemExercicio, media);
    }

    private EstabilidadeClinicaDTO calcularIndiceExercicio(long quantidade, long diasSemExercicio, Double mediaExercicio) {
        if (quantidade < minimoRegistros || mediaExercicio == null) {
            return new EstabilidadeClinicaDTO("exercicio", null, "n/a", 1.0, LocalDateTime.now());
        }

        int indice;
        if (diasSemExercicio >= semExercicioCriticoDias) {
            indice = 3; // crítico
        } else if (mediaExercicio >= exercicioMinimoSaudavel) {
            indice = 9; // saudável
        } else {
            indice = 6; // moderado
        }

        return new EstabilidadeClinicaDTO("exercicio", indice, classificar(indice), 1.0, LocalDateTime.now());
    }

    private int normalizar(double valor, String tipo) {
        return switch (tipo) {
            case "fc_bpm" -> (valor >= 60 && valor <= 100) ? 9 : (valor < 50 || valor > 120) ? 3 : 6;
            case "fr_rpm" -> (valor >= 12 && valor <= 20) ? 9 : (valor < 8 || valor > 30) ? 3 : 6;
            case "pa_sistolica" -> (valor >= 90 && valor <= 120) ? 9 : (valor < 80 || valor > 140) ? 3 : 6;
            case "pa_diastolica" -> (valor >= 60 && valor <= 80) ? 9 : (valor < 50 || valor > 100) ? 3 : 6;
            case "temp_celcius" -> (valor >= 36 && valor <= 37.5) ? 9 : (valor < 35 || valor > 39) ? 3 : 6;
            case "spo2" -> (valor >= 95) ? 9 : (valor < 90) ? 3 : 6;
            case "peso" -> 6;
            default -> 6;
        };
    }

    private String classificar(Integer indice) {
        if (indice == null) return "n/a";
        if (indice >= 8) return "saudavel";
        if (indice >= 5) return "moderado";
        return "critico";
    }

    public void testarDisparoAlerta(Integer pacienteId, List<EstabilidadeClinicaDTO> indicesTeste) {
        EstabilidadeClinicaDTO geral = indicesTeste.stream()
                .filter(i -> "geral".equals(i.getTipo()))
                .findFirst()
                .orElse(new EstabilidadeClinicaDTO("geral", null, "n/a", 1.0, LocalDateTime.now()));

        dispararAlertasDetalhados(pacienteId, indicesTeste, geral);
    }
}
