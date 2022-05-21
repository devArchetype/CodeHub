package controllers;

import ferramentas.*;

import java.util.prefs.Preferences;
import models.Usuario;
import views.UsuarioView;

public class UsuarioController {

    private Usuario usuario;
    private UsuarioView usuarioView;

    public UsuarioController() {
        this.usuario = new Usuario();
        this.usuarioView = new UsuarioView();
    }

    public void registraUsuario() {
        String nomeUsuario = Validacao.entradaNome();
        String email = Validacao.entradaEmail(false);
        String senha = Validacao.entradaSenha(false);

        this.usuario.setNome(nomeUsuario);
        this.usuario.setEmail(email);
        this.usuario.setSenha(senha);

        if (usuario.registraDados()) {
            System.out.println("\nConta cadastrada com sucesso!");
        } else {
            System.out.println("\nEmail ja cadastrado!");
        }
    }

    public void acessaConta(){
        //Verifica se ele estava logado antes com cookies
        //Se estiver vazio é a primeira vez depois de um tempo no sistema, então continua o código
        String emailCookie = Preferences.userRoot().get("emailUser", "");
        System.out.println(emailCookie);

        if (!emailCookie.equals("")) {
            String senhaCookie = Preferences.userRoot().get("passUser", "");

            //Se não estiver vazio cria um objeto e verifica
            Usuario cookieUser = new Usuario(emailCookie, senhaCookie);
            System.out.println(cookieUser.validarAcesso());

            if (!cookieUser.validarAcesso()) {
                //Algo deu errado, limpando o cookie e pedindo para acessa denovo
                Preferences.userRoot().put("emailUser", "");
                Preferences.userRoot().put("passUser", "");
                acessaConta();
            }
        } else {
            String email = Validacao.entradaEmail(true);
            String senha = Validacao.entradaSenha(true);

            //Verificação se a o usuario existe e se a senha bate
            this.usuario.setEmail(email);
            this.usuario.setSenha(senha);

            if(usuario.validarAcesso()){
                System.out.println("Email e senha corretos, bem-vindo ao CodeHub!");
            } else {
                System.out.println("Email ou senha incorretos, tente novamente!");
            }
        }
    }

    public void sairConta() {
        /*
           Faz logout do acesso criado cookie do método acima
        */
        String emailCookie = Preferences.userRoot().get("emailUser", ""); // pega o email no cookie
        if (!emailCookie.equals("")) { // se o email não estiver vazio, há alguem logado.
            String senhaCookie = Preferences.userRoot().get("passUser", ""); // verifica a senha também

            if(!senhaCookie.equals("")){
                // limpeza do cookie para efetivação do Log out
                Preferences.userRoot().put("emailUser", "");  // como o user está logado, para remover, adiciona vazio
                Preferences.userRoot().put("passUser", ""); // como o user está logado, para remover, adiciona vazio

                // exibição para o usuário no terminal:
                System.out.println("Sessao encerrada!");
                System.out.println("Volte em breve!");
                System.exit(0);
                //Logger.getLogger("CodeHubAutenticationLog").log(Level.INFO,emailCookie + " desconectou-se do GitHub em " + new Date().toString());
            }
        } else  {
            System.out.println("Voce deve efetuar o acesso para poder sair de uma conta!");
        }

    }

    public void chamarAjuda() {
        this.usuarioView.imprimirComandos();
    }

    //verificacao de login meramente criada pra testes, para ser usado no Main, como uma verificacao geral
    public boolean estaLogado() {
        String emailCookie = Preferences.userRoot().get("emailUser", "");
        return !emailCookie.equals("");
    }

}
