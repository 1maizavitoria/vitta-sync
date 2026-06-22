package br.com.vittasync.vittasync.Controller;

import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HabitosController.class)
@AutoConfigureMockMvc(addFilters = false)
class HabitosControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private HabitosService service;
    @MockBean private JwtService jwtService;
    @MockBean private UsuarioService usuarioService;
    @MockBean private PermissaoService permissaoService;
    @MockBean private SessaoService sessaoService;
    @MockBean private EventoPacienteService eventoPacienteService;

    @Test
    void testCreateOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");
        Habitos habito = new Habitos();
        habito.setId(1);
        habito.setPaciente(paciente);
        habito.setHorasSono(8);
        habito.setMinutosExercicio(30);
        habito.setDataReferencia(LocalDate.now());

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.create(any(Habitos.class), anyInt())).thenReturn(habito);

        mockMvc.perform(post("/habitos/cadastrar/222")
                        .header("Authorization","Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"horasSono\":8,\"minutosExercicio\":30,\"dataReferencia\":\"2026-06-21\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");
        Habitos habito = new Habitos();
        habito.setId(1);
        habito.setPaciente(paciente);
        habito.setHorasSono(7);
        habito.setMinutosExercicio(45);
        habito.setDataReferencia(LocalDate.now());

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.update(anyInt(), any(Habitos.class), anyInt())).thenReturn(habito);

        mockMvc.perform(put("/habitos/editar/1/222")
                        .header("Authorization","Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"horasSono\":7,\"minutosExercicio\":45,\"dataReferencia\":\"2026-06-21\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        doNothing().when(service).delete(anyInt(), anyInt());

        mockMvc.perform(delete("/habitos/deletar/1/222")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");
        Habitos habito = new Habitos();
        habito.setId(1);
        habito.setPaciente(paciente);
        habito.setHorasSono(6);
        habito.setMinutosExercicio(20);
        habito.setDataReferencia(LocalDate.now());

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.findByPacienteCpf("222")).thenReturn(List.of(habito));

        mockMvc.perform(get("/habitos/getHabitos/222")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testListForbidden() throws Exception {
        Usuario usuarioLogado = new Usuario(); usuarioLogado.setId(1); usuarioLogado.setCpf("123");
        Usuario usuarioPaciente = new Usuario(); usuarioPaciente.setId(2); usuarioPaciente.setCpf("456");

        when(jwtService.extrairCpf("token")).thenReturn("123");
        when(usuarioService.searchByCpf("123")).thenReturn(usuarioLogado);
        when(usuarioService.searchByCpf("456")).thenReturn(usuarioPaciente);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(false);

        mockMvc.perform(get("/habitos/getHabitos/456")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isForbidden());
    }
}
