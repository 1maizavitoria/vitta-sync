package br.com.vittasync.vittasync.DTO;


public class LoginDTO {

    private String cpf;
    private String senha;
    private String canal;

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }
}
