package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.RelatorioCSVDTO;
import br.com.vittasync.vittasync.DTO.RelatorioPDFDTO;
import br.com.vittasync.vittasync.DTO.RelatorioPacienteResumoDTO;
import br.com.vittasync.vittasync.DTO.RelatorioPreviewDTO;
import br.com.vittasync.vittasync.DTO.LinhaTempoSintomasDTO;
import br.com.vittasync.vittasync.Model.RelatorioLog;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.RelatorioService;
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
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RelatorioController.class)
@AutoConfigureMockMvc(addFilters = false)
class RelatorioControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private RelatorioService relatorioService;
    @MockBean private JwtService jwtService;
    @MockBean private UsuarioService usuarioService;
    @MockBean private PermissaoService permissaoService;
    @MockBean private SessaoService sessaoService;

    private Usuario usuario;
    private Usuario paciente;
    private RelatorioPreviewDTO comRegistros;
    private RelatorioPreviewDTO semRegistros;

    @BeforeEach
    void setup() {
        usuario = new Usuario(); usuario.setId(1); usuario.setCpf("111");
        paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222"); paciente.setNome("Paciente");

        RelatorioPacienteResumoDTO resumo = new RelatorioPacienteResumoDTO("Paciente", "222", null, null, null);
        comRegistros = new RelatorioPreviewDTO(resumo, List.of(), List.of(),
                List.of(new LinhaTempoSintomasDTO(1, LocalDateTime.now(), "Dor", 3, LocalDate.now())));
        semRegistros = new RelatorioPreviewDTO(resumo, List.of(), List.of(), List.of());

        when(usuarioService.getUsuarioLogado()).thenReturn(usuario);
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(true);
    }

    private String filtro(String formato, boolean preview, String inicio, String fim) {
        return "{\"formato\":\"" + formato + "\",\"categorias\":[\"SINTOMAS\",\"SINTOMAS\"],"
                + "\"dataInicio\":" + inicio + ",\"dataFim\":" + fim + ",\"preview\":" + preview + "}";
    }

    @Test
    void testExportarPreview() throws Exception {
        when(relatorioService.gerarPreview(eq(paciente), eq(List.of("SINTOMAS")), any(), any())).thenReturn(comRegistros);

        mockMvc.perform(post("/relatorio/exportar/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filtro("PDF", true, "\"2026-01-01\"", "\"2026-01-31\"")))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.sintomas[0].sintoma").value("Dor"));

        verify(relatorioService, never()).registrarExportacao(any(), any(), any(), any(), any(), any());
    }

    @Test
    void testExportarSemRegistros() throws Exception {
        when(relatorioService.gerarPreview(any(), anyList(), any(), any())).thenReturn(semRegistros);

        mockMvc.perform(post("/relatorio/exportar/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filtro("PDF", false, "null", "null")))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.value").value("noRecords"));
    }

    @Test
    void testExportarPdf() throws Exception {
        when(relatorioService.gerarPreview(any(), anyList(), any(), any())).thenReturn(comRegistros);
        when(relatorioService.gerarPDF(comRegistros)).thenReturn(new RelatorioPDFDTO(new byte[]{1, 2}));

        mockMvc.perform(post("/relatorio/exportar/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filtro("pdf", false, "\"2026-01-01\"", "\"2026-01-31\"")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=\"relatorio-2026-01-01-a-2026-01-31.pdf\""));

        verify(relatorioService).registrarExportacao(paciente, usuario, "PDF", List.of("SINTOMAS"),
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));
    }

    @Test
    void testExportarCsv() throws Exception {
        when(relatorioService.gerarPreview(any(), anyList(), any(), any())).thenReturn(comRegistros);
        when(relatorioService.gerarCSV(comRegistros)).thenReturn(new RelatorioCSVDTO("a;b".getBytes()));

        mockMvc.perform(post("/relatorio/exportar/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filtro("CSV", false, "null", "null")))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=\"relatorio-inicio-a-fim.csv\""));

        verify(relatorioService).registrarExportacao(paciente, usuario, "CSV", List.of("SINTOMAS"), null, null);
    }

    @Test
    void testExportarForbidden() throws Exception {
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(false);

        mockMvc.perform(post("/relatorio/exportar/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filtro("PDF", true, "null", "null")))
                .andExpect(status().isForbidden());
    }

    @Test
    void testExportarJsonInvalido() throws Exception {
        mockMvc.perform(post("/relatorio/exportar/222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"formato\":\"PDF\",\"categorias\":[\"SINAIS\"],\"dataInicio\":\"data\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.value").value("invalidData"));
    }

    @Test
    void testGetRelatorioExportacaoLog() throws Exception {
        RelatorioLog log = new RelatorioLog();
        log.setFormato("PDF");
        log.setUsuario(usuario);
        log.setPaciente(paciente);
        when(relatorioService.consultarLogs(2)).thenReturn(List.of(log));

        mockMvc.perform(get("/relatorio/getRelatorioExportacaoLog/222"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].formato").value("PDF"))
                .andExpect(jsonPath("$[0].pacienteId").value(2));
    }

    @Test
    void testGetRelatorioExportacaoLogForbidden() throws Exception {
        when(permissaoService.podeVisualizarPaciente(1, 2)).thenReturn(false);

        mockMvc.perform(get("/relatorio/getRelatorioExportacaoLog/222"))
                .andExpect(status().isForbidden());
    }
}
