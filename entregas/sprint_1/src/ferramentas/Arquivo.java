package ferramentas;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import models.Usuario;

public class Arquivo {
    
    private static final Gson GSON = new Gson(); //objeto com funcionalidades para arquivos json

    //identifica o SO, retornando a estrutura adequada do path
    public static String resolverPath() {
        String sistemaOperacional = System.getProperty("os.name");

        if (sistemaOperacional.startsWith("Windows")) {
            return "\\";
        } else { //linux, mac, etc...
            return "/";
        }
    }

    public static boolean escreverJson(File arquivo, Object objeto) {
        String json = GSON.toJson(objeto); //conversao de um objeto java para json

        try {
            FileWriter gravacaoArquivo = new FileWriter(arquivo.getAbsolutePath());
            gravacaoArquivo.write(json);
            gravacaoArquivo.close();
        } catch (IOException ex) {
            return false;
        }

        return true;
    }

    public static String recuperaSenha(FileReader arquivo) {
        Usuario user =  GSON.fromJson(arquivo, Usuario.class);
        String senha = user.getSenha();
        return senha;
    }

}
