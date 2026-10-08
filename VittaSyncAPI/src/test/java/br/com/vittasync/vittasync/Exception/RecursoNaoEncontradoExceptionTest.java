package br.com.vittasync.vittasync.Exception;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class RecursoNaoEncontradoExceptionTest {

    @Test
    void testMensagemException() {
        RecursoNaoEncontradoException ex = new RecursoNaoEncontradoException("Item não encontrado");
        assertEquals("Item não encontrado", ex.getMessage());
    }
}
