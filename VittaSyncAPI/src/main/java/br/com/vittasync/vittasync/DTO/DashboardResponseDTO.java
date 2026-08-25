package br.com.vittasync.vittasync.DTO;

import java.time.LocalDate;
import java.util.List;

public class DashboardResponseDTO {

    private String pacienteCpf;
    private String pacienteNome;
    private LocalDate inicio;
    private LocalDate fim;
    private List<DashboardCategoriaDTO> categorias;

    // Novo campo: índices de estabilidade clínica
    private List<EstabilidadeClinicaDTO> estabilidadeClinica;

    public DashboardResponseDTO(
            String pacienteCpf,
            String pacienteNome,
            LocalDate inicio,
            LocalDate fim,
            List<DashboardCategoriaDTO> categorias,
            List<EstabilidadeClinicaDTO> estabilidadeClinica
    ) {
        this.pacienteCpf = pacienteCpf;
        this.pacienteNome = pacienteNome;
        this.inicio = inicio;
        this.fim = fim;
        this.categorias = categorias;
        this.estabilidadeClinica = estabilidadeClinica;
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

    public List<EstabilidadeClinicaDTO> getEstabilidadeClinica() {
        return estabilidadeClinica;
    }
}
