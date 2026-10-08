package br.com.vittasync.vittasync.Service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhone;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void enviarCodigo(String telefone, String codigo) {
        String mensagem = "VittaSync\n"
                + "Seu código de verificação é: "
                + codigo
                + ". Validade: 10 minutos.";

        enviarSMS(telefone, mensagem);
    }

    public void enviarLembrete(String telefone, String nomePaciente, String mensagemLembrete) {
        String mensagem = "Olá, " + nomePaciente + ". " + mensagemLembrete;
        enviarSMS(telefone, mensagem);
    }

    public void enviarAlertaClinico(String telefone, String nomeContato, String nomePaciente, String categoria, String detalhes) {
        String emoji = switch (categoria.toLowerCase()) {
            case "saudavel" -> "✅";
            case "moderado" -> "⚠️";
            case "critico" -> "🚨";
            default -> "ℹ️";
        };

        String mensagem = emoji + " VittaSync - Paciente " + nomePaciente + ": " + categoria;

        enviarSMS(telefone, mensagem);
    }

    public void enviarAlertaRepouso(String telefone, String nomePaciente) {
        String mensagem = "🔵 VittaSync - Repouso recomendado para paciente " + nomePaciente
                + " devido ao desequilíbrio entre sono e exercício.";
        enviarSMS(telefone, mensagem);
    }


    public void enviarDesvioLinhaBase(String telefone, String nomePaciente, String detalhes) {
        enviarSMS(telefone, "VittaSync - Paciente " + nomePaciente
                + ": medição fora do padrão individual.\n" + detalhes);
    }

    private void enviarSMS(String telefone, String mensagem) {
        try {
            String numeroFormatado = telefone.startsWith("+55") ? telefone : "+55" + telefone;

            logger.info("Enviando SMS para: {}", numeroFormatado);

            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(numeroFormatado),
                    new com.twilio.type.PhoneNumber(twilioPhone),
                    mensagem
            ).create();

            logger.info("SMS enviado. SID: {} STATUS: {}", message.getSid(), message.getStatus());

        } catch (Exception e) {
            logger.error("Erro ao enviar SMS para: {}", telefone, e);
        }
    }
}
