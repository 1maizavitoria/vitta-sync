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
}
