package bancodados;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import ferramentas.*;
import models.Repositorio;
import models.Usuario;

public class BancoDados {

    //referencia do BD
    private static final File BANCO_DADOS = iniciaBancoDados();
    
    //referencia das eventuais "tabelas"
    private static final File TABELA_USUARIO = new File(BANCO_DADOS.getAbsolutePath() +
            Arquivo.resolvePath() + "usuarios");
    private static final File TABELA_REPOSITORIO = new File(BANCO_DADOS.getAbsolutePath() +
            Arquivo.resolvePath() + "repositorios");

    //inicia o fake banco de dados na home do usuario
    private static File iniciaBancoDados() {
        String homeUsuario = System.getProperty("user.home");

        File bancoDados = new File(homeUsuario + Arquivo.resolvePath() + ".bancoDadosCH");

        if (!bancoDados.exists()) {
            bancoDados.mkdir();
        }

        return bancoDados;
    }

    //metodo auxiliar para o registro de uma ocorrencia qualquer no BD
    private static boolean registraOcorrencia(File registro, Object ocorrencia) {
        try {
            if (!registro.exists()) {
                return registro.createNewFile() &&
                        Arquivo.escreveJson(registro, ocorrencia);
            }
        } catch (IOException ignore) {
            //excecao ignorada
        }

        return false;
    }

    //metodo para a insercao de um usuario no BD
    public static boolean registraUsuario(Usuario usuario) {
        if (!TABELA_USUARIO.exists()) {
            TABELA_USUARIO.mkdir();
        }

        //caminho do arquivo json a ser criado para o usuario
        File registroUsuario = new File(TABELA_USUARIO.getAbsolutePath() +
                Arquivo.resolvePath() + usuario.getEmail() + ".json");
        //no momento, usa-se o email como chave primaria(nome do arquivo json)

        return registraOcorrencia(registroUsuario, usuario);
    }

    //procura por uma ocorrencia de Usuario, retornando-a se possivel ou null
    public static Usuario consultaUsuario(String chave) {
        try {
            FileReader registroUsuario = new FileReader(TABELA_USUARIO.getAbsolutePath() +
                    Arquivo.resolvePath() + chave + ".json");
            return Arquivo.leUsuarioJson(registroUsuario);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static boolean validaUsuario(Usuario user) {
        File registroUsuario = new File(TABELA_USUARIO.getAbsolutePath() +
                Arquivo.resolvePath() + user.getEmail() + ".json");
       
        if (!registroUsuario.exists()) {
            //Se o arquivo não existe email não existe, então retornamos false
            return false;
        }
        
        //Se o email existe verificamos a senha
         String senha="";
         
        //Recuperamos a senha primeiramente
        try {
            senha = Arquivo.recuperaSenha(new FileReader(TABELA_USUARIO.getAbsolutePath()
                    + Arquivo.resolvePath() + user.getEmail() + ".json"));
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

    //metodo para a insercao de um repositorio no BD
    public static boolean registraRepositorio(Repositorio repositorio) {
        if (!TABELA_REPOSITORIO.exists()) {
            TABELA_REPOSITORIO.mkdir();
        }

        //caminho do arquivo json a ser criado para o usuario
        File registroRepositorio = new File(TABELA_REPOSITORIO.getAbsolutePath() +
                Arquivo.resolvePath() + repositorio.getChavePrimaria() + ".json");
        //usa-se o atributo chavePrimaria como nome do arquivo json

        return registraOcorrencia(registroRepositorio, repositorio);
    }

}
