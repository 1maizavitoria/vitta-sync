package br.com.vittasync.vittasync.Service;


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

            case "sms":

                smsService.enviarCodigo(
                        usuario.getTelefone(),
                        codigo
                );

                break;

            case "email":

                emailService.enviarCodigo(
                        usuario.getEmail(),
                        codigo
                );

                break;

            default:

                throw new RuntimeException(
                        "Canal inválido"
                );
        }
    }

    public void enviarLembrete(
            Usuario usuario,
            String mensagem,
            String canal
    ) {

        switch (canal.toLowerCase()) {

            case "sms":

                smsService.enviarLembrete(
                        usuario.getTelefone(),
                        usuario.getNome(),
                        mensagem
                );

                break;

            case "email":

                emailService.enviarLembrete(
                        usuario.getEmail(),
                        usuario.getNome(),
                        mensagem
                );

                break;

            case "ambos":

                smsService.enviarLembrete(
                        usuario.getTelefone(),
                        usuario.getNome(),
                        mensagem
                );

                emailService.enviarLembrete(
                        usuario.getEmail(),
                        usuario.getNome(),
                        mensagem
                );

                break;

            default:

                throw new RuntimeException(
                        "Canal inválido"
                );
        }
    }
}