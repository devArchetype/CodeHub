package controllers;

import bancodados.BancoDados;
import com.google.gson.Gson;
import ferramentas.Arquivo;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.prefs.Preferences;
import models.Repositorio;
import models.Usuario;

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
        File pastaVersoes = new File(
          pastaCodeHub.getAbsolutePath() + Arquivo.resolvePath() + "versoes"
        );
        File pastaContainer = new File(
          pastaCodeHub.getAbsolutePath() + Arquivo.resolvePath() + "container"
        );
        File arquivoContainer = new File(
          pastaContainer.getAbsolutePath() +
          Arquivo.resolvePath() +
          "container.json"
        );

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
    File arquivoContainerJson = new File(
      this.repositorio.getPath() +
      Arquivo.resolvePath() +
      "container" +
      Arquivo.resolvePath() +
      "container.json"
    );

    //objeto FileReader necessario para se fazer a leitura de um arquivo qualquer
    FileReader containerLeitura = null;
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
    File diretorioPai = new File(
      new File(this.repositorio.getPath()).getParent()
    );

    //verificando se o diretorio pai possui filhos
    if (diretorioPai.listFiles() != null) {
      //verificando se o arquivo a ser adicionado no container existe na pasta do projeto, assim o adicionando
      for (File arquivoVerificar : diretorioPai.listFiles()) {
        if (
          arquivoAdicionar.equals(".") &&
          !arquivoVerificar.getName().equals(".CodeHub")
        ) {
          //parametro que diz para adicionar todos os arquivos do projeto no container
          container.add(arquivoVerificar.getAbsolutePath());
        } else if (arquivoVerificar.getName().equals(arquivoAdicionar)) {
          //existe, então, adicione ele ao ArrayList do container.json e pare por aqui
          container.add(arquivoVerificar.getAbsolutePath() + ","); // para separar por v´rgulas
          break;
        }
      }
    }

    //escrevendo o objeto ArrayList<String> no container.json, agora com novos arquivos adicionados ou nao
    Arquivo.escreveJson(arquivoContainerJson, container);
  }

  public void versionar(String descricao) {
    /*
        Cria e confirma (commit) uma nova versão
        parametros: descrição (String) - identificação textual
        retorn: void
        */
    // lista diretório de versões para identificar se o arquivo já existe:
    String caminhoParaPastaContainer =
      this.repositorio.getPath() + Arquivo.resolvePath() + "container";
    File containerFiles = new File(caminhoParaPastaContainer);
    threadVersionar(Arrays.toString(containerFiles.listFiles()), descricao);
  }

  private void threadVersionar(String caminhos, String comentario) {
    // cria threads para executar as funções do versionar
    String repositorio = this.repositorio.getPath();

    new Thread() {
      @Override
      public void run() {
        //seleciona o repositorio onde o usuario chamou o método iniciar()
        File pastaCodeHub = new File(repositorio);
        try {
          String hash = geraHashDaVersao();
          // acessa a pasta:
          File pastaVersoes = new File(
            pastaCodeHub.getAbsolutePath() + Arquivo.resolvePath() + "versoes"
          );
          // se tiver conteúdo, não permite o versionamento:
          if (
            Objects.requireNonNull(pastaVersoes.listFiles()).length > 0
          ) System.out.println(
            "Existem arquivos ja versionados\nNada foi alterado."
          ); else {
            String jsonVersoes =
              "{\"versao:\" " +
              hash +
              "\",Data:\"" +
              new Date() +
              ",\"usuario:\"" +
              Preferences.userRoot().get("emailUser", "") +
              ", \"comentario:\"" +
              comentario +
              ",\"paths:\"";
            // conversão para json
            String[] paths = caminhos
              .replace("[", "")
              .replace("]", "")
              .split(",");
            for (String caminho : paths) {
              jsonVersoes += caminho + ", ";
            }
            jsonVersoes += "}";
            String obj = new Gson().toJson(jsonVersoes);
            // cria o arquivo com o json
            File versao = new File(
              pastaVersoes + Arquivo.resolvePath() + hash + ".json"
            );
            // analisa se , por acaso , não existe (BEM IMPROVAVEL):
            if (!versao.exists()) versao.createNewFile();
            FileWriter escrevedorArquivo = new FileWriter(versao, true);
            escrevedorArquivo.write(obj); // escreve o json
            escrevedorArquivo.close();
          }
        } catch (Exception e) {
          System.out.println("Erro ao gerar hash da versão!");
        }
      }
    }
      .start();
  }

  private String geraHashDaVersao() throws NoSuchAlgorithmException {
    // gera hash com base em letras do alfabeto
    int index;
    String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    StringBuilder constroe = new StringBuilder(1016);

    for (int m = 0; m < 16; m++) {
      index = (int) (alpha.length() * Math.random());
      constroe.append(alpha.charAt(index));
    }
    // palavra aleatoria gerada pelos métodos acima:
    String word = constroe.toString(); // palavra a ser transformada em hash
    MessageDigest mensagem = MessageDigest.getInstance("MD5"); // criptografia do tipo md5, mais básicona msm
    BigInteger hash = new BigInteger(
      1,
      mensagem.digest(word.getBytes(StandardCharsets.UTF_8))
    );
    return hash.toString(); // retorna o hash numérico como texto
  }
  /*
    METODO PARA IMPLEMENTACAO FUTURA

    ---------------------------------------------------------------------------------------------------------------------------
    private boolean verificaAlteracoesNosArquivos(String local_arquivo, String ultimo_hash) throws FileNotFoundException{
        // verifica se houve alterações nos arquivos.
        // implementacao futura
        // não descrito,pois, ainda não será implementado
        MessageDigest digest = null;
        try { digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) { e.printStackTrace();}
        File arquivo = new File(local_arquivo);
        InputStream is = null;
        try { is = new FileInputStream(arquivo);  //  b9f3430ccc11413aa4c68ee87675f35f
        } catch (FileNotFoundException e) {  }
        byte[] buffer = new byte[8192];
        int leitura = 0;
        try {
            while( (leitura = is.read(buffer)) > 0) digest.update(buffer, 0, leitura); // le o arquivo até o final
            byte[] md5sum = digest.digest();
            BigInteger inteiro = new BigInteger(1, md5sum);
            String hash = inteiro.toString(16);
            if(hash.equals(ultimo_hash)) return true;
        } catch(IOException e) { throw new RuntimeException("CODEHUB EXCEPTION", e);
        }finally {
            try { is.close(); }
            catch(IOException e) { throw new RuntimeException("CODEHUB EXCEPTION", e); }}
        return false;
    }
    ---------------------------------------------------------------------------------------------------------------------------
        */
}
