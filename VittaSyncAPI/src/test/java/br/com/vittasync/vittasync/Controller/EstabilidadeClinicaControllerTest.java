package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.EstabilidadeClinicaDTO;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.EstabilidadeClinicaService;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.SessaoService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(EstabilidadeClinicaController.class)
@AutoConfigureMockMvc(addFilters = false)
class EstabilidadeClinicaControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private EstabilidadeClinicaService estabilidadeClinicaService;
    @MockBean private JwtService jwtService;
    @MockBean private UsuarioService usuarioService;
    @MockBean private PermissaoService permissaoService;
    @MockBean private SessaoService sessaoService;

    @BeforeEach
    void setup() {
        Usuario usuario = new Usuario(); usuario.setId(1); usuario.setCpf("111");
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");

        when(usuarioService.getUsuarioLogado()).thenReturn(usuario);
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
    }

    @Test
    void testConsultarEstabilidadeOk() throws Exception {
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(true);
        when(estabilidadeClinicaService.consultarIndices(2)).thenReturn(List.of(
                new EstabilidadeClinicaDTO("geral", 9, "saudavel", 1.0, LocalDateTime.now())));

        mockMvc.perform(get("/estabilidadeclinica/paciente/222"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("geral"))
                .andExpect(jsonPath("$[0].categoria").value("saudavel"));
    }

    @Test
    void testConsultarEstabilidadeForbidden() throws Exception {
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(false);

        mockMvc.perform(get("/estabilidadeclinica/paciente/222"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testTestarAlertaOk() throws Exception {
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(true);

        mockMvc.perform(get("/estabilidadeclinica/teste-alerta/222/fc_bpm/critico"))
                .andExpect(status().isOk());

        verify(estabilidadeClinicaService).testarDisparoAlerta(eq(2), anyList());
    }

    @Test
    void testTestarAlertaForbidden() throws Exception {
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(false);

        mockMvc.perform(get("/estabilidadeclinica/teste-alerta/222/fc_bpm/critico"))
                .andExpect(status().isForbidden());
    }
}
