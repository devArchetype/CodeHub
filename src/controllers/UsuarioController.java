package controllers;

import ferramentas.Validacao;
import models.Usuario;

public class UsuarioController {
    
    
    public static void registrarUsuario() {
        String nomeUsuario = Validacao.entradaNome();
        String email = Validacao.entradaEmail();
        String senha = Validacao.entradaSenha();
        
        Usuario user = new Usuario(nomeUsuario, email, senha);
        
        if (user.registrar()) {
            System.out.println("\nConta cadastrada com sucesso!");
        } else {
            System.out.println("\nEmail já cadastrado! Tente novamente");
        }
    }
    
}
