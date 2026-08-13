package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.DTO.MetaAcompanhamentoInputDTO;
import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.MetaAcompanhamento;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import br.com.vittasync.vittasync.Repository.MetaAcompanhamentoRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MetaAcompanhamentoService {
    private final MetaAcompanhamentoRepository repository;
    private final EventoPacienteService eventoPacienteService;
    private final SinaisVitaisRepository sinaisVitaisRepository;
    private final HabitosRepository habitosRepository;

    public MetaAcompanhamentoService(
            MetaAcompanhamentoRepository repository,
            EventoPacienteService eventoPacienteService,
            SinaisVitaisRepository sinaisVitaisRepository,
            HabitosRepository habitosRepository
    ) {
        this.repository = repository;
        this.eventoPacienteService = eventoPacienteService;
        this.sinaisVitaisRepository = sinaisVitaisRepository;
        this.habitosRepository = habitosRepository;
    }

    public MetaAcompanhamento create(MetaAcompanhamentoInputDTO dto, Usuario paciente, Integer usuarioLogadoId) {
        MetaAcompanhamento meta = new MetaAcompanhamento();
        meta.setPaciente(paciente);
        meta.setNome(dto.getNome());
        meta.setTipoDado(dto.getTipoDado());
        aplicarConfiguracao(meta, dto);
        meta.setValorAlvo(dto.getValorAlvo());
        meta.setDataLimite(dto.getDataLimite());
        meta.setDataCriacao(LocalDateTime.now());
        meta.setStatus("em_andamento");
        atualizarValoresEProgresso(meta, usuarioLogadoId);

        MetaAcompanhamento salvo = repository.save(meta);
        eventoPacienteService.criarEvento(
                paciente.getId(), usuarioLogadoId, "META_CRIADA", "Meta criada",
                "Nova meta de acompanhamento registrada",
                EventoPacienteService.metadata("patientName", paciente.getNome(), "goalName", meta.getNome()),
                "normal"
        );
        return salvo;
    }

    public MetaAcompanhamento update(Long id, MetaAcompanhamentoInputDTO dto, Usuario paciente, Integer usuarioLogadoId) {
        MetaAcompanhamento meta = buscarMetaDoPaciente(id, paciente.getId());
        meta.setNome(dto.getNome());
        meta.setTipoDado(dto.getTipoDado());
        aplicarConfiguracao(meta, dto);
        meta.setValorAlvo(dto.getValorAlvo());
        meta.setDataLimite(dto.getDataLimite());
        meta.setDataModificacao(LocalDateTime.now());
        atualizarValoresEProgresso(meta, usuarioLogadoId);

        MetaAcompanhamento atualizada = repository.save(meta);
        eventoPacienteService.criarEvento(
                paciente.getId(), usuarioLogadoId, "META_ATUALIZADA", "Meta atualizada",
                "Meta de acompanhamento foi modificada",
                EventoPacienteService.metadata("patientName", paciente.getNome(), "goalName", meta.getNome()),
                "normal"
        );
        return atualizada;
    }

    public void delete(Long id, Integer pacienteId, Integer usuarioLogadoId) {
        MetaAcompanhamento meta = buscarMetaDoPaciente(id, pacienteId);
        eventoPacienteService.criarEvento(
                pacienteId, usuarioLogadoId, "META_REMOVIDA", "Meta removida",
                "Meta de acompanhamento foi excluída",
                EventoPacienteService.metadata("patientName", meta.getPaciente().getNome(), "goalName", meta.getNome()),
                "normal"
        );
        repository.delete(meta);
    }

    public MetaAcompanhamento concluirMeta(Long id, Integer pacienteId, Integer usuarioLogadoId) {
        MetaAcompanhamento meta = buscarMetaDoPaciente(id, pacienteId);
        meta.setStatus(meta.getDataLimite().isBefore(LocalDateTime.now().toLocalDate())
                ? "concluido_atrasado" : "concluido");
        meta.setDataConclusao(LocalDateTime.now());
        MetaAcompanhamento concluida = repository.save(meta);
        eventoPacienteService.criarEvento(
                pacienteId, usuarioLogadoId, "META_CONCLUIDA", "Meta alcançada",
                "Meta de acompanhamento concluída com sucesso",
                EventoPacienteService.metadata("patientName", meta.getPaciente().getNome(), "goalName", meta.getNome()),
                "alta"
        );
        return concluida;
    }

    public MetaAcompanhamento atualizarValorManual(
            Long id,
            Integer pacienteId,
            Double valorAtual,
            Integer usuarioLogadoId
    ) {
        MetaAcompanhamento meta = buscarMetaDoPaciente(id, pacienteId);
        if (!"personalizado".equalsIgnoreCase(meta.getIndicador())) {
            throw new IllegalArgumentException("Somente metas personalizadas aceitam atualização manual");
        }
        if (valorAtual == null) throw new IllegalArgumentException("O valor atual é obrigatório");
        meta.setValorAtual(valorAtual);
        meta.setProgresso(calcularProgresso(meta));
        meta.setDataModificacao(LocalDateTime.now());
        concluirAutomaticamenteSeAtingida(meta, usuarioLogadoId);
        return repository.save(meta);
    }

    public List<MetaAcompanhamento> listarPorPaciente(Integer pacienteId, Integer usuarioLogadoId) {
        List<MetaAcompanhamento> metas = repository.findByPacienteId(pacienteId);
        metas.forEach(meta -> atualizarValoresEProgresso(meta, usuarioLogadoId));
        return repository.saveAll(metas);
    }

    private MetaAcompanhamento buscarMetaDoPaciente(Long id, Integer pacienteId) {
        MetaAcompanhamento meta = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Meta não encontrada"));
        if (!meta.getPaciente().getId().equals(pacienteId)) {
            throw new RecursoNaoEncontradoException("Meta não encontrada");
        }
        return meta;
    }

    private void aplicarConfiguracao(MetaAcompanhamento meta, MetaAcompanhamentoInputDTO dto) {
        meta.setIndicador(dto.getIndicador());
        meta.setDirecao(dto.getDirecao());
        meta.setValorInicial(dto.getValorInicial());
        meta.setValorAtual(dto.getValorAtual());
        meta.setUnidade(dto.getUnidade());
    }

    private void atualizarValoresEProgresso(MetaAcompanhamento meta, Integer usuarioLogadoId) {
        Double valorAtual = buscarValorAtual(meta);
        if (valorAtual != null) meta.setValorAtual(valorAtual);
        if (meta.getValorInicial() == null && meta.getValorAtual() != null) {
            meta.setValorInicial(meta.getValorAtual());
        }
        meta.setProgresso(calcularProgresso(meta));
        concluirAutomaticamenteSeAtingida(meta, usuarioLogadoId);
    }

    private void concluirAutomaticamenteSeAtingida(MetaAcompanhamento meta, Integer usuarioLogadoId) {
        if (meta.getProgresso() == null
                || meta.getProgresso() < 100.0
                || meta.getStatus() == null
                || meta.getStatus().startsWith("concluido")) {
            return;
        }

        meta.setStatus(meta.getDataLimite().isBefore(LocalDateTime.now().toLocalDate())
                ? "concluido_atrasado" : "concluido");
        meta.setDataConclusao(LocalDateTime.now());

        eventoPacienteService.criarEvento(
                meta.getPaciente().getId(), usuarioLogadoId, "META_CONCLUIDA", "Meta alcançada",
                "Meta de acompanhamento concluída automaticamente",
                EventoPacienteService.metadata(
                        "patientName", meta.getPaciente().getNome(),
                        "goalName", meta.getNome()
                ),
                "alta"
        );
    }

    private Double buscarValorAtual(MetaAcompanhamento meta) {
        if (meta.getIndicador() == null) return meta.getValorAtual();
        Integer pacienteId = meta.getPaciente().getId();
        return switch (meta.getIndicador().toLowerCase()) {
            case "peso" -> sinaisVitaisRepository.findFirstByPacienteIdOrderByDataRegistroDesc(pacienteId)
                    .map(registro -> registro.getPeso()).orElse(meta.getValorAtual());
            case "horas_sono" -> habitosRepository.findFirstByPacienteIdOrderByDataRegistroDesc(pacienteId)
                    .map(registro -> registro.getHorasSono() == null ? null : registro.getHorasSono().doubleValue())
                    .orElse(meta.getValorAtual());
            case "minutos_exercicio" -> habitosRepository.findFirstByPacienteIdOrderByDataRegistroDesc(pacienteId)
                    .map(registro -> registro.getMinutosExercicio() == null ? null : registro.getMinutosExercicio().doubleValue())
                    .orElse(meta.getValorAtual());
            case "personalizado" -> meta.getValorAtual();
            default -> throw new IllegalArgumentException("Indicador de meta inválido");
        };
    }

    private double calcularProgresso(MetaAcompanhamento meta) {
        Double inicial = meta.getValorInicial();
        Double atual = meta.getValorAtual();
        Double alvo = meta.getValorAlvo();
        if (atual == null || alvo == null) return 0.0;

        double progresso;
        if ("reduzir".equalsIgnoreCase(meta.getDirecao())) {
            if (inicial == null || inicial.equals(alvo)) return atual <= alvo ? 100.0 : 0.0;
            progresso = ((inicial - atual) / (inicial - alvo)) * 100.0;
        } else {
            if (alvo == 0) return 0.0;
            progresso = (atual / alvo) * 100.0;
        }
        return Math.max(0.0, Math.min(progresso, 100.0));
    }
}
