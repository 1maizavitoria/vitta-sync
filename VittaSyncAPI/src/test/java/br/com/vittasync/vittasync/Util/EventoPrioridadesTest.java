package br.com.vittasync.vittasync.Util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.assertThat;

class EventoPrioridadesTest {

    @Test
    void testConstantes() {
        assertThat(EventoPrioridades.NORMAL).isEqualTo("normal");
        assertThat(EventoPrioridades.ALTA).isEqualTo("alta");
        assertThat(EventoPrioridades.CRITICO).isEqualTo("critico");
    }

    @Test
    void testConstrutorPrivadoViaReflexao() throws Exception {
        Constructor<EventoPrioridades> constructor = EventoPrioridades.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        EventoPrioridades instancia = constructor.newInstance();
        assertThat(instancia).isNotNull();
    }
}
