package controllers;

import models.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioControllerTest {

    private static UsuarioController usuarioController;

    @BeforeAll
    public static void init() {
        usuarioController = new UsuarioController();
    }

    @Test
    void registraUsuarioSucesso() {
        Usuario usuario = new Usuario("BoB", "batata123@gmail.com", "PaoDeBatata");
        assertTrue(usuarioController.registraUsuario(usuario));
    }

    @Test
    void registraUsuarioFalha() {
        Usuario usuario = new Usuario("BoB", "batata123@gmail.com", "PaoDeBatata");
        usuarioController.registraUsuario(usuario);
        assertFalse(usuarioController.registraUsuario(usuario));
    }

    //teste unitario chamarAjuda (existe somente a possibilidade de sucesso ao exibir ajuda)
    @Test
    void chamarAjudaSucesso() {
        assertTrue(usuarioController.chamarAjuda());
    }

}