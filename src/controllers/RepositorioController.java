package controllers;

import bancodados.BancoDados;
import ferramentas.Arquivo;
import models.Repositorio;
import models.Usuario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class RepositorioController {

    private Repositorio repositorio;

    public RepositorioController() {
        this.repositorio = new Repositorio();
    }

    public void iniciaRepositorio() {
        //pegando as credenciais do usuario logado
        String emailCookie = Preferences.userRoot().get("emailUser", "");
        Usuario cookieUser = BancoDados.consultaUsuario(emailCookie);
        this.repositorio = new Repositorio(cookieUser);

        //selecionando o repositorio onde o usuario chamou o método iniciar()
        File pastaCodeHub = new File(this.repositorio.getPath());

        try {
            if (!pastaCodeHub.exists()) {
                //criacao da estrutura de .CodeHub
                File pastaVersoes = new File(pastaCodeHub.getAbsolutePath()
                        + Arquivo.resolvePath() + "versoes");
                File pastaContainer = new File(pastaCodeHub.getAbsolutePath()
                        + Arquivo.resolvePath() + "container");
                File arquivoContainer = new File(pastaContainer.getAbsolutePath()
                        + Arquivo.resolvePath() + "container.json");

                //criacao dos arquivos
                pastaCodeHub.mkdir();
                pastaVersoes.mkdir();
                pastaContainer.mkdir();
                arquivoContainer.createNewFile();

                //registrando o repositorio no "banco de dados"
                this.repositorio.registraDados();

                System.out.println("CodeHub inicializado com sucesso!");
            } else {
                System.out.println("O CodeHub ja foi inicializado!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void adicionaAoContainer(String arquivoAdicionar) {
        //selecionando o arquivo container.json
        File arquivoContainerJson = new File(this.repositorio.getPath() +
                Arquivo.resolvePath() + "container" + Arquivo.resolvePath() + "container.json");

        //objeto FileReader necessario para se fazer a leitura de um arquivo qualquer
        FileReader containerLeitura  = null;
        try {
            containerLeitura = new FileReader(arquivoContainerJson);
        } catch (FileNotFoundException e) {
            System.exit(0);
        }

        //leitura do arquivo container.json, assim retornando um ArrayList<String> com todos os paths existentes nele
        ArrayList<String> container = Arquivo.leContainerJson(containerLeitura);
        //se nao haver nada no container.json, um ArrayList<String> vazio e iniciado
        if (container == null) container = new ArrayList<>();

        //diretorio do projeto, que contem a pasta .CodeHub
        File diretorioPai = new File(new File(this.repositorio.getPath()).getParent());

        //verificando se o diretorio pai possui filhos
       if (diretorioPai.listFiles() != null) {
           //verificando se o arquivo a ser adicionado no container existe na pasta do projeto, assim o adicionando
           for (File arquivoVerificar : diretorioPai.listFiles()) {
               if (arquivoAdicionar.equals(".") && !arquivoVerificar.getName().equals(".CodeHub")) {
                   //parametro que diz para adicionar todos os arquivos do projeto no container
                   container.add(arquivoVerificar.getAbsolutePath());
               } else if (arquivoVerificar.getName().equals(arquivoAdicionar)) {
                   //existe, então, adicione ele ao ArrayList do container.json e pare por aqui
                   container.add(arquivoVerificar.getAbsolutePath());
                   break;
               }
           }
       }

        //escrevendo o objeto ArrayList<String> no container.json, agora com novos arquivos adicionados ou nao
        Arquivo.escreveJson(arquivoContainerJson, container);
    }
    
    public void removeDoContainer (String arquivoRemover) {
        //seleciona o arquivo container.json e armazena em um objeto file criado
        File arquivoContainerJson = new File(this.repositorio.getPath() + 
                Arquivo.resolvePath()  + "container" + Arquivo.resolvePath() + "container.json");
    
        //objeto FileReader necessario para se fazer a leitura de um arquivo qualquer
        FileReader containerLeitura = null;
        try {
            containerLeitura = new FileReader(arquivoContainerJson);
        } catch (FileNotFoundException e) {
            System.exit(0);
        }
        
        //leitura do arquivo container.json, assim retornando um ArrayList<String> com todos os paths existentes nele
        ArrayList<String> container = Arquivo.leContainerJson(containerLeitura);
        
        //percorre o array container que contem os paths
        for (int i = 0; i < container.size(); i++){
            String arquivoAtual = container.get(i); 
            
            //remove o arquivo que foi solicitado
            if (arquivoAtual.endsWith(arquivoRemover)){
                container.remove(i);
                break;
            }
        }
        
        //reescreve o container com as devidas remocoes solicitadas pelo usuario
        Arquivo.escreveJson(arquivoContainerJson, container);
    }
    
}

