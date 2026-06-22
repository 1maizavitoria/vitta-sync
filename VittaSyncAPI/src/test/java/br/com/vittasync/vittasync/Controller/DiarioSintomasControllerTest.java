package br.com.vittasync.vittasync.Controller;

import br.com.vittasync.vittasync.Model.DiarioSintomas;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.DiarioSintomasService;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DiarioSintomasController.class)
@AutoConfigureMockMvc(addFilters = false)
class DiarioSintomasControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private DiarioSintomasService service;
    @MockBean private JwtService jwtService;
    @MockBean private UsuarioService usuarioService;
    @MockBean private PermissaoService permissaoService;
    @MockBean private SessaoService sessaoService;

    @Test
    void testCreateOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");
        DiarioSintomas sintoma = new DiarioSintomas();
        sintoma.setId(1);
        sintoma.setPaciente(paciente);
        sintoma.setSintoma("Dor de cabeça");
        sintoma.setIntensidadeDor(5);
        sintoma.setDataReferencia(LocalDate.now());

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.create(any(DiarioSintomas.class), anyInt())).thenReturn(sintoma);

        mockMvc.perform(post("/diariosintomas/cadastrar/222")
                        .header("Authorization","Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sintoma\":\"Dor de cabeça\",\"intensidadeDor\":5,\"dataReferencia\":\"2026-06-21\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");
        DiarioSintomas sintoma = new DiarioSintomas();
        sintoma.setId(1);
        sintoma.setPaciente(paciente);
        sintoma.setSintoma("Dor nas costas");
        sintoma.setIntensidadeDor(7);
        sintoma.setDataReferencia(LocalDate.now());

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.update(anyInt(), any(DiarioSintomas.class), anyInt())).thenReturn(sintoma);

        mockMvc.perform(put("/diariosintomas/editar/1/222")
                        .header("Authorization","Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sintoma\":\"Dor nas costas\",\"intensidadeDor\":7,\"dataReferencia\":\"2026-06-21\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        doNothing().when(service).delete(anyInt(), anyInt());

        mockMvc.perform(delete("/diariosintomas/deletar/1/222")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testListOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");
        DiarioSintomas sintoma = new DiarioSintomas();
        sintoma.setId(1);
        sintoma.setPaciente(paciente);
        sintoma.setSintoma("Náusea");
        sintoma.setIntensidadeDor(3);
        sintoma.setDataReferencia(LocalDate.now());

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.findByPacienteCpf("222")).thenReturn(List.of(sintoma));

        mockMvc.perform(get("/diariosintomas/getSintomas/222")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testListForbidden() throws Exception {
        Usuario u = new Usuario(); u.setId(1); u.setCpf("123");
        Usuario p = new Usuario(); p.setId(2); p.setCpf("456");

        when(jwtService.extrairCpf("token")).thenReturn("123");
        when(usuarioService.searchByCpf("123")).thenReturn(u);
        when(usuarioService.searchByCpf("456")).thenReturn(p);
        when(permissaoService.podeVisualizarPaciente(1,2)).thenReturn(false);

        mockMvc.perform(get("/diariosintomas/getSintomas/456")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isForbidden());
    }
}
