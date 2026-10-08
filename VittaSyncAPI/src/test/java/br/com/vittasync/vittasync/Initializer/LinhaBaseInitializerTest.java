package br.com.vittasync.vittasync.Initializer;


import br.com.vittasync.vittasync.Repository.SinaisVitaisRepository;
import br.com.vittasync.vittasync.Service.LinhaBaseService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;
import java.util.List;
import static org.mockito.Mockito.*;


class LinhaBaseInitializerTest {

    @Test
    void testRunAtualizaTodosOsPacientesMesmoComFalha() {
        SinaisVitaisRepository sinaisVitaisRepository = mock(SinaisVitaisRepository.class);
        LinhaBaseService linhaBaseService = mock(LinhaBaseService.class);
        LinhaBaseInitializer initializer = new LinhaBaseInitializer(sinaisVitaisRepository, linhaBaseService);

        when(sinaisVitaisRepository.findTodosPacientesIds()).thenReturn(List.of(1, 2, 3));
        doThrow(new RuntimeException("falha")).when(linhaBaseService).atualizarLinhasBase(2);

        initializer.run(new DefaultApplicationArguments());

        verify(linhaBaseService).atualizarLinhasBase(1);
        verify(linhaBaseService).atualizarLinhasBase(2);
        verify(linhaBaseService).atualizarLinhasBase(3);
    }
}
