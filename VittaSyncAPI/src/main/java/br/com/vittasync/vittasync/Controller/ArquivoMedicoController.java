package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.ArquivoMedicoOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.ArquivoMedico;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.ArquivoMedicoService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/documentos")
public class ArquivoMedicoController {

    private final ArquivoMedicoService service;
    private final UsuarioService usuarioService;
    private final PermissaoService permissaoService;

    public ArquivoMedicoController(ArquivoMedicoService service,
                                   UsuarioService usuarioService,
                                   PermissaoService permissaoService) {
        this.service = service;
        this.usuarioService = usuarioService;
        this.permissaoService = permissaoService;
    }

    @PostMapping(value = "/upload/{cpfPaciente}", consumes = "multipart/form-data")
    public ResponseEntity<ArquivoMedicoOutputDTO> upload(@PathVariable String cpfPaciente,
                                                         @RequestParam("nomeArquivo") String nomeArquivo,
                                                         @RequestParam("arquivo") MultipartFile arquivo) throws IOException {
        Usuario medico = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpfPaciente);

        if (!permissaoService.medicoVinculadoAoPaciente(medico.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o documento");
        }

        String originalName = arquivo.getOriginalFilename();
        String extensao = originalName != null
                ? originalName.substring(originalName.lastIndexOf(".")).toLowerCase()
                : ".pdf";

        ArquivoMedico salvo = service.upload(
                medico,
                paciente,
                nomeArquivo,
                extensao,
                originalName,
                arquivo.getBytes()
        );

        ArquivoMedicoOutputDTO dto = new ArquivoMedicoOutputDTO();
        dto.setId(salvo.getId());
        dto.setNomeArquivo(salvo.getNomeArquivo());
        dto.setExtensao(salvo.getExtensao());
        dto.setDataUpload(salvo.getDataUpload());
        dto.setPacienteCpf(paciente.getCpf());
        dto.setNomeOriginal(salvo.getNomeOriginal());
        dto.setMedicoNome(salvo.getMedico().getNome());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/getDocumentosPaciente/{cpf}")
    public ResponseEntity<List<ArquivoMedicoOutputDTO>> listarPorPaciente(@PathVariable String cpf) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        Usuario paciente = usuarioService.searchByCpf(cpf);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), paciente.getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o documento");
        }

        List<ArquivoMedico> docs = service.listarPorPaciente(paciente);

        List<ArquivoMedicoOutputDTO> output = docs.stream().map(d -> {
            ArquivoMedicoOutputDTO dto = new ArquivoMedicoOutputDTO();
            dto.setId(d.getId());
            dto.setNomeArquivo(d.getNomeArquivo());
            dto.setDataUpload(d.getDataUpload());
            dto.setPacienteCpf(d.getPaciente().getCpf());
            dto.setExtensao(d.getExtensao());
            dto.setNomeOriginal(d.getNomeOriginal());
            dto.setMedicoNome(d.getMedico().getNome());
            return dto;
        }).toList();

        return ResponseEntity.ok(output);
    }

    @GetMapping("/getDocumentosMedico")
    public ResponseEntity<List<ArquivoMedicoOutputDTO>> listarPorMedico() {
        Usuario medico = usuarioService.getUsuarioLogado();

        if (!permissaoService.isMedico(medico)) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o documento");
        }

        List<ArquivoMedico> docs = service.listarPorMedico(medico);

        List<ArquivoMedicoOutputDTO> output = docs.stream().map(d -> {
            ArquivoMedicoOutputDTO dto = new ArquivoMedicoOutputDTO();

            dto.setId(d.getId());
            dto.setNomeArquivo(d.getNomeArquivo());
            dto.setDataUpload(d.getDataUpload());
            dto.setPacienteCpf(d.getPaciente().getCpf());

            dto.setPacienteNome(
                    d.getPaciente().getNome()
            );

            dto.setExtensao(d.getExtensao());
            dto.setNomeOriginal(d.getNomeOriginal());
            dto.setMedicoNome(d.getMedico().getNome());

            return dto;

        }).toList();

        return ResponseEntity.ok(output);
    }

    @GetMapping("/{id}/visualizarDocumento")
    public ResponseEntity<byte[]> visualizar(@PathVariable Integer id) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        ArquivoMedico doc = service.visualizar(id);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), doc.getPaciente().getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o documento");
        }

        String ext = doc.getExtensao() != null ? doc.getExtensao().toLowerCase() : "";
        String contentType = "application/octet-stream";

        if (ext.equals(".pdf")) {
            contentType = "application/pdf";
        } else if (ext.equals(".png")) {
            contentType = "image/png";
        } else if (ext.equals(".jpg") || ext.equals(".jpeg")) {
            contentType = "image/jpeg";
        }

        String fileName = doc.getNomeOriginal();

        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(doc.getArquivo());
    }

    @GetMapping("/{id}/downloadDocumento")
    public ResponseEntity<byte[]> download(@PathVariable Integer id) {
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();

        ArquivoMedico doc = service.visualizar(id);

        if (!permissaoService.podeVisualizarPaciente(usuarioLogado.getId(), doc.getPaciente().getId())) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o documento");
        }

        String ext = doc.getExtensao() != null ? doc.getExtensao() : ".pdf";
        String contentType = "application/octet-stream";

        if (ext.equals(".pdf")) contentType = "application/pdf";
        else if (ext.equals(".png")) contentType = "image/png";
        else if (ext.equals(".jpg") || ext.equals(".jpeg")) contentType = "image/jpeg";

        // nome no download: "Exame de Sangue.pdf"
        String fileName = doc.getNomeOriginal() + ext;

        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
                .body(doc.getArquivo());
    }

    @DeleteMapping("/deletarDocumento/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        Usuario medico = usuarioService.getUsuarioLogado();

        if (!permissaoService.isMedico(medico)) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o documento");
        }

        service.deletar(medico, id);
        return ResponseEntity.noContent().build();
    }
}
