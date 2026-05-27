package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.EventoPaciente;
import br.com.vittasync.vittasync.Model.EventoVisualizacao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Model.Vinculo;
import br.com.vittasync.vittasync.Repository.EventoPacienteRepository;
import br.com.vittasync.vittasync.Repository.EventoVisualizacaoRepository;
import br.com.vittasync.vittasync.Repository.UsuarioRepository;
import br.com.vittasync.vittasync.Repository.VinculoRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoPacienteService {

    private final EventoPacienteRepository repository;
    private final VinculoRepository vinculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EventoVisualizacaoRepository eventoVisualizacaoRepository;

    public EventoPacienteService(
            EventoPacienteRepository repository,
            VinculoRepository vinculoRepository,
            UsuarioRepository usuarioRepository,
            EventoVisualizacaoRepository eventoVisualizacaoRepository) {
        this.repository = repository;
        this.vinculoRepository = vinculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.eventoVisualizacaoRepository = eventoVisualizacaoRepository;
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

        EventoPaciente eventoSalvo = repository.save(evento);

        List<Vinculo> vinculos =
                vinculoRepository.findByPacienteId(
                        pacienteId
                );

        for (Vinculo vinculo : vinculos) {

            if (
                    vinculo.getUsuarioId()
                            .equals(usuarioId)
            ) {
                continue;
            }

            Usuario usuario =
                    usuarioRepository.findById(
                            vinculo.getUsuarioId()
                    ).orElse(null);

            if (usuario == null) {
                continue;
            }

            EventoVisualizacao visualizacao =
                    new EventoVisualizacao();

            visualizacao.setEventoPaciente(
                    eventoSalvo
            );

            visualizacao.setUsuario(
                    usuario
            );

            visualizacao.setVisualizado(false);

            eventoVisualizacaoRepository.save(
                    visualizacao
            );
        }

    }

    public List<EventoPaciente> listarPorPaciente(Integer pacienteId) {

        return repository
                .findByPacienteIdOrderByCriadoEmDesc(
                        pacienteId
                );
    }

    public void marcarComoVisualizados(
            Integer pacienteId,
            Integer usuarioId
    ) {

        Usuario usuario =
                usuarioRepository.findById(
                        usuarioId
                ).orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Usuário não encontrado"
                        )
                );

        List<EventoVisualizacao> visualizacoes =
                eventoVisualizacaoRepository
                        .findByUsuarioAndVisualizadoFalseAndEventoPacientePacienteId(
                                usuario,
                                pacienteId
                        );

        for (
                EventoVisualizacao visualizacao
                : visualizacoes
        ) {

            visualizacao.setVisualizado(
                    true
            );

            visualizacao.setVisualizadoEm(
                    LocalDateTime.now()
            );
        }

        eventoVisualizacaoRepository.saveAll(
                visualizacoes
        );
    }

    public Long contarNaoVisualizados(
            Integer pacienteId,
            Integer usuarioId
    ) {

        Usuario usuario =
                usuarioRepository.findById(
                        usuarioId
                ).orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Usuário não encontrado"
                        )
                );

        return eventoVisualizacaoRepository
                .countByUsuarioAndVisualizadoFalseAndEventoPacientePacienteId(
                        usuario,
                        pacienteId
                );
    }


}