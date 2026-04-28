package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Model.LembreteMedicao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.LembreteMedicaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LembreteMedicaoService {

    private final LembreteMedicaoRepository repository;

    public LembreteMedicaoService(LembreteMedicaoRepository repository) {
        this.repository = repository;
    }

    public LembreteMedicao salvarSubstituir(LembreteMedicao novoLembrete) {
        repository.findByUsuario(novoLembrete.getUsuario())
                .ifPresent(repository::delete);
        return repository.save(novoLembrete);
    }

    public Optional<LembreteMedicao> searchlembrete(Usuario usuario) {
        return repository.findByUsuario(usuario);
    }

    public List<LembreteMedicao> searchLembreteAtivo() {
        return repository.findByAtivoTrue();
    }

    public Optional<LembreteMedicao> ativar(Usuario usuario) {
        Optional<LembreteMedicao> lembreteOpt = repository.findByUsuario(usuario);
        lembreteOpt.ifPresent(lembrete -> {
            lembrete.setAtivo(true);
            repository.save(lembrete);
        });
        return lembreteOpt;
    }

    public Optional<LembreteMedicao> desativar(Usuario usuario) {
        Optional<LembreteMedicao> lembreteOpt = repository.findByUsuario(usuario);
        lembreteOpt.ifPresent(lembrete -> {
            lembrete.setAtivo(false);
            repository.save(lembrete);
        });
        return lembreteOpt;
    }
}
