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

    public PermissaoService(UsuarioRepository usuarioRepository, VinculoRepository vinculoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.vinculoRepository = vinculoRepository;
    }

    public boolean podeVisualizarPaciente(Integer usuarioId, Integer pacienteId) {

        if (usuarioId.equals(pacienteId)) {
            return true;
        }

        return vinculoRepository.existsByPacienteIdAndUsuarioId(pacienteId, usuarioId);
    }

    public boolean podeEditarPaciente(Integer usuarioId, Integer pacienteId) {

        if (usuarioId.equals(pacienteId)) {
            return true;
        }

        return vinculoRepository.existsByPacienteIdAndUsuarioIdAndTipo(pacienteId, usuarioId, "responsavel");
    }

    public boolean podeRemoverVinculo(Integer usuarioLogadoId, Vinculo vinculoAlvo) {
        System.out.println("ENTROU NO PODE REMOVER VINCULO");
        // paciente remove qualquer vínculo dele
        if (usuarioLogadoId.equals(vinculoAlvo.getPacienteId())) {
            return true;
        }

        Usuario usuarioLogado = usuarioRepository.findById(usuarioLogadoId).orElse(null);

        if (usuarioLogado == null) {
            System.out.println("usuarioLogadoId = " + usuarioLogadoId);
            return false;
        }
        System.out.println("usuarioLogadoId = " + usuarioLogadoId);
        System.out.println("usuarioLogadoTipo = " + usuarioLogado.getTipo());
        System.out.println("vinculoPacienteId = " + vinculoAlvo.getPacienteId());
        System.out.println("vinculoUsuarioId = " + vinculoAlvo.getUsuarioId());
        System.out.println("vinculoTipo = " + vinculoAlvo.getTipo());

        // médico ou responsável removendo o PRÓPRIO vínculo
        if (usuarioLogadoId.equals(vinculoAlvo.getUsuarioId())) {
            return true;
        }

        // responsável removendo médicos do paciente
        if (usuarioLogado.getTipo().equalsIgnoreCase("responsavel")
                && vinculoAlvo.getTipo().equalsIgnoreCase("saude")) {

            return vinculoRepository.existsByPacienteIdAndUsuarioIdAndTipo(vinculoAlvo.getPacienteId(), usuarioLogadoId, "responsavel");
        }

        return false;
    }

    public boolean isMedico(Usuario usuario) {
        return usuario.getTipo().equalsIgnoreCase("saude");
    }

    public boolean medicoVinculadoAoPaciente(Integer medicoId, Integer pacienteId) {
        return vinculoRepository.existsByPacienteIdAndUsuarioIdAndTipo(pacienteId, medicoId, "saude");
    }

    public boolean responsavelVinculadoAoPaciente(Integer responsavelId, Integer pacienteId) {
        return vinculoRepository.existsByPacienteIdAndUsuarioIdAndTipo(pacienteId, responsavelId, "responsavel");
    }


}