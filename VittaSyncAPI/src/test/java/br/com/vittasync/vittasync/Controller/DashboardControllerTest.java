package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.DashboardResponseDTO;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.DashboardService;
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
import java.time.LocalDate;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(DashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
class DashboardControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private DashboardService dashboardService;
    @MockBean private JwtService jwtService;
    @MockBean private UsuarioService usuarioService;
    @MockBean private PermissaoService permissaoService;
    @MockBean private SessaoService sessaoService;

    private Usuario paciente;

    @BeforeEach
    void setup() {
        Usuario usuario = new Usuario(); usuario.setId(1); usuario.setCpf("111");
        paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222"); paciente.setNome("Paciente");

        when(usuarioService.getUsuarioLogado()).thenReturn(usuario);
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
    }

    @Test
    void testConsultarOk() throws Exception {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 7);
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(true);
        when(dashboardService.consultar(paciente, inicio, fim, "peso")).thenReturn(
                new DashboardResponseDTO("222", "Paciente", inicio, fim, List.of(), List.of(), List.of()));

        mockMvc.perform(get("/dashboard/pacientes/222")
                        .param("inicio", "2026-01-01")
                        .param("fim", "2026-01-07")
                        .param("categorias", "peso"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pacienteCpf").value("222"));
    }

    @Test
    void testConsultarForbidden() throws Exception {
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(false);

        mockMvc.perform(get("/dashboard/pacientes/222"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(dashboardService);
    }
}
