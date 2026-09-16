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
import java.util.Optional;


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
                ? sinaisVitaisRepository.findByPacienteIdAndDataRegistroBetweenOrderByDataRegistroAsc(
                        paciente.getId(), inicio.atStartOfDay(), fim.atTime(23,59))
                .stream()
                .map(s -> new LinhaTempoSinaisVitaisDTO(
                        s.getId(),
                        s.getDataRegistro(),
                        Optional.ofNullable(s.getPeso()).orElse(0.0),
                        Optional.ofNullable(s.getFcBpm()).orElse(0),
                        Optional.ofNullable(s.getFrRpm()).orElse(0),
                        Optional.ofNullable(s.getPaSistolica()).orElse(0),
                        Optional.ofNullable(s.getPaDiastolica()).orElse(0),
                        Optional.ofNullable(s.getTempCelcius()).orElse(0.0),
                        Optional.ofNullable(s.getSpo2Porcento()).orElse(0)
                ))
                .toList()
                : List.of();

        List<LinhaTempoHabitosDTO> habitos = categorias.contains("HABITOS")
                ? habitosRepository.findByPacienteIdAndDataRegistroBetweenOrderByDataRegistroAsc(
                        paciente.getId(), inicio.atStartOfDay(), fim.atTime(23,59))
                .stream()
                .map(h -> new LinhaTempoHabitosDTO(
                        h.getId(),
                        h.getDataRegistro(),
                        Optional.ofNullable(h.getHorasSono()).orElse(0),
                        Optional.ofNullable(h.getMinutosExercicio()).orElse(0),
                        Optional.ofNullable(h.getIndiceRepouso()).orElse(0.0),
                        Optional.ofNullable(h.getRepouso()).orElse(false),
                        Optional.ofNullable(h.getCanal()).orElse(""),
                        h.getDataReferencia()
                ))
                .toList()
                : List.of();

        List<LinhaTempoSintomasDTO> sintomas = categorias.contains("SINTOMAS")
                ? diarioSintomasRepository.findByPacienteIdAndDataRegistroBetweenOrderByDataRegistroAsc(
                        paciente.getId(), inicio.atStartOfDay(), fim.atTime(23,59))
                .stream()
                .map(s -> new LinhaTempoSintomasDTO(
                        s.getId(),
                        s.getDataRegistro(),
                        Optional.ofNullable(s.getSintoma()).orElse(""),
                        Optional.ofNullable(s.getIntensidadeDor()).orElse(0),
                        s.getDataReferencia()
                ))
                .toList()
                : List.of();

        return new RelatorioPreviewDTO(resumo, sinais, habitos, sintomas);
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
