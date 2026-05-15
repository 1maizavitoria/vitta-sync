package br.com.vittasync.vittasync.Service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class SmsService {

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

    public void enviarCodigo(
            String telefone,
            String codigo
    ) {

        String mensagem =
                "VittaSync\n"
                        + "Seu código de verificação é: "
                        + codigo
                        + ". Validade: 10 minutos.";

        enviarSMS(
                telefone,
                mensagem
        );
    }

    public void enviarLembrete(
            String telefone,
            String nomePaciente,
            String mensagemLembrete
    ) {

        String mensagem =
                "Olá, "
                        + nomePaciente
                        + ". "
                        + mensagemLembrete;

        enviarSMS(
                telefone,
                mensagem
        );
    }

    private void enviarSMS(
            String telefone,
            String mensagem
    ) {

        try {

            String numeroFormatado =
                    formatarTelefoneBR(telefone);

            System.out.println(
                    "Enviando SMS para: "
                            + numeroFormatado
            );

            Message message =
                    Message.creator(
                            new com.twilio.type.PhoneNumber(
                                    numeroFormatado
                            ),
                            new com.twilio.type.PhoneNumber(
                                    twilioPhone
                            ),
                            mensagem
                    ).create();

            System.out.println(
                    "SID: "
                            + message.getSid()
            );

            System.out.println(
                    "STATUS: "
                            + message.getStatus()
            );

        }

        catch (Exception e) {

            System.out.println(
                    "ERRO AO ENVIAR SMS:"
            );

            e.printStackTrace();
        }
    }

    private String formatarTelefoneBR(
            String telefone
    ) {

        return "+55" + telefone;
    }
}