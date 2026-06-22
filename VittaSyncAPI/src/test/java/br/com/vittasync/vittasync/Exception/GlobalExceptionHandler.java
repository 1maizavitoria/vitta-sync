package br.com.vittasync.vittasync.Exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleAcessoNegado() {
        AcessoNegadoException ex = new AcessoNegadoException("Sem permissão");
        ResponseEntity<Map<String, String>> response = handler.handleAcessoNegado(ex);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("AxiosError: Request failed with status code 403", response.getBody().get("name"));
        assertEquals("accessDenied", response.getBody().get("value"));
    }

    @Test
    void testHandleUsuarioJaCadastrado() {
        UsuarioJaCadastradoException ex = new UsuarioJaCadastradoException("Duplicado");
        ResponseEntity<Map<String, String>> response = handler.handleUsuarioJaCadastrado(ex);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals("AxiosError: Request failed with status code 409", response.getBody().get("name"));
        assertEquals("duplicateEmail", response.getBody().get("value"));
    }

    @Test
    void testHandleDadosInvalidos() {
        DadosInvalidosException ex = new DadosInvalidosException("Dados inválidos");
        ResponseEntity<Map<String, String>> response = handler.handleDadosInvalidos(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("AxiosError: Request failed with status code 400", response.getBody().get("name"));
        assertEquals("invalidData", response.getBody().get("value"));
    }

    @Test
    void testHandleNaoEncontrado() {
        RecursoNaoEncontradoException ex = new RecursoNaoEncontradoException("Não achou");
        ResponseEntity<Map<String, String>> response = handler.handleNaoEncontrado(ex);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("AxiosError: Request failed with status code 404", response.getBody().get("name"));
        assertEquals("notFound", response.getBody().get("value"));
    }

    @Test
    void testHandleErroGenerico() {
        Exception ex = new Exception("Erro inesperado");
        ResponseEntity<Map<String, String>> response = handler.handleErroGenerico(ex);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("AxiosError: Request failed with status code 500", response.getBody().get("name"));
        assertEquals("internalServerError", response.getBody().get("value"));
    }

    @Test
    void testHandleValidationErrors() {
        FieldError fieldError = new FieldError("objetoTeste", "campoTeste", "Mensagem de erro");
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objetoTeste");
        bindingResult.addError(fieldError);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<Map<String, String>> response = handler.handleValidationErrors(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("AxiosError: Request failed with status code 400", response.getBody().get("name"));

        assertTrue(response.getBody().get("value").contains("campoTeste: Mensagem de erro"));
    }

}
