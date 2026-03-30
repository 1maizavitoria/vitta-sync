package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.UsuarioInputDTO;
import br.com.vittasync.vittasync.DTO.UsuarioOutputDTO;
import br.com.vittasync.vittasync.DTO.UsuarioUpdateDTO;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Util.HashUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public UsuarioController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioOutputDTO> create(@Valid @RequestBody UsuarioInputDTO dto) {

        if ("saude".equalsIgnoreCase(dto.getTipo()) && (dto.getConselho() == null || dto.getConselho().isBlank())) {
            throw new RuntimeException("Conselho é obrigatório para usuários do tipo saude");
        }

        Usuario usuario = new Usuario();
        usuario.setCpf(dto.getCpf());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(HashUtil.hashSenha(dto.getSenha()));
        usuario.setTipo(dto.getTipo());
        usuario.setConselho(dto.getConselho());
        usuario.setPrivCompartilharDiario(dto.getPrivCompartilharDiario());
        usuario.setPrivCompartilharHabitos(dto.getPrivCompartilharHabitos());
        usuario.setDataNascimento(dto.getDataNascimento());

        Usuario criado = usuarioService.create(usuario);
        return ResponseEntity.ok(toOutputDTO(criado));
    }

    @PutMapping("/editar/{cpf}")
    public ResponseEntity<UsuarioOutputDTO> update(@PathVariable String cpf,
                                                   @Valid @RequestBody UsuarioUpdateDTO dto,
                                                   @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String cpfToken = jwtService.extrairCpf(token);

        if (!cpfToken.equals(cpf)) {
            return ResponseEntity.status(403).build();
        }

        try {
            Usuario usuario = usuarioService.searchByCpf(cpf);

            usuario.setNome(dto.getNome());
            usuario.setEmail(dto.getEmail());
            usuario.setPrivCompartilharDiario(dto.getPrivCompartilharDiario());
            usuario.setPrivCompartilharHabitos(dto.getPrivCompartilharHabitos());
            usuario.setDataNascimento(dto.getDataNascimento());

            Usuario atualizado = usuarioService.update(usuario);

            return ResponseEntity.ok(toOutputDTO(atualizado));
        }

        catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getUsuario/{cpf}")
    public ResponseEntity<UsuarioOutputDTO> getByCpf(@PathVariable String cpf,
                                                     @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        String cpfToken = jwtService.extrairCpf(token);

        if (!cpfToken.equals(cpf)) {
            return ResponseEntity.status(403).build();
        }

        Usuario usuario = usuarioService.searchByCpf(cpf);
        return ResponseEntity.ok(toOutputDTO(usuario));
    }

    @DeleteMapping("/deletar/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable String cpf,
                                       @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        String cpfToken = jwtService.extrairCpf(token);

        if (!cpfToken.equals(cpf)) {
            return ResponseEntity.status(403).build();
        }

        Usuario usuario = usuarioService.searchByCpf(cpf);
        usuarioService.delete(usuario.getId());
        return ResponseEntity.ok().build();
    }

    private UsuarioOutputDTO toOutputDTO(Usuario usuario) {
        UsuarioOutputDTO out = new UsuarioOutputDTO();
        out.setCpf(usuario.getCpf());
        out.setNome(usuario.getNome());
        out.setEmail(usuario.getEmail());
        out.setTipo(usuario.getTipo());
        out.setConselho(usuario.getConselho());
        out.setPrivCompartilharDiario(usuario.getPrivCompartilharDiario());
        out.setPrivCompartilharHabitos(usuario.getPrivCompartilharHabitos());
        out.setDataNascimento(usuario.getDataNascimento());
        out.setDataCadastro(usuario.getDataCadastro());
        return out;
    }
}
