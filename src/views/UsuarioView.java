package views;

import ferramentas.Cores;

public class UsuarioView {

    public void imprimirComandos() {

        // lista os comandos de ajuda
        System.out.println(Cores.getCor("amarelo") + "Precisa de ajuda? Podemos te ajudar! \n"
                + "\nLista de comandos disponiveis: \n");

        // chamada de mensagens de ajuda
        System.out.println("--ajudar : exibe janela com todos os comandos\n");
        System.out.println("--registrar : registra um novo usuario\n");
        System.out.println("--acessar : acessa conta existente\n");
        System.out.println("--sair : encerra sessao\n");
        System.out.println("--iniciar : inicia um repositorio local\n");
        System.out.println("--adicionar [parâmetro]: adiciona item(s) ao container \n\tparâmetro: \n\t '.' adiciona TODO conteúdo do container \n\t 'nome do arquivo' adiciona o arquivo específico do container\n");
        System.out.println("--remover [parâmetro]: remove item(s) do container \n\tparâmetro: \n\t '.' remove TODO conteúdo do container \n\t 'nome do arquivo' remove o arquivo específico do container\n");
        System.out.println("--versionar [comentário] : cria uma nova versão\n");
        System.out.println("--historico : lista todas as versoes existentes no repositorio em ordem acendente conforme data\n");
        System.out.println("--voltar [comentário] : volta a versão determinada pelo usuario\n");
        System.out.println("--container : exibe o conteudo do container\n");

//      System.out.println("--excluir [id] : excluí uma determinada versão de acordo com seu id\n");
    }

}
