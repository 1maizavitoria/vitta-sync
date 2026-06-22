package br.com.vittasync.vittasync.Controller;

import br.com.vittasync.vittasync.Model.EventoPaciente;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.EventoPacienteService;
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

import java.sql.Timestamp;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventoPacienteController.class)
@AutoConfigureMockMvc(addFilters = false)
class EventoPacienteControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private EventoPacienteService service;
    @MockBean private JwtService jwtService;
    @MockBean private UsuarioService usuarioService;
    @MockBean private PermissaoService permissaoService;
    @MockBean private SessaoService sessaoService;

    @Test
    void testListarPorPacienteOk() throws Exception {
        Usuario usuario = new Usuario(); usuario.setId(1); usuario.setNome("Dr. Silva"); usuario.setTipo("MEDICO");
        EventoPaciente evento = new EventoPaciente();
        evento.setPacienteId(2);
        evento.setUsuarioId(1);
        evento.setTipoEvento("consulta");
        evento.setTitulo("Consulta marcada");
        evento.setDescricao("Paciente tem consulta amanhã");
        evento.setVisualizado(false);
        evento.setCriadoEm(new Timestamp(System.currentTimeMillis()));
        evento.setPrioridade("NORMAL");

        when(jwtService.extrairCpf("token")).thenReturn("123");
        when(usuarioService.searchByCpf("123")).thenReturn(usuario);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.listarPorPaciente(2)).thenReturn(List.of(evento));
        when(usuarioService.searchById(1)).thenReturn(usuario);

        mockMvc.perform(get("/eventos/paciente/2")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testContarNaoVisualizadosOk() throws Exception {
        Usuario usuario = new Usuario(); usuario.setId(1);

        when(jwtService.extrairCpf("token")).thenReturn("123");
        when(usuarioService.searchByCpf("123")).thenReturn(usuario);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.contarNaoVisualizados(2, 1)).thenReturn(5L);

        mockMvc.perform(get("/eventos/paciente/2/nao-visualizados")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testVisualizarEventosOk() throws Exception {
        Usuario usuario = new Usuario(); usuario.setId(1);

        when(jwtService.extrairCpf("token")).thenReturn("123");
        when(usuarioService.searchByCpf("123")).thenReturn(usuario);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(true);
        doNothing().when(service).marcarComoVisualizados(2, 1);

        mockMvc.perform(put("/eventos/paciente/2/visualizar")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testListarForbidden() throws Exception {
        Usuario usuario = new Usuario(); usuario.setId(1);

        when(jwtService.extrairCpf("token")).thenReturn("123");
        when(usuarioService.searchByCpf("123")).thenReturn(usuario);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(false);

        mockMvc.perform(get("/eventos/paciente/2")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isForbidden());
    }
}
