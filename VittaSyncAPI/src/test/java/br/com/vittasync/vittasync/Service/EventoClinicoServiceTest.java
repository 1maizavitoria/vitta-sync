package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Model.DiarioSintomas;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import br.com.vittasync.vittasync.Util.EventoTipos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class EventoClinicoServiceTest {

    private EventoPacienteService eventoPacienteService;
    private EventoClinicoService service;
    private Usuario paciente;

    @BeforeEach
    void setup() {
        eventoPacienteService = mock(EventoPacienteService.class);
        service = new EventoClinicoService(eventoPacienteService);

        paciente = new Usuario();
        paciente.setId(1);
    }

    private SinaisVitais sinaisBase() {
        SinaisVitais sinais = new SinaisVitais();
        sinais.setPaciente(paciente);
        sinais.setPaSistolica(120);
        sinais.setPaDiastolica(80);
        sinais.setTempCelcius(36.5);
        sinais.setSpo2Porcento(98);
        sinais.setFcBpm(70);
        sinais.setFrRpm(16);
        return sinais;
    }

    private Habitos habitosBase() {
        Habitos habitos = new Habitos();
        habitos.setPaciente(paciente);
        habitos.setHorasSono(8);
        habitos.setMinutosExercicio(30);
        return habitos;
    }

    @Test
    void testAnalisarPressaoAltaDisparaEvento() {
        SinaisVitais sinais = sinaisBase();
        sinais.setPaSistolica(150);
        sinais.setPaDiastolica(95);

        service.analisarSinaisVitais(sinais, 99);

        verify(eventoPacienteService).criarEvento(
                eq(1), eq(99),
                eq(EventoTipos.PRESSAO_ANORMAL),
                eq("Pressão arterial elevada"),
                eq("Foi registrada pressão arterial acima do normal"),
                eq(EventoPrioridades.ALTA)
        );
    }

    @Test
    void testAnalisarFebreDisparaEvento() {
        SinaisVitais sinais = sinaisBase();
        sinais.setTempCelcius(38.5);

        service.analisarSinaisVitais(sinais, 99);

        verify(eventoPacienteService).criarEvento(
                eq(1), eq(99),
                eq(EventoTipos.FEBRE_DETECTADA),
                eq("Febre detectada"),
                eq("Foi registrada temperatura corporal elevada"),
                eq(EventoPrioridades.ALTA)
        );
    }

    @Test
    void testAnalisarSpo2CriticoDisparaDoisEventos() {
        SinaisVitais sinais = sinaisBase();
        sinais.setSpo2Porcento(88);

        service.analisarSinaisVitais(sinais, 99);

        verify(eventoPacienteService).criarEvento(eq(1), eq(99),
                eq(EventoTipos.SPO2_BAIXA),
                anyString(), anyString(),
                eq(EventoPrioridades.ALTA));
        verify(eventoPacienteService).criarEvento(eq(1), eq(99),
                eq(EventoTipos.SPO2_CRITICA),
                anyString(), anyString(),
                eq(EventoPrioridades.CRITICO));
    }

    @Test
    void testAnalisarFrequenciaCardiacaAlta() {
        SinaisVitais sinais = sinaisBase();
        sinais.setFcBpm(120);

        service.analisarSinaisVitais(sinais, 99);

        verify(eventoPacienteService).criarEvento(eq(1), eq(99),
                eq(EventoTipos.TAQUICARDIA_DETECTADA),
                anyString(), anyString(),
                eq(EventoPrioridades.ALTA));
    }

    @Test
    void testAnalisarSintomaDorCritica() {
        DiarioSintomas sintoma = new DiarioSintomas();
        sintoma.setPaciente(paciente);
        sintoma.setIntensidadeDor(10);

        service.analisarSintoma(sintoma, 99);

        verify(eventoPacienteService).criarEvento(eq(1), eq(99),
                eq(EventoTipos.DOR_INTENSA_DETECTADA),
                anyString(), anyString(),
                eq(EventoPrioridades.ALTA));
        verify(eventoPacienteService).criarEvento(eq(1), eq(99),
                eq(EventoTipos.DOR_CRITICA_DETECTADA),
                anyString(), anyString(),
                eq(EventoPrioridades.CRITICO));
    }

    @Test
    void testAnalisarHabitosSonoCritico() {
        Habitos habitos = habitosBase();
        habitos.setHorasSono(3);

        service.analisarHabitos(habitos, 99);

        verify(eventoPacienteService).criarEvento(eq(1), eq(99),
                eq(EventoTipos.SONO_CRITICO),
                anyString(), anyString(),
                eq(EventoPrioridades.ALTA));
    }

    @Test
    void testAnalisarHabitosSedentarismo() {
        Habitos habitos = habitosBase();
        habitos.setMinutosExercicio(5);

        service.analisarHabitos(habitos, 99);

        verify(eventoPacienteService).criarEvento(eq(1), eq(99),
                eq(EventoTipos.SEDENTARISMO_DETECTADO),
                anyString(), anyString(),
                eq(EventoPrioridades.NORMAL));
    }
}
