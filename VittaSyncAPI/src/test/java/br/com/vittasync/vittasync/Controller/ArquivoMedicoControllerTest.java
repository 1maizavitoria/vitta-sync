package br.com.vittasync.vittasync.Controller;

import br.com.vittasync.vittasync.Model.ArquivoMedico;
import br.com.vittasync.vittasync.Model.Usuario;
import br.com.vittasync.vittasync.Service.ArquivoMedicoService;
import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.UsuarioService;
import br.com.vittasync.vittasync.Service.PermissaoService;
import br.com.vittasync.vittasync.Service.SessaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArquivoMedicoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ArquivoMedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private ArquivoMedicoService service;
    @MockBean private JwtService jwtService;
    @MockBean private UsuarioService usuarioService;
    @MockBean private PermissaoService permissaoService;
    @MockBean private SessaoService sessaoService;

    @Test
    void testUploadOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile("arquivo","teste.pdf","application/pdf","abc".getBytes());
        Usuario medico = new Usuario(); medico.setId(1); medico.setCpf("111"); medico.setNome("Dr. Teste");
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222"); paciente.setNome("Paciente Teste");
        ArquivoMedico doc = new ArquivoMedico(); doc.setId(10); doc.setMedico(medico); doc.setPaciente(paciente); doc.setNomeArquivo("teste.pdf"); doc.setExtensao(".pdf"); doc.setNomeOriginal("teste.pdf"); doc.setArquivo("abc".getBytes());

        when(jwtService.extrairCpf("token")).thenReturn("111");
        when(usuarioService.searchByCpf("111")).thenReturn(medico);
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.medicoVinculadoAoPaciente(1,2)).thenReturn(true);
        when(service.upload(eq(medico), eq(paciente), anyString(), anyString(), anyString(), any())).thenReturn(doc);

        mockMvc.perform(multipart("/documentos/upload/222")
                        .file(file)
                        .param("nomeArquivo","teste.pdf")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testListarPorPacienteOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222"); paciente.setNome("Paciente Teste");
        ArquivoMedico doc = new ArquivoMedico(); doc.setId(1); doc.setPaciente(paciente); doc.setMedico(new Usuario()); doc.getMedico().setNome("Dr. Teste");

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.listarPorPaciente(paciente)).thenReturn(List.of(doc));

        mockMvc.perform(get("/documentos/getDocumentosPaciente/222")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testListarPorMedicoOk() throws Exception {
        Usuario medico = new Usuario(); medico.setId(1); medico.setCpf("111"); medico.setNome("Dr. Teste");
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222"); paciente.setNome("Paciente Teste");
        ArquivoMedico doc = new ArquivoMedico(); doc.setId(1); doc.setMedico(medico); doc.setPaciente(paciente);

        when(jwtService.extrairCpf("token")).thenReturn("111");
        when(usuarioService.searchByCpf("111")).thenReturn(medico);
        when(permissaoService.isMedico(medico)).thenReturn(true);
        when(service.listarPorMedico(medico)).thenReturn(List.of(doc));

        mockMvc.perform(get("/documentos/getDocumentosMedico")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testVisualizarOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222"); paciente.setNome("Paciente Teste");
        ArquivoMedico doc = new ArquivoMedico(); doc.setId(5); doc.setPaciente(paciente); doc.setExtensao(".pdf"); doc.setArquivo("abc".getBytes()); doc.setNomeOriginal("teste.pdf"); doc.setMedico(new Usuario()); doc.getMedico().setNome("Dr. Teste");

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.visualizar(5)).thenReturn(doc);

        mockMvc.perform(get("/documentos/5/visualizarDocumento")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testDownloadOk() throws Exception {
        Usuario paciente = new Usuario(); paciente.setId(2); paciente.setCpf("222"); paciente.setNome("Paciente Teste");
        ArquivoMedico doc = new ArquivoMedico(); doc.setId(5); doc.setPaciente(paciente); doc.setExtensao(".pdf"); doc.setNomeOriginal("teste"); doc.setArquivo("abc".getBytes()); doc.setMedico(new Usuario()); doc.getMedico().setNome("Dr. Teste");

        when(jwtService.extrairCpf("token")).thenReturn("222");
        when(usuarioService.searchByCpf("222")).thenReturn(paciente);
        when(permissaoService.podeVisualizarPaciente(anyInt(), anyInt())).thenReturn(true);
        when(service.visualizar(5)).thenReturn(doc);

        mockMvc.perform(get("/documentos/5/downloadDocumento")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletarOk() throws Exception {
        Usuario medico = new Usuario(); medico.setId(1); medico.setCpf("111"); medico.setNome("Dr. Teste");

        when(jwtService.extrairCpf("token")).thenReturn("111");
        when(usuarioService.searchByCpf("111")).thenReturn(medico);
        when(permissaoService.isMedico(medico)).thenReturn(true);
        doNothing().when(service).deletar(eq(medico), eq(7));

        mockMvc.perform(delete("/documentos/deletarDocumento/7")
                        .header("Authorization","Bearer token"))
                .andExpect(status().isNoContent());
    }
}
