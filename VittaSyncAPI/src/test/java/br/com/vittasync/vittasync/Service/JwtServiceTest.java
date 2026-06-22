package br.com.vittasync.vittasync.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();
    }

    @Test
    void testGerarTokenContemCpf() {
        String cpf = "12345678901";
        String token = jwtService.gerarToken(cpf);

        String extraido = jwtService.extrairCpf(token);

        assertThat(extraido).isEqualTo(cpf);
    }

    @Test
    void testValidarTokenValido() {
        String token = jwtService.gerarToken("12345678901");

        boolean valido = jwtService.validarToken(token);

        assertThat(valido).isTrue();
    }

    @Test
    void testValidarTokenInvalido() {
        // Token inválido (string aleatória)
        String token = "abc.def.ghi";

        boolean valido = jwtService.validarToken(token);

        assertThat(valido).isFalse();
    }

    @Test
    void testExtrairCpfDoToken() {
        String cpf = "98765432100";
        String token = jwtService.gerarToken(cpf);

        String extraido = jwtService.extrairCpf(token);

        assertThat(extraido).isEqualTo(cpf);
    }
}
