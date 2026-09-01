package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Model.Usuario;
import org.springframework.stereotype.Service;


@Service
public class NotificacaoService {

    private final EmailService emailService;
    private final SmsService smsService;

    public NotificacaoService(
            EmailService emailService,
            SmsService smsService
    ) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    public void enviarCodigo(
            Usuario usuario,
            String codigo,
            String canal
    ) {
        switch (canal.toLowerCase()) {
            case "sms" -> smsService.enviarCodigo(usuario.getTelefone(), codigo);
            case "email" -> emailService.enviarCodigo(usuario.getEmail(), codigo);
            default -> throw new RuntimeException("Canal inválido");
        }
    }

    public void enviarLembrete(
            Usuario usuario,
            String mensagem,
            String canal
    ) {
        switch (canal.toLowerCase()) {
            case "sms" -> smsService.enviarLembrete(usuario.getTelefone(), usuario.getNome(), mensagem);
            case "email" -> emailService.enviarLembrete(usuario.getEmail(), usuario.getNome(), mensagem);
            case "ambos" -> {
                smsService.enviarLembrete(usuario.getTelefone(), usuario.getNome(), mensagem);
                emailService.enviarLembrete(usuario.getEmail(), usuario.getNome(), mensagem);
            }
            default -> throw new RuntimeException("Canal inválido");
        }
    }

    public void enviarAlertaEmergencia(
            ContatoEmergencia contato,
            String mensagemAgrupada,
            String categoria
    ) {
        String nomePaciente = contato.getPaciente().getNome();


        String tituloEmail = switch (categoria.toLowerCase()) {
            case "saudavel" -> "🟢 VittaSync - Estabilidade SAUDÁVEL do paciente " + nomePaciente;
            case "moderado" -> "🟡️ VittaSync - Estabilidade MODERADA do paciente " + nomePaciente;
            case "critico" -> "🔴 VittaSync - Estabilidade CRÍTICA do paciente " + nomePaciente;
            default -> "ℹ️ VittaSync - Paciente " + nomePaciente;
        };

        if (Boolean.TRUE.equals(contato.getCanalEmail())) {
            String corpoEmail = "Olá, " + contato.getNome() + "\n\n"
                    + tituloEmail + "\n\n"
                    + mensagemAgrupada + "\n\n"
                    + "Atenciosamente,\nEquipe VittaSync";

            emailService.enviarEmailPersonalizado(contato.getEmail(), tituloEmail, corpoEmail);
        }

        if (Boolean.TRUE.equals(contato.getCanalSms())) {
            smsService.enviarAlertaClinico(
                    contato.getTelefone(),
                    contato.getNome(),
                    nomePaciente,
                    categoria,
                    mensagemAgrupada
            );
        }
    }

    public void enviarAlertaRepouso(ContatoEmergencia contato, String canal) {
        String nomePaciente = contato.getPaciente().getNome();

        switch (canal.toLowerCase()) {
            case "sms" -> smsService.enviarAlertaRepouso(contato.getTelefone(), nomePaciente);
            case "email" -> emailService.enviarAlertaRepouso(contato.getEmail(), nomePaciente);
            case "ambos" -> {
                smsService.enviarAlertaRepouso(contato.getTelefone(), nomePaciente);
                emailService.enviarAlertaRepouso(contato.getEmail(), nomePaciente);
            }
            default -> throw new RuntimeException("Canal inválido");
        }
    }

}
