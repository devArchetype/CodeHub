import controllers.UsuarioController;
/**
 *
 * @author CODEHUB
 */
public class Main {
    public static void main(String[] args) {
        UsuarioController.chamarAjuda();
        UsuarioController.registrarUsuario();
        UsuarioController.acessaConta();
        UsuarioController.sairConta();
    }
    
//    public static void main(String[] args){
//        final String ANSI_YELLOW = "\u001B[33m";
//        String logo = "  ____               _          _   _           _     \n" +
//                "  / ___|   ___     __| |   ___  | | | |  _   _  | |__  \n" +
//                " | |      / _ \\   / _` |  / _ \\ | |_| | | | | | | '_ \\ \n" +
//                " | |___  | (_) | | (_| | |  __/ |  _  | | |_| | | |_) |\n" +
//                "  \\____|  \\___/   \\__,_|  \\___| |_| |_|  \\__,_| |_.__/ \n" +
//                "                                                     " ;
//        System.out.println(ANSI_YELLOW + logo);
//        System.out.println("\033[3m  Frase de  Efeito\033[0m " + Emojis.loboGuara());
//        System.out.println(ANSI_YELLOW + "  Digite: 'CodeHub --ajuda' para listar todos os comandos\n");
//
//
//        //String comando = "--sair";
//        try {
//            switch (args[0]) {
//                case "--ajuda" ->  {
//                    UsuarioController.ajuda();
//                }
//                case  "--registrar" -> {
//                    UsuarioController.registrar();
//                }
//                case "--acessar" -> {
//                    UsuarioController.acessar();
//                }
//                case "--sair" -> {
//                    UsuarioController.sair();
//                }
//                case "--iniciar" -> {
//                    UsuarioController.iniciar();
//                }
//                case "--adicionar" -> {
//                    RepositorioController.adicionar_ao_container(args[1]);
//                }
//                case "--exibir" -> {
//                    RepositorioController.exibir_container();
//                }
//                case "--remover" -> {
//                    RepositorioController.remover_item_container(args[1]);
//                }
//                case "--versionar" -> {
//                    VersaoController.versionar(args[1]);
//                }
//                case "--excluir" -> {
//                    VersaoController.remover_versao(args[1]);
//                }
//            }
//        } catch (ArrayIndexOutOfBoundsException e) {
//            UsuarioController.ajuda();
//        }
//
//    }
}
