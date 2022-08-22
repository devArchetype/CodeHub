
package network;

import bancodados.BancoDados;
import ferramentas.Arquivo;
import models.Repositorio;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.*;

class UpDadosTest {

    private UpDados dados;
    private final Repositorio repositorio = new Repositorio(BancoDados.consultaUsuario(Preferences.userRoot().get("emailUser", "")));
    private final File pastaCodeHub = new File(this.repositorio.getPath());
    private final File pastaVersoes = new File(pastaCodeHub.getAbsolutePath() + Arquivo.resolvePath() + "versoes");

    @BeforeEach
    void setUp() {
        this.dados = new UpDados();;
    }

    @Test
    void getPastaVersoes() {
        //testa se o método possui o endereço correspondente ao que da versão q será enviada
        assertEquals(this.dados.getPastaVersoes(), pastaVersoes);
    }

    @Test
    void enviarParaGitHub() {
        String[] comando = {
                "cd " + "C:\\C\\avulso\\Source\\",
                "git init",
                "git remote remove origin",
                "git add .",
                "git commit -m" + "commit qualquer",
                "git branch -M main",
                "git remote add origin https://github.com/CodeHub-IFMG/primeiro_up.git",
                "git push -u origin main"
        };
        String respostas;

        File destino = new File("C:\\C\\avulso\\Source\\");// (DEBUG)
        if(destino.exists()){
            String[] cmds = comando;

            try {
                ProcessBuilder processo_terminal = new ProcessBuilder("cmd", "/c",
                        String.join("& ", cmds));

                processo_terminal.redirectErrorStream(true);
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
                    assertNotEquals(respostas, dados.resp);
                }

            } catch(Exception e) {
                // erros podem estar relacionados a problemas na saída, não há má gerência dos comandos git
                System.err.println("ERRO não versionado");
            }
        }
    }
}