package br.com.vittasync.vittasync.Controller;

import br.com.vittasync.vittasync.DTO.VinculoInputDTO;
import br.com.vittasync.vittasync.DTO.VinculoOutputDTO;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.VinculoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vinculos")
public class VinculoController {

    private final VinculoService service;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public VinculoController(
            VinculoService service,
            JwtService jwtService,
            UsuarioService usuarioService
    ) {
        this.service = service;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/gerar")
    public ResponseEntity<String> gerarCodigo(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String cpf = jwtService.extrairCpf(token);
        Usuario usuario = usuarioService.searchByCpf(cpf);
        String codigo = service.gerarCodigo(usuario.getId());
        return ResponseEntity.ok(codigo);
    }

    @PostMapping("/validar")
    public ResponseEntity<VinculoOutputDTO> validarCodigo(
            @RequestBody VinculoInputDTO dto
    ) {
        VinculoOutputDTO output = service.validarCodigo(dto);
        return ResponseEntity.ok(output);
    }

    @PostMapping("/confirmar")
    public ResponseEntity<Void> confirmarVinculo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody VinculoInputDTO dto
    ) {

        String token = authHeader.replace("Bearer ", "");
        String cpf = jwtService.extrairCpf(token);
        Usuario usuario = usuarioService.searchByCpf(cpf);
        service.confirmarVinculo(dto.getCodigo(), usuario.getId());

        return ResponseEntity.ok().build();
    }
}