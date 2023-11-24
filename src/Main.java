import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        List<String> tokens = new LinkedList<>();

        AnalisadorLexico analisador = new AnalisadorLexico();
        while (analisador.hasNextToken()) {
            String token = analisador.nextToken();
            tokens.add(token);
            String tipo = analisador.tokenType(token);
            System.out.println("<" + tipo + "> " + token + " </" + tipo + ">");

        }
        AnalisadorSintatico parser = new AnalisadorSintatico();
        boolean main = parser.verificaFuncMain();
        boolean symbol = parser.verificaChavesParantesesFechados();
        boolean funcStructure = parser.verificaEstruturaFunc();
        boolean variavel = parser.verificaDeclaracaoVariaveis(tokens);

        System.out.println("Funcao Main = " + main);
        System.out.println("Funcao verificaChavesParantesesFechados = " + symbol);
        System.out.println("Funcao funcStructure= " + funcStructure);
        System.out.println("Funcao variavel= " + variavel);

    }
}
