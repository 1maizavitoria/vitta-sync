package br.com.vittasync.vittasync.Util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FuncoesResponsavelTest {

    @Test
    void testConstantes() {
        assertThat(FuncoesResponsavel.CUIDADOR).isEqualTo("cuidador");
        assertThat(FuncoesResponsavel.RESPONSAVEL_LEGAL).isEqualTo("responsavel_legal");
        assertThat(FuncoesResponsavel.ACOMPANHANTE).isEqualTo("acompanhante");
        assertThat(FuncoesResponsavel.CONTATO_EMERGENCIA).isEqualTo("contato_emergencia");
        assertThat(FuncoesResponsavel.TUTOR).isEqualTo("tutor");
    }

    @Test
    void testValidasContemTodasConstantes() {
        assertThat(FuncoesResponsavel.VALIDAS)
                .containsExactlyInAnyOrder(
                        FuncoesResponsavel.CUIDADOR,
                        FuncoesResponsavel.RESPONSAVEL_LEGAL,
                        FuncoesResponsavel.ACOMPANHANTE,
                        FuncoesResponsavel.CONTATO_EMERGENCIA,
                        FuncoesResponsavel.TUTOR
                );
    }
}
