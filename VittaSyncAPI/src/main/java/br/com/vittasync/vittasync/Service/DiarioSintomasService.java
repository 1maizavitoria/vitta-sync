package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.DiarioSintomas;
import br.com.vittasync.vittasync.Repository.DiarioSintomasRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiarioSintomasService {

    private final DiarioSintomasRepository repository;

    public DiarioSintomasService(DiarioSintomasRepository repository) {
        this.repository = repository;
    }

    public DiarioSintomas create(DiarioSintomas sintoma) {
        sintoma.setDataRegistro(LocalDateTime.now());
        sintoma.setDataModificacao(null);
        return repository.save(sintoma);
    }

    public DiarioSintomas update(Integer id, DiarioSintomas novosDados) {
        DiarioSintomas existente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sintoma não encontrado"));
        existente.setSintoma(novosDados.getSintoma());
        existente.setIntensidadeDor(novosDados.getIntensidadeDor());
        existente.setDataReferencia(novosDados.getDataReferencia());
        existente.setDataModificacao(LocalDateTime.now());
        return repository.save(existente);
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Sintoma não encontrado");
        }
        repository.deleteById(id);
    }

    public List<DiarioSintomas> findByPacienteCpf(String cpf) {
        return repository.findByPacienteCpf(cpf);
    }
}
