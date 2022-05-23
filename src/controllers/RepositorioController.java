package controllers;

import bancodados.BancoDados;
import com.google.gson.Gson;
import ferramentas.Arquivo;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.prefs.Preferences;
import models.Repositorio;
import models.Usuario;
import models.Versao;

public class RepositorioController {

  private Repositorio repositorio;

  public RepositorioController() {
    this.repositorio = new Repositorio();
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
        File pastaVersoes = new File(
            pastaCodeHub.getAbsolutePath() + Arquivo.resolvePath() + "versoes");
        File pastaContainer = new File(
            pastaCodeHub.getAbsolutePath() + Arquivo.resolvePath() + "container");
        File arquivoContainer = new File(
            pastaContainer.getAbsolutePath() +
                Arquivo.resolvePath() +
                "container.json");

        // criacao dos arquivos
        pastaCodeHub.mkdir();
        pastaVersoes.mkdir();
        pastaContainer.mkdir();
        arquivoContainer.createNewFile();

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
    // selecionando o arquivo container.json
    File arquivoContainerJson = new File(
        this.repositorio.getPath() + Arquivo.resolvePath() + "container" + Arquivo.resolvePath() + "container.json");
    try {
      arquivoContainerJson.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    // objeto FileReader necessario para se fazer a leitura de um arquivo qualquer
    FileReader containerLeitura = null;
    try {
      containerLeitura = new FileReader(arquivoContainerJson);
    } catch (FileNotFoundException e) {
      System.out.println(e);
      System.exit(0);
    } finally {
      // leitura do arquivo container.json, assim retornando um ArrayList<String> com
      // todos os paths existentes nele
      ArrayList<String> container = Arquivo.leContainerJson(containerLeitura);
      // se nao haver nada no container.json, um ArrayList<String> vazio e iniciado
      if (container == null)
        container = new ArrayList<>();
      // diretorio do projeto, que contem a pasta .CodeHub
      File diretorioPai = new File(new File(this.repositorio.getPath()).getParent());
      // verificando se o diretorio pai possui filhos
      if (diretorioPai.listFiles() != null) {
        // verificando se o arquivo a ser adicionado no container existe na pasta do
        // projeto, assim o adicionando
        for (File arquivoVerificar : diretorioPai.listFiles()) {
          if (arquivoAdicionar.equals(".") && !arquivoVerificar.getName().equals(".CodeHub")
              && !arquivoVerificar.getName().equals("CodeHub.jar")) {
            // parametro que diz para adicionar todos os arquivos do projeto no container
            container.add(arquivoVerificar.getAbsolutePath());
          } else if (arquivoVerificar.getName().equals(arquivoAdicionar)) {
            // existe, então, adicione ele ao ArrayList do container.json e pare por aqui
            container.add(arquivoVerificar.getAbsolutePath() + ","); // para separar por v´rgulas
            break;
          }
        }
      }

      // escrevendo o objeto ArrayList<String> no container.json, agora com novos
      // arquivos adicionados ou nao

      Arquivo.escreveJson(arquivoContainerJson, container);
    }
  }

  public void listarHistorico() {
    // Acessando diretório com as versões
    File diretorioVersoes = new File(
        this.repositorio.getPath() + Arquivo.resolvePath() + "versoes");

    // Array criado com todos os arquivos de versão
    File[] arquivoChaveVersoes = diretorioVersoes.listFiles();

    // Veto com o objeto Versao de cada versão
    Vector<Versao> objetosVersoes = new Vector<>();

    Set<Long> datasOrdenadasLong = new TreeSet<Long>();
    SimpleDateFormat formatacaoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    // Adicionando cada objeto da versão em um array
    for (File arquivoChaveVersao : arquivoChaveVersoes) {
      Versao versao = BancoDados.consultaVersao(arquivoChaveVersao.getName());
      objetosVersoes.add(versao);

      String dataVersao = versao.getData();
      try {
        // Pegando valor da data em String e passando para milisegundos e adicionando em
        // um array de Long
        datasOrdenadasLong.add(formatacaoData.parse(dataVersao).getTime());

      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    Vector<String> datasOrdenadasString = new Vector<>();

    for (Long data : datasOrdenadasLong) {
      // Convertendo data em milisegundos para String
      datasOrdenadasString.add(formatacaoData.format(data));
    }

    // Pegando dados da versão que contem aquela data
    while (!objetosVersoes.isEmpty()) {
      // Selecionando primeira versao
      Versao versao = objetosVersoes.remove(0);

      // Buscando dados do usuário no banco de dados através do email
      String emailUsuario = versao.getUsuario();
      Usuario usuario = BancoDados.consultaUsuario(emailUsuario);

      // Copiando valor da primeira data
      String data = datasOrdenadasString.get(0);

      // Comparando data copiada com data da versão
      // Se as datas forem identicas, exibe a versão e remove a data da fila
      if (versao.getData().equals(data)) {
        System.out.println(
          "Versao: " + versao.getChavePrimaria() + "\n" +
          "Comentario: " + versao.getComentario() + "\n" +
          "Autor: " + usuario.getNome() + " <" + usuario.getEmail() + ">" + "\n" +
          "Data: " + versao.getData() + "\n"
        );

        datasOrdenadasString.remove(0);
      } else {
        // Caso não sejam iguais, adiciona a versão ao final da fila
        objetosVersoes.add(versao);
      }
    }
  }
}
