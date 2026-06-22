package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class HabitosServiceTest {

    private HabitosRepository repository;
    private EventoPacienteService eventoPacienteService;
    private EventoClinicoService eventoClinicoService;
    private HabitosService service;

    private Usuario paciente;

    @BeforeEach
    void setup() {
        repository = mock(HabitosRepository.class);
        eventoPacienteService = mock(EventoPacienteService.class);
        eventoClinicoService = mock(EventoClinicoService.class);

        service = new HabitosService(repository, eventoPacienteService, eventoClinicoService);

        paciente = new Usuario();
        paciente.setId(1);
    }

    @Test
    void testCreateSalvaHabitoEDisparaEventos() {
        Habitos habito = new Habitos();
        habito.setPaciente(paciente);

        Habitos salvo = new Habitos();
        salvo.setPaciente(paciente);

        when(repository.save(habito)).thenReturn(salvo);

        Habitos resultado = service.create(habito, 99);

        assertThat(resultado).isEqualTo(salvo);
        verify(repository).save(habito);
        verify(eventoPacienteService).criarEvento(
                eq(paciente.getId()), eq(99),
                eq("habito_registrado"),
                eq("Hábito registrado"),
                eq("Um novo hábito foi registrado"),
                eq(EventoPrioridades.NORMAL)
        );
        verify(eventoClinicoService).analisarHabitos(salvo, 99);
    }

    @Test
    void testUpdateHabitoExistente() {
        Habitos existente = new Habitos();
        existente.setId(5);
        existente.setPaciente(paciente);

        Habitos novosDados = new Habitos();
        novosDados.setHorasSono(7);
        novosDados.setMinutosExercicio(30);

        when(repository.findById(5)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        Habitos atualizado = service.update(5, novosDados, 99);

        assertThat(atualizado.getHorasSono()).isEqualTo(7);
        assertThat(atualizado.getMinutosExercicio()).isEqualTo(30);
        verify(eventoPacienteService).criarEvento(
                eq(paciente.getId()), eq(99),
                eq("habito_editado"),
                eq("Habito atualizado"),
                eq("Um habito foi atualizado"),
                eq(EventoPrioridades.NORMAL)
        );
    }

    @Test
    void testUpdateHabitoNaoEncontradoLancaExcecao() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        Habitos novosDados = new Habitos();
        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.update(99, novosDados, 1));
    }

    @Test
    void testDeleteHabitoExistente() {
        Habitos habito = new Habitos();
        habito.setId(7);
        habito.setPaciente(paciente);

        when(repository.existsById(7)).thenReturn(true);
        when(repository.findById(7)).thenReturn(Optional.of(habito));

        service.delete(7, 99);

        verify(eventoPacienteService).criarEvento(
                eq(paciente.getId()), eq(99),
                eq("habito_removido"),
                eq("Hábito removido"),
                eq("Um registro de hábito foi removido"),
                eq(EventoPrioridades.NORMAL)
        );
        verify(repository).deleteById(7);
    }

    @Test
    void testDeleteHabitoNaoExistenteLancaExcecao() {
        when(repository.existsById(50)).thenReturn(false);

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.delete(50, 99));
    }

    @Test
    void testFindByPacienteCpf() {
        when(repository.findByPacienteCpf("12345678901")).thenReturn(List.of(new Habitos()));

        List<Habitos> lista = service.findByPacienteCpf("12345678901");

        assertThat(lista).hasSize(1);
        verify(repository).findByPacienteCpf("12345678901");
    }
}
