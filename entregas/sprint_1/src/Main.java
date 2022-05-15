import controllers.RepositorioController;
import controllers.UsuarioController;
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
                "                                                     " ;
        System.out.println(ANSI_YELLOW + logo);
        System.out.println("\033[3m  Frase de  Efeito\033[0m");
        System.out.println(ANSI_YELLOW + "  Digite: 'CodeHub --ajuda' para listar todos os comandos\n");

        UsuarioController usuario = new UsuarioController();
        RepositorioController repositorio = new RepositorioController();

        try {
            switch (args[0]) {
                case "--ajudar" ->  {
                    usuario.chamarAjuda();
                }
                case  "--registrar" -> {
                    usuario.registrarUsuario();
                }
                case  "--acessar" -> {
                    usuario.acessaConta();
                }
                case  "--sair" -> {
                    usuario.sairConta();
                }
                case  "--iniciar" -> {
                    repositorio.iniciaRepositorio();
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            usuario.chamarAjuda();
        }
    }

}
