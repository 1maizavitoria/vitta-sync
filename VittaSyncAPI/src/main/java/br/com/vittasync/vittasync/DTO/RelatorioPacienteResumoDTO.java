package br.com.vittasync.vittasync.DTO;


import java.time.LocalDate;


public class RelatorioPacienteResumoDTO {
    private String nome;
    private String cpf;
    private Double pesoInicial;
    private Double altura;
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
