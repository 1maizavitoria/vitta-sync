package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.Controller.AuthController;
import br.com.vittasync.vittasync.Service.AuthService;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.SessaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private AuthService authService;
    @MockBean private JwtService jwtService;
    @MockBean private SessaoService sessaoService;

    @Test
    @WithMockUser(username = "testuser")
    void testLoginSuccess() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cpf\":\"123\",\"senha\":\"senha\",\"canal\":\"email\"}"))
                .andExpect(status().isOk());

        verify(authService).login("123","senha","email");
    }

    @Test
    @WithMockUser(username = "testuser")
    void testValidarCodigoLogin() throws Exception {
        when(authService.validarCodigoLogin("abc")).thenReturn("token123");

        mockMvc.perform(post("/auth/validar-codigo-login")
                        .with(csrf())
                        .param("codigo","abc"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testEnviarCodigoRedefinirSenha() throws Exception {
        mockMvc.perform(post("/auth/enviar-codigo-redefinir-senha")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"teste@email.com\",\"canal\":\"sms\"}"))
                .andExpect(status().isOk());

        verify(authService).enviarCodigoRedefinirSenha("teste@email.com","sms");
    }

    @Test
    @WithMockUser(username = "testuser")
    void testRedefinirSenha() throws Exception {
        mockMvc.perform(post("/auth/redefinir-senha")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"abc\",\"novaSenha\":\"nova123\"}"))
                .andExpect(status().isOk());

        verify(authService).redefinirSenha("abc","nova123");
    }
}
