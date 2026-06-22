package br.com.vittasync.vittasync.Controller;

import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.SessaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private UsuarioService usuarioService;
    @MockBean private JwtService jwtService;
    @MockBean private PermissaoService permissaoService;
    @MockBean private SessaoService sessaoService;

    @Test
    void testCadastrarUsuarioOk() throws Exception {
        Usuario u = new Usuario();
        u.setId(1);
        u.setCpf("12345678900");
        u.setNome("João Silva");
        u.setEmail("joao@email.com");

        when(usuarioService.create(any(Usuario.class))).thenReturn(u);

        mockMvc.perform(post("/usuario/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cpf\":\"12345678900\",\"nome\":\"João Silva\",\"email\":\"joao@email.com\",\"telefone\":\"41999999999\",\"senha\":\"123456\",\"tipo\":\"paciente\",\"pesoInicial\":70.0,\"altura\":1.75,\"funcaoResponsavel\":\"responsável\",\"dataNascimento\":\"2000-01-01\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testEditarUsuarioOk() throws Exception {
        Usuario u = new Usuario();
        u.setId(1);
        u.setCpf("12345678900");
        u.setNome("João Atualizado");
        u.setEmail("joao.atualizado@email.com");

        when(jwtService.extrairCpf("token")).thenReturn("12345678900");
        when(usuarioService.searchByCpf("12345678900")).thenReturn(u);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(usuarioService.update(any(Usuario.class))).thenReturn(u);

        mockMvc.perform(put("/usuario/editar/12345678900")
                        .header("Authorization","Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"João Atualizado\",\"email\":\"joao.atualizado@email.com\",\"telefone\":\"41988888888\",\"altura\":1.75,\"funcaoResponsavel\":\"responsável\",\"dataNascimento\":\"2000-01-01\",\"pesoInicial\":72.0}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUsuarioOk() throws Exception {
        Usuario u = new Usuario();
        u.setId(1);
        u.setCpf("12345678900");
        u.setNome("João Silva");

        when(jwtService.extrairCpf("token")).thenReturn("12345678900");
        when(usuarioService.searchByCpf("12345678900")).thenReturn(u);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(true);

        mockMvc.perform(get("/usuario/getUsuario/12345678900")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletarUsuarioOk() throws Exception {
        Usuario u = new Usuario();
        u.setId(1);
        u.setCpf("12345678900");

        when(jwtService.extrairCpf("token")).thenReturn("12345678900");
        when(usuarioService.searchByCpf("12345678900")).thenReturn(u);
        doNothing().when(usuarioService).delete(anyInt());

        mockMvc.perform(delete("/usuario/deletar/12345678900")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }
}
