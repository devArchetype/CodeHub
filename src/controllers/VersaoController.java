package controllers;

import bancodados.BancoDados;
import ferramentas.Arquivo;
import models.Repositorio;
import models.Versao;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class VersaoController {
    // atributos de classe
    private Repositorio repositorio = new Repositorio();
    private Versao versao;
    private File pastaCodeHub = new File(this.repositorio.getPath());
    private File pastaVersoes = new File(pastaCodeHub.getAbsolutePath() + Arquivo.resolvePath() + "versoes");
    private File pastaLixeira = new File(pastaCodeHub.getAbsolutePath() + Arquivo.resolvePath() + "lixeira");

    // métodos getters
    public Repositorio getRepositorio() {
        return repositorio;
    }

    public Versao getVersao() {
        return versao;
    }

    public File getPastaVersoes() {
        return pastaVersoes;
    }

    public File getPastaLixeira() {
        return pastaLixeira;
    }

    // construtor
    public VersaoController() {
        this.versao = new Versao();
    }

    public void versiona(String descricao) {
        /*
         * Cria e confirma (commit) uma nova versão
         * parametros: descrição (String) - identificação textual
         * retorn: void
         */
        threadVersiona(descricao);
    }

    private void threadVersiona(String comentario) {
        // cria threads para executar as funções do versionar (não dm)
        String emailCookie = Preferences.userRoot().get("emailUser", "");
        new Thread() {
            @Override
            public void run() {
                // seleciona o repositorio onde o usuario chamou o método iniciar()
                String hash = null;
                try {
                    hash = geraHashDaVersao();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                // acessa a pasta ou cria, se não existir:
                if (!getPastaVersoes().exists())
                    getPastaVersoes().mkdir();
                // constroi objeto a ser inserido no banco de dados, no formato adequado. ****
                // AINDA FALTA INSERIR O NOME,pois, não HÁ NO COOKIE
                getVersao().setUsuario(emailCookie);
                getVersao().setData(dataAtual());
                getVersao().setVersao(hash);
                getVersao().setComentario(comentario);

                // Definindo versão como versão atual
                getVersao().setVersaoAtual(true);

                // Removendo atributo de versão atual da versão anterior(antes de salvar a nova
                // versão no BD)
                atualizaVersaoAtual();

                // cria copia dos arquivos do repositório para o destino:
                if (criaCopiaArquivosRepositorio(hash)) {
                    // salva versão no banco de dados.
                    BancoDados.registraVersao(getVersao());
                }

                // limpa o container depois de versionar
                RepositorioController repositorioController = new RepositorioController();
                repositorioController.removeDoContainer(".");
            }
        }.start(); // executa thread, mesmo se a main travar ou for interrompida, será executado o
                   // versionamento.
    }

    private String geraHashDaVersao() throws NoSuchAlgorithmException {
        // gera hash com base em letras do alfabeto
        int index;
        String word;
        BigInteger hash;
        MessageDigest mensagem;
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder constroe = new StringBuilder(1016);

        for (int m = 0; m < 16; m++) {
            index = (int) (alpha.length() * Math.random());
            constroe.append(alpha.charAt(index));
        }

        // palavra aleatoria gerada pelos métodos acima:
        word = constroe.toString(); // palavra a ser transformada em hash
        mensagem = MessageDigest.getInstance("MD5"); // criptografia do tipo md5
        hash = new BigInteger(1, mensagem.digest(word.getBytes(StandardCharsets.UTF_8)));
        return hash.toString(); // retorna o hash numérico como texto
    }

    private ArrayList<String> conteudoRepositorio() {
        // le o conteudo do repositorio
        File arquivoContainerJson = new File(getRepositorio().getPath() + Arquivo.resolvePath()
                + "container" + Arquivo.resolvePath() + "container.json");

        // le container .json
        ArrayList<String> arquivosContainer = null;

        FileReader containerJson;
        try {
            containerJson = new FileReader(arquivoContainerJson);
            arquivosContainer = Arquivo.leContainerJson(containerJson); // armazena conteúdo após a leitura do container
        } catch (FileNotFoundException e) {
            System.exit(0);
        }

        return arquivosContainer;
    }

    private boolean criaCopiaArquivosRepositorio(String hash) {
        /*
         * cria cópia dos arquivos do repositorio e move para a pasta de versões
         * obs: O windows não permite copiar a pasta origem em si, por isso, é
         * necessário criar
         * uma nova e mover os arquivos.
         */

        // copia itens do repositorio para pasta versoes
        if (conteudoRepositorio() != null && !conteudoRepositorio().isEmpty()) {
            File destino = new File(getPastaVersoes().toString() + Arquivo.resolvePath() + hash);
            if (!destino.exists())
                destino.mkdir();

            for (String caminhos : conteudoRepositorio()) {
                File arquivoAtual = new File(caminhos);
                try {
                    if (Arquivo.resolvePath().equals("/")) {
                        Runtime.getRuntime().exec("cp -r " + arquivoAtual.getAbsolutePath() +
                                " " + destino.getAbsolutePath());
                    } else {
                        if (arquivoAtual.isFile()) {
                            Runtime.getRuntime().exec("Xcopy " + caminhos + " " + destino);
                        } else {
                            File auxiliar = new File(destino + Arquivo.resolvePath() + new File(caminhos).getName());
                            if (!auxiliar.exists())
                                auxiliar.mkdir(); // cria a pasta

                            try { // cria a cópia
                                Runtime.getRuntime().exec("Xcopy /E /I " + caminhos + " " + destino
                                        + Arquivo.resolvePath() + new File(caminhos).getName());
                            } catch (IOException e) {
                                System.exit(0);
                            }
                        }
                    }
                } catch (IOException e) {
                    System.exit(0);
                }
            }

            return true;
        }

        return false;
    }

    private String dataAtual() {
        // fornece data e hora, formatadas, para inserção no log.
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }

    // Atualiza versao com atributo de versão atual
    protected void atualizaVersaoAtual() {
        // Selecionando todas as versões do repositório atual
        File[] arquivoChaveVersoes = pastaVersoes.listFiles();
        if (arquivoChaveVersoes == null)
            return;

        for (File arquivo : arquivoChaveVersoes) {
            String chaveArquivo = arquivo.getName();

            Versao versao = BancoDados.consultaVersao(chaveArquivo);

            // Se o atributo versaoAtual for true, troca para false e da um update no banco
            // de dados
            if (versao != null && versao.getValorAtual()) {
                versao.setVersaoAtual(false);

                File caminhoVersao = new File(BancoDados.getTabelaVersoes()
                        + Arquivo.resolvePath() + chaveArquivo + ".json");

                // Fazendo update no BD
                Arquivo.escreveJson(caminhoVersao, versao);
                break;
            }
        }
    }

    public static void deleteDirectory(File file) {
        for (File subArquivo : file.listFiles()) {

            if (subArquivo.isDirectory()) {
                deleteDirectory(subArquivo);
            }

            subArquivo.delete();
        }
    }

    public void deletarVersao(String hashVersao) {
        // Origem da versao relativo a pasta versoes
        File origem = new File(getPastaVersoes() + Arquivo.resolvePath() + hashVersao);

        File lixeira = new File(getPastaLixeira().toString());
        // Destino da versao relativa a pasta lixeira
        File destino = new File(lixeira + Arquivo.resolvePath() + hashVersao);

        try {
            if (origem.exists() && !destino.exists()) {

                File[] versaoAntiga = lixeira.listFiles();
                deleteDirectory(versaoAntiga[0]);

                versaoAntiga[0].delete();

                // Files.move(source, target, options);
                Files.move(origem.toPath(), destino.toPath());
            } else {
                System.out.println("A versao informada nao existe");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
