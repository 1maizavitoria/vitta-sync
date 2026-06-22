package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import br.com.vittasync.vittasync.Util.EventoTipos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SinaisVitaisServiceTest {

    private SinaisVitaisRepository repository;
    private EventoPacienteService eventoPacienteService;
    private EventoClinicoService eventoClinicoService;
    private SinaisVitaisService service;

    private Usuario paciente;

    @BeforeEach
    void setup() {
        repository = mock(SinaisVitaisRepository.class);
        eventoPacienteService = mock(EventoPacienteService.class);
        eventoClinicoService = mock(EventoClinicoService.class);

        service = new SinaisVitaisService(repository, eventoPacienteService, eventoClinicoService);

        paciente = new Usuario();
        paciente.setId(1);
    }

    @Test
    void testCreateSalvaSinaisEDisparaEventos() {
        SinaisVitais sinais = new SinaisVitais();
        sinais.setPaciente(paciente);

        SinaisVitais salvo = new SinaisVitais();
        salvo.setPaciente(paciente);

        when(repository.save(sinais)).thenReturn(salvo);

        SinaisVitais resultado = service.create(sinais, 99);

        assertThat(resultado).isEqualTo(salvo);
        verify(repository).save(sinais);
        verify(eventoPacienteService).criarEvento(
                eq(paciente.getId()), eq(99),
                eq(EventoTipos.SINAIS_VITAIS_CRIADOS),
                eq("Sinais vitais registrados"),
                eq("Novos sinais vitais foram registrados"),
                eq(EventoPrioridades.NORMAL)
        );
        verify(eventoClinicoService).analisarSinaisVitais(salvo, 99);
    }

    @Test
    void testUpdateSinaisExistente() {
        SinaisVitais existente = new SinaisVitais();
        existente.setId(5);
        existente.setPaciente(paciente);

        SinaisVitais novosDados = new SinaisVitais();
        novosDados.setPeso(70.0);
        novosDados.setFcBpm(80);

        when(repository.findById(5)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        SinaisVitais atualizado = service.update(5, novosDados, 99);

        assertThat(atualizado.getPeso()).isEqualTo(70.0);
        assertThat(atualizado.getFcBpm()).isEqualTo(80);
        verify(eventoPacienteService).criarEvento(
                eq(paciente.getId()), eq(99),
                eq(EventoTipos.SINAIS_VITAIS_EDITADOS),
                eq("Sinais vitais atualizados"),
                eq("Sinais vitais foram atualizados"),
                eq(EventoPrioridades.NORMAL)
        );
    }

    @Test
    void testUpdateSinaisNaoEncontradoLancaExcecao() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        SinaisVitais novosDados = new SinaisVitais();
        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.update(99, novosDados, 1));
    }

    @Test
    void testDeleteSinaisExistente() {
        SinaisVitais sinais = new SinaisVitais();
        sinais.setId(7);
        sinais.setPaciente(paciente);

        when(repository.existsById(7)).thenReturn(true);
        when(repository.findById(7)).thenReturn(Optional.of(sinais));

        service.delete(7, 99);

        verify(eventoPacienteService).criarEvento(
                eq(paciente.getId()), eq(99),
                eq(EventoTipos.SINAIS_VITAIS_REMOVIDOS),
                eq("Sinais vitais removidos"),
                eq("Um registro de sinais vitais foi removido"),
                eq(EventoPrioridades.NORMAL)
        );
        verify(repository).deleteById(7);
    }

    @Test
    void testDeleteSinaisNaoExistenteLancaExcecao() {
        when(repository.existsById(50)).thenReturn(false);

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.delete(50, 99));
    }

    @Test
    void testFindByPacienteCpf() {
        when(repository.findByPacienteCpf("12345678901")).thenReturn(List.of(new SinaisVitais()));

        List<SinaisVitais> lista = service.findByPacienteCpf("12345678901");

        assertThat(lista).hasSize(1);
        verify(repository).findByPacienteCpf("12345678901");
    }
}
