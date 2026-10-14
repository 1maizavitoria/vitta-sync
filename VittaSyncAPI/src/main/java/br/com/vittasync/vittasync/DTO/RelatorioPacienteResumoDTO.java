package br.com.vittasync.vittasync.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;


@Schema(description = "Dados básicos do paciente no relatório")
public class RelatorioPacienteResumoDTO {
    @Schema(description = "Nome do paciente", example = "Maria Souza")
    private String nome;

    @Schema(description = "CPF do paciente", example = "52998224725")
    private String cpf;

    @Schema(description = "Peso informado no cadastro em kg", example = "74.0")
    private Double pesoInicial;

    @Schema(description = "Altura em metros", example = "1.65")
    private Double altura;

    @Schema(description = "Data de nascimento", example = "1958-04-22")
    private LocalDate dataNascimento;


    public RelatorioPacienteResumoDTO(String nome, String cpf, Double pesoInicial, Double altura, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.pesoInicial = pesoInicial;
        this.altura = altura;
        this.dataNascimento = dataNascimento;
    }


    public String getNome() { return nome; }
    
    public String getCpf() { return cpf; }
    
    public Double getPesoInicial() { return pesoInicial; }
    
    public Double getAltura() { return altura; }
    
    public LocalDate getDataNascimento() { return dataNascimento; }
    
}
