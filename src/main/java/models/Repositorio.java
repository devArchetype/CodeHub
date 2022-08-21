package models;

import bancodados.BancoDados;
import ferramentas.Arquivo;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class Repositorio {

    private String chavePrimaria; // servira como nome do arquivo .json referente
    private Usuario administrador;
    private String path;
    private String data;
    private ArrayList<Usuario> contribuidores;

    // construtor para a criacao de um novo repositorio
    public Repositorio(Usuario administrador) {
        this.chavePrimaria = UUID.randomUUID().toString().replaceAll("-", ""); // hash aleatorio
        this.administrador = administrador;
        this.path = this.iniciaPath(true);
        this.data = this.iniciaData();
        this.contribuidores = new ArrayList<>();
    }

    public Repositorio() {
        this.path = this.iniciaPath(false);
    }

    /*
     * inicia o path do repositorio de acordo com a necessidade
     * quando for iniciar um novo repositorio, o caminho do diretorio atual e
     * incrementado
     * quando não for iniciar um novo repositorio, o path de algum ja existente e
     * procurado para ser usado
     */
    private String iniciaPath(boolean criarNovoRepo) {
        String diretorioAtual = System.getProperty("user.dir"); // diretorio onde o programa esta rodado

        // diretorio pai, onde sera procurado, entre seus filhos, o repositorio .CodeHub
        File diretorioPai = new File(diretorioAtual);
        // variavel para a possivel ocorrencia de repositorio .CodeHub
        File repositorioCodeHub = null;

        // procurando por um diretorio .CodeHub ate chegar na raiz de todos as pastas do
        // pc, ou encontrar um
        while (diretorioPai != null && repositorioCodeHub == null) {
            File[] arquivosVerificar = diretorioPai.listFiles(); // todos os arquivos dentro de um diretorio pai

            if (arquivosVerificar != null) {
                for (File arquivoVerificar : arquivosVerificar) {
                    if (arquivoVerificar.getName().equals(".CodeHub")) {
                        // achou
                        repositorioCodeHub = arquivoVerificar;
                        break;
                    }
                }
            }

            // diretorio pai, agora um nivel acima do anterior
            diretorioPai = diretorioPai.getParentFile();
        }

        // decisao de qual path usar
        if (repositorioCodeHub == null || criarNovoRepo) return diretorioAtual + Arquivo.resolvePath() + ".CodeHub";
        else return repositorioCodeHub.getAbsolutePath();
    }

    // inicia a data atual em formato de string
    private String iniciaData() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter template = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return agora.format(template);
    }

    // registra o objeto vigente no BD
    public boolean registraDados() {
        return BancoDados.registraRepositorio(this);
    }

    public String getChavePrimaria() {
        return chavePrimaria;
    }

    public void setChavePrimaria(String chavePrimaria) {
        this.chavePrimaria = chavePrimaria;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Usuario getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Usuario administrador) {
        this.administrador = administrador;
    }

    public ArrayList<Usuario> getContribuidores() {
        return contribuidores;
    }

    public void setContribuidores(ArrayList<Usuario> contribuidores) {
        this.contribuidores = contribuidores;
    }

}
