package views;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import bancodados.BancoDados;
import ferramentas.Arquivo;
import ferramentas.Cores;
import models.Repositorio;
import models.Usuario;
import models.Versao;

public class RepositorioView {

    public void listarHistorico(Repositorio repositorio) {
        // Acessando diretório com as versões
        File diretorioVersoes = new File(repositorio.getPath() + Arquivo.resolvePath() + "versoes");

        // Array criado com todos os arquivos de versão
        File[] arquivoChaveVersoes = diretorioVersoes.listFiles();

        if(arquivoChaveVersoes == null || arquivoChaveVersoes.length <= 0) {
            System.out.println("O repositório atual não possui versões.");
            return;
        }

        // Veto com o objeto Versao de cada versão
        Vector<Versao> objetosVersoes = new Vector<>();

        Set<Long> datasOrdenadasLong = new TreeSet<Long>();
        SimpleDateFormat formatacaoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // Adicionando cada objeto da versão em um array
        for (File arquivoChaveVersao : arquivoChaveVersoes) {
            Versao versao = BancoDados.consultaVersao(arquivoChaveVersao.getName());
            objetosVersoes.add(versao);

            String dataVersao = versao.getData();
            try {
                // Pegando valor da data em String e passando para milisegundos e adicionando em
                // um array de Long
                datasOrdenadasLong.add(formatacaoData.parse(dataVersao).getTime());
            } catch (ParseException e) {
                System.exit(0);
            }
        }

        Vector<String> datasOrdenadasString = new Vector<>();

        for (Long data : datasOrdenadasLong) {
            // Convertendo data em milisegundos para String
            datasOrdenadasString.add(formatacaoData.format(data));
        }

        // Caso queia inverter a ordem do histórico, deixando em ordem crescente(novo -> velho)
        // Collections.reverse(datasOrdenadasString);

        // Pegando dados da versão que contem aquela data
        while (!objetosVersoes.isEmpty()) {
            // Selecionando primeira versao
            Versao versao = objetosVersoes.remove(0);

            // Buscando dados do usuário no banco de dados através do email
            String emailUsuario = versao.getUsuario();
            Usuario usuario = BancoDados.consultaUsuario(emailUsuario);

            // Copiando valor da primeira data
            String data = datasOrdenadasString.get(0);
            String indicadorVersaoAtual = "";

            // Comparando data copiada com data da versão
            // Se as datas forem identicas, exibe a versão e remove a data da fila
            if (versao.getData().equals(data)) {

                if (versao.getValorAtual()) {
                    indicadorVersaoAtual = Cores.getCor("ciano") + " [Versao Atual]";
                }

                System.out.println(
                        Cores.getCor("amarelo") + "Versao: " + versao.getChavePrimaria() + indicadorVersaoAtual + "\n" +
                                "Comentario: " + versao.getComentario() + "\n" +
                                "Autor: " + usuario.getNome() + " <" + usuario.getEmail() + ">" + "\n" +
                                "Data: " + versao.getData() + "\n"
                );

                datasOrdenadasString.remove(0);
            } else {

                // Caso não sejam iguais, adiciona a versão ao final da fila
                objetosVersoes.add(versao);
            }
        }
    }

    //montando a lista de path/arquivos dentro do repositorio container
    public void exibeArquivosContainer (ArrayList<String> container,String path){
        //verifica a existencia de itens no container e os exibe na tela
        if (container == null || container.size() <= 0) {
            System.out.println(Cores.getCor("vermelho") + "O container atual nao possui itens!");
            return;
        }

        path = path.replace(".CodeHub","");

        //impressao dos paths do container
        System.out.println(Cores.getCor("amarelo") + "O container atual contem os seguintes itens: \n");

        System.out.println(Cores.getCor("amarelo") + "[Arquivos]");

        //percorre o array container que contem os paths de arquivos
        for (int i = 0; i < container.size(); i++) {
            String arquivoAtual = container.get(i);
            File arquivo = new File(arquivoAtual);

            //adiciona flag de arquivo ou diretorio em cada item do container
            if(arquivo.isFile()){
                arquivoAtual = arquivoAtual.replace(path,"");
                System.out.println("\t" + Cores.getCor("verde") + arquivoAtual);
            }
        }

        System.out.println(" ");
        System.out.println(Cores.getCor("amarelo") + "[Diretorios]");

        //percorre o array container que contem os paths de pastas/diretorios
        for (int i = 0; i < container.size(); i++) {
            String arquivoAtual = container.get(i);
            File arquivo = new File(arquivoAtual);

            //adiciona flag de arquivo ou diretorio em cada item do container
            if(arquivo.isDirectory()) {
                arquivoAtual = arquivoAtual.replace(path,"");
                System.out.println("\t" + Cores.getCor("verde") + arquivoAtual);
            }
        }
    }

}


