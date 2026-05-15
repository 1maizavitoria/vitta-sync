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

        // paciente remove qualquer vínculo dele
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

        // médico ou responsável removendo o PRÓPRIO vínculo
        if (
                usuarioLogadoId.equals(
                        vinculoAlvo.getUsuarioId()
                )
        ) {
            return true;
        }

        // responsável removendo médicos do paciente
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