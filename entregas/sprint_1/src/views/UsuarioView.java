package views;

public class UsuarioView {
    
    public void imprimirComandos () {
        
        //lista os comandos de ajuda
        System.out.println("Precisa de ajuda? Podemos te ajudar! :) \n" + "\nLista de comandos disponiveis: \n");

        //chamada de mensagens de ajuda
        System.out.println("--ajuda : exibe janela com todos os comandos\n");
        System.out.println("--registrar : registra um novo usuario\n");
        System.out.println("--acessar : acessa conta existente\n");
        System.out.println("--sair : encerra sessao\n");
        System.out.println("--iniciar : inicia um repositotio local\n");
//        System.out.println("--adicionar [parâmetro]: adiciona item(s) ao container \n\tparâmetro: \n\t '.' adiciona TODO conteúdo do container \n\t 'nome do arquivo' adiciona o arquivo específico do container\n");
//        System.out.println("--exibir : exibe o conteúdo do container\n");
//        System.out.println("--remover [parâmetro]: remove item(s) do container \n\tparâmetro: \n\t '.' remove TODO conteúdo do container \n\t 'nome do arquivo' remove o arquivo específico do container\n");
//        System.out.println("--versionar [comentário] : cria uma nova versão\n");
//        System.out.println("--excluir [id] : excluí uma determinada versão de acordo com seu id\n");
    }
}
