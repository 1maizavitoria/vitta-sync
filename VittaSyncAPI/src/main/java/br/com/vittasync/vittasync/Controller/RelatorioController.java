package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.*;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.RelatorioLog;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.RelatorioService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public RelatorioController(RelatorioService relatorioService,
                               UsuarioService usuarioService,
                               PermissaoService permissaoService) {
        this.relatorioService = relatorioService;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }

    @PostMapping("/exportar/{cpf}")
    public ResponseEntity<?> exportarRelatorio(@PathVariable String cpf,
                                               @Valid @RequestBody RelatorioFiltroDTO filtros) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para visualizar o paciente");
        }

        List<String> categorias = filtros.getCategorias().stream().distinct().toList();
        LocalDate inicio = filtros.getDataInicio();
        LocalDate fim = filtros.getDataFim();
        String formato = filtros.getFormato().toUpperCase(Locale.ROOT);

        RelatorioPreviewDTO preview = relatorioService.gerarPreview(paciente, categorias, inicio, fim);

        if (filtros.isPreview()) {
            return ResponseEntity.ok().header(HttpHeaders.CACHE_CONTROL, "no-store").body(preview);
        }

        if (preview.isSemRegistros()) {
            return ResponseEntity.unprocessableEntity().body(Map.of(
                    "value", "noRecords", "message", preview.getMensagem()));
        }
        String nomeArquivo = "relatorio-" + (inicio == null ? "inicio" : inicio.toString())
                + "-a-" + (fim == null ? "fim" : fim.toString()) + "." + formato.toLowerCase(Locale.ROOT);

        if ("PDF".equalsIgnoreCase(filtros.getFormato())) {
            RelatorioPDFDTO pdf = relatorioService.gerarPDF(preview);
            relatorioService.registrarExportacao(paciente, usuarioLogado, "PDF", categorias, inicio, fim);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf.getArquivo());
        }

        if ("CSV".equalsIgnoreCase(filtros.getFormato())) {
            RelatorioCSVDTO csv = relatorioService.gerarCSV(preview);
            relatorioService.registrarExportacao(paciente, usuarioLogado, "CSV", categorias, inicio, fim);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                    .body(csv.getArquivo());
        }

        return ResponseEntity.badRequest().body("Formato inválido");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> tratarCorpoInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("value", "invalidData",
                "message", "Confira o JSON: use datas válidas no formato yyyy-MM-dd e preview como booleano."));
    }

    @GetMapping("/getRelatorioExportacaoLog/{cpf}")
    public ResponseEntity<List<RelatorioLogDTO>> getRelatorioExportacaoLog(@PathVariable String cpf) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para visualizar o paciente");
        }

        List<RelatorioLog> logs = relatorioService.consultarLogs(paciente.getId());

        List<RelatorioLogDTO> dtoList = logs.stream()
                .map(RelatorioLogDTO::new)
                .toList();

        return ResponseEntity.ok(dtoList);
    }
}
