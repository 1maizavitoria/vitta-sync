package br.com.vittasync.vittasync.Controller;

import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.ContatoEmergenciaService;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.SessaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContatoEmergenciaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContatoEmergenciaControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private ContatoEmergenciaService service;
    @MockBean private JwtService jwtService;
    @MockBean private UsuarioService usuarioService;
    @MockBean private PermissaoService permissaoService;
    @MockBean private SessaoService sessaoService;

    @Test
    void testCadastrarOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");
        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setId(1);
        contato.setPaciente(paciente);
        contato.setNome("Maria");
        contato.setTelefone("999999999");

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.create(anyInt(), any(Usuario.class), any(ContatoEmergencia.class))).thenReturn(contato);

        mockMvc.perform(post("/contatoemergencia/cadastrar/222")
                        .header("Authorization","Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Maria\",\"telefone\":\"999999999\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testEditarOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");
        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setId(1);
        contato.setPaciente(paciente);
        contato.setNome("João");
        contato.setTelefone("888888888");

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.update(anyInt(), any(ContatoEmergencia.class))).thenReturn(contato);

        mockMvc.perform(put("/contatoemergencia/editar/1/222")
                        .header("Authorization","Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"João\",\"telefone\":\"888888888\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletarOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        doNothing().when(service).delete(anyInt(), anyInt());

        mockMvc.perform(delete("/contatoemergencia/deletar/1/222")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListarOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");
        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setId(1);
        contato.setPaciente(paciente);
        contato.setNome("Carlos");
        contato.setTelefone("777777777");

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.listar(anyInt(), any(Usuario.class))).thenReturn(List.of(contato));

        mockMvc.perform(get("/contatoemergencia/listar/222")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testListarForbidden() throws Exception {
        Usuario usuarioLogado = new Usuario(); usuarioLogado.setId(1); usuarioLogado.setCpf("123");
        Usuario usuarioPaciente = new Usuario(); usuarioPaciente.setId(2); usuarioPaciente.setCpf("456");

        when(jwtService.extrairCpf("token")).thenReturn("123");
        when(usuarioService.searchByCpf("123")).thenReturn(usuarioLogado);
        when(usuarioService.searchByCpf("456")).thenReturn(usuarioPaciente);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(false);

        mockMvc.perform(get("/contatoemergencia/listar/456")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isForbidden());
    }
}
