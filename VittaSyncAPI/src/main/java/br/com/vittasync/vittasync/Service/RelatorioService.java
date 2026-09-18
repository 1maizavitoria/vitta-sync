package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.DTO.*;
import br.com.vittasync.vittasync.Model.RelatorioLog;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import br.com.vittasync.vittasync.Repository.DiarioSintomasRepository;
import br.com.vittasync.vittasync.Repository.RelatorioLogRepository;
import br.com.vittasync.vittasync.Util.RelatorioPDFGenerator;
import br.com.vittasync.vittasync.Util.RelatorioCSVGenerator;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;


@Service
public class RelatorioService {

    private final SinaisVitaisRepository sinaisVitaisRepository;
    private final HabitosRepository habitosRepository;
    private final DiarioSintomasRepository diarioSintomasRepository;
    private final RelatorioLogRepository relatorioLogRepository;

    public RelatorioService(SinaisVitaisRepository sinaisVitaisRepository,
                            HabitosRepository habitosRepository,
                            DiarioSintomasRepository diarioSintomasRepository,
                            RelatorioLogRepository relatorioLogRepository) {
        this.sinaisVitaisRepository = sinaisVitaisRepository;
        this.habitosRepository = habitosRepository;
        this.diarioSintomasRepository = diarioSintomasRepository;
        this.relatorioLogRepository = relatorioLogRepository;
    }

    public RelatorioPreviewDTO gerarPreview(Usuario paciente,
                                            List<String> categorias,
                                            LocalDate inicio,
                                            LocalDate fim) {
        RelatorioPacienteResumoDTO resumo = new RelatorioPacienteResumoDTO(
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getPesoInicial(),
                paciente.getAltura(),
                paciente.getDataNascimento()
        );

        List<LinhaTempoSinaisVitaisDTO> sinais = categorias.contains("SINAIS")
                ? sinaisVitaisRepository.buscarParaRelatorio(
                        paciente.getId(), inicio == null ? null : inicio.atStartOfDay(),
                        fim == null || fim.equals(LocalDate.of(9999, 12, 31))
                                ? null : fim.plusDays(1).atStartOfDay())
                .stream()
                .map(s -> new LinhaTempoSinaisVitaisDTO(
                        s.getId(),
                        s.getDataRegistro(),
                        s.getPeso(), s.getFcBpm(), s.getFrRpm(), s.getPaSistolica(),
                        s.getPaDiastolica(), s.getTempCelcius(), s.getSpo2Porcento()
                ))
                .toList()
                : List.of();

        List<LinhaTempoHabitosDTO> habitos = categorias.contains("HABITOS")
                ? habitosRepository.buscarParaRelatorio(paciente.getId(), inicio, fim)
                .stream()
                .map(h -> new LinhaTempoHabitosDTO(
                        h.getId(),
                        h.getDataRegistro(),
                        h.getHorasSono(), h.getMinutosExercicio(), h.getIndiceRepouso(),
                        h.getRepouso(), h.getCanal(),
                        h.getDataReferencia()
                ))
                .toList()
                : List.of();

        List<LinhaTempoSintomasDTO> sintomas = categorias.contains("SINTOMAS")
                ? diarioSintomasRepository.buscarParaRelatorio(paciente.getId(), inicio, fim)
                .stream()
                .map(s -> new LinhaTempoSintomasDTO(
                        s.getId(),
                        s.getDataRegistro(),
                        s.getSintoma(), s.getIntensidadeDor(),
                        s.getDataReferencia()
                ))
                .toList()
                : List.of();

        RelatorioPreviewDTO preview = new RelatorioPreviewDTO(resumo, sinais, habitos, sintomas);
        preview.setDataInicio(inicio);
        preview.setDataFim(fim);
        preview.setDataEmissao(LocalDateTime.now());
        preview.setCategorias(categorias);
        preview.setResumo(new RelatorioResumoDTO(
                categorias.contains("SINAIS") ? resumirSinais(sinais) : List.of(),
                categorias.contains("HABITOS") ? resumirHabitos(habitos) : null));
        return preview;
    }

    private List<RelatorioResumoDTO.Indicador> resumirSinais(List<LinhaTempoSinaisVitaisDTO> sinais) {
        List<RelatorioResumoDTO.Indicador> indicadores = new ArrayList<>();
        indicadores.add(resumirIndicador(sinais, "fcBpm", "Frequência cardíaca", "bpm", LinhaTempoSinaisVitaisDTO::getFcBpm));
        indicadores.add(resumirIndicador(sinais, "frRpm", "Frequência respiratória", "rpm", LinhaTempoSinaisVitaisDTO::getFrRpm));
        indicadores.add(resumirIndicador(sinais, "paSistolica", "Pressão sistólica", "mmHg", LinhaTempoSinaisVitaisDTO::getPaSistolica));
        indicadores.add(resumirIndicador(sinais, "paDiastolica", "Pressão diastólica", "mmHg", LinhaTempoSinaisVitaisDTO::getPaDiastolica));
        indicadores.add(resumirIndicador(sinais, "tempCelcius", "Temperatura", "°C", LinhaTempoSinaisVitaisDTO::getTempCelcius));
        indicadores.add(resumirIndicador(sinais, "spo2Porcento", "Saturação", "%", LinhaTempoSinaisVitaisDTO::getSpo2Porcento));
        return indicadores;
    }

    private RelatorioResumoDTO.Indicador resumirIndicador(List<LinhaTempoSinaisVitaisDTO> sinais,
            String chave, String nome, String unidade, Function<LinhaTempoSinaisVitaisDTO, Number> valor) {
        DoubleSummaryStatistics total = new DoubleSummaryStatistics();
        Map<LocalDate, DoubleSummaryStatistics> porDia = new TreeMap<>();
        for (LinhaTempoSinaisVitaisDTO sinal : sinais) {
            Number numero = valor.apply(sinal);
            if (numero == null) continue;
            total.accept(numero.doubleValue());
            if (sinal.getDataHora() != null) {
                porDia.computeIfAbsent(sinal.getDataHora().toLocalDate(), d -> new DoubleSummaryStatistics())
                        .accept(numero.doubleValue());
            }
        }
        List<RelatorioResumoDTO.PontoDiario> pontos = porDia.entrySet().stream()
                .map(e -> new RelatorioResumoDTO.PontoDiario(e.getKey(), e.getValue().getAverage())).toList();
        boolean vazio = total.getCount() == 0;
        return new RelatorioResumoDTO.Indicador(chave, nome, unidade,
                vazio ? null : total.getAverage(), vazio ? null : total.getMin(),
                vazio ? null : total.getMax(), total.getCount(), pontos);
    }

    private RelatorioResumoDTO.Habitos resumirHabitos(List<LinhaTempoHabitosDTO> habitos) {
        Map<LocalDate, DoubleSummaryStatistics> sonoPorDia = new TreeMap<>();
        Map<LocalDate, Long> exercicioPorDia = new TreeMap<>();
        for (LinhaTempoHabitosDTO habito : habitos) {
            if (habito.getDataReferencia() == null) continue;
            if (habito.getHorasSono() != null) {
                sonoPorDia.computeIfAbsent(habito.getDataReferencia(), d -> new DoubleSummaryStatistics())
                        .accept(habito.getHorasSono());
            }
            if (habito.getMinutosExercicio() != null) {
                exercicioPorDia.merge(habito.getDataReferencia(), habito.getMinutosExercicio().longValue(), Long::sum);
            }
        }
        Double mediaSono = sonoPorDia.isEmpty() ? null : sonoPorDia.values().stream()
                .mapToDouble(DoubleSummaryStatistics::getAverage).average().orElseThrow();
        Long totalExercicio = exercicioPorDia.isEmpty() ? null : exercicioPorDia.values().stream()
                .mapToLong(Long::longValue).sum();
        return new RelatorioResumoDTO.Habitos(mediaSono, sonoPorDia.size(), totalExercicio, exercicioPorDia.size());
    }

    public RelatorioPDFDTO gerarPDF(RelatorioPreviewDTO preview) {
        try {
            RelatorioPDFGenerator generator = new RelatorioPDFGenerator();
            byte[] pdfBytes = generator.gerarRelatorio(preview);
            return new RelatorioPDFDTO(pdfBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    public RelatorioCSVDTO gerarCSV(RelatorioPreviewDTO preview) {
        RelatorioCSVGenerator generator = new RelatorioCSVGenerator();
        byte[] csvBytes = generator.gerarRelatorio(preview);
        return new RelatorioCSVDTO(csvBytes);
    }

    public void registrarExportacao(Usuario paciente,
                                    Usuario usuario,
                                    String formato,
                                    List<String> categorias,
                                    LocalDate inicio,
                                    LocalDate fim) {
        RelatorioLog log = new RelatorioLog();
        log.setPaciente(paciente);
        log.setUsuario(usuario);
        log.setFormato(formato);
        log.setCategorias(String.join(",", categorias));
        log.setDataInicio(inicio);
        log.setDataFim(fim);
        log.setDataExportacao(LocalDateTime.now());
        relatorioLogRepository.save(log);
    }

    public List<RelatorioLog> consultarLogs(Integer pacienteId) {
        return relatorioLogRepository.findByPacienteIdOrderByDataExportacaoDesc(pacienteId);
    }
}
