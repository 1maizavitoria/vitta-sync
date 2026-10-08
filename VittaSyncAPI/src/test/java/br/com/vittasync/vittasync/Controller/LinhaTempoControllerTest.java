package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.LinhaTempoResponseDTO;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.LinhaTempoService;
import br.com.vittasync.vittasync.Service.SessaoService;
import br.com.vittasync.vittasync.Service.UsuarioService;
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


@WebMvcTest(LinhaTempoController.class)
@AutoConfigureMockMvc(addFilters = false)
class LinhaTempoControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private LinhaTempoService linhaTempoService;
    @MockBean private JwtService jwtService;
    @MockBean private UsuarioService usuarioService;
    @MockBean private SessaoService sessaoService;

    @Test
    void testConsultarLinhaTempoOk() throws Exception {
        Usuario usuario = new Usuario(); usuario.setId(1); usuario.setCpf("111");
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 7);

        when(usuarioService.getUsuarioLogado()).thenReturn(usuario);
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(linhaTempoService.consultarLinhaTempo(1, 2, inicio, fim))
                .thenReturn(new LinhaTempoResponseDTO(List.of(), List.of(), List.of(), List.of()));

        mockMvc.perform(get("/api/linha-tempo/paciente/222")
                        .param("inicio", "2026-01-01")
                        .param("fim", "2026-01-07"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itens").isArray());

        verify(linhaTempoService).consultarLinhaTempo(1, 2, inicio, fim);
    }
}
