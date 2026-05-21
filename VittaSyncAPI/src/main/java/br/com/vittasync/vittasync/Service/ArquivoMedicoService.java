package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.ArquivoMedico;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.ArquivoMedicoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class ArquivoMedicoService {

    private final ArquivoMedicoRepository repository;

    public ArquivoMedicoService(ArquivoMedicoRepository repository) {
        this.repository = repository;
    }

    public ArquivoMedico upload(Usuario medico, Usuario paciente, String nomeArquivo, byte[] arquivo) {
        ArquivoMedico doc = new ArquivoMedico();
        doc.setMedico(medico);
        doc.setPaciente(paciente);
        doc.setNomeArquivo(nomeArquivo);
        doc.setArquivo(arquivo);
        doc.setDataUpload(LocalDateTime.now());
        return repository.save(doc);
    }

    public List<ArquivoMedico> listarPorPaciente(Usuario paciente) {
        return repository.findByPaciente(paciente);
    }

    public List<ArquivoMedico> listarPorMedico(Usuario medico) {
        return repository.findByMedico(medico);
    }

    public ArquivoMedico visualizar(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Documento não encontrado"));
    }

    public void deletar(Usuario medico, Integer id) {
        ArquivoMedico doc = repository.findById(id).orElseThrow(() -> new RuntimeException("Documento não encontrado"));
        if (!doc.getMedico().getId().equals(medico.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar este documento");
        }
        repository.delete(doc);
    }
}
