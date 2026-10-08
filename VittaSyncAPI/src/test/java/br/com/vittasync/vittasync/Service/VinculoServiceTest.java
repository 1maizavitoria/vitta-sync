package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.DTO.ConviteVinculoOutputDTO;
import br.com.vittasync.vittasync.DTO.PacienteResumoDTO;
import br.com.vittasync.vittasync.DTO.VinculoOutputDTO;
import br.com.vittasync.vittasync.Exception.AcessoNegadoException;
import br.com.vittasync.vittasync.Exception.DadosInvalidosException;
import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.ConviteVinculo;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Model.Vinculo;
import br.com.vittasync.vittasync.Repository.ConviteVinculoRepository;
import br.com.vittasync.vittasync.Repository.UsuarioRepository;
import br.com.vittasync.vittasync.Repository.VinculoRepository;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import br.com.vittasync.vittasync.Util.EventoTipos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class VinculoServiceTest {

    private ConviteVinculoRepository conviteRepository;
    private VinculoRepository vinculoRepository;
    private UsuarioRepository usuarioRepository;
    private EmailService emailService;
    private EventoPacienteService eventoPacienteService;
    private VinculoService service;

    private Usuario paciente;
    private Usuario responsavel;

    @BeforeEach
    void setup() {
        conviteRepository = mock(ConviteVinculoRepository.class);
        vinculoRepository = mock(VinculoRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        emailService = mock(EmailService.class);
        eventoPacienteService = mock(EventoPacienteService.class);

        service = new VinculoService(conviteRepository, vinculoRepository, usuarioRepository, emailService, eventoPacienteService);

        paciente = new Usuario();
        paciente.setId(1);
        paciente.setNome("Paciente");
        paciente.setEmail("paciente@teste.com");
        paciente.setCpf("12345678901");
        paciente.setTipo("paciente");

        responsavel = new Usuario();
        responsavel.setId(2);
        responsavel.setNome("Responsável");
        responsavel.setEmail("resp@teste.com");
        responsavel.setTipo("responsavel");
    }

    @Test
    void testGerarCodigoParaPaciente() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(paciente));
        when(conviteRepository.existsByCodigo(anyString())).thenReturn(false);

        ConviteVinculoOutputDTO dto = service.gerarCodigo(1);

        assertThat(dto.getCodigo()).isNotBlank();
        assertThat(dto.getLink()).contains(dto.getCodigo());
        verify(conviteRepository).save(any(ConviteVinculo.class));
    }

    @Test
    void testGerarCodigoUsuarioNaoPacienteLancaExcecao() {
        Usuario medico = new Usuario();
        medico.setId(3);
        medico.setTipo("saude");
        when(usuarioRepository.findById(3)).thenReturn(Optional.of(medico));

        assertThrows(RuntimeException.class,
                () -> service.gerarCodigo(3));
    }

    @Test
    void testRemoverVinculo() {
        Vinculo vinculo = new Vinculo();
        vinculo.setPacienteId(1);
        vinculo.setUsuarioId(2);

        when(vinculoRepository.findById(10L)).thenReturn(Optional.of(vinculo));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(responsavel));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(paciente));

        service.removerVinculo(10L, 1);

        verify(eventoPacienteService).criarEvento(eq(1), eq(1),
                eq(EventoTipos.VINCULO_REMOVIDO), eq("Participante removido"),
                contains("removeu"), contains("\"linkedUserName\":\"Responsável\""), eq(EventoPrioridades.NORMAL));
        verify(vinculoRepository).delete(vinculo);
    }

    @Test
    void testListarPorPaciente() {
        Vinculo vinculo = new Vinculo();
        vinculo.setPacienteId(1);
        vinculo.setUsuarioId(2);
        vinculo.setTipo("responsavel");
        vinculo.setFuncao("PAI"); // função válida

        when(vinculoRepository.findByPacienteId(1)).thenReturn(List.of(vinculo));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(responsavel));

        List<VinculoOutputDTO> lista = service.listarPorPaciente(1);

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getNome()).isEqualTo("Responsável");
    }

    private ConviteVinculo convite(boolean ativo, LocalDateTime expiraEm) {
        ConviteVinculo convite = new ConviteVinculo();
        convite.setPacienteId(1);
        convite.setCodigo("ABC123");
        convite.setAtivo(ativo);
        convite.setExpiraEm(Timestamp.valueOf(expiraEm));
        return convite;
    }

    private Usuario medico() {
        Usuario medico = new Usuario();
        medico.setId(3);
        medico.setNome("Médico");
        medico.setEmail("medico@teste.com");
        medico.setTipo("saude");
        medico.setConselho("CRM 123");
        return medico;
    }

    @Test
    void testGerarCodigoUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(9)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> service.gerarCodigo(9));
    }

    @Test
    void testEntrarComCodigoResponsavel() {
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(responsavel));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(paciente));
        when(conviteRepository.findByCodigo("ABC123")).thenReturn(Optional.of(convite(true, LocalDateTime.now().plusHours(1))));
        when(vinculoRepository.existsByPacienteIdAndUsuarioId(1, 2)).thenReturn(false);

        PacienteResumoDTO resumo = service.entrarComCodigo("ABC123", "responsavel_legal", 2);

        assertThat(resumo.getId()).isEqualTo(1);
        assertThat(resumo.getNome()).isEqualTo("Paciente");
        verify(vinculoRepository).save(any(Vinculo.class));
        verify(eventoPacienteService).criarEvento(eq(1), eq(2), eq(EventoTipos.VINCULO_CRIADO),
                eq("Novo participante no grupo"), eq("Responsável entrou no grupo como responsavel legal"),
                anyString(), eq(EventoPrioridades.NORMAL));
    }

    @Test
    void testEntrarComCodigoMedico() {
        when(usuarioRepository.findById(3)).thenReturn(Optional.of(medico()));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(paciente));
        when(conviteRepository.findByCodigo("ABC123")).thenReturn(Optional.of(convite(true, LocalDateTime.now().plusHours(1))));

        service.entrarComCodigo("ABC123", "especialista", 3);

        verify(eventoPacienteService).criarEvento(eq(1), eq(3), eq(EventoTipos.VINCULO_CRIADO),
                anyString(), eq("Médico entrou no grupo"), anyString(), eq(EventoPrioridades.NORMAL));
    }

    @Test
    void testEntrarComCodigoValidacoes() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(responsavel));
        when(usuarioRepository.findById(3)).thenReturn(Optional.of(medico()));

        assertThrows(AcessoNegadoException.class, () -> service.entrarComCodigo("ABC123", null, 1));
        assertThrows(DadosInvalidosException.class, () -> service.entrarComCodigo("ABC123", " ", 2));
        assertThrows(DadosInvalidosException.class, () -> service.entrarComCodigo("ABC123", null, 3));
        assertThrows(DadosInvalidosException.class, () -> service.entrarComCodigo("ABC123", "especialista", 2));
        assertThrows(DadosInvalidosException.class, () -> service.entrarComCodigo("ABC123", "tutor", 3));
    }

    @Test
    void testEntrarComCodigoInvalidoInativoExpiradoOuRepetido() {
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(responsavel));

        when(conviteRepository.findByCodigo("ABC123")).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> service.entrarComCodigo("ABC123", "tutor", 2));

        when(conviteRepository.findByCodigo("ABC123")).thenReturn(Optional.of(convite(false, LocalDateTime.now().plusHours(1))));
        assertThrows(DadosInvalidosException.class, () -> service.entrarComCodigo("ABC123", "tutor", 2));

        ConviteVinculo expirado = convite(true, LocalDateTime.now().minusHours(1));
        when(conviteRepository.findByCodigo("ABC123")).thenReturn(Optional.of(expirado));
        assertThrows(DadosInvalidosException.class, () -> service.entrarComCodigo("ABC123", "tutor", 2));
        assertThat(expirado.getAtivo()).isFalse();
        verify(conviteRepository).save(expirado);

        when(conviteRepository.findByCodigo("ABC123")).thenReturn(Optional.of(convite(true, LocalDateTime.now().plusHours(1))));
        when(vinculoRepository.existsByPacienteIdAndUsuarioId(1, 2)).thenReturn(true);
        assertThrows(DadosInvalidosException.class, () -> service.entrarComCodigo("ABC123", "tutor", 2));
    }

    @Test
    void testEnviarConviteEmail() {
        when(conviteRepository.findByCodigo("ABC123")).thenReturn(Optional.of(convite(true, LocalDateTime.now().plusHours(1))));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findByEmail("resp@teste.com")).thenReturn(Optional.of(responsavel));

        service.enviarConviteEmail("resp@teste.com", "ABC123");

        verify(emailService).enviarConviteVinculo("resp@teste.com", "Responsável", "Paciente", "ABC123",
                "http://localhost:5173/entrar?codigo=ABC123");
    }

    @Test
    void testEnviarConviteEmailValidacoes() {
        when(conviteRepository.findByCodigo("X")).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> service.enviarConviteEmail("a@a.com", "X"));

        when(conviteRepository.findByCodigo("ABC123")).thenReturn(Optional.of(convite(false, LocalDateTime.now().plusHours(1))));
        assertThrows(DadosInvalidosException.class, () -> service.enviarConviteEmail("a@a.com", "ABC123"));

        ConviteVinculo expirado = convite(true, LocalDateTime.now().minusHours(1));
        when(conviteRepository.findByCodigo("ABC123")).thenReturn(Optional.of(expirado));
        assertThrows(DadosInvalidosException.class, () -> service.enviarConviteEmail("a@a.com", "ABC123"));
        assertThat(expirado.getAtivo()).isFalse();

        when(conviteRepository.findByCodigo("ABC123")).thenReturn(Optional.of(convite(true, LocalDateTime.now().plusHours(1))));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findByEmail("paciente@teste.com")).thenReturn(Optional.of(paciente));
        assertThrows(DadosInvalidosException.class, () -> service.enviarConviteEmail("paciente@teste.com", "ABC123"));

        verifyNoInteractions(emailService);
    }

    @Test
    void testBuscarPorId() {
        Vinculo vinculo = new Vinculo();
        when(vinculoRepository.findById(1L)).thenReturn(Optional.of(vinculo));
        when(vinculoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThat(service.buscarPorId(1L)).isEqualTo(vinculo);
        assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(2L));
    }

    @Test
    void testListarComoPaciente() {
        Vinculo vinculo = new Vinculo();
        vinculo.setPacienteId(1);
        vinculo.setUsuarioId(3);
        vinculo.setTipo("saude");
        vinculo.setFuncao("especialista");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(3)).thenReturn(Optional.of(medico()));
        when(vinculoRepository.findByPacienteId(1)).thenReturn(List.of(vinculo));

        List<VinculoOutputDTO> lista = service.listar(1);

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getNome()).isEqualTo("Médico");
        assertThat(lista.get(0).getConselho()).isEqualTo("CRM 123");
        assertThat(lista.get(0).getFuncao()).isEqualTo("especialista");
    }

    @Test
    void testListarComoResponsavel() {
        Vinculo vinculo = new Vinculo();
        vinculo.setPacienteId(1);
        vinculo.setUsuarioId(2);
        vinculo.setTipo("responsavel");

        when(usuarioRepository.findById(2)).thenReturn(Optional.of(responsavel));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(paciente));
        when(vinculoRepository.findByUsuarioId(2)).thenReturn(List.of(vinculo));

        List<VinculoOutputDTO> lista = service.listar(2);

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getNome()).isEqualTo("Paciente");
    }

    @Test
    void testListarPacientesDoUsuario() {
        Vinculo vinculo = new Vinculo();
        vinculo.setPacienteId(1);
        vinculo.setUsuarioId(2);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(paciente));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(responsavel));
        when(vinculoRepository.findByUsuarioId(2)).thenReturn(List.of(vinculo));

        List<PacienteResumoDTO> doPaciente = service.listarPacientesDoUsuario(1);
        List<PacienteResumoDTO> doResponsavel = service.listarPacientesDoUsuario(2);

        assertThat(doPaciente).hasSize(1);
        assertThat(doPaciente.get(0).getCpf()).isEqualTo("12345678901");
        assertThat(doResponsavel).hasSize(1);
        assertThat(doResponsavel.get(0).getNome()).isEqualTo("Paciente");
    }
}
