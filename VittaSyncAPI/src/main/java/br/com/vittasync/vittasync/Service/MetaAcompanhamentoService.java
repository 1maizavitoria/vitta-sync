package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.DTO.MetaAcompanhamentoInputDTO;
import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.MetaAcompanhamento;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.MetaAcompanhamentoRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class MetaAcompanhamentoService {

    private final MetaAcompanhamentoRepository repository;
    private final UsuarioService usuarioService;
    private final EventoPacienteService eventoPacienteService;
    private final SinaisVitaisRepository sinaisVitaisRepository;
    private final HabitosRepository habitosRepository;

    public MetaAcompanhamentoService(
            MetaAcompanhamentoRepository repository,
            UsuarioService usuarioService,
            EventoPacienteService eventoPacienteService,
            SinaisVitaisRepository sinaisVitaisRepository,
            HabitosRepository habitosRepository
    ) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.eventoPacienteService = eventoPacienteService;
        this.sinaisVitaisRepository = sinaisVitaisRepository;
        this.habitosRepository = habitosRepository;
    }


    public MetaAcompanhamento create(MetaAcompanhamentoInputDTO dto, Usuario paciente, Integer usuarioLogadoId) {
        MetaAcompanhamento meta = new MetaAcompanhamento();
        meta.setPaciente(paciente);
        meta.setNome(dto.getNome());
        meta.setTipoDado(dto.getTipoDado());
        meta.setValorAlvo(dto.getValorAlvo());
        meta.setDataLimite(dto.getDataLimite());
        meta.setDataCriacao(LocalDateTime.now());
        meta.setStatus("em_andamento");

        MetaAcompanhamento salvo = repository.save(meta);

        eventoPacienteService.criarEvento(
                paciente.getId(),
                usuarioLogadoId,
                "META_CRIADA",
                "Meta criada",
                "Nova meta de acompanhamento registrada",
                EventoPacienteService.metadata("patientName", paciente.getNome(), "goalName", meta.getNome()),
                "normal"
        );

        return salvo;
    }


    public MetaAcompanhamento update(Long id, MetaAcompanhamentoInputDTO dto, Usuario paciente, Integer usuarioLogadoId) {
        MetaAcompanhamento existente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Meta não encontrada"));

        existente.setPaciente(paciente);
        existente.setNome(dto.getNome());
        existente.setTipoDado(dto.getTipoDado());
        existente.setValorAlvo(dto.getValorAlvo());
        existente.setDataLimite(dto.getDataLimite());
        existente.setDataModificacao(LocalDateTime.now());

        MetaAcompanhamento atualizado = repository.save(existente);

        eventoPacienteService.criarEvento(
                paciente.getId(),
                usuarioLogadoId,
                "META_ATUALIZADA",
                "Meta atualizada",
                "Meta de acompanhamento foi modificada",
                EventoPacienteService.metadata("patientName", paciente.getNome(), "goalName", existente.getNome()),
                "normal"
        );

        return atualizado;
    }


    public void delete(Long id, Integer usuarioLogadoId) {
        MetaAcompanhamento meta = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Meta não encontrada"));

        eventoPacienteService.criarEvento(
                meta.getPaciente().getId(),
                usuarioLogadoId,
                "META_REMOVIDA",
                "Meta removida",
                "Meta de acompanhamento foi excluída",
                EventoPacienteService.metadata("patientName", meta.getPaciente().getNome(), "goalName", meta.getNome()),
                "normal"
        );

        repository.delete(meta);
    }


    public MetaAcompanhamento atualizarProgresso(Long id) {
        MetaAcompanhamento meta = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Meta não encontrada"));

        double progresso = 0.0;

        switch (meta.getTipoDado().toLowerCase()) {
            case "sinais_vitais" -> {
                List<SinaisVitais> registros = sinaisVitaisRepository.findUltimos3ByPacienteId(meta.getPaciente().getId());
                if (!registros.isEmpty()) {
                    double media = registros.stream()
                            .mapToDouble(SinaisVitais::getPeso) // exemplo: pode ser glicemia, PA, etc.
                            .average()
                            .orElse(0.0);
                    progresso = calcularProgresso(meta.getValorAlvo(), media);
                }
            }
            case "habitos" -> {
                List<Habitos> registros = habitosRepository.findUltimos3ByPacienteId(meta.getPaciente().getId());
                if (!registros.isEmpty()) {
                    double media = registros.stream()
                            .mapToDouble(Habitos::getHorasSono) // exemplo: pode ser sono, exercício, etc.
                            .average()
                            .orElse(0.0);
                    progresso = calcularProgresso(meta.getValorAlvo(), media);
                }
            }
            case "personalizado" -> {
                progresso = calcularProgresso(meta.getValorAlvo(), meta.getProgresso());
            }
        }

        meta.setProgresso(progresso);
        meta.setDataModificacao(LocalDateTime.now());
        return repository.save(meta);
    }


    private double calcularProgresso(Double valorAlvo, Double valorAtual) {
        if (valorAlvo == null || valorAtual == null || valorAlvo == 0) return 0.0;
        double progresso = (valorAtual / valorAlvo) * 100;
        return Math.min(progresso, 100.0);
    }

    public MetaAcompanhamento concluirMeta(Long id, Integer usuarioLogadoId) {
        MetaAcompanhamento meta = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Meta não encontrada"));

        meta.setStatus(meta.getDataLimite().isBefore(LocalDateTime.now().toLocalDate()) ? "concluido_atrasado" : "concluido");
        meta.setDataConclusao(LocalDateTime.now());

        MetaAcompanhamento concluida = repository.save(meta);

        eventoPacienteService.criarEvento(
                meta.getPaciente().getId(),
                usuarioLogadoId,
                "META_CONCLUIDA",
                "Meta alcançada",
                "Meta de acompanhamento concluída com sucesso",
                EventoPacienteService.metadata("patientName", meta.getPaciente().getNome(), "goalName", meta.getNome()),
                "alta"
        );

        return concluida;
    }

    public List<MetaAcompanhamento> listarPorPaciente(Integer pacienteId) {
        return repository.findByPacienteId(pacienteId);
    }
}
