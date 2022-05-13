package controllers;

import ferramentas.Validacao;
import java.util.prefs.Preferences;
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
     public static void acessaConta(){
           //Verifica se ele estava logado antes com cookies
        //Se estiver vazio é a primeira vez depois de um tempo no sistema, então continua o código
        String emailCookie = Preferences.userRoot().get("emailUser", "");
        
        if (!emailCookie.equals("")) {
            String senhaCookie = Preferences.userRoot().get("passUser", "");
            //Se não estiver vazio cria um objeto e verifica
            Usuario cookieUser = new Usuario(emailCookie, senhaCookie);
            if (cookieUser.validarAcesso()) {
             //Tudo certo, continua
                System.out.println("Acesso via cookie, retirar esse System.out depois");
            }else{
                //Algo deu errado, limpando o cookie e pedindo para acessa denovo
                System.out.println("Acesso via cookie, retirar esse System.out depois");
                 Preferences.userRoot().put("emailUser", "");
                 Preferences.userRoot().put("passUser", "");
                 acessaConta();
            }
                
        } else {
        String email = Validacao.entradaEmail(true);
        String senha = Validacao.entradaSenha(true);
        
        //Verificação se a o usuario existe e se a senha bate
        Usuario user = new Usuario(email, senha);
        if(user.validarAcesso()){
            System.out.println("Email e senha corretos, bem vindo ao CODEHUB");
        }else{
            System.out.println("Email ou senha incorretos, tente novamente");
            acessaConta();
        }
        }
    }

}
