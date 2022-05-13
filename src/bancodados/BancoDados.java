package bancodados;

import ferramentas.Arquivo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import models.Usuario;

public class BancoDados {

    //referencia do BD
    private static final File BANCO_DADOS = iniciarBancoDados();
    
    //referencia das eventuais "tabelas"
    private static final File TABELA_USUARIO = new File(BANCO_DADOS.getAbsolutePath() +
            Arquivo.resolverPath() + "usuarios");

    private static File iniciarBancoDados() {
        String homeUsuario = System.getProperty("user.home");

        File bancoDados = new File(homeUsuario + Arquivo.resolverPath() + ".bancoDadosCH");

        if (!bancoDados.exists()) {
            bancoDados.mkdir();
        }

        return bancoDados;
    }

    public static boolean registrarUsuario(Usuario user) {
        if (!TABELA_USUARIO.exists()) {
            TABELA_USUARIO.mkdir();
        }
        
        File registroUsuario = new File(TABELA_USUARIO.getAbsoluteFile() + 
                Arquivo.resolverPath() + user.getEmail() + ".json");

        try {
            if (!registroUsuario.exists()) {
                return registroUsuario.createNewFile() &&
                        Arquivo.escreverJson(registroUsuario, user);
            }
        } catch (IOException ignore) {
            //excecao ignorada
        }

        return false;
    }
    public static boolean validaUsuario(Usuario user) {
        File registroUsuario = new File(TABELA_USUARIO.getAbsoluteFile() + Arquivo.resolverPath() + user.getEmail() + ".json");
       
        if (!registroUsuario.exists()) {
            //Se o arquivo não existe email não existe, então retornamos false
            return false;
        }
        
        //Se o email existe verificamos a senha
         String senha="";
         
         //Recuperamos a senha primeiramente
        try {
            senha = Arquivo.recuperaSenha(new FileReader(TABELA_USUARIO.getAbsoluteFile() + Arquivo.resolverPath() + user.getEmail() + ".json"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BancoDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        //E em sequência verificamos 
        if(!senha.equals(user.getSenha())){
            //Senha incorreta
            return false;
        }
        //Caso email e senha corretos armazenamos as informações em um cookie para a proxima ação
        Preferences.userRoot().put("emailUser", user.getEmail());
        Preferences.userRoot().put("passUser", user.getSenha());

        
        /*
        * Código para pegar o tempo, email e senha(Somente mudar o primeiro parametro para o nome da var desejada
        * String email = Preferences.userRoot().get("emailUser","");
        */
        //Senha e email correto
        return true;
    }

}
