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
                + "Equipe Vittasync";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no.reply.vittasync@gmail.com");
        message.setTo(to);
        message.setSubject("Código de Verificação VittaSync");
        message.setText(mensagem);

        mailSender.send(message);
    }

    public void enviarLembrete(String to, String mensagem) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no.reply.vittasync@gmail.com");
        message.setTo(to);
        message.setSubject("Lembrete de Medição - VittaSync");
        message.setText(mensagem);

        mailSender.send(message);
    }

    public void enviarConviteVinculo(
            String to,
            String nomeConvidado,
            String nomePaciente,
            String codigo,
            String link
    ) {

        String mensagem = "Olá, "
                + nomeConvidado + "\n\n"

                + nomePaciente
                + " convidou você para participar "
                + "do contexto compartilhado de saúde "
                + "na plataforma VittaSync.\n\n"

                + "Código de vínculo: "
                + codigo + "\n\n"

                + "Ou utilize o link abaixo:\n"
                + link + "\n\n"

                + "Este convite possui tempo limitado.\n\n"

                + "Se você não reconhece este convite, "
                + "ignore este email.\n\n"

                + "Atenciosamente,\n"
                + "Equipe VittaSync";

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(
                "no.reply.vittasync@gmail.com"
        );

        message.setTo(to);

        message.setSubject(
                "Convite de vínculo com "
                        + nomePaciente
                        + " - VittaSync"
        );

        message.setText(mensagem);

        mailSender.send(message);
    }
}