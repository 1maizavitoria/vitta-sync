package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.Model.LembreteMedicao;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.LembreteMedicaoRepository;
import br.com.vittasync.vittasync.Util.EventoPrioridades;
import br.com.vittasync.vittasync.Util.EventoTipos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


class LembreteMedicaoServiceTest {

    private LembreteMedicaoRepository repository;
    private EventoPacienteService eventoPacienteService;
    private LembreteMedicaoService service;
    private Usuario usuario;

    @BeforeEach
    void setup() {
        repository = mock(LembreteMedicaoRepository.class);
        eventoPacienteService = mock(EventoPacienteService.class);
        service = new LembreteMedicaoService(repository, eventoPacienteService);

        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Paciente Teste");
    }

    @Test
    void testSalvarSubstituirRemoveAnteriorESalvaNovo() {
        LembreteMedicao antigo = new LembreteMedicao();
        antigo.setUsuario(usuario);

        LembreteMedicao novo = new LembreteMedicao();
        novo.setUsuario(usuario);

        when(repository.findByUsuario(usuario)).thenReturn(Optional.of(antigo));
        when(repository.save(novo)).thenReturn(novo);

        LembreteMedicao resultado = service.salvarSubstituir(novo);

        assertThat(resultado).isEqualTo(novo);
        verify(repository).delete(antigo);
        verify(repository).save(novo);
        verify(eventoPacienteService).criarEvento(
                eq(usuario.getId()),
                eq(usuario.getId()),
                eq(EventoTipos.LEMBRETE_ATUALIZADO),
                eq("Lembrete atualizado"),
                eq("Um lembrete de medição foi atualizado"),
                contains("\"patientName\":\"Paciente Teste\""),
                eq(EventoPrioridades.NORMAL)
        );
    }

    @Test
    void testSearchLembreteRetornaOptional() {
        LembreteMedicao lembrete = new LembreteMedicao();
        lembrete.setUsuario(usuario);

        when(repository.findByUsuario(usuario)).thenReturn(Optional.of(lembrete));

        Optional<LembreteMedicao> resultado = service.searchlembrete(usuario);

        assertThat(resultado).isPresent();
        assertThat(resultado.get()).isEqualTo(lembrete);
    }

    @Test
    void testSearchLembreteAtivoRetornaLista() {
        when(repository.findByAtivoTrue()).thenReturn(List.of(new LembreteMedicao()));

        List<LembreteMedicao> ativos = service.searchLembreteAtivo();

        assertThat(ativos).hasSize(1);
        verify(repository).findByAtivoTrue();
    }

    @Test
    void testSearchLembretesDoMinutoUsaProcedure() {
        LembreteMedicao lembrete = new LembreteMedicao();
        when(repository.buscarLembretesDoMinuto("MONDAY", LocalTime.of(8, 0))).thenReturn(List.of(lembrete));

        List<LembreteMedicao> lembretes = service.searchLembretesDoMinuto(DayOfWeek.MONDAY, LocalTime.of(8, 0));

        assertThat(lembretes).containsExactly(lembrete);
        verify(repository, never()).findByAtivoTrue();
    }

    @Test
    void testSearchLembretesDoMinutoSemProcedureFiltraNoJava() {
        LembreteMedicao certo = new LembreteMedicao();
        certo.setDiasSemana("MONDAY,FRIDAY");
        certo.setHorario(LocalTime.of(8, 0, 30));

        LembreteMedicao outroDia = new LembreteMedicao();
        outroDia.setDiasSemana("TUESDAY");
        outroDia.setHorario(LocalTime.of(8, 0));

        LembreteMedicao outroHorario = new LembreteMedicao();
        outroHorario.setDiasSemana("monday");
        outroHorario.setHorario(LocalTime.of(9, 0));

        when(repository.buscarLembretesDoMinuto(anyString(), any())).thenThrow(new RuntimeException("procedure inexistente"));
        when(repository.findByAtivoTrue()).thenReturn(List.of(certo, outroDia, outroHorario));

        List<LembreteMedicao> primeira = service.searchLembretesDoMinuto(DayOfWeek.MONDAY, LocalTime.of(8, 0));
        List<LembreteMedicao> segunda = service.searchLembretesDoMinuto(DayOfWeek.MONDAY, LocalTime.of(8, 0));

        assertThat(primeira).containsExactly(certo);
        assertThat(segunda).containsExactly(certo);
        verify(repository, times(1)).buscarLembretesDoMinuto(anyString(), any());
    }

    @Test
    void testAtivarMarcaComoAtivo() {
        LembreteMedicao lembrete = new LembreteMedicao();
        lembrete.setUsuario(usuario);
        lembrete.setAtivo(false);

        when(repository.findByUsuario(usuario)).thenReturn(Optional.of(lembrete));
        when(repository.save(lembrete)).thenReturn(lembrete);

        Optional<LembreteMedicao> resultado = service.ativar(usuario);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().isAtivo()).isTrue();
        verify(repository).save(lembrete);
        verify(eventoPacienteService).criarEvento(
                eq(usuario.getId()),
                eq(usuario.getId()),
                eq(EventoTipos.LEMBRETE_ATUALIZADO),
                eq("Lembrete ativado"),
                eq("Um lembrete de medição foi ativado"),
                contains("\"patientName\":\"Paciente Teste\""),
                eq(EventoPrioridades.NORMAL)
        );
    }

    @Test
    void testDesativarMarcaComoInativo() {
        LembreteMedicao lembrete = new LembreteMedicao();
        lembrete.setUsuario(usuario);
        lembrete.setAtivo(true);

        when(repository.findByUsuario(usuario)).thenReturn(Optional.of(lembrete));
        when(repository.save(lembrete)).thenReturn(lembrete);

        Optional<LembreteMedicao> resultado = service.desativar(usuario);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().isAtivo()).isFalse();
        verify(repository).save(lembrete);
        verify(eventoPacienteService).criarEvento(
                eq(usuario.getId()),
                eq(usuario.getId()),
                eq(EventoTipos.LEMBRETE_ATUALIZADO),
                eq("Lembrete desativado"),
                eq("Um lembrete de medição foi desativado"),
                contains("\"patientName\":\"Paciente Teste\""),
                eq(EventoPrioridades.NORMAL)
        );
    }
}
