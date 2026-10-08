package br.com.vittasync.vittasync.DTO;


import java.time.LocalDate;
import java.util.List;


public class DashboardResponseDTO {

    private String pacienteCpf;
    private String pacienteNome;
    private LocalDate inicio;
    private LocalDate fim;
    private List<DashboardCategoriaDTO> categorias;
    private List<EstabilidadeClinicaDTO> estabilidadeClinica;
    private List<LinhaBaseDTO> linhasBase;

    public DashboardResponseDTO(
            String pacienteCpf,
            String pacienteNome,
            LocalDate inicio,
            LocalDate fim,
            List<DashboardCategoriaDTO> categorias,
            List<EstabilidadeClinicaDTO> estabilidadeClinica,
            List<LinhaBaseDTO> linhasBase
    ) {
        this.pacienteCpf = pacienteCpf;
        this.pacienteNome = pacienteNome;
        this.inicio = inicio;
        this.fim = fim;
        this.categorias = categorias;
        this.estabilidadeClinica = estabilidadeClinica;
        this.linhasBase = linhasBase;
    }

    public String getPacienteCpf() {return pacienteCpf;}

    public String getPacienteNome() {return pacienteNome;}

    public LocalDate getInicio() {return inicio;}

    public LocalDate getFim() {return fim;}

    public List<DashboardCategoriaDTO> getCategorias() {return categorias;}

    public List<EstabilidadeClinicaDTO> getEstabilidadeClinica() {return estabilidadeClinica;}

    public List<LinhaBaseDTO> getLinhasBase() {return linhasBase;}
    
}
