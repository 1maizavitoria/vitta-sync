package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.DiarioSintomas;
import br.com.vittasync.vittasync.Repository.DiarioSintomasRepository;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import br.com.vittasync.vittasync.Util.EventoTipos;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiarioSintomasService {

    private final DiarioSintomasRepository repository;
    private final EventoPacienteService eventoPacienteService;
    private final EventoClinicoService eventoClinicoService;

    public DiarioSintomasService(
            DiarioSintomasRepository repository,
            EventoPacienteService eventoPacienteService,
            EventoClinicoService eventoClinicoService
    ) {
        this.repository = repository;
        this.eventoPacienteService = eventoPacienteService;
        this.eventoClinicoService = eventoClinicoService;
    }

    public DiarioSintomas create(
            DiarioSintomas sintoma,
            Integer usuarioLogadoId
    ) {
        sintoma.setRegistradoPorUsuarioId(usuarioLogadoId);
        sintoma.setDataRegistro(LocalDateTime.now());
        sintoma.setDataModificacao(null);

        DiarioSintomas salvo =
                repository.save(sintoma);

        eventoPacienteService.criarEvento(
                sintoma.getPaciente().getId(),
                usuarioLogadoId,
                EventoTipos.SINTOMAS_CRIADOS,
                "Sintoma registrado",
                "Um novo sintoma foi registrado",
                EventoPacienteService.metadata(
                        "patientName",
                        sintoma.getPaciente().getNome(),
                        "symptomName",
                        sintoma.getSintoma()
                ),
                EventoPrioridades.NORMAL
        );

        eventoClinicoService.analisarSintoma(
                salvo,
                usuarioLogadoId
        );

        return salvo;
    }

    public DiarioSintomas update(Integer id, DiarioSintomas novosDados, Integer usuarioLogadoId) {
        DiarioSintomas existente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sintoma não encontrado"));
        existente.setSintoma(novosDados.getSintoma());
        existente.setIntensidadeDor(novosDados.getIntensidadeDor());
        existente.setDataReferencia(novosDados.getDataReferencia());
        existente.setDataModificacao(LocalDateTime.now());
        DiarioSintomas atualizado =
                repository.save(existente);

        eventoPacienteService.criarEvento(
                existente.getPaciente().getId(),
                usuarioLogadoId,
                EventoTipos.SINTOMAS_EDITADOS,
                "Sintoma atualizado",
                "Um sintoma foi atualizado",
                EventoPacienteService.metadata(
                        "patientName",
                        existente.getPaciente().getNome(),
                        "symptomName",
                        existente.getSintoma()
                ),
                EventoPrioridades.NORMAL
        );

        return atualizado;
    }

    public void delete(Integer id, Integer usuarioLogadoId) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Sintoma não encontrado");
        }
        DiarioSintomas sintoma =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNaoEncontradoException(
                                        "Sintoma não encontrado"
                                )
                        );
        eventoPacienteService.criarEvento(
                sintoma.getPaciente().getId(),
                usuarioLogadoId,
                EventoTipos.SINTOMAS_REMOVIDOS,
                "Sintoma removido",
                "Um registro de sintoma foi removido",
                EventoPacienteService.metadata(
                        "patientName",
                        sintoma.getPaciente().getNome(),
                        "symptomName",
                        sintoma.getSintoma()
                ),
                EventoPrioridades.NORMAL
        );
        repository.deleteById(id);
    }

    public List<DiarioSintomas> findByPacienteCpf(String cpf) {
        return repository.findByPacienteCpf(cpf);
    }
}
