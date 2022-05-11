package ferramentas;

import java.util.Scanner;
import java.util.regex.*;

public class Validacao {
    
    private static final Scanner SCAN = new Scanner(System.in);

    public static String entradaNome() {
        System.out.print("Nome: ");
        String nome = SCAN.nextLine();
        
        //restricao de formato
        if (nome.length() > 20 || nome.length() < 5) {
            System.out.println("\nQuantidade de caracteres inválida!");
            System.out.println("O nome do usuário deve conter de 5 a 20 caracteres!\n");
            
            return entradaNome();
        }
        
        return nome;
    }
    
    /*
       parametro login dita qual sera o comportamento especifico dos metodos 
       entradaEmail e entradaSenha, acessar = true ou registrar = false
    */
    
    public static String entradaEmail(boolean login) {
        System.out.print("Email: ");
        String email = SCAN.nextLine();

        //regex para o formato valido do email
        String regex = "^[a-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        
        if (!matcher.matches()) {
            System.out.println("\nEmail inválido!");
            
            if(login) System.exit(0); //verificacao de comportamento
            
            System.out.println("Exemplo de email válido: exemplo@dominio.com\n");
            
            return entradaEmail(false);
        }

        return email;
    }

    public static String entradaSenha(boolean login) {
        System.out.print("Senha: ");
        String senha = SCAN.nextLine();
        
        if (!login) { //verificacao de comportamento
            System.out.print("Confirmação da senha: ");
            String senhaConfirmada = SCAN.nextLine();

            if (!senha.equals(senhaConfirmada)) {
                System.out.println("\nSenhas incompatíveis!\n");
                return entradaSenha(false);
            }
        }
        
        //regex para o formato valido da senha
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[$*&@#])[0-9a-zA-Z$*&@#]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(senha);

        if (!matcher.matches()) {
            System.out.println("\nSenha inválida!");
            
            if(login) System.exit(0); //verificacao de comportamento
            
            System.out.println("""
                    Deve conter ao menos 8 dígitos
                    Deve conter ao menos uma letra maiúscula
                    Deve conter ao menos uma letra minúscula
                    Deve conter ao menos um caractere especial
                                        
                    """);
            
            return entradaSenha(false);
        }

        return senha;
    }
}