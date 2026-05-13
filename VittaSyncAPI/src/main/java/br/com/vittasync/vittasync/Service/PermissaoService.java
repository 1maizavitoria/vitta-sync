package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Model.Vinculo;
import br.com.vittasync.vittasync.Repository.UsuarioRepository;
import br.com.vittasync.vittasync.Repository.VinculoRepository;

import org.springframework.stereotype.Service;

@Service
public class PermissaoService {

    private final UsuarioRepository usuarioRepository;

    private final VinculoRepository vinculoRepository;

    public PermissaoService(
            UsuarioRepository usuarioRepository,
            VinculoRepository vinculoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.vinculoRepository = vinculoRepository;
    }

    public boolean podeVisualizarPaciente(
            Integer usuarioId,
            Integer pacienteId
    ) {

        if (usuarioId.equals(pacienteId)) {
            return true;
        }

        return vinculoRepository
                .existsByPacienteIdAndUsuarioId(
                        pacienteId,
                        usuarioId
                );
    }

    public boolean podeEditarPaciente(
            Integer usuarioId,
            Integer pacienteId
    ) {

        if (usuarioId.equals(pacienteId)) {
            return true;
        }

        return vinculoRepository
                .existsByPacienteIdAndUsuarioIdAndTipo(
                        pacienteId,
                        usuarioId,
                        "responsavel"
                );
    }

    public boolean podeRemoverVinculo(
            Integer usuarioLogadoId,
            Vinculo vinculoAlvo
    ) {

        if (
                usuarioLogadoId.equals(
                        vinculoAlvo.getPacienteId()
                )
        ) {
            return true;
        }

        Usuario usuarioLogado =
                usuarioRepository.findById(
                        usuarioLogadoId
                ).orElse(null);

        if (usuarioLogado == null) {
            return false;
        }

        if (
                usuarioLogado.getTipo()
                        .equalsIgnoreCase("responsavel")
                        &&
                        vinculoAlvo.getTipo()
                                .equalsIgnoreCase("medico")
        ) {

            return vinculoRepository
                    .existsByPacienteIdAndUsuarioIdAndTipo(
                            vinculoAlvo.getPacienteId(),
                            usuarioLogadoId,
                            "responsavel"
                    );
        }

        return false;
    }



}