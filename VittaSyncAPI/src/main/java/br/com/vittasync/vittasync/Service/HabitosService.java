package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.DiarioSintomas;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import br.com.vittasync.vittasync.Util.EventoTipos;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class HabitosService {

    private final HabitosRepository repository;
    private final EventoPacienteService eventoPacienteService;
    private final EventoClinicoService eventoClinicoService;

    public HabitosService(
            HabitosRepository repository,
            EventoPacienteService eventoPacienteService,
            EventoClinicoService eventoClinicoService
    ) {
        this.repository = repository;
        this.eventoPacienteService = eventoPacienteService;
        this.eventoClinicoService = eventoClinicoService;
    }

    public Habitos create(Habitos habito, Integer usuarioLogadoId) {
        habito.setDataRegistro(LocalDateTime.now());
        habito.setDataModificacao(null);
        Habitos salvo =
                repository.save(habito);

        eventoPacienteService.criarEvento(
                habito.getPaciente().getId(),
                usuarioLogadoId,
                EventoTipos.HABITOS_CRIADOS,
                "Hábito registrado",
                "Um novo hábito foi registrado",
                EventoPacienteService.metadata(
                        "patientName",
                        habito.getPaciente().getNome()
                ),
                EventoPrioridades.NORMAL
        );

        eventoClinicoService.analisarHabitos(
                salvo,
                usuarioLogadoId
        );

        return salvo;
    }

    public Habitos update(Integer id, Habitos novosDados, Integer usuarioLogadoId) {
        Habitos existente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Hábito(s) não encontrado(s)"));
        existente.setHorasSono(novosDados.getHorasSono());
        existente.setMinutosExercicio(novosDados.getMinutosExercicio());
        existente.setDataReferencia(novosDados.getDataReferencia());
        existente.setDataModificacao(LocalDateTime.now());
        Habitos atualizado =
                repository.save(existente);

        eventoPacienteService.criarEvento(
                existente.getPaciente().getId(),
                usuarioLogadoId,
                EventoTipos.HABITOS_EDITADOS,
                "Habito atualizado",
                "Um habito foi atualizado",
                EventoPacienteService.metadata(
                        "patientName",
                        existente.getPaciente().getNome()
                ),
                EventoPrioridades.NORMAL
        );

        return atualizado;
    }

    public void delete(Integer id, Integer usuarioLogadoId) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Hábito(s) não encontrado(s)");
        }
        Habitos habitos =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNaoEncontradoException(
                                        "Habitos não encontrado"
                                )
                        );
        eventoPacienteService.criarEvento(
                habitos.getPaciente().getId(),
                usuarioLogadoId,
                EventoTipos.HABITOS_REMOVIDOS,
                "Hábito removido",
                "Um registro de hábito foi removido",
                EventoPacienteService.metadata(
                        "patientName",
                        habitos.getPaciente().getNome()
                ),
                EventoPrioridades.NORMAL
        );
        repository.deleteById(id);
    }

    public List<Habitos> findByPacienteCpf(String cpf) {
        return repository.findByPacienteCpf(cpf);
    }
}
