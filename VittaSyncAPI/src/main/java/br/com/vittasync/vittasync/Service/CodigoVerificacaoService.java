package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.CodigoVerificacao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.CodigoVerificacaoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;


@Service
public class CodigoVerificacaoService {

    private final CodigoVerificacaoRepository codigoRepository;

    public CodigoVerificacaoService(CodigoVerificacaoRepository codigoRepository) {
        this.codigoRepository = codigoRepository;
    }

    public CodigoVerificacao gerarCodigo(Usuario usuario, String tipo) {
        codigoRepository.findByUsuarioIdAndTipoAndUtilizadoFalse(usuario.getId(), tipo)
                .forEach(c -> {
                    c.setUtilizado(true);
                    codigoRepository.save(c);
                });

        String codigo = String.valueOf(new Random().nextInt(90000) + 10000);
        CodigoVerificacao cv = new CodigoVerificacao();

        cv.setUsuario(usuario);
        cv.setCodigo(codigo);
        cv.setTipo(tipo);
        cv.setExpira(LocalDateTime.now().plusMinutes(10));
        cv.setUtilizado(false);

        return codigoRepository.save(cv);
    }

    public CodigoVerificacao validarCodigo(String codigo, String tipo) {
        CodigoVerificacao cv = codigoRepository.findCodigoValido(codigo, tipo)
                .orElseThrow(() -> new RuntimeException("Código inválido ou expirado"));

        cv.setUtilizado(true);
        codigoRepository.save(cv);

        return cv;
    }
}
