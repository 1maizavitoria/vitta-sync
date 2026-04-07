package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class HabitosService {

    private final HabitosRepository repository;

    public HabitosService(HabitosRepository repository) {
        this.repository = repository;
    }

    public Habitos create(Habitos habito) {
        habito.setDataRegistro(LocalDateTime.now());
        habito.setDataModificacao(null);
        return repository.save(habito);
    }

    public Habitos update(Integer id, Habitos novosDados) {
        Habitos existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sublista não encontrada"));
        existente.setHorasSono(novosDados.getHorasSono());
        existente.setMinutosExercicio(novosDados.getMinutosExercicio());
        existente.setDataReferencia(novosDados.getDataReferencia());
        existente.setDataModificacao(LocalDateTime.now());
        return repository.save(existente);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Habitos> findByPacienteCpf(String cpf) {
        return repository.findByPacienteCpf(cpf);
    }
}
