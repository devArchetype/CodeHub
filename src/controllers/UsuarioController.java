package controllers;

import ferramentas.Validacao;
import models.Usuario;

public class UsuarioController {

    public static void registrarUsuario() {
        String nomeUsuario = Validacao.entradaNome();
        String email = Validacao.entradaEmail(false);
        String senha = Validacao.entradaSenha(false);

        Usuario usuario = new Usuario(nomeUsuario, email, senha);

        if (usuario.registrar()) {
            System.out.println("\nConta cadastrada com sucesso!");
        } else {
            System.out.println("\nEmail já cadastrado!");
        }
    }

}
