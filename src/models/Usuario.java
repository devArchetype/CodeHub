package models;

import bancodados.BancoDados;

public class Usuario {
    
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
        //default
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }
    
    public boolean registraDados() {
        return BancoDados.registraUsuario(this);
    }

    public boolean validarAcesso(){
        return BancoDados.validaUsuario(this);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}
