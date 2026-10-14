package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.LembreteMedicao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.LembreteMedicaoRepository;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import br.com.vittasync.vittasync.Util.EventoTipos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


@Service
public class LembreteMedicaoService {

    private final LembreteMedicaoRepository repository;
    private final EventoPacienteService eventoPacienteService;

    private static final Logger logger = LoggerFactory.getLogger(LembreteMedicaoService.class);

    private boolean usarProcedure = true;

    public LembreteMedicaoService(
            LembreteMedicaoRepository repository,
            EventoPacienteService eventoPacienteService
    ) {
        this.repository = repository;
        this.eventoPacienteService = eventoPacienteService;
    }

    public LembreteMedicao salvarSubstituir(LembreteMedicao novoLembrete) {
        return salvarSubstituir(novoLembrete, novoLembrete.getUsuario().getId());
    }

    public LembreteMedicao salvarSubstituir(
            LembreteMedicao novoLembrete,
            Integer usuarioLogadoId
    ) {
        boolean jaExistia = repository.findByUsuario(novoLembrete.getUsuario())
                .map(lembrete -> {
                    repository.delete(lembrete);
                    return true;
                })
                .orElse(false);

        LembreteMedicao salvo = repository.save(novoLembrete);

        criarEventoLembrete(
                novoLembrete.getUsuario(),
                usuarioLogadoId,
                jaExistia
                        ? EventoTipos.LEMBRETE_ATUALIZADO
                        : EventoTipos.LEMBRETE_CRIADO,
                jaExistia
                        ? "Lembrete atualizado"
                        : "Lembrete criado",
                jaExistia
                        ? "Um lembrete de medição foi atualizado"
                        : "Um lembrete de medição foi criado"
        );

        return salvo;
    }

    public Optional<LembreteMedicao> searchlembrete(Usuario usuario) {
        return repository.findByUsuario(usuario);
    }

    public List<LembreteMedicao> searchLembreteAtivo() {
        return repository.findByAtivoTrue();
    }

    public List<LembreteMedicao> searchLembretesDoMinuto(DayOfWeek dia, LocalTime hora) {
        if (usarProcedure) {
            try {
                return repository.buscarLembretesDoMinuto(dia.name(), hora);
            } catch (RuntimeException e) {
                usarProcedure = false;
                logger.warn("Procedure sp_lembretes_do_minuto indisponível, os lembretes serão filtrados no Java");
            }
        }

        return repository.findByAtivoTrue().stream()
                .filter(lembrete -> lembrete.getDiasSemana().toUpperCase().contains(dia.name()))
                .filter(lembrete -> lembrete.getHorario().truncatedTo(ChronoUnit.MINUTES).equals(hora))
                .toList();
    }

    public Optional<LembreteMedicao> ativar(Usuario usuario) {
        return ativar(usuario, usuario.getId());
    }

    public Optional<LembreteMedicao> ativar(
            Usuario usuario,
            Integer usuarioLogadoId
    ) {
        Optional<LembreteMedicao> lembreteOpt = repository.findByUsuario(usuario);
        lembreteOpt.ifPresent(lembrete -> {
            lembrete.setAtivo(true);
            repository.save(lembrete);
            criarEventoLembrete(
                    usuario,
                    usuarioLogadoId,
                    EventoTipos.LEMBRETE_ATUALIZADO,
                    "Lembrete ativado",
                    "Um lembrete de medição foi ativado"
            );
        });
        return lembreteOpt;
    }

    public Optional<LembreteMedicao> desativar(Usuario usuario) {
        return desativar(usuario, usuario.getId());
    }

    public Optional<LembreteMedicao> desativar(
            Usuario usuario,
            Integer usuarioLogadoId
    ) {
        Optional<LembreteMedicao> lembreteOpt = repository.findByUsuario(usuario);
        lembreteOpt.ifPresent(lembrete -> {
            lembrete.setAtivo(false);
            repository.save(lembrete);
            criarEventoLembrete(
                    usuario,
                    usuarioLogadoId,
                    EventoTipos.LEMBRETE_ATUALIZADO,
                    "Lembrete desativado",
                    "Um lembrete de medição foi desativado"
            );
        });
        return lembreteOpt;
    }

    private void criarEventoLembrete(
            Usuario paciente,
            Integer usuarioLogadoId,
            String tipoEvento,
            String titulo,
            String descricao
    ) {
        eventoPacienteService.criarEvento(
                paciente.getId(),
                usuarioLogadoId,
                tipoEvento,
                titulo,
                descricao,
                EventoPacienteService.metadata(
                        "patientName",
                        paciente.getNome()
                ),
                EventoPrioridades.NORMAL
        );
    }
}
