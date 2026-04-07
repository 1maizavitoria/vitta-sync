package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.SessaoToken;
import br.com.vittasync.vittasync.Repository.SessaoTokenRepository;
import org.springframework.stereotype.Service;


@Service
public class SessaoService {

    private final SessaoTokenRepository tokenRepo;
    private final JwtService jwtService;

    public SessaoService(SessaoTokenRepository tokenRepo, JwtService jwtService) {
        this.tokenRepo = tokenRepo;
        this.jwtService = jwtService;
    }

    public void registrarToken(String token) {
        SessaoToken st = new SessaoToken();
        st.setToken(token);
        st.setAtivo(true);
        tokenRepo.save(st);
    }

    public void logout(String token) {
        if (!jwtService.validarToken(token)) {
            throw new RuntimeException("Token JWT inválido ou expirado");
        }

        SessaoToken st = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token não encontrado na sessão"));

        st.setAtivo(false);
        tokenRepo.save(st);
    }

    public boolean isTokenAtivo(String token) {
        return jwtService.validarToken(token) &&
                tokenRepo.findByToken(token)
                        .map(SessaoToken::getAtivo)
                        .orElse(false);
    }
}

