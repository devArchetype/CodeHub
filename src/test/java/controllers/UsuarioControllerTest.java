package controllers;

import models.Usuario;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioControllerTest {

    private static UsuarioController usuarioController;

    @BeforeAll
    public static void init() {
        usuarioController = new UsuarioController();
    }

    @AfterAll
    public static void finish() {
        usuarioController.sairConta();
    }

    //testes unitarios registraUsuario
    @Test
    void registraUsuarioSucesso() {
        Usuario usuario = new Usuario("BoB", "batata123@gmail.com", "PaoDeBatata");
        assertTrue(usuarioController.registraUsuario(usuario));
    }

    @Test
    void registraUsuarioFalha() {
        Usuario usuario = new Usuario("BobEsponja", "batata1236@gmail.com", "HamburguerDeSiri");
        usuarioController.registraUsuario(usuario);
        assertFalse(usuarioController.registraUsuario(usuario));
    }

    //teste unitario chamarAjuda (existe somente a possibilidade de sucesso ao exibir ajuda)
    @Test
    void chamarAjudaSucesso() {
        assertTrue(usuarioController.chamarAjuda());
    }

    //Testes unitarios logar em uma conta
    @Test
    void acessaContaScucesso() {
        Usuario usuario = new Usuario("BoB", "batata12w3@gmail.com", "PaoDeBatata");
        usuarioController.registraUsuario(usuario);
        assertTrue(usuarioController.acessaConta(usuario));
    }
    @Test
    void acessaContaFalha() {
        Usuario usuario = new Usuario("BobEsponja", "batata1236@gmail.com", "PaoDeBatata");
        usuarioController.registraUsuario(usuario);
        usuario.setSenha("PaoDeBatata1");
        assertFalse(usuarioController.acessaConta(usuario));
    }

}