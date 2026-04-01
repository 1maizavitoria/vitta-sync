package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class SinaisVitaisService {

    private final SinaisVitaisRepository repository;

    public SinaisVitaisService(SinaisVitaisRepository repository) {
        this.repository = repository;
    }

    public SinaisVitais create(SinaisVitais sinais) {
        sinais.setDataRegistro(LocalDateTime.now());
        sinais.setDataModificacao(null);
        return repository.save(sinais);
    }

    public SinaisVitais update(Integer id, SinaisVitais novosDados) {
        SinaisVitais existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sublista não encontrada"));
        existente.setFcBpm(novosDados.getFcBpm());
        existente.setFrRpm(novosDados.getFrRpm());
        existente.setPaSistolica(novosDados.getPaSistolica());
        existente.setPaDiastolica(novosDados.getPaDiastolica());
        existente.setTempCelcius(novosDados.getTempCelcius());
        existente.setSpo2Porcento(novosDados.getSpo2Porcento());
        existente.setDataModificacao(LocalDateTime.now());
        return repository.save(existente);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<SinaisVitais> findByPacienteCpf(String cpf) {
        return repository.findByPacienteCpf(cpf);
    }
}
