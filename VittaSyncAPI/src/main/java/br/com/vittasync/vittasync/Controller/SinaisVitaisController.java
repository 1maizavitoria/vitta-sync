package br.com.vittasync.vittasync.Controller;


import br.com.vittasync.vittasync.DTO.SinaisVitaisInputDTO;
import br.com.vittasync.vittasync.DTO.SinaisVitaisOutputDTO;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.SinaisVitaisService;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/sinaisvitais")
public class SinaisVitaisController {

    private final SinaisVitaisService service;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public SinaisVitaisController(SinaisVitaisService service, JwtService jwtService, UsuarioService usuarioService) {
        this.service = service;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar/{cpf}")
    public ResponseEntity<SinaisVitaisOutputDTO> create(@PathVariable String cpf,
                                                        @RequestHeader("Authorization") String authHeader,
                                                        @RequestBody SinaisVitaisInputDTO dto) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        if (!cpfDoToken.equals(cpf)) {
            return ResponseEntity.status(403).build();
        }

        Usuario paciente = usuarioService.searchByCpf(cpf);

        SinaisVitais entity = new SinaisVitais();
        entity.setPaciente(paciente);
        entity.setFcBpm(dto.getFcBpm());
        entity.setFrRpm(dto.getFrRpm());
        entity.setPaSistolica(dto.getPaSistolica());
        entity.setPaDiastolica(dto.getPaDiastolica());
        entity.setTempCelcius(dto.getTempCelcius());
        entity.setSpo2Porcento(dto.getSpo2Porcento());

        SinaisVitais salvo = service.create(entity);
        return ResponseEntity.ok(toOutputDTO(salvo));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<SinaisVitaisOutputDTO> update(@PathVariable Integer id,
                                                        @RequestHeader("Authorization") String authHeader,
                                                        @RequestBody SinaisVitaisInputDTO dto) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        Usuario paciente = usuarioService.searchByCpf(cpfDoToken);

        SinaisVitais entity = new SinaisVitais();
        entity.setPaciente(paciente);
        entity.setFcBpm(dto.getFcBpm());
        entity.setFrRpm(dto.getFrRpm());
        entity.setPaSistolica(dto.getPaSistolica());
        entity.setPaDiastolica(dto.getPaDiastolica());
        entity.setTempCelcius(dto.getTempCelcius());
        entity.setSpo2Porcento(dto.getSpo2Porcento());

        SinaisVitais atualizado = service.update(id, entity);
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

    @GetMapping("/getSinaisVitais")
    public ResponseEntity<List<SinaisVitaisOutputDTO>> list(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String cpfDoToken = jwtService.extrairCpf(token);

        List<SinaisVitais> lista = service.findByPacienteCpf(cpfDoToken);
        return ResponseEntity.ok(lista.stream().map(this::toOutputDTO).collect(Collectors.toList()));
    }


    private SinaisVitaisOutputDTO toOutputDTO(SinaisVitais entity) {
        SinaisVitaisOutputDTO dto = new SinaisVitaisOutputDTO();
        dto.setId(entity.getId());
        dto.setFcBpm(entity.getFcBpm());
        dto.setFrRpm(entity.getFrRpm());
        dto.setPaSistolica(entity.getPaSistolica());
        dto.setPaDiastolica(entity.getPaDiastolica());
        dto.setTempCelcius(entity.getTempCelcius());
        dto.setSpo2Porcento(entity.getSpo2Porcento());
        dto.setDataRegistro(entity.getDataRegistro());
        dto.setDataModificacao(entity.getDataModificacao());
        return dto;
    }
}
