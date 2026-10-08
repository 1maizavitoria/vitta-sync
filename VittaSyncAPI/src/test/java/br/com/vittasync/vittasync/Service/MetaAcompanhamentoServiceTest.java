package br.com.vittasync.vittasync.Service;


import br.com.vittasync.vittasync.DTO.MetaAcompanhamentoInputDTO;
import br.com.vittasync.vittasync.Exception.RecursoNaoEncontradoException;
import br.com.vittasync.vittasync.Model.Habitos;
import br.com.vittasync.vittasync.Model.MetaAcompanhamento;
import br.com.vittasync.vittasync.Model.SinaisVitais;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Repository.HabitosRepository;
import br.com.vittasync.vittasync.Repository.MetaAcompanhamentoRepository;
import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class MetaAcompanhamentoServiceTest {

    private MetaAcompanhamentoRepository repository;
    private EventoPacienteService eventoPacienteService;
    private SinaisVitaisRepository sinaisVitaisRepository;
    private HabitosRepository habitosRepository;
    private MetaAcompanhamentoService service;

    private Usuario paciente;

    @BeforeEach
    void setup() {
        repository = mock(MetaAcompanhamentoRepository.class);
        eventoPacienteService = mock(EventoPacienteService.class);
        sinaisVitaisRepository = mock(SinaisVitaisRepository.class);
        habitosRepository = mock(HabitosRepository.class);

        service = new MetaAcompanhamentoService(repository, eventoPacienteService, sinaisVitaisRepository, habitosRepository);

        paciente = new Usuario();
        paciente.setId(1);
        paciente.setNome("Paciente Teste");

        when(repository.save(any(MetaAcompanhamento.class))).thenAnswer(inv -> inv.getArgument(0));
        when(repository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));
        when(sinaisVitaisRepository.findFirstByPacienteIdOrderByDataRegistroDesc(1)).thenReturn(Optional.empty());
        when(habitosRepository.findFirstByPacienteIdOrderByDataRegistroDesc(1)).thenReturn(Optional.empty());
    }

    private MetaAcompanhamentoInputDTO dto(String indicador, String direcao, Double inicial, Double atual, Double alvo) {
        MetaAcompanhamentoInputDTO dto = new MetaAcompanhamentoInputDTO();
        dto.setNome("Meta");
        dto.setTipoDado("sinais_vitais");
        dto.setIndicador(indicador);
        dto.setDirecao(direcao);
        dto.setValorInicial(inicial);
        dto.setValorAtual(atual);
        dto.setUnidade("un");
        dto.setValorAlvo(alvo);
        dto.setDataLimite(LocalDate.now().plusDays(10));
        return dto;
    }

    private MetaAcompanhamento meta(Long id, String indicador, String direcao, Double inicial, Double atual, Double alvo, LocalDate limite) {
        MetaAcompanhamento meta = new MetaAcompanhamento();
        meta.setId(id);
        meta.setPaciente(paciente);
        meta.setNome("Meta");
        meta.setIndicador(indicador);
        meta.setDirecao(direcao);
        meta.setValorInicial(inicial);
        meta.setValorAtual(atual);
        meta.setValorAlvo(alvo);
        meta.setDataLimite(limite);
        meta.setStatus("em_andamento");
        return meta;
    }

    @Test
    void testCreateMetaDePesoUsaUltimaMedicao() {
        SinaisVitais ultimo = new SinaisVitais();
        ultimo.setPeso(85.0);
        when(sinaisVitaisRepository.findFirstByPacienteIdOrderByDataRegistroDesc(1)).thenReturn(Optional.of(ultimo));

        MetaAcompanhamento criada = service.create(dto("peso", "reduzir", 90.0, 90.0, 80.0), paciente, 99);

        assertThat(criada.getValorAtual()).isEqualTo(85.0);
        assertThat(criada.getProgresso()).isEqualTo(50.0);
        assertThat(criada.getStatus()).isEqualTo("em_andamento");
        assertThat(criada.getDataCriacao()).isNotNull();
        verify(eventoPacienteService).criarEvento(eq(1), eq(99), eq("META_CRIADA"), anyString(), anyString(), anyString(), eq("normal"));
    }

    @Test
    void testCreateMetaPersonalizadaComecaZerada() {
        MetaAcompanhamento criada = service.create(dto("personalizado", "aumentar", 10.0, 5.0, 20.0), paciente, 99);

        assertThat(criada.getValorInicial()).isEqualTo(0.0);
        assertThat(criada.getValorAtual()).isEqualTo(0.0);
        assertThat(criada.getProgresso()).isEqualTo(0.0);
    }

    @Test
    void testCreateMetaAtingidaEConcluidaAutomaticamente() {
        Habitos habito = new Habitos();
        habito.setHorasSono(8);
        when(habitosRepository.findFirstByPacienteIdOrderByDataRegistroDesc(1)).thenReturn(Optional.of(habito));

        MetaAcompanhamento criada = service.create(dto("horas_sono", "aumentar", null, null, 8.0), paciente, 99);

        assertThat(criada.getValorInicial()).isEqualTo(8.0);
        assertThat(criada.getStatus()).isEqualTo("concluido");
        assertThat(criada.getDataConclusao()).isNotNull();
        verify(eventoPacienteService).criarEvento(eq(1), eq(99), eq("META_CONCLUIDA"), anyString(), anyString(), anyString(), eq("alta"));
    }

    @Test
    void testCreateMetaDeExercicio() {
        Habitos habito = new Habitos();
        habito.setMinutosExercicio(15);
        when(habitosRepository.findFirstByPacienteIdOrderByDataRegistroDesc(1)).thenReturn(Optional.of(habito));

        MetaAcompanhamento criada = service.create(dto("minutos_exercicio", "aumentar", 0.0, 0.0, 30.0), paciente, 99);

        assertThat(criada.getValorAtual()).isEqualTo(15.0);
        assertThat(criada.getProgresso()).isEqualTo(50.0);
    }

    @Test
    void testCreateIndicadorInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> service.create(dto("outro", "aumentar", 0.0, 0.0, 10.0), paciente, 99));
    }

    @Test
    void testCreateSemIndicadorMantemValorInformado() {
        MetaAcompanhamento criada = service.create(dto(null, "aumentar", 0.0, 5.0, 0.0), paciente, 99);

        assertThat(criada.getValorAtual()).isEqualTo(5.0);
        assertThat(criada.getProgresso()).isEqualTo(0.0);
    }

    @Test
    void testUpdateMetaExistente() {
        MetaAcompanhamento existente = meta(1L, "peso", "reduzir", 90.0, 90.0, 80.0, LocalDate.now().plusDays(5));
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        MetaAcompanhamento atualizada = service.update(1L, dto("personalizado", "reduzir", 0.0, 0.0, 10.0), paciente, 99);

        assertThat(atualizada.getIndicador()).isEqualTo("personalizado");
        assertThat(atualizada.getValorAtual()).isEqualTo(0.0);
        assertThat(atualizada.getDataModificacao()).isNotNull();
        verify(eventoPacienteService).criarEvento(eq(1), eq(99), eq("META_ATUALIZADA"), anyString(), anyString(), anyString(), eq("normal"));
    }

    @Test
    void testUpdateMetaDeOutroPaciente() {
        Usuario outro = new Usuario();
        outro.setId(2);
        MetaAcompanhamento existente = meta(1L, "peso", "reduzir", 90.0, 90.0, 80.0, LocalDate.now());
        existente.setPaciente(outro);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.update(1L, dto("peso", "reduzir", 90.0, 90.0, 80.0), paciente, 99));
    }

    @Test
    void testUpdateMetaNaoEncontrada() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.update(5L, dto("peso", "reduzir", 90.0, 90.0, 80.0), paciente, 99));
    }

    @Test
    void testDelete() {
        MetaAcompanhamento existente = meta(1L, "peso", "reduzir", 90.0, 90.0, 80.0, LocalDate.now());
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        service.delete(1L, 1, 99);

        verify(repository).delete(existente);
        verify(eventoPacienteService).criarEvento(eq(1), eq(99), eq("META_REMOVIDA"), anyString(), anyString(), anyString(), eq("normal"));
    }

    @Test
    void testConcluirMetaNoPrazo() {
        MetaAcompanhamento existente = meta(1L, "peso", "reduzir", 90.0, 85.0, 80.0, LocalDate.now().plusDays(1));
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        MetaAcompanhamento concluida = service.concluirMeta(1L, 1, 99);

        assertThat(concluida.getStatus()).isEqualTo("concluido");
        assertThat(concluida.getDataConclusao()).isNotNull();
    }

    @Test
    void testConcluirMetaAtrasada() {
        MetaAcompanhamento existente = meta(1L, "peso", "reduzir", 90.0, 85.0, 80.0, LocalDate.now().minusDays(1));
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        assertThat(service.concluirMeta(1L, 1, 99).getStatus()).isEqualTo("concluido_atrasado");
    }

    @Test
    void testAtualizarValorManualMetaPersonalizada() {
        MetaAcompanhamento existente = meta(1L, "personalizado", "aumentar", 0.0, 0.0, 10.0, LocalDate.now().plusDays(5));
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        MetaAcompanhamento atualizada = service.atualizarValorManual(1L, 1, 5.0, 99);

        assertThat(atualizada.getValorAtual()).isEqualTo(5.0);
        assertThat(atualizada.getProgresso()).isEqualTo(50.0);
        assertThat(atualizada.getStatus()).isEqualTo("em_andamento");
    }

    @Test
    void testAtualizarValorManualUltrapassaLimiteMaximo() {
        MetaAcompanhamento existente = meta(1L, "personalizado", "reduzir", 0.0, 0.0, 10.0, LocalDate.now().plusDays(5));
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        MetaAcompanhamento atualizada = service.atualizarValorManual(1L, 1, 15.0, 99);

        assertThat(atualizada.getStatus()).isEqualTo("nao_atingida");
        assertThat(atualizada.getProgresso()).isEqualTo(150.0);
        verify(eventoPacienteService).criarEvento(eq(1), eq(99), eq("META_NAO_ATINGIDA"), anyString(),
                eq("Limite da meta ultrapassado"), anyString(), eq("alta"));
    }

    @Test
    void testAtualizarValorManualIndicadorNaoPersonalizado() {
        MetaAcompanhamento existente = meta(1L, "peso", "reduzir", 90.0, 90.0, 80.0, LocalDate.now());
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () -> service.atualizarValorManual(1L, 1, 5.0, 99));
    }

    @Test
    void testAtualizarValorManualSemValor() {
        MetaAcompanhamento existente = meta(1L, "personalizado", "aumentar", 0.0, 0.0, 10.0, LocalDate.now());
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () -> service.atualizarValorManual(1L, 1, null, 99));
    }

    @Test
    void testListarPorPacienteAtualizaStatusDasMetas() {
        LocalDate vencida = LocalDate.now().minusDays(1);
        MetaAcompanhamento limiteVencido = meta(1L, "personalizado", "reduzir", 0.0, 5.0, 10.0, vencida);
        MetaAcompanhamento personalizadaVencida = meta(2L, "personalizado", "aumentar", 0.0, 5.0, 10.0, vencida);
        MetaAcompanhamento atingidaAtrasada = meta(3L, "peso", "aumentar", 0.0, 10.0, 10.0, vencida);
        MetaAcompanhamento jaConcluida = meta(4L, "peso", "aumentar", 0.0, 10.0, 10.0, vencida);
        jaConcluida.setStatus("concluido");
        MetaAcompanhamento reduzirSemInicial = meta(5L, "peso", "reduzir", null, null, 80.0, LocalDate.now().plusDays(5));
        MetaAcompanhamento alvoZero = meta(6L, "personalizado", "aumentar", 0.0, 5.0, 0.0, LocalDate.now().plusDays(5));
        MetaAcompanhamento aumentarAlvoZero = meta(7L, "peso", "aumentar", 0.0, 5.0, 0.0, LocalDate.now().plusDays(5));
        MetaAcompanhamento reduzirIgual = meta(8L, "peso", "reduzir", 80.0, 79.0, 80.0, LocalDate.now().plusDays(5));

        when(repository.findByPacienteId(1)).thenReturn(new ArrayList<>(List.of(limiteVencido, personalizadaVencida,
                atingidaAtrasada, jaConcluida, reduzirSemInicial, alvoZero, aumentarAlvoZero, reduzirIgual)));

        List<MetaAcompanhamento> metas = service.listarPorPaciente(1, 99);

        assertThat(metas).hasSize(8);
        assertThat(limiteVencido.getStatus()).isEqualTo("concluido");
        assertThat(personalizadaVencida.getStatus()).isEqualTo("nao_atingida");
        assertThat(atingidaAtrasada.getStatus()).isEqualTo("concluido_atrasado");
        assertThat(jaConcluida.getStatus()).isEqualTo("concluido");
        assertThat(reduzirSemInicial.getProgresso()).isEqualTo(0.0);
        assertThat(alvoZero.getProgresso()).isEqualTo(0.0);
        assertThat(aumentarAlvoZero.getProgresso()).isEqualTo(0.0);
        assertThat(reduzirIgual.getProgresso()).isEqualTo(100.0);
        assertThat(reduzirIgual.getStatus()).isEqualTo("concluido");
        verify(eventoPacienteService).criarEvento(eq(1), eq(99), eq("META_NAO_ATINGIDA"), anyString(),
                eq("Prazo encerrado antes de atingir a meta"), anyString(), eq("alta"));
    }
}
