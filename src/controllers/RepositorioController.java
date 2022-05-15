package controllers;

import java.io.File;

public class RepositorioController {

    public static void iniciaRepositorio() {

        // Selecionando repositório onde o usuario chamou o método iniciar()
        File pathRepositorioAtual = new File(".");

        File pastaCodeHub = new File(pathRepositorioAtual.getAbsolutePath() + ".CodeHub");

        if(!pastaCodeHub.exists()) {
            pastaCodeHub.mkdir();
            System.out.println("CodeHub inicializado com sucesso!");
        } else {
            System.out.println("O CodeHub ja foi inicializado!");
        }
    }
}
