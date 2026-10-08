package br.com.vittasync.vittasync.Util;


import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import static org.assertj.core.api.Assertions.assertThat;


class EventoTiposTest {

    @Test
    void testConstantes() {
        assertThat(EventoTipos.SINAIS_VITAIS_CRIADOS).isEqualTo("sinais_vitais_criados");
        assertThat(EventoTipos.SINAIS_VITAIS_EDITADOS).isEqualTo("sinais_vitais_editados");
        assertThat(EventoTipos.SINAIS_VITAIS_REMOVIDOS).isEqualTo("sinais_vitais_removidos");

        assertThat(EventoTipos.PRESSAO_ANORMAL).isEqualTo("pressao_anormal");
        assertThat(EventoTipos.FEBRE_DETECTADA).isEqualTo("febre_detectada");
        assertThat(EventoTipos.SPO2_BAIXA).isEqualTo("spo2_baixa");
        assertThat(EventoTipos.SPO2_CRITICA).isEqualTo("spo2_critica");

        assertThat(EventoTipos.TAQUICARDIA_DETECTADA).isEqualTo("taquicardia_detectada");
        assertThat(EventoTipos.BRADICARDIA_DETECTADA).isEqualTo("bradicardia_detectada");

        assertThat(EventoTipos.TAQUIPNEIA_DETECTADA).isEqualTo("taquipneia_detectada");
        assertThat(EventoTipos.BRADIPNEIA_DETECTADA).isEqualTo("bradipneia_detectada");

        assertThat(EventoTipos.DOR_INTENSA_DETECTADA).isEqualTo("dor_intensa_detectada");
        assertThat(EventoTipos.DOR_CRITICA_DETECTADA).isEqualTo("dor_critica_detectada");

        assertThat(EventoTipos.SONO_CRITICO).isEqualTo("sono_critico");
        assertThat(EventoTipos.SEDENTARISMO_DETECTADO).isEqualTo("sedentarismo_detectado");

        assertThat(EventoTipos.DOCUMENTO_ENVIADO).isEqualTo("documento_enviado");
        assertThat(EventoTipos.DOCUMENTO_REMOVIDO).isEqualTo("documento_removido");
    }

    @Test
    void testToFrontendCode() {
        assertThat(EventoTipos.toFrontendCode(null)).isNull();
        assertThat(EventoTipos.toFrontendCode("outro_tipo")).isEqualTo("outro_tipo");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.DESVIO_LINHA_BASE)).isEqualTo("desvio_linha_base");

        assertThat(EventoTipos.toFrontendCode(EventoTipos.DOCUMENTO_ENVIADO)).isEqualTo("DOCUMENT_UPLOADED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.DOCUMENTO_REMOVIDO)).isEqualTo("DOCUMENT_REMOVED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.SINAIS_VITAIS_CRIADOS)).isEqualTo("VITAL_SIGNS_CREATED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.SINAIS_VITAIS_EDITADOS)).isEqualTo("VITAL_SIGNS_UPDATED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.SINAIS_VITAIS_REMOVIDOS)).isEqualTo("VITAL_SIGNS_REMOVED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.HABITOS_CRIADOS)).isEqualTo("HABITS_CREATED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.HABITOS_EDITADOS)).isEqualTo("HABITS_UPDATED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.HABITOS_REMOVIDOS)).isEqualTo("HABITS_REMOVED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.SINTOMAS_CRIADOS)).isEqualTo("SYMPTOMS_CREATED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.SINTOMAS_EDITADOS)).isEqualTo("SYMPTOMS_UPDATED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.SINTOMAS_REMOVIDOS)).isEqualTo("SYMPTOMS_REMOVED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.VINCULO_CRIADO)).isEqualTo("LINK_CREATED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.VINCULO_REMOVIDO)).isEqualTo("LINK_REMOVED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.LEMBRETE_CRIADO)).isEqualTo("REMINDER_CREATED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.LEMBRETE_ATUALIZADO)).isEqualTo("REMINDER_UPDATED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.PRESSAO_ANORMAL)).isEqualTo("HIGH_BLOOD_PRESSURE");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.FEBRE_DETECTADA)).isEqualTo("FEVER_DETECTED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.SPO2_BAIXA)).isEqualTo("LOW_OXYGEN_SATURATION");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.SPO2_CRITICA)).isEqualTo("CRITICAL_OXYGEN_SATURATION");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.TAQUICARDIA_DETECTADA)).isEqualTo("HIGH_HEART_RATE");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.BRADICARDIA_DETECTADA)).isEqualTo("LOW_HEART_RATE");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.TAQUIPNEIA_DETECTADA)).isEqualTo("HIGH_RESPIRATORY_RATE");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.BRADIPNEIA_DETECTADA)).isEqualTo("LOW_RESPIRATORY_RATE");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.DOR_INTENSA_DETECTADA)).isEqualTo("INTENSE_PAIN_DETECTED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.DOR_CRITICA_DETECTADA)).isEqualTo("CRITICAL_PAIN_DETECTED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.SONO_CRITICO)).isEqualTo("CRITICAL_SLEEP");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.SEDENTARISMO_DETECTADO)).isEqualTo("LOW_PHYSICAL_ACTIVITY");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.META_CRIADA)).isEqualTo("GOAL_CREATED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.META_ATUALIZADA)).isEqualTo("GOAL_UPDATED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.META_REMOVIDA)).isEqualTo("GOAL_REMOVED");
        assertThat(EventoTipos.toFrontendCode(EventoTipos.META_CONCLUIDA)).isEqualTo("GOAL_COMPLETED");
    }

    @Test
    void testConstrutorPrivadoViaReflexao() throws Exception {
        Constructor<EventoTipos> constructor = EventoTipos.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        EventoTipos instancia = constructor.newInstance();
        assertThat(instancia).isNotNull();
    }
}
