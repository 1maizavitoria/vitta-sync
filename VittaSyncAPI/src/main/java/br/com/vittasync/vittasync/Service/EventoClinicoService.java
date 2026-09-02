package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Model.DiarioSintomas;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Repository.ContatoEmergenciaRepository;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import br.com.vittasync.vittasync.Util.EventoTipos;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventoClinicoService {

    private final EventoPacienteService eventoPacienteService;
    private final ContatoEmergenciaRepository contatoEmergenciaRepository;
    private final NotificacaoService notificacaoService;

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

    public EventoClinicoService(
            EventoPacienteService eventoPacienteService,
            ContatoEmergenciaRepository contatoEmergenciaRepository,
            NotificacaoService notificacaoService
    ) {
        this.eventoPacienteService = eventoPacienteService;
        this.contatoEmergenciaRepository = contatoEmergenciaRepository;
        this.notificacaoService = notificacaoService;
    }

    public void analisarSinaisVitais(
            SinaisVitais sinais,
            Integer usuarioLogadoId
    ) {
        List<String> situacoesCriticas = detectarSituacoesCriticas(sinais);

        if (!situacoesCriticas.isEmpty()) {
            registrarAlertaEmergencia(sinais, usuarioLogadoId, situacoesCriticas);
            notificarContatosEmergencia(sinais, situacoesCriticas);
        }

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

        if (pressaoCritica(sinais)) {
            return;
        }

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

        if (temperaturaCritica(sinais)) {
            return;
        }

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

        if (spo2Critica(sinais)) {
            return;
        }

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

    }

    private void analisarFrequenciaCardiaca(
            SinaisVitais sinais,
            Integer usuarioLogadoId
    ) {

        if (frequenciaCardiacaCritica(sinais)) {
            return;
        }

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

        if (frequenciaRespiratoriaCritica(sinais)) {
            return;
        }

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

    private List<String> detectarSituacoesCriticas(SinaisVitais sinais) {
        List<String> situacoes = new ArrayList<>();

        if (frequenciaCardiacaCritica(sinais)) {
            situacoes.add("Frequência cardíaca: " + sinais.getFcBpm() + " bpm");
        }

        if (frequenciaRespiratoriaCritica(sinais)) {
            situacoes.add("Frequência respiratória: " + sinais.getFrRpm() + " rpm");
        }

        if (pressaoCritica(sinais)) {
            situacoes.add("Pressão arterial: " + sinais.getPaSistolica() + "/" + sinais.getPaDiastolica() + " mmHg");
        }

        if (temperaturaCritica(sinais)) {
            situacoes.add("Temperatura: " + sinais.getTempCelcius() + " °C");
        }

        if (spo2Critica(sinais)) {
            situacoes.add("Saturação de oxigênio: " + sinais.getSpo2Porcento() + "%");
        }

        return situacoes;
    }

    private void registrarAlertaEmergencia(
            SinaisVitais sinais,
            Integer usuarioLogadoId,
            List<String> situacoesCriticas
    ) {
        String descricao = "Medição crítica registrada: " + String.join("; ", situacoesCriticas);

        eventoPacienteService.criarEvento(
                sinais.getPaciente().getId(),
                usuarioLogadoId,
                "alerta_emergencia",
                "Alerta de emergência",
                descricao,
                EventoPacienteService.metadata(
                        "patientName",
                        sinais.getPaciente().getNome()
                ),
                EventoPrioridades.CRITICO
        );
    }

    private void notificarContatosEmergencia(
            SinaisVitais sinais,
            List<String> situacoesCriticas
    ) {
        List<ContatoEmergencia> contatos = contatoEmergenciaRepository
                .findByPacienteIdOrderByDataRegistroAsc(sinais.getPaciente().getId());

        String mensagem = "Sinais vitais em nível crítico:\n- "
                + String.join("\n- ", situacoesCriticas);

        for (ContatoEmergencia contato : contatos) {
            if (Boolean.TRUE.equals(contato.getReceberAlertaSinaisVitaisCritico())) {
                notificacaoService.enviarAlertaEmergencia(contato, mensagem, "critico");
            }
        }
    }

    private boolean frequenciaCardiacaCritica(SinaisVitais sinais) {
        return sinais.getFcBpm() != null
                && (sinais.getFcBpm() < 50 || sinais.getFcBpm() > 120);
    }

    private boolean frequenciaRespiratoriaCritica(SinaisVitais sinais) {
        return sinais.getFrRpm() != null
                && (sinais.getFrRpm() < 8 || sinais.getFrRpm() > 30);
    }

    private boolean pressaoCritica(SinaisVitais sinais) {
        return (sinais.getPaSistolica() != null
                && (sinais.getPaSistolica() < 80 || sinais.getPaSistolica() > 140))
                || (sinais.getPaDiastolica() != null
                && (sinais.getPaDiastolica() < 50 || sinais.getPaDiastolica() > 100));
    }

    private boolean temperaturaCritica(SinaisVitais sinais) {
        return sinais.getTempCelcius() != null
                && (sinais.getTempCelcius() < 35 || sinais.getTempCelcius() > 39);
    }

    private boolean spo2Critica(SinaisVitais sinais) {
        return sinais.getSpo2Porcento() != null && sinais.getSpo2Porcento() < 90;
    }

}


