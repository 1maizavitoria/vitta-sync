package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Model.DiarioSintomas;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import br.com.vittasync.vittasync.Util.EventoTipos;
import org.springframework.stereotype.Service;

@Service
public class EventoClinicoService {

    private final EventoPacienteService eventoPacienteService;

    private void criarEventoClinico(
            Integer pacienteId,
            Integer usuarioLogadoId,
            String tipoEvento,
            String titulo,
            String descricao,
            String prioridade
    ) {
        eventoPacienteService.criarEvento(
                pacienteId,
                usuarioLogadoId,
                tipoEvento,
                titulo,
                descricao,
                prioridade
        );
    }

    public EventoClinicoService(EventoPacienteService eventoPacienteService) {
        this.eventoPacienteService = eventoPacienteService;
    }

    public void analisarSinaisVitais(
            SinaisVitais sinais,
            Integer usuarioLogadoId
    ) {
        analisarPressao(
                sinais,
                usuarioLogadoId
        );

        analisarTemperatura(
                sinais,
                usuarioLogadoId
        );

        analisarSpo2(
                sinais,
                usuarioLogadoId
        );

        analisarFrequenciaCardiaca(
                sinais,
                usuarioLogadoId
        );

        analisarFrequenciaRespiratoria(
                sinais,
                usuarioLogadoId
        );
    }

    public void analisarSintoma(
            DiarioSintomas sintoma,
            Integer usuarioLogadoId
    ) {

        analisarDorIntensa(
                sintoma,
                usuarioLogadoId
        );
    }

    public void analisarHabitos(
            Habitos habitos,
            Integer usuarioLogadoId
    ) {

        analisarSono(
                habitos,
                usuarioLogadoId
        );

        analisarExercicio(
                habitos,
                usuarioLogadoId
        );
    }

    //Sinais vitais
    private void analisarPressao(
            SinaisVitais sinais,
            Integer usuarioLogadoId
    ) {

        if (
                sinais.getPaSistolica() >= 140
                        || sinais.getPaDiastolica() >= 90
        ) {

            criarEventoClinico(
                    sinais.getPaciente().getId(),
                    usuarioLogadoId,
                    EventoTipos.PRESSAO_ANORMAL,
                    "Pressão arterial elevada",
                    "Foi registrada pressão arterial acima do normal",
                    EventoPrioridades.ALTA
            );
        }
    }

    private void analisarTemperatura(
            SinaisVitais sinais,
            Integer usuarioLogadoId
    ) {

        if (
                sinais.getTempCelcius() >= 38.0
        ) {

            criarEventoClinico(
                    sinais.getPaciente().getId(),
                    usuarioLogadoId,
                    EventoTipos.FEBRE_DETECTADA,
                    "Febre detectada",
                    "Foi registrada temperatura corporal elevada",
                    EventoPrioridades.ALTA
            );
        }
    }

    private void analisarSpo2(
            SinaisVitais sinais,
            Integer usuarioLogadoId
    ) {

        if (
                sinais.getSpo2Porcento() < 95
        ) {

            criarEventoClinico(
                    sinais.getPaciente().getId(),
                    usuarioLogadoId,
                    EventoTipos.SPO2_BAIXA,
                    "Saturação baixa detectada",
                    "Foi registrada saturação abaixo do normal",
                    EventoPrioridades.ALTA
            );
        }

        if (
                sinais.getSpo2Porcento() < 90
        ) {

            criarEventoClinico(
                    sinais.getPaciente().getId(),
                    usuarioLogadoId,
                    EventoTipos.SPO2_CRITICA,
                    "Saturação crítica detectada",
                    "Foi registrada saturação em nível crítico",
                    EventoPrioridades.CRITICO
            );
        }
    }

    private void analisarFrequenciaCardiaca(
            SinaisVitais sinais,
            Integer usuarioLogadoId
    ) {

        if (
                sinais.getFcBpm() > 100
        ) {

            criarEventoClinico(
                    sinais.getPaciente().getId(),
                    usuarioLogadoId,
                    EventoTipos.TAQUICARDIA_DETECTADA,
                    "Frequência cardíaca elevada",
                    "Foi registrada frequência cardíaca acima do normal",
                    EventoPrioridades.ALTA
            );
        }

        if (
                sinais.getFcBpm() < 50
        ) {

            criarEventoClinico(
                    sinais.getPaciente().getId(),
                    usuarioLogadoId,
                    EventoTipos.BRADICARDIA_DETECTADA,
                    "Frequência cardíaca baixa",
                    "Foi registrada frequência cardíaca abaixo do normal",
                    EventoPrioridades.ALTA
            );
        }
    }

    private void analisarFrequenciaRespiratoria(
            SinaisVitais sinais,
            Integer usuarioLogadoId
    ) {

        if (
                sinais.getFrRpm() > 24
        ) {

            criarEventoClinico(
                    sinais.getPaciente().getId(),
                    usuarioLogadoId,
                    EventoTipos.TAQUIPNEIA_DETECTADA,
                    "Frequência respiratória elevada",
                    "Foi registrada frequência respiratória acima do normal",
                    EventoPrioridades.ALTA
            );
        }

        if (
                sinais.getFrRpm() < 12
        ) {

            criarEventoClinico(
                    sinais.getPaciente().getId(),
                    usuarioLogadoId,
                    EventoTipos.BRADIPNEIA_DETECTADA,
                    "Frequência respiratória baixa",
                    "Foi registrada frequência respiratória abaixo do normal",
                    EventoPrioridades.ALTA
            );
        }
    }

    //Sintomas
    private void analisarDorIntensa(
            DiarioSintomas sintoma,
            Integer usuarioLogadoId
    ) {

        if (
                sintoma.getIntensidadeDor() >= 8
        ) {

            criarEventoClinico(
                    sintoma.getPaciente().getId(),
                    usuarioLogadoId,
                    EventoTipos.DOR_INTENSA_DETECTADA,
                    "Dor intensa registrada",
                    "Foi registrada dor em nível elevado",
                    EventoPrioridades.ALTA
            );
        }

        if (
                sintoma.getIntensidadeDor() == 10
        ) {

            criarEventoClinico(
                    sintoma.getPaciente().getId(),
                    usuarioLogadoId,
                    EventoTipos.DOR_CRITICA_DETECTADA,
                    "Dor extrema registrada",
                    "Foi registrada dor em nível crítico",
                    EventoPrioridades.CRITICO
            );
        }
    }

    //Habitos
    private void analisarSono(
            Habitos habitos,
            Integer usuarioLogadoId
    ) {

        if (
                habitos.getHorasSono() < 4
        ) {

            criarEventoClinico(
                    habitos.getPaciente().getId(),
                    usuarioLogadoId,
                    EventoTipos.SONO_CRITICO,
                    "Poucas horas de sono",
                    "Foi registrado período de sono muito abaixo do ideal",
                    EventoPrioridades.ALTA
            );
        }
    }

    private void analisarExercicio(
            Habitos habitos,
            Integer usuarioLogadoId
    ) {

        if (
                habitos.getMinutosExercicio() < 10
        ) {

            criarEventoClinico(
                    habitos.getPaciente().getId(),
                    usuarioLogadoId,
                    EventoTipos.SEDENTARISMO_DETECTADO,
                    "Baixo nível de atividade física",
                    "Foi registrado nível muito baixo de exercício físico",
                    EventoPrioridades.NORMAL
            );
        }
    }

}


