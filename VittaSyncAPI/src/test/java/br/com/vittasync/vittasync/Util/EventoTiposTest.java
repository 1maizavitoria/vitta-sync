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
    void testConstrutorPrivadoViaReflexao() throws Exception {
        Constructor<EventoTipos> constructor = EventoTipos.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        EventoTipos instancia = constructor.newInstance();
        assertThat(instancia).isNotNull();
    }
}
