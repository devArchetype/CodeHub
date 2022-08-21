package views;

import ferramentas.Cores;

public class UsuarioView {

    public boolean imprimirComandos() {

        // lista os comandos de ajuda
        System.out.println(Cores.getCor("amarelo") + "Precisa de ajuda? Podemos te ajudar! \n"
                + "\nLista de comandos disponiveis: \n");

        // chamada de mensagens de ajuda
        System.out.println("--ajuda : exibe janela com todos os comandos\n");
        System.out.println("--registrar : registra um novo usuario\n");
        System.out.println("--acessar : acessa conta existente\n");
        System.out.println("--sair : encerra sessao\n");
        System.out.println("--iniciar : inicia um repositorio local\n");
        System.out.println("--adicionar [parâmetro]: adiciona item(s) ao container \n\tparâmetro: \n\t '.' adiciona TODO conteúdo do container \n\t 'nome do arquivo' adiciona o arquivo específico do container\n");
        System.out.println("--exibir : exibe o conteúdo do container\n");
        System.out.println("--remover [parâmetro]: remove item(s) do container \n\tparâmetro: \n\t '.' remove TODO conteúdo do container \n\t 'nome do arquivo' remove o arquivo específico do container\n");
        System.out.println("--versionar [comentário] : cria uma nova versão\n");
        System.out.println("--voltar [comentário] : volta a versão determinada pelo usuario\n");
        System.out.println("--apagar [hash] : excluí totalmente uma determinada versão de acordo com seu hash\n");
        System.out.println("--apagar -f [hash] : excluí uma determinada versão de acordo com seu hash, mas mantem o seu backup ate um outro for feito\n");
        System.out.println("--container : exibe o conteudo do container\n");
        System.out.println("--restaurar : restaura o backup salvo de uma versao removida\n");
        // - ultima

        return true;
    }

}
