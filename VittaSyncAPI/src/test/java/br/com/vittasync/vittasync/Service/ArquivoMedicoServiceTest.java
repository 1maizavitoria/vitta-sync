package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Model.ArquivoMedico;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.ArquivoMedicoRepository;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import br.com.vittasync.vittasync.Util.EventoTipos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ArquivoMedicoServiceTest {

    private ArquivoMedicoRepository repository;
    private EventoPacienteService eventoPacienteService;
    private ArquivoMedicoService service;

    private Usuario medico;
    private Usuario paciente;

    @BeforeEach
    void setup() {
        repository = mock(ArquivoMedicoRepository.class);
        eventoPacienteService = mock(EventoPacienteService.class);
        service = new ArquivoMedicoService(repository, eventoPacienteService);

        medico = new Usuario();
        medico.setId(1);
        medico.setNome("Dr. Teste");

        paciente = new Usuario();
        paciente.setId(2);
        paciente.setNome("Paciente Teste");
    }

    @Test
    void testUploadSalvaDocumentoEDisparaEvento() {
        ArquivoMedico doc = new ArquivoMedico();
        doc.setId(10);
        when(repository.save(any(ArquivoMedico.class))).thenReturn(doc);

        ArquivoMedico salvo = service.upload(medico, paciente, "arquivo.pdf", "pdf", "original.pdf", new byte[]{1,2,3});

        assertThat(salvo.getId()).isEqualTo(10);
        verify(repository, times(1)).save(any(ArquivoMedico.class));
        verify(eventoPacienteService, times(1)).criarEvento(
                eq(paciente.getId()),
                eq(medico.getId()),
                eq(EventoTipos.DOCUMENTO_ENVIADO),
                eq("Novo documento enviado"),
                contains("arquivo.pdf"),
                eq(EventoPrioridades.NORMAL)
        );
    }

    @Test
    void testListarPorPaciente() {
        when(repository.findByPaciente(paciente)).thenReturn(List.of(new ArquivoMedico()));

        List<ArquivoMedico> docs = service.listarPorPaciente(paciente);

        assertThat(docs).hasSize(1);
        verify(repository, times(1)).findByPaciente(paciente);
    }

    @Test
    void testListarPorMedico() {
        when(repository.findByMedico(medico)).thenReturn(List.of(new ArquivoMedico()));

        List<ArquivoMedico> docs = service.listarPorMedico(medico);

        assertThat(docs).hasSize(1);
        verify(repository, times(1)).findByMedico(medico);
    }

    @Test
    void testVisualizarDocumentoExistente() {
        ArquivoMedico doc = new ArquivoMedico();
        doc.setId(5);
        when(repository.findById(5)).thenReturn(Optional.of(doc));

        ArquivoMedico resultado = service.visualizar(5);

        assertThat(resultado.getId()).isEqualTo(5);
    }

    @Test
    void testVisualizarDocumentoInexistenteLancaExcecao() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.visualizar(99));
    }

    @Test
    void testDeletarDocumentoComPermissao() {
        ArquivoMedico doc = new ArquivoMedico();
        doc.setId(7);
        doc.setMedico(medico);
        doc.setPaciente(paciente);
        doc.setNomeArquivo("teste.pdf");

        when(repository.findById(7)).thenReturn(Optional.of(doc));

        service.deletar(medico, 7);

        verify(repository, times(1)).delete(doc);
        verify(eventoPacienteService, times(1)).criarEvento(
                eq(paciente.getId()),
                eq(medico.getId()),
                eq(EventoTipos.DOCUMENTO_REMOVIDO),
                eq("Documento removido"),
                contains("teste.pdf"),
                eq(EventoPrioridades.NORMAL)
        );
    }

    @Test
    void testDeletarDocumentoSemPermissaoLancaExcecao() {
        Usuario outroMedico = new Usuario();
        outroMedico.setId(99);

        ArquivoMedico doc = new ArquivoMedico();
        doc.setId(8);
        doc.setMedico(medico);
        doc.setPaciente(paciente);

        when(repository.findById(8)).thenReturn(Optional.of(doc));

        assertThrows(RuntimeException.class, () -> service.deletar(outroMedico, 8));
        verify(repository, never()).delete(any());
    }
}
