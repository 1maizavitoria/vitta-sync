package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Model.EventoPaciente;
import br.com.vittasync.vittasync.Repository.EventoPacienteRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoPacienteService {

    private final EventoPacienteRepository repository;

    public EventoPacienteService(EventoPacienteRepository repository) {
        this.repository = repository;
    }

    public void criarEvento(
            Integer pacienteId,
            Integer usuarioId,
            String tipoEvento,
            String titulo,
            String descricao,
            String prioridade
    ) {

        EventoPaciente evento =
                new EventoPaciente();

        evento.setPacienteId(pacienteId);
        evento.setUsuarioId(usuarioId);
        evento.setTipoEvento(tipoEvento);
        evento.setTitulo(titulo);
        evento.setDescricao(descricao);
        evento.setVisualizado(false);
        evento.setPrioridade(prioridade);

        evento.setCriadoEm(
                Timestamp.valueOf(
                        LocalDateTime.now()
                )
        );

        repository.save(evento);
    }

    public List<EventoPaciente> listarPorPaciente(Integer pacienteId) {

        return repository
                .findByPacienteIdOrderByCriadoEmDesc(
                        pacienteId
                );
    }
}