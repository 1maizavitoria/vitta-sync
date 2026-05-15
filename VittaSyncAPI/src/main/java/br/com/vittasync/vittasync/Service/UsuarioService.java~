package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.SessaoTokenRepository;
import br.com.vittasync.vittasync.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final SessaoTokenRepository tokenRepo;
    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          SessaoTokenRepository tokenRepo,
                          JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepo = tokenRepo;
        this.jwtService = jwtService;
    }

    public Usuario create(Usuario usuario) {
        usuario.setDataCadastro(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    public Usuario searchByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario searchByCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario update(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void delete(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        tokenRepo.findAll().stream()
                .filter(t -> {
                    try {
                        return jwtService.extrairCpf(t.getToken()).equals(usuario.getCpf());
                    } catch (Exception e) {
                        return false;
                    }
                })
                .forEach(t -> {
                    t.setAtivo(false);
                    tokenRepo.save(t);
                });

        usuarioRepository.deleteById(id);
    }
}
