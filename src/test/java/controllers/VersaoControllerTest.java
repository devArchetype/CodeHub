package controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VersaoControllerTest {

    private static RepositorioController repositorioController;
    private static VersaoController versaoController;

    @BeforeAll
    public static void init() {
        repositorioController = new RepositorioController();
        versaoController = new VersaoController();
    }

    @Test
    void recuperarVersaoSucesso() {
        repositorioController.iniciaRepositorio();
        repositorioController.adicionaAoContainer(".");
        versaoController.versiona("comentario");
        versaoController.deletarVersao(versaoController.getVersao().getChavePrimaria());
        assertTrue(versaoController.recuperarVersao());
    }

    @Test
    void recuperarVersaoFalha() {
        repositorioController.iniciaRepositorio();
        repositorioController.adicionaAoContainer(".");
        versaoController.versiona("comentario");
        assertFalse(versaoController.recuperarVersao());
    }

}