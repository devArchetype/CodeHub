package controllers;

import bancodados.BancoDados;
import ferramentas.Arquivo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.prefs.Preferences;

import ferramentas.Cores;
import models.Repositorio;
import models.Usuario;
import models.Versao;
import views.RepositorioView;

public class RepositorioController {

    private Repositorio repositorio;

    private RepositorioView repositorioView;

    public RepositorioController() {
        this.repositorio = new Repositorio();
        this.repositorioView = new RepositorioView();
    }

    public void iniciaRepositorio() {
        // pegando as credenciais do usuario logado
        String emailCookie = Preferences.userRoot().get("emailUser", "");
        Usuario cookieUser = BancoDados.consultaUsuario(emailCookie);
        this.repositorio = new Repositorio(cookieUser);

        // selecionando o repositorio onde o usuario chamou o método iniciar()
        File pastaCodeHub = new File(this.repositorio.getPath());

        try {
            if (!pastaCodeHub.exists()) {
                // criacao da estrutura de .CodeHub
                File pastaVersoes = new File(pastaCodeHub.getAbsolutePath()
                        + Arquivo.resolvePath() + "versoes");
                File pastaContainer = new File(pastaCodeHub.getAbsolutePath()
                        + Arquivo.resolvePath() + "container");
                File arquivoContainer = new File(pastaContainer.getAbsolutePath()
                        + Arquivo.resolvePath() + "container.json");
                File pastaLixeira = new File(pastaCodeHub.getAbsolutePath()
                        + Arquivo.resolvePath() + "lixeira");

                // criacao dos arquivos
                pastaCodeHub.mkdir();
                pastaVersoes.mkdir();
                pastaContainer.mkdir();
                arquivoContainer.createNewFile();
                pastaLixeira.mkdir();

                // registrando o repositorio no "banco de dados"
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
        //verificando se existe um repositorio '.CodeHub' iniciado
        if (!new File(this.repositorio.getPath()).exists()) return;

        //selecionando o arquivo container.json
        File arquivoContainerJson = new File(this.repositorio.getPath() + Arquivo.resolvePath() +
                "container" + Arquivo.resolvePath() + "container.json");
        try {
            arquivoContainerJson.createNewFile();
        } catch (IOException e) {
            System.exit(0);
        }

        //objeto FileReader necessario para se fazer a leitura de um arquivo qualquer
        FileReader containerLeitura = null;
        try {
            containerLeitura = new FileReader(arquivoContainerJson);
        } catch (FileNotFoundException e) {
            System.exit(0);
        }

        // leitura do arquivo container.json, assim retornando um ArrayList<String> com
        // todos os paths existentes nele
        ArrayList<String> container = Arquivo.leContainerJson(containerLeitura);

        // se nao haver nada no container.json, um ArrayList<String> vazio e iniciado
        if (container == null) container = new ArrayList<>();

        // diretorio do projeto, que contem a pasta .CodeHub
        File diretorioPai = new File(new File(this.repositorio.getPath()).getParent());

        // verificando se o diretorio pai possui filhos
        if (diretorioPai.listFiles() != null) {
            // verificando se o arquivo a ser adicionado no container existe na pasta do projeto, assim o adicionando
            for (File arquivoVerificar : diretorioPai.listFiles()) {
                if (arquivoAdicionar.equals(".") && !arquivoVerificar.getName().equals(".CodeHub")
                        && !arquivoVerificar.getName().equals("CodeHub.jar")) {
                    // parametro que diz para adicionar todos os arquivos do projeto no container
                    container.add(arquivoVerificar.getAbsolutePath());
                } else if (arquivoVerificar.getName().equals(arquivoAdicionar)) {
                    // existe, então, adicione ele ao ArrayList do container.json e pare por aqui
                    container.add(arquivoVerificar.getAbsolutePath());
                    break;
                }
            }
        }

        // escrevendo o objeto ArrayList<String> no container.json, agora com novos arquivos adicionados ou nao
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

        //remove todo o conteudo do container caso a flag "." existir
        ArrayList<String> container = new ArrayList<>();

        //remove o conteudo determinado do container
        if(!arquivoRemover.equals(".")) {
            //leitura do arquivo container.json, assim retornando um ArrayList<String> com todos os paths existentes nele
            container = Arquivo.leContainerJson(containerLeitura);

            //percorre o array container que contem os paths
            for (int i = 0; i < container.size(); i++){
                String arquivoAtual = container.get(i);

                //remove o arquivo que foi solicitado
                if (arquivoAtual.endsWith(arquivoRemover)){
                    container.remove(i);
                    break;
                }
            }
        }

        //reescreve o container com as devidas remocoes solicitadas pelo usuario
        Arquivo.escreveJson(arquivoContainerJson, container);
    }

    //exibe todos os paths(arquivos) disponveis dentro do container
    public void listaArquivosDoContainer () {
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

        //armazena o container .json como um arraylist
        ArrayList<String> container = Arquivo.leContainerJson(containerLeitura);

        repositorioView.exibeArquivosContainer(container,this.repositorio.getPath());
    }

    public void listarHistorico() {
        repositorioView.listarHistorico(this.repositorio);
    }
    public void deletaVersao(String versaoHash){
        
      // Vetores usados para saber quais arquivos deletar e quais não deletar
        Vector arquivosVerificar = new Vector<>();
        Vector arquivosProcessar = new Vector<>();
        
        //Caminho do arquivo atual que vai ser deletado
        File diretorioPai = new File(this.repositorio.getPath() + Arquivo.resolvePath() + "versoes" + Arquivo.resolvePath() + versaoHash);
        while(diretorioPai.exists()){
        //add todos os diretorios da pasta raiz
        Collections.addAll(arquivosVerificar, diretorioPai.listFiles());

        //Forcando o programa a deletar todos os arquivos
        while (!arquivosVerificar.isEmpty()) {
                //abrindo cada uma das sub pastas
                File arqAtual = (File) arquivosVerificar.remove(0);
                //Se for um arquivo adiciona, se for uma pasta ela é salva para olharmos dentro dela
                if (arqAtual.isFile()) {
                    arquivosProcessar.add(arqAtual);
                } else {
                        Collections.addAll(arquivosVerificar,
                                arqAtual.listFiles());        
                    arquivosProcessar.add(arqAtual);
                }
            }
            //Loopando os arquivos que acabamos de salvar e deletamos cada um
            for (int arq = 0; arq != arquivosProcessar.size(); arq++) {
                File arquivo = (File) arquivosProcessar.get(arq);
                    //.deleteOnExit, deleta o arquivo na saida do programa, não no momento atual
                      arquivo.delete();
            }

            //O metodo acima não deleta as pastas, por isso é necessario criar um loop para deletar estas
            if (diretorioPai.listFiles() != null) {       
                for (File arquivoVerificar : diretorioPai.listFiles()){            
                        //.deleteOnExit, deleta o arquivo na saida do programa, não no momento atual
                        arquivoVerificar.delete();
                }
            }
            
            //Deleta a pasta pai
           diretorioPai.delete();
        }
    }
     /*
    * Função responsavel por voltar versões anteriores
    */
    public void voltarVersao(String hash) {
        
        //Verifica se a Hash está certa 
         Versao ver = BancoDados.consultaVersao(hash);
         if(ver == null){
             System.out.println("A hash inserida não existe no banco");
             return;
         }

        VersaoController v = new VersaoController();
        
        //Talvez em um futuro salvar a versão atual do usuario para backup!:
        //v.versiona("Versão criada automaticamente pelo sistema ao voltar versão");
        
        // Vetores usados para saber quais arquivos deletar e quais não deletar
        Vector arquivosVerificar = new Vector<>();
        Vector arquivosProcessar = new Vector<>();
        
        //Caminho do arquivo atual que vai ser deletado
        File diretorioPai = new File(new File(this.repositorio.getPath()).getParent());

        //add todos os diretorios da pasta raiz
        Collections.addAll(arquivosVerificar, diretorioPai.listFiles());

        //abrindo cada uma das sub pastas
        while (!arquivosVerificar.isEmpty()) {
            File arqAtual = (File) arquivosVerificar.remove(0);
            //Se for um arquivo adiciona, se for uma pasta ela é salva para olharmos dentro dela
            if (arqAtual.isFile()) {
                arquivosProcessar.add(arqAtual);
            } else {
                //pode ser pasta ou o arquivo onde guardamos as versões
                if (arqAtual.isDirectory() && !arqAtual.getName().equals("versoes") && !arqAtual.getName().equals("container")) {
                    Collections.addAll(arquivosVerificar,
                            arqAtual.listFiles());
                }
                arquivosProcessar.add(arqAtual);
            }
        }
        //Loopando os arquivos que acabamos de salvar e deletamos cada um
        for (int arq = 0; arq != arquivosProcessar.size(); arq++) {
            File arquivo = (File) arquivosProcessar.get(arq);
            if (!arquivo.getName().equals(".CodeHub") && !arquivo.getName().equals("CodeHub.jar")) {
                //.deleteOnExit, deleta o arquivo na saida do programa, não no momento atual
                  arquivo.delete();
            }
        }

        //O metodo acima não deleta as pastas, por isso é necessario criar um loop para deletar estas
        if (diretorioPai.listFiles() != null) {       
            for (File arquivoVerificar : diretorioPai.listFiles()) {
                if (!arquivoVerificar.getName().equals(".CodeHub") && !arquivoVerificar.getName().equals("CodeHub.jar")) {
                    //.deleteOnExit, deleta o arquivo na saida do programa, não no momento atual
                    arquivoVerificar.delete();
                }
            }
        }

        //Definimos a versão que voltara como a atual no banco de dados
        ver.setVersaoAtual(true);
        v.atualizaVersaoAtual();
        
        // salva as modificações no banco de dados.
        File registroVersao = new File(BancoDados.getTabelaVersoes().getAbsolutePath() + Arquivo.resolvePath() + ver.getChavePrimaria() + ".json");     
        Arquivo.escreveJson(registroVersao, ver);

        //Finalmente volta a versão selecionada
        File voltaArquivos = new File(this.repositorio.getPath() + Arquivo.resolvePath() + "versoes" + Arquivo.resolvePath() + ver.getChavePrimaria());
        
        if (voltaArquivos.listFiles() != null) {
        //verificando se o arquivo a ser adicionado no container existe na pasta do
            // projeto, assim o adicionando
            for (File arquivoVerificar : voltaArquivos.listFiles()) {
                if (!arquivoVerificar.getName().equals(".CodeHub") && !arquivoVerificar.getName().equals("CodeHub.jar")) {
                    try {
                        //Copia os diretorios de arquivo da Hash, para o lugar escolhido
                        copiandoDiretorios(arquivoVerificar.getPath(), "" + new File(this.repositorio.getPath()).getParent() + Arquivo.resolvePath() + arquivoVerificar.getName());
                        
                         //Comando que inves de copiar, move os arquivos : Pode ser útil no futuro( Retorna true se o arquivo foi movido com sucesso, e false se não foi)
                         //System.out.println(arquivoVerificar.renameTo(new File(new File(this.repositorio.getPath()).getParent() + Arquivo.resolvePath()+arquivoVerificar.getName())));
                    } catch (IOException e) {
                        //Sem Debug em produção
                        //e.printStackTrace();
                    }
                }
            }
        }
 
    }

    public static void copiandoDiretorios(String origemDoArquivo, String destinoDoArquivo) throws IOException {
        //Anda pela arvore copiando arquivo por arquivo e diretorio por diretorio
        //Documentação : https://www.baeldung.com/java-copy-directory
        Files.walk(Paths.get(origemDoArquivo))
                .forEach(source -> {
                    Path destination = Paths.get(destinoDoArquivo, source.toString()
                            .substring(origemDoArquivo.length()));
                    try {
                        Files.copy(source, destination);
                    } catch (IOException e) {
                        //Sem Debug na versão de produção
                       // e.printStackTrace();
                    }
                });        
    }

}

