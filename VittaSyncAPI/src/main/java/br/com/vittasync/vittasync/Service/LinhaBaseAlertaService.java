package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Model.LinhaBase;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Repository.ContatoEmergenciaRepository;
import br.com.vittasync.vittasync.Repository.LinhaBaseRepository;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import br.com.vittasync.vittasync.Util.EventoTipos;
import org.springframework.stereotype.Service;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;


@Service
public class LinhaBaseAlertaService {

    private final LinhaBaseRepository linhaBaseRepository;
    private final LinhaBaseService linhaBaseService;
    private final EventoPacienteService eventoPacienteService;
    private final ContatoEmergenciaRepository contatoEmergenciaRepository;
    private final NotificacaoService notificacaoService;

    public LinhaBaseAlertaService(LinhaBaseRepository linhaBaseRepository,
                                 LinhaBaseService linhaBaseService,
                                 EventoPacienteService eventoPacienteService,
                                 ContatoEmergenciaRepository contatoEmergenciaRepository,
                                 NotificacaoService notificacaoService) {
        this.linhaBaseRepository = linhaBaseRepository;
        this.linhaBaseService = linhaBaseService;
        this.eventoPacienteService = eventoPacienteService;
        this.contatoEmergenciaRepository = contatoEmergenciaRepository;
        this.notificacaoService = notificacaoService;
    }

    public void notificarDesvios(SinaisVitais sinais, Integer autorId, Set<Integer> contatosEmergenciaAcionados) {
        Integer pacienteId = sinais.getPaciente().getId();
        List<String> desvios = new ArrayList<>();
        NumberFormat formato = NumberFormat.getNumberInstance(Locale.forLanguageTag("pt-BR"));
        formato.setMaximumFractionDigits(6);

        for (LinhaBase base : linhaBaseRepository.findByPacienteId(pacienteId)) {
            //a medição que completa a formação não é comparada com a própria referência.
            if (sinais.getDataRegistro() == null || base.getDataFormacao() == null
                    || !sinais.getDataRegistro().isAfter(base.getDataFormacao())) continue;

            Number valor = linhaBaseService.valorSinal(sinais, base.getSinal());
            String comparacao = linhaBaseService.comparar(base, valor);
            if (!"abaixo".equals(comparacao) && !"acima".equals(comparacao)) continue;

            desvios.add(nomeSinal(base.getSinal()) + ": " + formato.format(valor)
                    + " " + unidade(base.getSinal()) + " (" + comparacao + " da faixa "
                    + formato.format(base.getLimiteInferior()) + "–"
                    + formato.format(base.getLimiteSuperior()) + " " + unidade(base.getSinal()) + ")");
        }

        if (desvios.isEmpty()) return;

        String detalhes = String.join("; ", desvios);
        eventoPacienteService.criarEvento(pacienteId, autorId, EventoTipos.DESVIO_LINHA_BASE,
                "Medição fora do padrão individual", detalhes,
                EventoPacienteService.metadata("patientName", sinais.getPaciente().getNome(),
                        "measurementId", sinais.getId().toString()), EventoPrioridades.ALTA);

        for (ContatoEmergencia contato : contatoEmergenciaRepository.findByPacienteIdOrderByDataRegistroAsc(pacienteId)) {
            //prioriza o envio de emergência já solicitado para esta mesma medição.
            if (contatosEmergenciaAcionados.contains(contato.getId())) continue;
            notificacaoService.enviarDesvioLinhaBase(contato.getId(), contato.getNome(), contato.getEmail(),
                    contato.getTelefone(), Boolean.TRUE.equals(contato.getCanalEmail()),
                    Boolean.TRUE.equals(contato.getCanalSms()), sinais.getPaciente().getNome(), detalhes);
        }
    }

    private String nomeSinal(String sinal) {
        return switch (sinal) {
            case "peso" -> "Peso";
            case "frequencia_cardiaca" -> "Frequência cardíaca";
            case "frequencia_respiratoria" -> "Frequência respiratória";
            case "pressao_sistolica" -> "Pressão sistólica";
            case "pressao_diastolica" -> "Pressão diastólica";
            case "temperatura" -> "Temperatura";
            case "saturacao" -> "Saturação";
            default -> sinal;
        };
    }

    private String unidade(String sinal) {
        return switch (sinal) {
            case "peso" -> "kg";
            case "frequencia_cardiaca" -> "bpm";
            case "frequencia_respiratoria" -> "rpm";
            case "pressao_sistolica", "pressao_diastolica" -> "mmHg";
            case "temperatura" -> "°C";
            case "saturacao" -> "%";
            default -> "";
        };
    }
}
