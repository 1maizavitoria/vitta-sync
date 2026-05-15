package br.com.vittasync.vittasync.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleUsuarioJaCadastrado(UsuarioJaCadastradoException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("name", "AxiosError: Request failed with status code 409");
        error.put("value", "duplicateEmail");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<Map<String, String>> handleAcessoNegado(AcessoNegadoException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("name", "AxiosError: Request failed with status code 403");
        error.put("value", "accessDenied");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<Map<String, String>> handleDadosInvalidos(DadosInvalidosException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("name", "AxiosError: Request failed with status code 400");
        error.put("value", "invalidData");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleNaoEncontrado(RecursoNaoEncontradoException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("name", "AxiosError: Request failed with status code 404");
        error.put("value", "notFound");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos");

        Map<String, String> error = new HashMap<>();
        error.put("name", "AxiosError: Request failed with status code 400");
        error.put("value", msg);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleErroGenerico(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("name", "AxiosError: Request failed with status code 500");
        error.put("value", "internalServerError");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
