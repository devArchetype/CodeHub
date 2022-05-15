package Controllers;

import bancodados.BancoDados;
import ferramentas.*;

import java.io.File;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
         System.out.println(emailCookie);
        if (!emailCookie.equals("")) {
            String senhaCookie = Preferences.userRoot().get("passUser", "");
            //Se não estiver vazio cria um objeto e verifica
            Usuario cookieUser = new Usuario(emailCookie, senhaCookie);
            System.out.println(cookieUser.validarAcesso());
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
            //Logger.getLogger("CodeHubAutenticationLog").log(Level.INFO,emailCookie + " conectou-se do GitHub em " + new Date().toString());
        }else{
            System.out.println("Email ou senha incorretos, tente novamente");
            acessaConta();
        }
        }
    }

    public static void sairConta() {
        /*
            Faz logout do acesso criado cookie do método acima
            Não tem retorno.
            Não tem parâmetro.
         */
        String emailCookie = Preferences.userRoot().get("emailUser", ""); // pega o email no cookie
        if (!emailCookie.equals("")) { // se o email não estiver vazio, há alguem logado.
            String senhaCookie = Preferences.userRoot().get("passUser", ""); // verifica a senha também
            if(!senhaCookie.equals("")){
                // limpeza do cookie para efetivação do Log out
                Preferences.userRoot().put("emailUser", "");  // como o user está logado, para remover, adiciona vazio
                Preferences.userRoot().put("passUser", ""); // como o user está logado, para remover, adiciona vazio

                // exibição para o usuário no terminal:
                System.out.println("Sessão encerrada!");
                System.out.println("Volte em breve☺!");
                //Logger.getLogger("CodeHubAutenticationLog").log(Level.INFO,emailCookie + " desconectou-se do GitHub em " + new Date().toString());
            }
        }else System.out.println("Você deve efetuar o acesso para poder sair de uma conta!");

    }


}
