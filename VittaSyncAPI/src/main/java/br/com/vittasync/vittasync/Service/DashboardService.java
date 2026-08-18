package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.DTO.DashboardCategoriaDTO;
import br.com.vittasync.vittasync.DTO.DashboardPontoDTO;
import br.com.vittasync.vittasync.DTO.DashboardResponseDTO;
import br.com.vittasync.vittasync.DTO.DashboardSerieDTO;
import br.com.vittasync.vittasync.Exception.DadosInvalidosException;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class DashboardService {

    private static final List<String> TODAS_CATEGORIAS = List.of(
            "pressao",
            "frequencia_cardiaca",
            "frequencia_respiratoria",
            "temperatura",
            "saturacao",
            "peso",
            "sono",
            "exercicio"
    );

    private final SinaisVitaisRepository sinaisVitaisRepository;
    private final HabitosRepository habitosRepository;

    public DashboardService(
            SinaisVitaisRepository sinaisVitaisRepository,
            HabitosRepository habitosRepository
    ) {
        this.sinaisVitaisRepository = sinaisVitaisRepository;
        this.habitosRepository = habitosRepository;
    }

    public DashboardResponseDTO consultar(
            Usuario paciente,
            LocalDate inicioInformado,
            LocalDate fimInformado,
            String categoriasInformadas
    ) {
        LocalDate fim = fimInformado != null ? fimInformado : LocalDate.now();
        LocalDate inicio = inicioInformado != null ? inicioInformado : fim.minusDays(6);

        if (inicio.isAfter(fim)) {
            throw new DadosInvalidosException("A data inicial não pode ser posterior à data final");
        }

        Set<String> categorias = resolverCategorias(categoriasInformadas);

        List<SinaisVitais> sinais = sinaisVitaisRepository
                .findByPacienteIdAndDataRegistroBetweenOrderByDataRegistroAsc(
                        paciente.getId(),
                        inicio.atStartOfDay(),
                        fim.atTime(LocalTime.MAX)
                );

        List<Habitos> habitos = habitosRepository
                .findByPacienteIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(
                        paciente.getId(),
                        inicio,
                        fim
                );

        List<DashboardCategoriaDTO> categoriasResposta = new ArrayList<>();
        for (String categoria : TODAS_CATEGORIAS) {
            if (categorias.contains(categoria)) {
                categoriasResposta.add(montarCategoria(categoria, sinais, habitos));
            }
        }

        return new DashboardResponseDTO(
                paciente.getCpf(),
                paciente.getNome(),
                inicio,
                fim,
                categoriasResposta
        );
    }

    private Set<String> resolverCategorias(String categoriasInformadas) {
        if (categoriasInformadas == null || categoriasInformadas.isBlank()) {
            return new LinkedHashSet<>(TODAS_CATEGORIAS);
        }

        Set<String> categorias = new LinkedHashSet<>();
        for (String valor : categoriasInformadas.split(",")) {
            String categoria = valor.trim().toLowerCase(Locale.ROOT);
            if (categoria.isBlank() || !TODAS_CATEGORIAS.contains(categoria)) {
                throw new DadosInvalidosException("Categoria de dashboard inválida");
            }
            categorias.add(categoria);
        }
        return categorias;
    }

    private DashboardCategoriaDTO montarCategoria(
            String categoria,
            List<SinaisVitais> sinais,
            List<Habitos> habitos
    ) {
        return switch (categoria) {
            case "pressao" -> new DashboardCategoriaDTO(
                    "pressao",
                    "Pressão arterial",
                    "mmHg",
                    List.of(
                            serieSinais("sistolica", "Sistólica", sinais, "paSistolica"),
                            serieSinais("diastolica", "Diastólica", sinais, "paDiastolica")
                    )
            );
            case "frequencia_cardiaca" -> categoriaSinal(
                    categoria, "Frequência cardíaca", "bpm", "frequencia_cardiaca", "Frequência cardíaca", sinais
            );
            case "frequencia_respiratoria" -> categoriaSinal(
                    categoria, "Frequência respiratória", "rpm", "frequencia_respiratoria", "Frequência respiratória", sinais
            );
            case "temperatura" -> categoriaSinal(
                    categoria, "Temperatura corporal", "°C", "temperatura", "Temperatura", sinais
            );
            case "saturacao" -> categoriaSinal(
                    categoria, "Saturação de oxigênio", "%", "saturacao", "Saturação", sinais
            );
            case "peso" -> categoriaSinal(
                    categoria, "Peso", "kg", "peso", "Peso", sinais
            );
            case "sono" -> new DashboardCategoriaDTO(
                    "sono",
                    "Sono",
                    "horas",
                    List.of(serieHabitos("sono", "Horas de sono", habitos, true))
            );
            case "exercicio" -> new DashboardCategoriaDTO(
                    "exercicio",
                    "Atividade física",
                    "minutos",
                    List.of(serieHabitos("exercicio", "Minutos de exercício", habitos, false))
            );
            default -> throw new DadosInvalidosException("Categoria de dashboard inválida");
        };
    }

    private DashboardCategoriaDTO categoriaSinal(
            String codigo,
            String nome,
            String unidade,
            String codigoSerie,
            String nomeSerie,
            List<SinaisVitais> sinais
    ) {
        return new DashboardCategoriaDTO(
                codigo,
                nome,
                unidade,
                List.of(serieSinais(codigoSerie, nomeSerie, sinais, codigo))
        );
    }

    private DashboardSerieDTO serieSinais(
            String codigo,
            String nome,
            List<SinaisVitais> sinais,
            String campo
    ) {
        List<DashboardPontoDTO> pontos = new ArrayList<>();
        for (SinaisVitais sinal : sinais) {
            Number valor = switch (campo) {
                case "paSistolica" -> sinal.getPaSistolica();
                case "paDiastolica" -> sinal.getPaDiastolica();
                case "frequencia_cardiaca" -> sinal.getFcBpm();
                case "frequencia_respiratoria" -> sinal.getFrRpm();
                case "temperatura" -> sinal.getTempCelcius();
                case "saturacao" -> sinal.getSpo2Porcento();
                case "peso" -> sinal.getPeso();
                default -> null;
            };

            if (valor != null) {
                pontos.add(new DashboardPontoDTO(sinal.getDataRegistro(), valor));
            }
        }
        return new DashboardSerieDTO(codigo, nome, pontos);
    }

    private DashboardSerieDTO serieHabitos(
            String codigo,
            String nome,
            List<Habitos> habitos,
            boolean usarSono
    ) {
        List<DashboardPontoDTO> pontos = new ArrayList<>();
        for (Habitos habito : habitos) {
            Number valor = usarSono ? habito.getHorasSono() : habito.getMinutosExercicio();
            if (valor != null) {
                LocalDateTime data = habito.getDataReferencia().atStartOfDay();
                pontos.add(new DashboardPontoDTO(data, valor));
            }
        }
        return new DashboardSerieDTO(codigo, nome, pontos);
    }
}
