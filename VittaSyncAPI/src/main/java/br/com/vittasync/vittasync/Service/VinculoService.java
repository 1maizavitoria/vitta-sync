package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.DTO.VinculoInputDTO;
import br.com.vittasync.vittasync.DTO.VinculoOutputDTO;
import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.SolicitacaoVinculo;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.SolicitacaoVinculoRepository;
import br.com.vittasync.vittasync.Repository.UsuarioRepository;

import br.com.vittasync.vittasync.Model.PacienteMedico;
import br.com.vittasync.vittasync.Model.PacienteResponsavel;

import br.com.vittasync.vittasync.Repository.PacienteMedicoRepository;
import br.com.vittasync.vittasync.Repository.PacienteResponsavelRepository;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VinculoService {

    private final SolicitacaoVinculoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final PacienteMedicoRepository pacienteMedicoRepository;

    private final PacienteResponsavelRepository pacienteResponsavelRepository;

    public VinculoService(
            SolicitacaoVinculoRepository repository,
            UsuarioRepository usuarioRepository,
            PacienteMedicoRepository pacienteMedicoRepository,
            PacienteResponsavelRepository pacienteResponsavelRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.pacienteMedicoRepository = pacienteMedicoRepository;
        this.pacienteResponsavelRepository = pacienteResponsavelRepository;
    }


    public String gerarCodigo(Integer usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Usuário não encontrado"
                        )
                );

        if (
                !usuario.getTipo().equalsIgnoreCase("medico") &&
                        !usuario.getTipo().equalsIgnoreCase("responsavel")
        ) {
            throw new RuntimeException(
                    "Usuário não pode gerar vínculo"
            );
        }

        String codigo;

        do {

            codigo = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 6)
                    .toUpperCase();

        } while (repository.existsByCodigo(codigo));

        SolicitacaoVinculo solicitacao =
                new SolicitacaoVinculo();

        solicitacao.setSolicitanteId(usuario.getId());

        solicitacao.setCodigo(codigo);

        solicitacao.setTipo(usuario.getTipo());

        solicitacao.setStatus("PENDENTE");

        solicitacao.setUtilizado(false);

        solicitacao.setCriadoEm(
                Timestamp.valueOf(LocalDateTime.now())
        );

        solicitacao.setExpiraEm(
                Timestamp.valueOf(
                        LocalDateTime.now().plusHours(1)
                )
        );

        repository.save(solicitacao);

        return codigo;
    }

    public VinculoOutputDTO validarCodigo(
            VinculoInputDTO dto
    ) {

        SolicitacaoVinculo solicitacao =
                repository.findByCodigo(dto.getCodigo())
                        .orElseThrow(() ->
                                new RecursoNaoEncontradoException(
                                        "Código inválido"
                                )
                        );

        if (solicitacao.getUtilizado()) {
            throw new RuntimeException(
                    "Código já utilizado"
            );
        }

        if (
                solicitacao.getExpiraEm().before(
                        Timestamp.valueOf(LocalDateTime.now())
                )
        ) {
            solicitacao.setStatus("EXPIRADO");
            repository.save(solicitacao);
            throw new RuntimeException(
                    "Código expirado"
            );
        }

        Usuario solicitante =
                usuarioRepository.findById(
                        solicitacao.getSolicitanteId()
                ).orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Usuário não encontrado"
                        )
                );

        VinculoOutputDTO output =
                new VinculoOutputDTO();

        output.setNome(solicitante.getNome());

        output.setTipo(solicitante.getTipo());

        output.setCodigo(solicitacao.getCodigo());

        return output;
    }

    public void confirmarVinculo(
            String codigo,
            Integer pacienteId
    ) {

        Usuario paciente = usuarioRepository.findById(pacienteId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Paciente não encontrado"
                        )
                );

        if (!paciente.getTipo().equalsIgnoreCase("paciente")) {
            throw new RuntimeException(
                    "Usuário não é paciente"
            );
        }

        SolicitacaoVinculo solicitacao =
                repository.findByCodigo(codigo)
                        .orElseThrow(() ->
                                new RecursoNaoEncontradoException(
                                        "Código inválido"
                                )
                        );

        if (solicitacao.getUtilizado()) {
            throw new RuntimeException(
                    "Código já utilizado"
            );
        }

        if (
                solicitacao.getExpiraEm().before(
                        Timestamp.valueOf(LocalDateTime.now())
                )
        ) {
            solicitacao.setStatus("EXPIRADO");
            repository.save(solicitacao);
            throw new RuntimeException(
                    "Código expirado"
            );
        }

        Integer solicitanteId =
                solicitacao.getSolicitanteId();

        if (
                solicitacao.getTipo()
                        .equalsIgnoreCase("medico")
        ) {

            boolean jaExiste =
                    pacienteMedicoRepository
                            .existsByPacienteIdAndMedicoId(
                                    pacienteId,
                                    solicitanteId
                            );

            if (jaExiste) {
                throw new RuntimeException(
                        "Vínculo já existe"
                );
            }

            PacienteMedico vinculo =
                    new PacienteMedico();

            vinculo.setPacienteId(pacienteId);

            vinculo.setMedicoId(solicitanteId);

            vinculo.setCriadoEm(
                    Timestamp.valueOf(LocalDateTime.now())
            );

            pacienteMedicoRepository.save(vinculo);

        } else if (
                solicitacao.getTipo()
                        .equalsIgnoreCase("responsavel")
        ) {

            boolean jaExiste =
                    pacienteResponsavelRepository
                            .existsByPacienteIdAndResponsavelId(
                                    pacienteId,
                                    solicitanteId
                            );

            if (jaExiste) {
                throw new RuntimeException(
                        "Vínculo já existe"
                );
            }

            PacienteResponsavel vinculo =
                    new PacienteResponsavel();

            vinculo.setPacienteId(pacienteId);

            vinculo.setResponsavelId(solicitanteId);

            vinculo.setCriadoEm(
                    Timestamp.valueOf(LocalDateTime.now())
            );

            pacienteResponsavelRepository.save(vinculo);
        }

        solicitacao.setUtilizado(true);

        solicitacao.setStatus("ACEITO");

        repository.save(solicitacao);
    }


}