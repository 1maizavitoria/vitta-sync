package br.com.vittasync.vittasync.Util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HashUtilTest {

    @Test
    void testHashSenhaConsistencia() {
        String senha = "minhaSenha123";
        String hash1 = HashUtil.hashSenha(senha);
        String hash2 = HashUtil.hashSenha(senha);
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void testHashSenhaNaoIgualSenhaOriginal() {
        String senha = "minhaSenha123";
        String hash = HashUtil.hashSenha(senha);
        assertThat(hash).isNotEqualTo(senha);
    }

    @Test
    void testHashSenhaFormatoHexadecimal() {
        String senha = "testeHex";
        String hash = HashUtil.hashSenha(senha);
        assertThat(hash).matches("^[0-9a-f]+$");
    }

    @Test
    void testHashSenhaTamanhoCorreto() {
        String senha = "testeTamanho";
        String hash = HashUtil.hashSenha(senha);
        assertThat(hash.length()).isEqualTo(64);
    }
}
