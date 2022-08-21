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

}