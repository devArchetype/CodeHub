package controllers;

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

    //testes unitarios adicionaAoContainer
    @Test
    void adicionaAoContainerFalha() {
        File repo = new File(repositorioController.getRepositorio().getPath());
        repo.delete();
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
}
