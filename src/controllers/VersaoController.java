package controllers;

import bancodados.BancoDados;
import ferramentas.Arquivo;
import models.Repositorio;
import models.Versao;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.prefs.Preferences;

/*
FAZ CONTROLE DAS VERSÕES
OS ARQUIVOS MODIFICADOS ESTARÃO CONTIDOS NO REPOSITÓRIO .CodeHub
E NO BANCO DE DADOS.
*/

public class VersaoController {
    // atributos de classe
    private Repositorio repositorio = new Repositorio(
            BancoDados.consultaUsuario(Preferences.userRoot().get("emailUser", "")));
    private Versao versao;
    private File pastaCodeHub = new File(this.repositorio.getPath());
    private File pastaVersoes = new File(pastaCodeHub.getAbsolutePath() + Arquivo.resolvePath() + "versoes");

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
        // cria threads para executar as funções do versionar
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

                // salva versão no banco de dados.
                BancoDados.registraVersao(getVersao());
                // define o path até o json no banco de dados
                File caminhoParaBanco = criaPastaNoBanco();
                File versao_ = new File(caminhoParaBanco + Arquivo.resolvePath() + hash + ".json");
                // cria copia dos arquivos do repositório para o destino:
                criaCopiaArquivosRepositorio(hash);
                // escreve no banco detalhes sobre o versionamento :
                if (!versao_.exists()) {
                    try {
                        versao_.createNewFile();
                        Arquivo.escreveJson(versao_, getVersao());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
        File arquivoContainerJson = new File(getRepositorio().getPath() + Arquivo.resolvePath() + "container"
                + Arquivo.resolvePath() + "container.json");
        // le container .json
        ArrayList<String> arquivosContainer = null;
        FileReader containerJson = null;
        try {
            containerJson = new FileReader(arquivoContainerJson);
            arquivosContainer = Arquivo.leContainerJson(containerJson); // armazena conteúdo após a leitura do container
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return arquivosContainer;

    }

    private void criaCopiaArquivosRepositorio(String hash) {
        /*
         * cria cópia dos arquivos do repositorio e move para a pasta de versões
         * obs: O windows não permite copiar a pasta origem em si, por isso, é
         * necessário criar
         * uma nova e mover os arquivos.
         */
        File destino = new File(getPastaVersoes().toString() + Arquivo.resolvePath() + hash);
        if (!destino.exists())
            destino.mkdir();
        File auxiliar;
        // copia itens do repositorio para pasta versoes
        if (conteudoRepositorio() != null) {
            for (String caminhos : conteudoRepositorio()) {
                if (new File(caminhos).isFile()) { // se for arquivo, gera um arquivo no destino e escreve nele
                    try {
                        if (Arquivo.resolvePath().equals("/"))
                            Runtime.getRuntime().exec("cp " + caminhos + " " + destino); // não testado no linux
                        else
                            Runtime.getRuntime().exec("Xcopy " + caminhos + " " + destino);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else { // se não for arquivo, cria uma pasta e move os itens da origem para o destino
                    auxiliar = new File(destino + Arquivo.resolvePath() + new File(caminhos).getName());
                    if (!auxiliar.exists())
                        auxiliar.mkdir(); // cria a pasta
                    try { // cria a cópia
                        if (Arquivo.resolvePath().equals("/"))
                            Runtime.getRuntime().exec("cp -R" + caminhos + " " + destino + Arquivo.resolvePath()
                                    + new File(caminhos).getName()); // não testado no linux
                        else
                            Runtime.getRuntime().exec("Xcopy /E /I " + caminhos + " " + destino + Arquivo.resolvePath()
                                    + new File(caminhos).getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private File criaPastaNoBanco() {
        // cria pasta de versao no banco de dados
        BancoDados bd = new BancoDados();
        // se não existir, cria a pasta e retorna o path:
        bd.getTabelaVersoes().mkdir();
        return bd.getTabelaVersoes();
    }

    private String dataAtual() {
        // fornece data e hora, formatadas, para inserção no log.
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }

    // Atualiza versao com atributo de versão atual
    private void atualizaVersaoAtual() {
        // Selecionando todas as versões do repositório atual
        File[] arquivoChaveVersoes = pastaVersoes.listFiles();

        for (File arquivo : arquivoChaveVersoes) {
            String chaveArquivo = arquivo.getName();

            Versao versao = BancoDados.consultaVersao(chaveArquivo);

            // Se o atributo versaoAtual for true, troca para false e da um update no banco de dados
            if (versao.getValorAtual()) {
                versao.setVersaoAtual(false);

                File caminhoVersao = new File(
                        BancoDados.getTabelaVersoes() + Arquivo.resolvePath() + chaveArquivo + ".json");

                // Fazendo update no BD
                Arquivo.escreveJson(caminhoVersao, versao);
                break;
            }
        }
    }

    /*
     * METODO PARA IMPLEMENTACAO FUTURA
     * 
     * -----------------------------------------------------------------------------
     * ----------------------------------------------
     * private boolean verificaAlteracoesNosArquivos(String local_arquivo, String
     * ultimo_hash) throws FileNotFoundException{
     * // verifica se houve alterações nos arquivos.
     * // implementacao futura
     * // não descrito,pois, ainda não será implementado
     * MessageDigest digest = null;
     * try { digest = MessageDigest.getInstance("MD5");
     * } catch (NoSuchAlgorithmException e) { e.printStackTrace();}
     * File arquivo = new File(local_arquivo);
     * InputStream is = null;
     * try { is = new FileInputStream(arquivo); // b9f3430ccc11413aa4c68ee87675f35f
     * } catch (FileNotFoundException e) { }
     * byte[] buffer = new byte[8192];
     * int leitura = 0;
     * try {
     * while( (leitura = is.read(buffer)) > 0) digest.update(buffer, 0, leitura); //
     * le o arquivo até o final
     * byte[] md5sum = digest.digest();
     * BigInteger inteiro = new BigInteger(1, md5sum);
     * String hash = inteiro.toString(16);
     * if(hash.equals(ultimo_hash)) return true;
     * } catch(IOException e) { throw new RuntimeException("CODEHUB EXCEPTION", e);
     * }finally {
     * try { is.close(); }
     * catch(IOException e) { throw new RuntimeException("CODEHUB EXCEPTION", e); }}
     * return false;
     * }
     * -----------------------------------------------------------------------------
     * ----------------------------------------------
     */
}
