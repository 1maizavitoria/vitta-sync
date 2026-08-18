package br.com.vittasync.vittasync.DTO;

import java.time.LocalDate;
import java.util.List;

public class DashboardResponseDTO {

    private String pacienteCpf;
    private String pacienteNome;
    private LocalDate inicio;
    private LocalDate fim;
    private List<DashboardCategoriaDTO> categorias;

    public DashboardResponseDTO(
            String pacienteCpf,
            String pacienteNome,
            LocalDate inicio,
            LocalDate fim,
            List<DashboardCategoriaDTO> categorias
    ) {
        this.pacienteCpf = pacienteCpf;
        this.pacienteNome = pacienteNome;
        this.inicio = inicio;
        this.fim = fim;
        this.categorias = categorias;
    }

    public String getPacienteCpf() {
        return pacienteCpf;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public LocalDate getFim() {
        return fim;
    }

    public List<DashboardCategoriaDTO> getCategorias() {
        return categorias;
    }
}
