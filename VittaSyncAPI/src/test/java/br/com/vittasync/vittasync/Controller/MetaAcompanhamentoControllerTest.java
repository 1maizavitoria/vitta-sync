package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.Model.MetaAcompanhamento;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.MetaAcompanhamentoService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.SessaoService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MetaAcompanhamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
class MetaAcompanhamentoControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private MetaAcompanhamentoService service;
    @MockBean private JwtService jwtService;
    @MockBean private UsuarioService usuarioService;
    @MockBean private PermissaoService permissaoService;
    @MockBean private SessaoService sessaoService;

    private MetaAcompanhamento meta;

    private static final String JSON = "{\"nome\":\"Perder peso\",\"tipoDado\":\"sinais_vitais\",\"indicador\":\"peso\","
            + "\"direcao\":\"reduzir\",\"valorInicial\":90,\"valorAtual\":90,\"unidade\":\"kg\",\"valorAlvo\":80,"
            + "\"dataLimite\":\"2026-12-31\"}";

    @BeforeEach
    void setup() {
        Usuario usuario = new Usuario(); usuario.setId(1); usuario.setCpf("111");
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222");

        meta = new MetaAcompanhamento();
        meta.setId(1L);
        meta.setPaciente(paciente);
        meta.setNome("Perder peso");
        meta.setIndicador("peso");
        meta.setValorAlvo(80.0);
        meta.setDataLimite(LocalDate.of(2026, 12, 31));
        meta.setStatus("em_andamento");

        when(usuarioService.getUsuarioLogado()).thenReturn(usuario);
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
    }

    @Test
    void testCadastrarOk() throws Exception {
        when(permissaoService.podeCriarEditarMeta(1, 2)).thenReturn(true);
        when(service.create(any(), any(), eq(1))).thenReturn(meta);

        mockMvc.perform(post("/metaacompanhamento/cadastrar/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Perder peso"));
    }

    @Test
    void testCadastrarForbidden() throws Exception {
        when(permissaoService.podeCriarEditarMeta(1, 2)).thenReturn(false);

        mockMvc.perform(post("/metaacompanhamento/cadastrar/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testEditarOk() throws Exception {
        when(permissaoService.podeCriarEditarMeta(1, 2)).thenReturn(true);
        when(service.update(eq(1L), any(), any(), eq(1))).thenReturn(meta);

        mockMvc.perform(put("/metaacompanhamento/editar/1/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testEditarForbidden() throws Exception {
        when(permissaoService.podeCriarEditarMeta(1, 2)).thenReturn(false);

        mockMvc.perform(put("/metaacompanhamento/editar/1/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeletarOk() throws Exception {
        when(permissaoService.podeEditarPaciente(1, 2)).thenReturn(true);

        mockMvc.perform(delete("/metaacompanhamento/deletar/1/222"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L, 2, 1);
    }

    @Test
    void testDeletarForbidden() throws Exception {
        when(permissaoService.podeEditarPaciente(1, 2)).thenReturn(false);

        mockMvc.perform(delete("/metaacompanhamento/deletar/1/222"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testConcluirOk() throws Exception {
        when(permissaoService.podeCriarEditarMeta(1, 2)).thenReturn(true);
        when(service.concluirMeta(1L, 2, 1)).thenReturn(meta);

        mockMvc.perform(post("/metaacompanhamento/concluir/1/222"))
                .andExpect(status().isOk());
    }

    @Test
    void testConcluirForbidden() throws Exception {
        when(permissaoService.podeCriarEditarMeta(1, 2)).thenReturn(false);

        mockMvc.perform(post("/metaacompanhamento/concluir/1/222"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testListarOk() throws Exception {
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(true);
        when(service.listarPorPaciente(2, 1)).thenReturn(List.of(meta));

        mockMvc.perform(get("/metaacompanhamento/getMetas/222"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testListarForbidden() throws Exception {
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(false);

        mockMvc.perform(get("/metaacompanhamento/getMetas/222"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAtualizarValorManualOk() throws Exception {
        when(permissaoService.podeEditarPaciente(1, 2)).thenReturn(true);
        when(service.atualizarValorManual(1L, 2, 5.0, 1)).thenReturn(meta);

        mockMvc.perform(put("/metaacompanhamento/atualizar-valor/1/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valorAtual\":5.0}"))
                .andExpect(status().isOk());
    }

    @Test
    void testAtualizarValorManualForbidden() throws Exception {
        when(permissaoService.podeEditarPaciente(1, 2)).thenReturn(false);

        mockMvc.perform(put("/metaacompanhamento/atualizar-valor/1/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valorAtual\":5.0}"))
                .andExpect(status().isForbidden());
    }
}
