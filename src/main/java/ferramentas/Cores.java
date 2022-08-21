package ferramentas;

public class Cores {

	public static String getCor(String nomeCor) {
		switch (nomeCor) {
			case "vermelho" -> {
				return "\u001B[31m";
			}
			case "verde" -> {
				return "\u001B[32m";
			}
			case "amarelo" -> {
				return "\u001B[33m";
			}
			case "azul" -> {
				return "\u001B[34m";
			}
			case "roxo" -> {
				return "\u001B[35m";
			}
			case "ciano" -> {
				return "\u001B[36m";
			}
			default -> { // branco
				return "\u001B[37m";
			}
		}

	}
}
