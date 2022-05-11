package bancodados;

import ferramentas.Arquivo;

import java.io.File;
import java.io.IOException;

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

}
