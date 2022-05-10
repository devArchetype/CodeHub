package ferramentas;

import java.util.Scanner;
import java.util.regex.*;

public class Validacao {
    
    private static final Scanner SCAN = new Scanner(System.in);

    public static String entradaNome() {
        System.out.print("Insira um nome de usuário: ");
        String nome = SCAN.nextLine();
        
        //restricao de formato
        if (nome.length() > 20 || nome.length() < 5) {
            System.out.println("Quantidade de caracteres inválida!");
            System.out.println("O nome do usuário deve conter de 5 a 20 caracteres!\n");
            return entradaNome();
        }
        
        return nome;
    }

    public static String entradaEmail() {
        System.out.print("Insira um email: ");
        String email = SCAN.nextLine();

        //regex para o formato valido do email
        String regex = "^[a-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        
        if (!matcher.matches()) {
            System.out.println("Formato de email inválido!");
            System.out.println("Exemplo de email válido: exemplo@dominio.com\n");
            return entradaEmail();
        }

        return email;
    }

    public static String entradaSenha() {
        System.out.print("Crie uma senha: ");
        String senha = SCAN.nextLine();

        System.out.print("Confirme a senha: ");
        String senhaConfirmada = SCAN.nextLine();

        if (!senha.equals(senhaConfirmada)) {
            System.out.println("Senhas incompatíveis!\n");
            return entradaSenha();
        }
        
        //regex para o formato valido da senha
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[$*&@#])[0-9a-zA-Z$*&@#]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(senha);

        if (!matcher.matches()) {
            System.out.println("Senha inválida!");
            System.out.println("Deve conter ao menos 8 dígitos" +
                    "\nDeve conter ao menos uma letra maiúscula" +
                    "\nDeve conter ao menos uma letra minúscula" +
                    "\nDeve conter ao menos um caractere especial\n");
            return entradaSenha();
        }

        return senha;
    }
}
