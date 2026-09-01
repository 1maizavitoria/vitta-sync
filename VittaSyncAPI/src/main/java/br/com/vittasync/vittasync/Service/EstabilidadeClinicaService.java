package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.DTO.EstabilidadeClinicaDTO;
import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.EstabilidadeClinica;
import br.com.vittasync.vittasync.Repository.EstabilidadeClinicaRepository;
import br.com.vittasync.vittasync.Repository.ContatoEmergenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class EstabilidadeClinicaService {

    private final EstabilidadeClinicaRepository estabilidadeClinicaRepository;
    private final ContatoEmergenciaRepository contatoEmergenciaRepository;
    private final NotificacaoService notificacaoService;

    private static final int minimoRegistros = 3;
    private static final int minimoFatoresIndiceGeral = 3;
    private static final int sonoMinimoSaudavel = 7;
    private static final int sonoMaximoSaudavel = 9;
    private static final int exercicioMinimoSaudavel = 60;
    private static final int semExercicioCriticoDias = 10;

    public EstabilidadeClinicaService(
            EstabilidadeClinicaRepository estabilidadeClinicaRepository,
            ContatoEmergenciaRepository contatoEmergenciaRepository,
            NotificacaoService notificacaoService
    ) {
        this.estabilidadeClinicaRepository = estabilidadeClinicaRepository;
        this.contatoEmergenciaRepository = contatoEmergenciaRepository;
        this.notificacaoService = notificacaoService;
    }

    public void verificarMudancaEstabilidade(Integer pacienteId,
                                             List<SinaisVitais> sinais,
                                             List<Habitos> habitos) {
        List<EstabilidadeClinicaDTO> indices = calcularIndices(pacienteId, sinais, habitos);

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

    @Async
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

        if (sistolica.getIndice() == null || diastolica.getIndice() == null) {
            return new EstabilidadeClinicaDTO("pressao", null, "n/a", peso, LocalDateTime.now());
        }

        int indice = Math.min(sistolica.getIndice(), diastolica.getIndice());
        return new EstabilidadeClinicaDTO("pressao", indice, classificar(indice), peso, LocalDateTime.now());
    }

    private EstabilidadeClinicaDTO calcularIndiceSinal(String tipo, List<? extends Number> valores, double peso) {
        if (valores.stream().filter(v -> v != null).count() < minimoRegistros) {
            return new EstabilidadeClinicaDTO(tipo, null, "n/a", peso, LocalDateTime.now());
        }

        double media = valores.stream()
                .filter(v -> v != null)
                .mapToDouble(Number::doubleValue)
                .average()
                .orElse(Double.NaN);

        if (Double.isNaN(media)) {
            return new EstabilidadeClinicaDTO(tipo, null, "n/a", peso, LocalDateTime.now());
        }

        int indice = normalizar(media, tipo);
        return new EstabilidadeClinicaDTO(tipo, indice, classificar(indice), peso, LocalDateTime.now());
    }

    private EstabilidadeClinicaDTO calcularIndiceSono(List<Habitos> habitos) {
        if (habitos.stream().filter(h -> h.getHorasSono() != null).count() < minimoRegistros) {
            return new EstabilidadeClinicaDTO("sono", null, "n/a", 1.0, LocalDateTime.now());
        }

        double mediaSono = habitos.stream()
                .filter(h -> h.getHorasSono() != null)
                .mapToDouble(Habitos::getHorasSono)
                .average()
                .orElse(Double.NaN);

        if (Double.isNaN(mediaSono)) {
            return new EstabilidadeClinicaDTO("sono", null, "n/a", 1.0, LocalDateTime.now());
        }

        int indice = (mediaSono >= sonoMinimoSaudavel && mediaSono <= sonoMaximoSaudavel) ? 9 :
                (mediaSono < 5 || mediaSono > 11) ? 3 : 6;

        return new EstabilidadeClinicaDTO("sono", indice, classificar(indice), 1.0, LocalDateTime.now());
    }

    private EstabilidadeClinicaDTO calcularIndiceExercicio(List<Habitos> habitos) {
        if (habitos.stream().filter(h -> h.getMinutosExercicio() != null).count() < minimoRegistros) {
            return new EstabilidadeClinicaDTO("exercicio", null, "n/a", 1.0, LocalDateTime.now());
        }

        long diasSemExercicio = habitos.stream()
                .filter(h -> h.getMinutosExercicio() != null)
                .filter(h -> h.getMinutosExercicio() == 0)
                .count();

        double mediaExercicio = habitos.stream()
                .filter(h -> h.getMinutosExercicio() != null)
                .mapToDouble(Habitos::getMinutosExercicio)
                .average()
                .orElse(Double.NaN);

        if (Double.isNaN(mediaExercicio)) {
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
