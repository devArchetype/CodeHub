package network;

import bancodados.BancoDados;
import models.Repositorio;
import ferramentas.*;
import controllers.VersaoController;
import models.Versao;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.prefs.Preferences;



public class UpDados {
         /* Classe que insere os arquivos no github
           ► Requisitos do push:
             • Estar configurado o git na máquina cliente(caching de credenciais e token de acesso)
             • Ter acesso a conta CodeHub (ter user e email) -> conversar com adm
             • Download do git em: https://git-scm.com/downloads
             • https://docs.github.com/pt/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token
             • https://docs.github.com/pt/get-started/getting-started-with-git/caching-your-github-credentials-in-git
        */


        // atributos
        private final Repositorio repositorio = new Repositorio(BancoDados.consultaUsuario(Preferences.userRoot().get("emailUser", "")));
        private final File pastaCodeHub = new File(this.repositorio.getPath());
        private final File pastaVersoes = new File(pastaCodeHub.getAbsolutePath() + Arquivo.resolvePath() + "versoes");
        static final private Runtime run = Runtime.getRuntime();
        static private Process pro;
        static private BufferedReader read;
    public String resp;
    private Versao versao;
        private String descricao = new VersaoController()._descricao;

        // método get
        public File getPastaVersoes() {
            return pastaVersoes;
        }

        private void instrucoesDeEnvioParaOusuario(String commit){
            // apenas para o usuário ver os comandos q são utilizados:
            System.out.println("CodeHub comentario -M '" + commit + "'");
            System.out.println("CodeHub ramo - M main(principal)");
            System.out.println("CodeHub adicionar origem remota https://github.com/CodeHub-IFMG/primeiro_up.git");
            System.out.println("CodeHub enviar -u para a origem main(principal)");
        }

        private String[] comandos(String commit, File destino){
            // comandos para realizar o push no github
            String[] comando = {
                    "cd " + destino,
                    "git init",
                    "git remote remove origin",
                    "git add .",
                    "git commit -m" + commit,
                    "git branch -M main",
                    "git remote add origin https://github.com/CodeHub-IFMG/primeiro_up.git",
                    "git push -u origin main"
            };
            return comando;
        }

        public void enviarParaGitHub(){
            // envia arquivo para o github,mas, antes,pede para selecionar a pasta e definir o commit
            VersaoController versao = new VersaoController();
            String hash = versao.hash_name;
            File destino = new File(getPastaVersoes().toString() + Arquivo.resolvePath() + hash + Arquivo.resolvePath());
            String respostas;

            //File destino = new File("C:\\C\\avulso\\Source\\"); (DEBUG)
            if(destino.exists()){
                String[] cmds = comandos(descricao, destino);

                try {
                    ProcessBuilder processo_terminal = new ProcessBuilder("cmd", "/c",
                            String.join("& ", cmds));

                    processo_terminal.redirectErrorStream(true);
                    instrucoesDeEnvioParaOusuario(descricao);
                    Process processo_iniciado = processo_terminal.start();

                    BufferedReader saida_console = new BufferedReader(
                            new InputStreamReader(
                                    processo_iniciado.getInputStream()
                            )
                    );

                    // le, até terminar a saída do console
                    while (true) {
                        respostas = saida_console.readLine();
                        if (respostas == null) {
                            break;
                        }
                        System.out.println(respostas);  //exibe na saída padrão a resposta do terminal/console
                    }

                } catch(Exception e) {
                    // erros podem estar relacionados a problemas na saída, não há má gerência dos comandos git
                    System.err.println("ERRO não versionado");
                 }
                }
            }
}