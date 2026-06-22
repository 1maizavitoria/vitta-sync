package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Exception.DadosInvalidosException;
import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.ContatoEmergencia;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.ContatoEmergenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ContatoEmergenciaServiceTest {

    private ContatoEmergenciaRepository repository;
    private PermissaoService permissaoService;
    private ContatoEmergenciaService service;

    private Usuario paciente;
    private Usuario usuarioLogado;

    @BeforeEach
    void setup() {
        repository = mock(ContatoEmergenciaRepository.class);
        permissaoService = mock(PermissaoService.class);
        service = new ContatoEmergenciaService(repository, permissaoService);

        paciente = new Usuario();
        paciente.setId(1);

        usuarioLogado = new Usuario();
        usuarioLogado.setId(99);
    }

    @Test
    void testCreateComPermissaoEQuantidadeValida() {
        when(permissaoService.podeEditarPaciente(99, 1)).thenReturn(true);
        when(repository.countByPaciente(paciente)).thenReturn(0);

        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setNome("Contato Teste");
        contato.setTelefone("123456789");

        when(repository.save(contato)).thenReturn(contato);

        ContatoEmergencia salvo = service.create(99, paciente, contato);

        assertThat(salvo.getPaciente()).isEqualTo(paciente);
        verify(repository, times(1)).save(contato);
    }

    @Test
    void testCreateSemPermissaoLancaExcecao() {
        when(permissaoService.podeEditarPaciente(99, 1)).thenReturn(false);

        ContatoEmergencia contato = new ContatoEmergencia();
        assertThrows(AcessoNegadoException.class, () -> service.create(99, paciente, contato));
    }

    @Test
    void testCreateComMaisDeTresContatosLancaExcecao() {
        when(permissaoService.podeEditarPaciente(99, 1)).thenReturn(true);
        when(repository.countByPaciente(paciente)).thenReturn(3);

        ContatoEmergencia contato = new ContatoEmergencia();
        assertThrows(DadosInvalidosException.class, () -> service.create(99, paciente, contato));
    }

    @Test
    void testListarComPermissao() {
        when(permissaoService.podeVisualizarPaciente(99, 1)).thenReturn(true);
        when(repository.findByPaciente(paciente)).thenReturn(List.of(new ContatoEmergencia()));

        List<ContatoEmergencia> contatos = service.listar(99, paciente);

        assertThat(contatos).hasSize(1);
        verify(repository, times(1)).findByPaciente(paciente);
    }

    @Test
    void testListarSemPermissaoLancaExcecao() {
        when(permissaoService.podeVisualizarPaciente(99, 1)).thenReturn(false);

        assertThrows(AcessoNegadoException.class, () -> service.listar(99, paciente));
    }

    @Test
    void testUpdateComPermissao() {
        ContatoEmergencia existente = new ContatoEmergencia();
        existente.setId(10);
        existente.setPaciente(paciente);

        ContatoEmergencia atualizado = new ContatoEmergencia();
        atualizado.setId(10);
        atualizado.setPaciente(paciente);
        atualizado.setNome("Novo Nome");
        atualizado.setTelefone("987654321");

        when(permissaoService.podeEditarPaciente(99, 1)).thenReturn(true);
        when(repository.findById(10)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        ContatoEmergencia resultado = service.update(99, atualizado);

        assertThat(resultado.getNome()).isEqualTo("Novo Nome");
        assertThat(resultado.getTelefone()).isEqualTo("987654321");
        verify(repository, times(1)).save(existente);
    }

    @Test
    void testUpdateContatoNaoEncontradoLancaExcecao() {
        when(permissaoService.podeEditarPaciente(99, 1)).thenReturn(true);
        when(repository.findById(10)).thenReturn(Optional.empty());

        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setId(10);
        contato.setPaciente(paciente);

        assertThrows(RecursoNaoEncontradoException.class, () -> service.update(99, contato));
    }

    @Test
    void testDeleteComPermissao() {
        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setId(20);
        contato.setPaciente(paciente);

        when(repository.findById(20)).thenReturn(Optional.of(contato));
        when(permissaoService.podeEditarPaciente(99, 1)).thenReturn(true);

        service.delete(99, 20);

        verify(repository, times(1)).delete(contato);
    }

    @Test
    void testDeleteSemPermissaoLancaExcecao() {
        ContatoEmergencia contato = new ContatoEmergencia();
        contato.setId(20);
        contato.setPaciente(paciente);

        when(repository.findById(20)).thenReturn(Optional.of(contato));
        when(permissaoService.podeEditarPaciente(99, 1)).thenReturn(false);

        assertThrows(AcessoNegadoException.class, () -> service.delete(99, 20));
        verify(repository, never()).delete(any());
    }

    @Test
    void testDeleteContatoNaoEncontradoLancaExcecao() {
        when(repository.findById(30)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.delete(99, 30));
    }
}
