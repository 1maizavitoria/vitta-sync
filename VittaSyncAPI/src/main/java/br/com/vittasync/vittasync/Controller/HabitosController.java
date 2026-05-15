package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.HabitosInputDTO;
import br.com.vittasync.vittasync.DTO.HabitosOutputDTO;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.HabitosService;
import br.com.vittasync.vittasync.Service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.vittasync.vittasync.Service.UsuarioService;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/habitos")
public class HabitosController {

    private final HabitosService service;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public HabitosController(HabitosService service, JwtService jwtService, UsuarioService usuarioService) {
        this.service = service;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar/{cpf}")
    public ResponseEntity<HabitosOutputDTO> create(@PathVariable String cpf,
                                                   @RequestHeader("Authorization") String authHeader,
                                                   @RequestBody HabitosInputDTO dto) {

        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        if (!cpfDoToken.equals(cpf)) {
            return ResponseEntity.status(403).build();
        }

        Usuario paciente = usuarioService.searchByCpf(cpf);

        Habitos entity = new Habitos();
        entity.setPaciente(paciente);
        entity.setHorasSono(dto.getHorasSono());
        entity.setMinutosExercicio(dto.getMinutosExercicio());
        entity.setDataReferencia(dto.getDataReferencia());

        Habitos salvo = service.create(entity);
        return ResponseEntity.ok(toOutputDTO(salvo));
    }

    @PutMapping("/editar/{id}/{cpf}")
    public ResponseEntity<HabitosOutputDTO> update(@PathVariable Integer id,
                                                   @PathVariable String cpf,
                                                   @RequestHeader("Authorization") String authHeader,
                                                   @RequestBody HabitosInputDTO dto) {

        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        if (!cpfDoToken.equals(cpf)) {
            return ResponseEntity.status(403).build();
        }

        Usuario paciente = usuarioService.searchByCpf(cpf);

        Habitos entity = new Habitos();

        entity.setHorasSono(dto.getHorasSono());
        entity.setMinutosExercicio(dto.getMinutosExercicio());
        entity.setDataReferencia(dto.getDataReferencia());

        Habitos atualizado = service.update(id, entity);
        return ResponseEntity.ok(toOutputDTO(atualizado));
    }

    @DeleteMapping("/deletar/{id}/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable Integer id,
                                       @PathVariable String cpf,
                                       @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        if (!cpfDoToken.equals(cpf)) {
            return ResponseEntity.status(403).build();
        }

        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getHabitos/{cpf}")
    public ResponseEntity<List<HabitosOutputDTO>> list(@PathVariable String cpf,
                                                       @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        if (!cpfDoToken.equals(cpf)) {
            return ResponseEntity.status(403).build();
        }

        List<Habitos> lista = service.findByPacienteCpf(cpf);
        return ResponseEntity.ok(lista.stream().map(this::toOutputDTO).collect(Collectors.toList()));
    }

    private HabitosOutputDTO toOutputDTO(Habitos entity) {
        HabitosOutputDTO dto = new HabitosOutputDTO();
        dto.setId(entity.getId());
        dto.setHorasSono(entity.getHorasSono());
        dto.setMinutosExercicio(entity.getMinutosExercicio());
        dto.setDataReferencia(entity.getDataReferencia());
        dto.setDataRegistro(entity.getDataRegistro());
        dto.setDataModificacao(entity.getDataModificacao());
        return dto;
    }
}
