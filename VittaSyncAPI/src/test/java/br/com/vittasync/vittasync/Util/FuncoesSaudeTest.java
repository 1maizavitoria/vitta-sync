package br.com.vittasync.vittasync.Util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FuncoesSaudeTest {

    @Test
    void testConstantes() {
        assertThat(FuncoesSaude.MEDICO_PRINCIPAL).isEqualTo("medico_principal");
        assertThat(FuncoesSaude.ESPECIALISTA).isEqualTo("especialista");
        assertThat(FuncoesSaude.CONSULTOR).isEqualTo("consultor");
        assertThat(FuncoesSaude.ACOMPANHAMENTO_CLINICO).isEqualTo("acompanhamento_clinico");
        assertThat(FuncoesSaude.EQUIPE_ASSISTENCIAL).isEqualTo("equipe_assistencial");
    }

    @Test
    void testValidasContemTodasConstantes() {
        assertThat(FuncoesSaude.VALIDAS)
                .containsExactlyInAnyOrder(
                        FuncoesSaude.MEDICO_PRINCIPAL,
                        FuncoesSaude.ESPECIALISTA,
                        FuncoesSaude.CONSULTOR,
                        FuncoesSaude.ACOMPANHAMENTO_CLINICO,
                        FuncoesSaude.EQUIPE_ASSISTENCIAL
                );
    }
}
