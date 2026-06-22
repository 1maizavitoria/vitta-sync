package br.com.vittasync.vittasync.Service;

import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Model.Vinculo;
import br.com.vittasync.vittasync.Repository.UsuarioRepository;
import br.com.vittasync.vittasync.Repository.VinculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PermissaoServiceTest {

    private UsuarioRepository usuarioRepository;
    private VinculoRepository vinculoRepository;
    private PermissaoService service;

    @BeforeEach
    void setup() {
        usuarioRepository = mock(UsuarioRepository.class);
        vinculoRepository = mock(VinculoRepository.class);
        service = new PermissaoService(usuarioRepository, vinculoRepository);
    }

    @Test
    void testPodeVisualizarPacienteMesmoUsuario() {
        boolean resultado = service.podeVisualizarPaciente(1, 1);
        assertThat(resultado).isTrue();
    }

    @Test
    void testPodeVisualizarPacienteComVinculo() {
        when(vinculoRepository.existsByPacienteIdAndUsuarioId(10, 2)).thenReturn(true);

        boolean resultado = service.podeVisualizarPaciente(2, 10);

        assertThat(resultado).isTrue();
    }

    @Test
    void testPodeEditarPacienteMesmoUsuario() {
        boolean resultado = service.podeEditarPaciente(1, 1);
        assertThat(resultado).isTrue();
    }

    @Test
    void testPodeEditarPacienteResponsavel() {
        when(vinculoRepository.existsByPacienteIdAndUsuarioIdAndTipo(10, 2, "responsavel")).thenReturn(true);

        boolean resultado = service.podeEditarPaciente(2, 10);

        assertThat(resultado).isTrue();
    }

    @Test
    void testPodeRemoverVinculoPacienteRemoveQualquer() {
        Vinculo vinculo = new Vinculo();
        vinculo.setPacienteId(1);
        vinculo.setUsuarioId(2);

        boolean resultado = service.podeRemoverVinculo(1, vinculo);

        assertThat(resultado).isTrue();
    }

    @Test
    void testPodeRemoverVinculoUsuarioNaoEncontrado() {
        Vinculo vinculo = new Vinculo();
        vinculo.setPacienteId(10);
        vinculo.setUsuarioId(20);

        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        boolean resultado = service.podeRemoverVinculo(99, vinculo);

        assertThat(resultado).isFalse();
    }

    @Test
    void testPodeRemoverVinculoProprioUsuario() {
        Vinculo vinculo = new Vinculo();
        vinculo.setPacienteId(10);
        vinculo.setUsuarioId(5);

        Usuario usuario = new Usuario();
        usuario.setId(5);
        usuario.setTipo("saude");

        when(usuarioRepository.findById(5)).thenReturn(Optional.of(usuario));

        boolean resultado = service.podeRemoverVinculo(5, vinculo);

        assertThat(resultado).isTrue();
    }

    @Test
    void testPodeRemoverVinculoResponsavelRemoveMedico() {
        Vinculo vinculo = new Vinculo();
        vinculo.setPacienteId(10);
        vinculo.setUsuarioId(20);
        vinculo.setTipo("saude");

        Usuario usuario = new Usuario();
        usuario.setId(5);
        usuario.setTipo("responsavel");

        when(usuarioRepository.findById(5)).thenReturn(Optional.of(usuario));
        when(vinculoRepository.existsByPacienteIdAndUsuarioIdAndTipo(10, 5, "responsavel")).thenReturn(true);

        boolean resultado = service.podeRemoverVinculo(5, vinculo);

        assertThat(resultado).isTrue();
    }

    @Test
    void testIsMedico() {
        Usuario usuario = new Usuario();
        usuario.setTipo("saude");

        assertThat(service.isMedico(usuario)).isTrue();
    }

    @Test
    void testMedicoVinculadoAoPaciente() {
        when(vinculoRepository.existsByPacienteIdAndUsuarioIdAndTipo(10, 2, "saude")).thenReturn(true);

        boolean resultado = service.medicoVinculadoAoPaciente(2, 10);

        assertThat(resultado).isTrue();
    }

    @Test
    void testResponsavelVinculadoAoPaciente() {
        when(vinculoRepository.existsByPacienteIdAndUsuarioIdAndTipo(10, 3, "responsavel")).thenReturn(true);

        boolean resultado = service.responsavelVinculadoAoPaciente(3, 10);

        assertThat(resultado).isTrue();
    }
}
