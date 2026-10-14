package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.ArquivoMedicoOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Model.ArquivoMedico;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.ArquivoMedicoService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/documentos")
@Tag(name = "Documentos médicos", description = "Envio, consulta, visualização, download e exclusão de documentos médicos do paciente, "
        + "em PDF ou imagem")
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

    @Operation(
            summary = "Enviar documento",
            description = "Envia um documento médico (PDF ou imagem) para o paciente, a data de envio é "
                    + "automaticamente definida pelo servidor e o evento é registrado na linha do tempo do paciente. "
                    + "Permitido apenas ao profissional de saúde vinculado ao paciente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento enviado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário não é um profissional de saúde vinculado ao paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @PostMapping(value = "/upload/{cpfPaciente}", consumes = "multipart/form-data")
    public ResponseEntity<ArquivoMedicoOutputDTO> upload(@Parameter(description = "CPF do paciente (apenas números)")
                                                         @PathVariable String cpfPaciente,
                                                         @Parameter(description = "Nome de exibição do documento")
                                                         @RequestParam("nomeArquivo") String nomeArquivo,
                                                         @Parameter(description = "Arquivo do documento (PDF ou imagem)")
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

    @Operation(
            summary = "Listar documentos do paciente",
            description = "Retorna os dados de todos os documentos médicos enviados para o paciente, sem o conteúdo dos arquivos. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele (responsáveis e profissionais de saúde)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de documentos do paciente"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping("/getDocumentosPaciente/{cpf}")
    public ResponseEntity<List<ArquivoMedicoOutputDTO>> listarPorPaciente(@Parameter(description = "CPF do paciente (apenas números)")
                                                                          @PathVariable String cpf) {
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

    @Operation(
            summary = "Listar documentos enviados pelo profissional",
            description = "Retorna os dados de todos os documentos enviados pelo profissional de saúde logado, "
                    + "com o nome e o CPF de cada paciente. Permitido apenas a profissionais de saúde."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de documentos enviados pelo profissional"),
            @ApiResponse(responseCode = "403", description = "Usuário não é um profissional de saúde")
    })
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

    @Operation(
            summary = "Visualizar documento",
            description = "Retorna o conteúdo do arquivo do documento com o tipo correto (PDF ou imagem) e o nome original do arquivo. "
                    + "Permitido ao próprio paciente e a todos os usuários vinculados a ele."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conteúdo do documento"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    @GetMapping("/{id}/visualizarDocumento")
    public ResponseEntity<byte[]> visualizar(@Parameter(description = "ID do documento")
                                             @PathVariable Integer id) {
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
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
                .body(doc.getArquivo());
    }

    @Operation(
            summary = "Baixar documento",
            description = "Retorna o conteúdo do arquivo do documento com o nome de exibição e a extensão, "
                    + "por exemplo \"Exame de Sangue.pdf\". Permitido ao próprio paciente e a todos os usuários vinculados a ele."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conteúdo do documento"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para visualizar o paciente"),
            @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    @GetMapping("/{id}/downloadDocumento")
    public ResponseEntity<byte[]> download(@Parameter(description = "ID do documento")
                                           @PathVariable Integer id) {
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
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(doc.getArquivo());
    }

    @Operation(
            summary = "Excluir documento",
            description = "Exclui um documento médico e registra o evento na linha do tempo do paciente. "
                    + "Permitido apenas ao profissional de saúde que enviou o documento."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Documento excluído com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário não é o profissional de saúde que enviou o documento"),
            @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    @DeleteMapping("/deletarDocumento/{id}")
    public ResponseEntity<Void> deletar(@Parameter(description = "ID do documento")
                                        @PathVariable Integer id) {
        Usuario medico = usuarioService.getUsuarioLogado();

        if (!permissaoService.isMedico(medico)) {
            throw new AcessoNegadoException("Usuário sem permissão para acessar o documento");
        }

        service.deletar(medico, id);
        return ResponseEntity.noContent().build();
    }
}
