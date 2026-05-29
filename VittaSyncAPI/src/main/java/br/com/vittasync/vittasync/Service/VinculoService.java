package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.DTO.ConviteVinculoOutputDTO;
import br.com.vittasync.vittasync.DTO.EnviarConviteDTO;

import br.com.vittasync.vittasync.DTO.PacienteResumoDTO;
import br.com.vittasync.vittasync.DTO.VinculoOutputDTO;
import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;

import br.com.vittasync.vittasync.Model.ConviteVinculo;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Model.Vinculo;

import br.com.vittasync.vittasync.Repository.ConviteVinculoRepository;
import br.com.vittasync.vittasync.Repository.UsuarioRepository;
import br.com.vittasync.vittasync.Repository.VinculoRepository;


import br.com.vittasync.vittasync.Util.FuncoesResponsavel;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import br.com.vittasync.vittasync.Util.EventoPrioridades;

@Service
public class VinculoService {

    private final ConviteVinculoRepository conviteRepository;

    private final VinculoRepository vinculoRepository;

    private final UsuarioRepository usuarioRepository;

    private final EmailService emailService;

    private final EventoPacienteService eventoPacienteService;

    public VinculoService(ConviteVinculoRepository conviteRepository,
                          VinculoRepository vinculoRepository,
                          UsuarioRepository usuarioRepository,
                          EmailService emailService,
                          EventoPacienteService eventoPacienteService) {
        this.conviteRepository = conviteRepository;
        this.vinculoRepository = vinculoRepository;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.eventoPacienteService = eventoPacienteService;

    }

    public ConviteVinculoOutputDTO gerarCodigo(Integer usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (!usuario.getTipo().equalsIgnoreCase("paciente")) {

            throw new RuntimeException("Somente pacientes podem gerar vínculo");
        }

        String codigo;

        do {

            codigo = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();

        } while (conviteRepository.existsByCodigo(codigo));

        ConviteVinculo convite = new ConviteVinculo();

        convite.setPacienteId(usuario.getId());

        convite.setCodigo(codigo);

        convite.setAtivo(true);

        convite.setCriadoEm(Timestamp.valueOf(LocalDateTime.now()));

        convite.setExpiraEm(Timestamp.valueOf(LocalDateTime.now().plusHours(24)));

        conviteRepository.save(convite);

        ConviteVinculoOutputDTO output = new ConviteVinculoOutputDTO();

        output.setCodigo(codigo);

        output.setLink("http://localhost:5173/entrar?codigo=" + codigo);

        output.setExpiraEm(convite.getExpiraEm());

        return output;
    }

    public PacienteResumoDTO entrarComCodigo(
            String codigo,
            String funcao,
            Integer usuarioId
    ) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (usuario.getTipo().equalsIgnoreCase("paciente")) {

            throw new RuntimeException("Paciente não pode entrar com código");
        }

        if (usuario.getTipo().equalsIgnoreCase("responsavel") && (funcao == null || funcao.isBlank())) {

            throw new RuntimeException("Função é obrigatória");
        }

        if (usuario.getTipo().equalsIgnoreCase("responsavel") && !FuncoesResponsavel.VALIDAS.contains(funcao)) {

            throw new RuntimeException("Função inválida");
        }

        ConviteVinculo convite = conviteRepository.findByCodigo(codigo).orElseThrow(() -> new RecursoNaoEncontradoException("Código inválido"));

        if (!convite.getAtivo()) {

            throw new RuntimeException("Código inativo");
        }

        if (convite.getExpiraEm().before(Timestamp.valueOf(LocalDateTime.now()))) {

            convite.setAtivo(false);

            conviteRepository.save(convite);

            throw new RuntimeException("Código expirado");
        }

        boolean jaExiste = vinculoRepository.existsByPacienteIdAndUsuarioId(convite.getPacienteId(), usuarioId);

        if (jaExiste) {

            throw new RuntimeException("Vínculo já existe");
        }

        Vinculo vinculo = new Vinculo();

        vinculo.setPacienteId(convite.getPacienteId());

        vinculo.setUsuarioId(usuarioId);

        vinculo.setTipo(usuario.getTipo());

        vinculo.setFuncao(funcao);

        vinculo.setCriadoEm(Timestamp.valueOf(LocalDateTime.now()));

        vinculoRepository.save(vinculo);

        String descricao =
                usuario.getNome()
                        + " entrou no grupo";

        if (
                usuario.getTipo()
                        .equalsIgnoreCase("responsavel")
                        && funcao != null
                        && !funcao.isBlank()
        ) {

            descricao +=
                    " como "
                            + funcao.replace("_", " ");
        }

        eventoPacienteService.criarEvento(
                convite.getPacienteId(),
                usuario.getId(),
                "vinculo_criado",
                "Novo participante no grupo",
                descricao,
                EventoPrioridades.NORMAL
        );
        Usuario paciente =
                usuarioRepository
                        .findById(convite.getPacienteId())
                        .orElseThrow(() ->
                                new RecursoNaoEncontradoException(
                                        "Paciente não encontrado"
                                )
                        );
        return new PacienteResumoDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getCpf()
        );


    }

    public void enviarConviteEmail(String email, String codigo) {

        ConviteVinculo convite = conviteRepository.findByCodigo(codigo).orElseThrow(() -> new RecursoNaoEncontradoException("Convite não encontrado"));

        if (!convite.getAtivo()) {

            throw new RuntimeException("Convite inativo");
        }

        if (convite.getExpiraEm().before(Timestamp.valueOf(LocalDateTime.now()))) {

            convite.setAtivo(false);

            conviteRepository.save(convite);

            throw new RuntimeException("Convite expirado");
        }

        Usuario paciente = usuarioRepository.findById(convite.getPacienteId()).orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));

        Usuario convidado = usuarioRepository.findByEmail(email).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (convidado.getTipo().equalsIgnoreCase("paciente")) {

            throw new RuntimeException("Paciente não pode ser vinculado");
        }

        String link = "http://localhost:5173/entrar?codigo=" + convite.getCodigo();

        emailService.enviarConviteVinculo(email, convidado.getNome(), paciente.getNome(), convite.getCodigo(), link);
    }

    public void removerVinculo(Long id, Integer usuarioLogadoId) {

        Vinculo vinculo = vinculoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Vínculo não encontrado"));
        Usuario usuarioLogado =
                usuarioRepository.findById(
                        usuarioLogadoId
                ).orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Usuário não encontrado"
                        )
                );
        Usuario usuarioRemovido =
                usuarioRepository.findById(
                        vinculo.getUsuarioId()
                ).orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Usuário removido não encontrado"
                        )
                );
        String descricao =
                usuarioLogado.getNome()
                        + " removeu "
                        + usuarioRemovido.getNome()
                        + " do grupo";
        eventoPacienteService.criarEvento(
                vinculo.getPacienteId(),
                usuarioLogadoId,
                "vinculo_removido",
                "Participante removido",
                descricao,
                EventoPrioridades.NORMAL
        );
        vinculoRepository.delete(vinculo);
    }

    public List<VinculoOutputDTO> listar(Integer usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        List<Vinculo> vinculos;

        if (usuario.getTipo().equalsIgnoreCase("paciente")) {

            vinculos = vinculoRepository.findByPacienteId(usuarioId);

        } else {

            vinculos = vinculoRepository.findByUsuarioId(usuarioId);
        }

        return vinculos.stream().map(vinculo -> {

            Usuario usuarioVinculado;

            if (usuario.getTipo().equalsIgnoreCase("paciente")) {

                usuarioVinculado = usuarioRepository.findById(vinculo.getUsuarioId()).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

            } else {

                usuarioVinculado = usuarioRepository.findById(vinculo.getPacienteId()).orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));
            }

            VinculoOutputDTO dto = new VinculoOutputDTO();

            dto.setId(vinculo.getId());

            dto.setNome(usuarioVinculado.getNome());

            dto.setEmail(usuarioVinculado.getEmail());

            dto.setTipo(vinculo.getTipo());

            dto.setFuncao(vinculo.getFuncao());

            dto.setConselho(usuarioVinculado.getConselho());

            dto.setCriadoEm(vinculo.getCriadoEm());


            return dto;

        }).toList();
    }

    public List<PacienteResumoDTO> listarPacientesDoUsuario(Integer usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        // paciente vê apenas ele mesmo
        if (usuario.getTipo().equalsIgnoreCase("paciente")) {

            PacienteResumoDTO dto = new PacienteResumoDTO();

            dto.setId(usuario.getId());

            dto.setNome(usuario.getNome());

            dto.setEmail(usuario.getEmail());

            dto.setCpf(usuario.getCpf());

            return List.of(dto);
        }

        // responsável/médico
        List<Vinculo> vinculos = vinculoRepository.findByUsuarioId(usuarioId);

        return vinculos.stream().map(vinculo -> {

            Usuario paciente = usuarioRepository.findById(vinculo.getPacienteId()).orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));

            PacienteResumoDTO dto = new PacienteResumoDTO();

            dto.setId(paciente.getId());

            dto.setNome(paciente.getNome());

            dto.setEmail(paciente.getEmail());

            dto.setCpf(paciente.getCpf());

            return dto;

        }).toList();
    }

    public List<VinculoOutputDTO> listarPorPaciente(Integer pacienteId) {

        List<Vinculo> vinculos = vinculoRepository.findByPacienteId(pacienteId);

        return vinculos.stream().map(vinculo -> {

            Usuario usuarioVinculado = usuarioRepository.findById(vinculo.getUsuarioId()).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

            VinculoOutputDTO dto = new VinculoOutputDTO();

            dto.setId(vinculo.getId());

            dto.setNome(usuarioVinculado.getNome());

            dto.setEmail(usuarioVinculado.getEmail());

            dto.setTipo(vinculo.getTipo());

            dto.setFuncao(vinculo.getFuncao());

            dto.setConselho(usuarioVinculado.getConselho());

            dto.setCriadoEm(vinculo.getCriadoEm());

            return dto;

        }).toList();
    }
}

