package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.DiarioSintomas;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.DiarioSintomasRepository;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DiarioSintomasServiceTest {

    private DiarioSintomasRepository repository;
    private EventoPacienteService eventoPacienteService;
    private EventoClinicoService eventoClinicoService;
    private DiarioSintomasService service;

    private Usuario paciente;

    @BeforeEach
    void setup() {
        repository = mock(DiarioSintomasRepository.class);
        eventoPacienteService = mock(EventoPacienteService.class);
        eventoClinicoService = mock(EventoClinicoService.class);
        service = new DiarioSintomasService(repository, eventoPacienteService, eventoClinicoService);

        paciente = new Usuario();
        paciente.setId(1);
    }

    @Test
    void testCreateSalvaSintomaEDisparaEventos() {
        DiarioSintomas sintoma = new DiarioSintomas();
        sintoma.setPaciente(paciente);

        DiarioSintomas salvo = new DiarioSintomas();
        salvo.setId(10);
        salvo.setPaciente(paciente);

        when(repository.save(sintoma)).thenReturn(salvo);

        DiarioSintomas resultado = service.create(sintoma, 99);

        assertThat(resultado.getId()).isEqualTo(10);
        verify(repository, times(1)).save(sintoma);
        verify(eventoPacienteService, times(1)).criarEvento(
                eq(paciente.getId()),
                eq(99),
                eq("sintoma_registrado"),
                eq("Sintoma registrado"),
                eq("Um novo sintoma foi registrado"),
                eq(EventoPrioridades.NORMAL)
        );
        verify(eventoClinicoService, times(1)).analisarSintoma(salvo, 99);
    }

    @Test
    void testUpdateSintomaExistente() {
        DiarioSintomas existente = new DiarioSintomas();
        existente.setId(5);
        existente.setPaciente(paciente);

        DiarioSintomas novosDados = new DiarioSintomas();
        novosDados.setSintoma("Dor de cabeça");
        novosDados.setIntensidadeDor(7);

        when(repository.findById(5)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        DiarioSintomas atualizado = service.update(5, novosDados, 99);

        assertThat(atualizado.getSintoma()).isEqualTo("Dor de cabeça");
        assertThat(atualizado.getIntensidadeDor()).isEqualTo(7);
        verify(eventoPacienteService, times(1)).criarEvento(
                eq(paciente.getId()),
                eq(99),
                eq("sintoma_editado"),
                eq("Sintoma atualizado"),
                eq("Um sintoma foi atualizado"),
                eq(EventoPrioridades.NORMAL)
        );
    }

    @Test
    void testUpdateSintomaNaoEncontradoLancaExcecao() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        DiarioSintomas novosDados = new DiarioSintomas();
        assertThrows(RecursoNaoEncontradoException.class, () -> service.update(99, novosDados, 1));
    }

    @Test
    void testDeleteSintomaExistente() {
        DiarioSintomas sintoma = new DiarioSintomas();
        sintoma.setId(7);
        sintoma.setPaciente(paciente);

        when(repository.existsById(7)).thenReturn(true);
        when(repository.findById(7)).thenReturn(Optional.of(sintoma));

        service.delete(7, 99);

        verify(eventoPacienteService, times(1)).criarEvento(
                eq(paciente.getId()),
                eq(99),
                eq("sintoma_removido"),
                eq("Sintoma removido"),
                eq("Um registro de sintoma foi removido"),
                eq(EventoPrioridades.NORMAL)
        );
        verify(repository, times(1)).deleteById(7);
    }

    @Test
    void testDeleteSintomaNaoExistenteLancaExcecao() {
        when(repository.existsById(50)).thenReturn(false);

        assertThrows(RecursoNaoEncontradoException.class, () -> service.delete(50, 99));
    }

    @Test
    void testFindByPacienteCpf() {
        when(repository.findByPacienteCpf("12345678901")).thenReturn(List.of(new DiarioSintomas()));

        List<DiarioSintomas> lista = service.findByPacienteCpf("12345678901");

        assertThat(lista).hasSize(1);
        verify(repository, times(1)).findByPacienteCpf("12345678901");
    }
}
