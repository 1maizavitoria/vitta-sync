package br.com.vittasync.vittasync.Controller;

import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.SinaisVitaisService;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.EventoPacienteService;
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

@WebMvcTest(SinaisVitaisController.class)
@AutoConfigureMockMvc(addFilters = false)
class SinaisVitaisControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private SinaisVitaisService service;
    @MockBean private JwtService jwtService;
    @MockBean private UsuarioService usuarioService;
    @MockBean private PermissaoService permissaoService;
    @MockBean private EventoPacienteService eventoPacienteService;
    @MockBean private SessaoService sessaoService;

    @Test
    void testCadastrarOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");
        SinaisVitais vitais = new SinaisVitais(); vitais.setId(1); vitais.setPaciente(paciente);

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.create(any(SinaisVitais.class), anyInt())).thenReturn(vitais);

        mockMvc.perform(post("/sinaisvitais/cadastrar/222")
                        .header("Authorization","Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"peso\":70.0,\"fcBpm\":70,\"frRpm\":16,\"paSistolica\":120,\"paDiastolica\":80,\"tempCelcius\":36.5,\"spo2Porcento\":98}"))
                .andExpect(status().isOk());
    }

    @Test
    void testEditarOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");
        SinaisVitais vitais = new SinaisVitais(); vitais.setId(1); vitais.setPaciente(paciente);

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.update(anyInt(), any(SinaisVitais.class), anyInt())).thenReturn(vitais);

        mockMvc.perform(put("/sinaisvitais/editar/1/222")
                        .header("Authorization","Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"peso\":72.0,\"fcBpm\":75,\"frRpm\":18,\"paSistolica\":130,\"paDiastolica\":85,\"tempCelcius\":37.0,\"spo2Porcento\":97}"))
                .andExpect(status().isOk());
    }

    @Test
    void testListarOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");
        SinaisVitais vitais = new SinaisVitais(); vitais.setId(1); vitais.setPaciente(paciente);

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.findByPacienteCpf("222")).thenReturn(List.of(vitais));

        mockMvc.perform(get("/sinaisvitais/getSinaisVitais/222")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletarOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        doNothing().when(service).delete(anyInt(), anyInt());

        mockMvc.perform(delete("/sinaisvitais/deletar/1/222")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListUnauthorized() throws Exception {
        Usuario usuarioLogado = new Usuario(); usuarioLogado.setId(1); usuarioLogado.setCpf("123");
        Usuario usuarioPaciente = new Usuario(); usuarioPaciente.setId(2); usuarioPaciente.setCpf("456");

        when(jwtService.extrairCpf("token")).thenReturn("123");
        when(usuarioService.searchByCpf("123")).thenReturn(usuarioLogado);
        when(usuarioService.searchByCpf("456")).thenReturn(usuarioPaciente);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(false);

        mockMvc.perform(get("/sinaisvitais/getSinaisVitais/456")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isForbidden());
    }
}
