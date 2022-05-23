import controllers.RepositorioController;
import controllers.UsuarioController;
import controllers.VersaoController;
import views.RepositorioView;

/**
 *
 * @author CODEHUB
 */
public class Main {
    public static void main(String[] args) {
        final String ANSI_YELLOW = "\u001B[33m";
        String logo = "  ____               _          _   _           _     \n" +
                "  / ___|   ___     __| |   ___  | | | |  _   _  | |__  \n" +
                " | |      / _ \\   / _` |  / _ \\ | |_| | | | | | | '_ \\ \n" +
                " | |___  | (_) | | (_| | |  __/ |  _  | | |_| | | |_) |\n" +
                "  \\____|  \\___/   \\__,_|  \\___| |_| |_|  \\__,_| |_.__/ \n" +
                "                                                     ";
        System.out.println(ANSI_YELLOW + logo);
        System.out.println("\033[3m  Frase de  Efeito\033[0m");
        System.out.println(ANSI_YELLOW + "  Digite: 'CodeHub --ajuda' para listar todos os comandos\n");

        // controllers
        UsuarioController usuarioController = new UsuarioController();
        RepositorioController repositorioController = new RepositorioController();
        VersaoController versaoController = new VersaoController();
        // central de operacoes
        try {
            // verificacao de login meramente criada pra testes
            if (usuarioController.estaLogado()) {
                switch (args[0]) {
                    case "--ajudar" -> {
                        usuarioController.chamarAjuda();
                    }
                    case "--sair" -> {
                        usuarioController.sairConta();
                    }
                    case "--iniciar" -> {
                        repositorioController.iniciaRepositorio();
                    }
                    case "--adicionar" -> {
                        repositorioController.adicionaAoContainer(args[1]);
                    }
                    case "--registrar", "--acessar" -> {
                        System.out.println("Usuario logado!");
                    }
                    case "--versionar" -> {
                        versaoController.versiona(args[1]);
                    }
                    case "--historico" -> {
                        repositorioController.listarHistorico();
                    }
                }
            } else {
                switch (args[0]) {
                    case "--ajudar" -> {
                        usuarioController.chamarAjuda();
                    }
                    case "--registrar" -> {
                        usuarioController.registraUsuario();
                    }
                    case "--acessar" -> {
                        usuarioController.acessaConta();
                    }
                    default -> {
                        System.out.println("Faca login antes de continuar!");
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            usuarioController.chamarAjuda();
        }

        repositorioController.listarHistorico();

    }
}
