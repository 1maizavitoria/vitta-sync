package br.com.vittasync.vittasync.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCodigo(String to, String codigo) {
        String mensagem = "Olá,\n\n"
                + "Seu código de verificação é: " + codigo + "\n\n"
                + "Este código é válido por 10 minutos.\n\n"
                + "Se você não solicitou este código, ignore este email.\n\n"
                + "Atenciosamente,\n"
                + "Equipe VittaSync";

        enviarEmailPersonalizado(to, "Código de Verificação VittaSync", mensagem);
    }

    public void enviarLembrete(String to, String nomePaciente, String mensagemLembrete) {
        String mensagem = "Olá, " + nomePaciente + "\n\n"
                + mensagemLembrete + "\n\n"
                + "Este lembrete foi enviado pela plataforma VittaSync "
                + "para auxiliar no acompanhamento contínuo da saúde.\n\n"
                + "Atenciosamente,\nEquipe VittaSync";

        enviarEmailPersonalizado(to, "Lembrete de medição - VittaSync", mensagem);
    }

    public void enviarConviteVinculo(String to, String nomeConvidado, String nomePaciente, String codigo, String link) {
        String mensagem = "Olá, " + nomeConvidado + "\n\n"
                + nomePaciente + " convidou você para participar do contexto compartilhado de saúde "
                + "na plataforma VittaSync.\n\n"
                + "Código de vínculo: " + codigo + "\n\n"
                + "Ou utilize o link abaixo:\n" + link + "\n\n"
                + "Este convite possui tempo limitado.\n\n"
                + "Se você não reconhece este convite, ignore este email.\n\n"
                + "Atenciosamente,\nEquipe VittaSync";

        enviarEmailPersonalizado(to, "Convite de vínculo com " + nomePaciente + " - VittaSync", mensagem);
    }

    public void enviarAlertaClinico(String to, String nomeContato, String nomePaciente, String titulo, String detalhes) {
        String mensagem = "Olá, " + nomeContato + "\n\n"
                + "Foram detectadas alterações na estabilidade clínica do paciente "
                + nomePaciente + ":\n\n"
                + detalhes + "\n\n"
                + "Atenciosamente,\nEquipe VittaSync";

        enviarEmailPersonalizado(to, titulo, mensagem);
    }

    public void enviarEmailPersonalizado(String to, String subject, String corpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no.reply.vittasync@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(corpo);
        mailSender.send(message);
    }
}
