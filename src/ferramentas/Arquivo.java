package ferramentas;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import models.Usuario;

public class Arquivo {
    
    private static final Gson GSON = new Gson(); //objeto com funcionalidades para arquivos json

    //identifica o SO, retornando a estrutura adequada do path
    public static String resolvePath() {
        String sistemaOperacional = System.getProperty("os.name");

        if (sistemaOperacional.startsWith("Windows")) {
            return "\\";
        } else { //linux, mac, etc...
            return "/";
        }
    }

    //escreve qualquer objeto Java em um json
    public static boolean escreveJson(File arquivoJson, Object objeto) {
        String objetoJson = GSON.toJson(objeto); //conversao de um objeto java para json
        try {
            //gravacao do objeto no json
            FileWriter gravacaoArquivo = new FileWriter(arquivoJson.getAbsolutePath());
            gravacaoArquivo.write(objetoJson);
            gravacaoArquivo.close();
        } catch (IOException ex) {  return false; }
        return true;
    }

    public static String recuperaSenha(FileReader arquivo) {
        Usuario user =  GSON.fromJson(arquivo, Usuario.class);
        String senha = user.getSenha();
        return senha;
    }

    //le um arquivo usuario.json, gerando um objeto Java Usuario
    public static Usuario leUsuarioJson(FileReader arquivoJson) {
        return GSON.fromJson(arquivoJson, Usuario.class);
    }

    //le um arquivo container.json, gerando um objeto Java ArrayList<String>
    public static ArrayList<String> leContainerJson(FileReader arquivoJson) {
        Type tipoLista = new TypeToken<ArrayList<String>>(){}.getType();
        return GSON.fromJson(arquivoJson, tipoLista);
    }

}
