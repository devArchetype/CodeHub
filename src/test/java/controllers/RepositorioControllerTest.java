package controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class RepositorioControllerTest {

    private static RepositorioController repositorioController;

    @BeforeAll
    public static void init() {
        repositorioController = new RepositorioController();
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
}