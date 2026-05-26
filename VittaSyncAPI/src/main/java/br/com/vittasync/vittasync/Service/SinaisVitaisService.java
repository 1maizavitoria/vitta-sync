package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import br.com.vittasync.vittasync.Util.EventoTipos;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class SinaisVitaisService {

    private final SinaisVitaisRepository repository;
    private final EventoPacienteService eventoPacienteService;
    private final EventoClinicoService eventoClinicoService;

    public SinaisVitaisService(
            SinaisVitaisRepository repository,
            EventoPacienteService eventoPacienteService,
            EventoClinicoService eventoClinicoService
    ) {
        this.repository = repository;
        this.eventoPacienteService = eventoPacienteService;
        this.eventoClinicoService = eventoClinicoService;
    }

    public SinaisVitais create(
            SinaisVitais sinais,
            Integer usuarioLogadoId
    ) {

        sinais.setRegistradoPorUsuarioId(usuarioLogadoId);
        sinais.setDataRegistro(LocalDateTime.now());
        sinais.setDataModificacao(null);

        SinaisVitais salvo = repository.save(sinais);

        eventoPacienteService.criarEvento(
                sinais.getPaciente().getId(),
                usuarioLogadoId,
                EventoTipos.SINAIS_VITAIS_CRIADOS,
                "Sinais vitais registrados",
                "Novos sinais vitais foram registrados",
                EventoPrioridades.NORMAL
        );

        eventoClinicoService.analisarSinaisVitais(
                salvo,
                usuarioLogadoId
        );

        return salvo;
    }

    public SinaisVitais update(Integer id, SinaisVitais novosDados, Integer usuarioLogadoId) {
        SinaisVitais existente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sinais vitais não encontrados"));
        existente.setPeso(novosDados.getPeso());
        existente.setFcBpm(novosDados.getFcBpm());
        existente.setFrRpm(novosDados.getFrRpm());
        existente.setPaSistolica(novosDados.getPaSistolica());
        existente.setPaDiastolica(novosDados.getPaDiastolica());
        existente.setTempCelcius(novosDados.getTempCelcius());
        existente.setSpo2Porcento(novosDados.getSpo2Porcento());
        existente.setDataModificacao(LocalDateTime.now());
        SinaisVitais atualizado = repository.save(existente);
        eventoPacienteService.criarEvento(
                existente.getPaciente().getId(),
                usuarioLogadoId,
                EventoTipos.SINAIS_VITAIS_EDITADOS,
                "Sinais vitais atualizados",
                "Sinais vitais foram atualizados",
                EventoPrioridades.NORMAL
        );
        return atualizado;
    }

    public void delete(Integer id, Integer usuarioLogadoId) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Sinais vitais não encontrados");
        }
        SinaisVitais sinais =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNaoEncontradoException(
                                        "Sinais vitais não encontrados"
                                )
                        );
        eventoPacienteService.criarEvento(
                sinais.getPaciente().getId(),
                usuarioLogadoId,
                EventoTipos.SINAIS_VITAIS_REMOVIDOS,
                "Sinais vitais removidos",
                "Um registro de sinais vitais foi removido",
                EventoPrioridades.NORMAL
        );
        repository.deleteById(id);
    }

    public List<SinaisVitais> findByPacienteCpf(String cpf) {
        return repository.findByPacienteCpf(cpf);
    }
}
