package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.Controller.SessaoController;
import br.com.vittasync.vittasync.Service.SessaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class SessaoControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private SessaoService sessaoService;

    @Test
    void testLogoutSuccess() throws Exception {
        doNothing().when(sessaoService).logout(anyString());

        mockMvc.perform(post("/sessao/logout")
                        .header("Authorization","Bearer token123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout realizado com sucesso."));
    }

    @Test
    void testLogoutUnauthorized() throws Exception {
        doThrow(new RuntimeException("Token inválido")).when(sessaoService).logout(anyString());

        mockMvc.perform(post("/sessao/logout")
                        .header("Authorization","Bearer token123"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token inválido"));
    }
}

