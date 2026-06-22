package br.com.vittasync.vittasync.Controller;

import br.com.vittasync.vittasync.Model.LembreteMedicao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.LembreteMedicaoService;
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

import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LembreteMedicaoController.class)
@AutoConfigureMockMvc(addFilters = false)
class LembreteMedicaoControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private LembreteMedicaoService service;
    @MockBean private JwtService jwtService;
    @MockBean private UsuarioService usuarioService;
    @MockBean private PermissaoService permissaoService;
    @MockBean private SessaoService sessaoService;

    @Test
    void testCreateOk() throws Exception {
        Usuario usuario = new Usuario(); usuario.setId(1); usuario.setCpf("123");
        LembreteMedicao lembrete = new LembreteMedicao();
        lembrete.setUsuario(usuario);
        lembrete.setDiasSemana("SEGUNDA");
        lembrete.setHorario(LocalTime.of(8,0));
        lembrete.setAtivo(true);

        when(jwtService.extrairCpf("token")).thenReturn("123");
        when(usuarioService.searchByCpf("123")).thenReturn(usuario);
        when(usuarioService.searchByCpf("123")).thenReturn(usuario);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.salvarSubstituir(any(LembreteMedicao.class))).thenReturn(lembrete);

        mockMvc.perform(post("/lembretes/registrar/123")
                        .header("Authorization","Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"diasSemana\":\"segunda\",\"horario\":\"08:00:00\",\"ativo\":true,\"enviarEmail\":true,\"enviarSms\":false}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetLembreteOk() throws Exception {
        Usuario usuario = new Usuario(); usuario.setId(1); usuario.setCpf("123");
        LembreteMedicao lembrete = new LembreteMedicao();
        lembrete.setUsuario(usuario);
        lembrete.setDiasSemana("SEGUNDA");
        lembrete.setHorario(LocalTime.of(8,0));
        lembrete.setAtivo(true);

        when(jwtService.extrairCpf("token")).thenReturn("123");
        when(usuarioService.searchByCpf("123")).thenReturn(usuario);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.searchlembrete(usuario)).thenReturn(Optional.of(lembrete));

        mockMvc.perform(get("/lembretes/getLembrete/123")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testAtivarOk() throws Exception {
        Usuario usuario = new Usuario(); usuario.setId(1); usuario.setCpf("123");
        LembreteMedicao lembrete = new LembreteMedicao();
        lembrete.setUsuario(usuario);
        lembrete.setAtivo(true);

        when(jwtService.extrairCpf("token")).thenReturn("123");
        when(usuarioService.searchByCpf("123")).thenReturn(usuario);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.ativar(usuario)).thenReturn(Optional.of(lembrete));

        mockMvc.perform(put("/lembretes/ativar/123")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testDesativarOk() throws Exception {
        Usuario usuario = new Usuario(); usuario.setId(1); usuario.setCpf("123");
        LembreteMedicao lembrete = new LembreteMedicao();
        lembrete.setUsuario(usuario);
        lembrete.setAtivo(false);

        when(jwtService.extrairCpf("token")).thenReturn("123");
        when(usuarioService.searchByCpf("123")).thenReturn(usuario);
        when(permissaoService.podeEditarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.desativar(usuario)).thenReturn(Optional.of(lembrete));

        mockMvc.perform(put("/lembretes/desativar/123")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetLembreteUnauthorized() throws Exception {
        Usuario usuarioLogado = new Usuario(); usuarioLogado.setId(1); usuarioLogado.setCpf("123");
        Usuario usuarioPaciente = new Usuario(); usuarioPaciente.setId(2); usuarioPaciente.setCpf("456");

        when(jwtService.extrairCpf("token")).thenReturn("123");
        when(usuarioService.searchByCpf("123")).thenReturn(usuarioLogado);
        when(usuarioService.searchByCpf("456")).thenReturn(usuarioPaciente);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(false);

        mockMvc.perform(get("/lembretes/getLembrete/456")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isForbidden());
    }
}
