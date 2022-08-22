package controllers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class RepositorioControllerTest {

    private static RepositorioController repositorioController;
     private static VersaoController versaoController;
    @BeforeAll
    public static void init() {
        repositorioController = new RepositorioController();
        versaoController = new VersaoController();
    }

    private static void deleteDirectory(File file) {
        if (file.listFiles() != null && file.listFiles().length > 0) {
            for (File subArquivo : file.listFiles()) {

                if (subArquivo.isDirectory()) {
                    deleteDirectory(subArquivo);
                }

                subArquivo.delete();
            }

            file.delete();
        }
    }

    @AfterAll
    public static void finish() {
        File pastaCodeHub = new File("./.CodeHub");
        deleteDirectory(pastaCodeHub);
    }


    //testes unitarios adicionaAoContainer
    @Test
    void adicionaAoContainerFalha() {
        assertFalse(repositorioController.adicionaAoContainer("."));
    }

    @Test
    void adicionaAoContainerSucesso() {
        repositorioController.iniciaRepositorio();
        assertTrue(repositorioController.adicionaAoContainer("."));
    }

    //testes unitarios removeDoContainer
    @Test
    void removeDoContainerFalha() {
        repositorioController.iniciaRepositorio();
        repositorioController.adicionaAoContainer(".");
        assertFalse(repositorioController.removeDoContainer("asd16@!#54df1"));
    }

    @Test
    void removeDoContainerSucesso() {
        repositorioController.iniciaRepositorio();
        repositorioController.adicionaAoContainer(".");
        assertTrue(repositorioController.removeDoContainer("."));
    }

    //testes unitarios listaArquivosDoContainer
    @Test
    void listaArquivosDoContainerVazio() {
        repositorioController.iniciaRepositorio();
        assertFalse(repositorioController.listaArquivosDoContainer());
    }

    @Test
    void listaArquivosDoContainerCheio() {
        repositorioController.iniciaRepositorio();
        repositorioController.adicionaAoContainer(".");
        assertTrue(repositorioController.listaArquivosDoContainer());
    }
    @Test
    void removerversaoSucesso() {
        repositorioController.iniciaRepositorio();
        repositorioController.adicionaAoContainer(".");
        versaoController.versiona("Comentario");
        assertTrue(repositorioController.deletaVersao(versaoController.getVersao().getChavePrimaria()));
    }

    @Test
    void removerversaoFalha() {
        repositorioController.iniciaRepositorio();
        repositorioController.adicionaAoContainer(".");
        versaoController.versiona("Comentario");
        assertFalse(repositorioController.deletaVersao("83"));
    }

    @Test
    void criaPastaCodeHubNoRepositorioSucesso() {
        boolean pastaFoiCriada = repositorioController.iniciaRepositorio();
        File pastaCodeHub = new File("./.CodeHub");
        assertTrue(pastaCodeHub.exists());
        assertTrue(pastaFoiCriada);
    }
    @Test
    void criaPastasAuxiliaresNaPastaRepositorioSucesso() {
        File pastaCodeHub = new File("./.CodeHub");
        if(!pastaCodeHub.exists()) repositorioController.iniciaRepositorio();

        File pastaVersoes = new File("./.CodeHub/versoes");
        File pastaContainer = new File("./.CodeHub/container");
        File pastaLixeira = new File("./.CodeHub/lixeira");

        assertTrue(pastaVersoes.exists());
        assertTrue(pastaContainer.exists());
        assertTrue(pastaLixeira.exists());
    }

    @Test
    void iniciaRepositorioFalha() {
        repositorioController.iniciaRepositorio();
        boolean pastaJaExiste = repositorioController.iniciaRepositorio();
        assertFalse(pastaJaExiste);
    }

    //Testes unitarios remover versão
    @Test
    void voltarVersaoSucesso() {
        repositorioController.iniciaRepositorio();
        repositorioController.adicionaAoContainer(".");
        versaoController.versiona("Comentario");
        assertTrue(repositorioController.deletaVersao(versaoController.getVersao().getChavePrimaria()));
    }
    @Test
    void voltarVersaoFalha() {
        repositorioController.iniciaRepositorio();
        repositorioController.adicionaAoContainer(".");
        versaoController.versiona("Comentario");
        assertFalse(repositorioController.deletaVersao("83"));
    }
}
