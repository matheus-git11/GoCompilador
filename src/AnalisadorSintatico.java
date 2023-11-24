import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalisadorSintatico {
    private final AnalisadorLexico lex;
    private String currentToken;

    public AnalisadorSintatico() throws IOException {
        lex = new AnalisadorLexico();
        advanceToken();
    }

    public boolean verificaDeclaracaoVariaveis(List<String> tokens) {
        int i = 0;
        while (i < tokens.size()) {
            String currentToken = tokens.get(i);

            if (currentToken.equals("var")) {
                i++;

                if (!verificaIdentificador(tokens, i)) {
                    System.out.println("Erro: Identificador inválido após 'var'.");
                    return false;
                }
                i++;

                if (!verificaTipo(tokens, i)) {
                    System.out.println("Erro: Tipo de dado inválido para a variável.");
                    return false;
                }
                i++;

                if (i < tokens.size() && tokens.get(i).equals("=")) {
                    i++;

                }

                while (i < tokens.size() && tokens.get(i).equals(",")) {
                    i++;
                    if (!verificaIdentificador(tokens, i)) {
                        System.out.println("Erro: Identificador inválido na lista de variáveis.");
                        return false;
                    }
                    i++;
                }
            } else {
                i++;
            }
        }
        return true;
    }

    private boolean verificaIdentificador(List<String> tokens, int index) {
        return index < tokens.size() && tokens.get(index).matches("[a-zA-Z_][a-zA-Z0-9_]*");
    }

    private boolean verificaTipo(List<String> tokens, int index) {
        if (index >= tokens.size())
            return false;
        String tipo = tokens.get(index);
        return tipo.equals("int") || tipo.equals("string") || tipo.equals("bool");

    }

    public boolean verificaFuncMain() {
        boolean temMain = false;
        advanceToken();

        while (lex.hasNextToken()) {
            String token = lex.nextToken();

            if (token.equals("func")) {

                if (lex.hasNextToken() && lex.nextToken().equals("main")) {
                    temMain = true;
                    break;
                }
            }
        }

        return temMain;
    }

    public boolean verificaEstruturaFunc() throws IOException {
        AnalisadorLexico lex = new AnalisadorLexico();
        boolean estruturaCorreta = false;

        while (lex.hasNextToken()) {
            String token = lex.nextToken();

            if (token.equals("func")) {

                if (lex.hasNextToken() && lex.tokenType(lex.nextToken()).equals("identifier")) {
                    estruturaCorreta = true;
                } else {
                    return false;
                }
            }
        }

        return estruturaCorreta;
    }

    public boolean verificaChavesParantesesFechados() {
        Stack<String> pilha = new Stack<>();

        while (lex.hasNextToken()) {
            String token = lex.nextToken();

            if (token.equals("{") || token.equals("[") || token.equals("(")) {
                pilha.push(token);
            } else if (token.equals("}") || token.equals("]") || token.equals(")")) {
                if (pilha.isEmpty()) {
                    return false;
                }

                String topo = pilha.peek();
                if ((token.equals("}") && topo.equals("{")) ||
                        (token.equals("]") && topo.equals("[")) ||
                        (token.equals(")") && topo.equals("("))) {
                    pilha.pop();
                } else {
                    return false;
                }
            }
        }

        return pilha.isEmpty();
    }

    private void advanceToken() {
        if (lex.hasNextToken()) {
            currentToken = lex.nextToken();
        } else {
            currentToken = null;
        }
    }

    private boolean matchToken(String expectedToken) {
        if (currentToken != null && currentToken.equals(expectedToken)) {
            advanceToken();
            return true;
        }
        return false;
    }

    

    private boolean isNumber(String token) {
        
        return token.matches("-?\\d+(\\.\\d+)?"); 
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private void expression() {
        term();
        while (currentToken != null && (currentToken.equals("+") || currentToken.equals("-"))) {
            advanceToken();
            term();
        }
    }

    private void term() {
        factor();
        while (currentToken != null && (currentToken.equals("*") || currentToken.equals("/"))) {
            advanceToken();
            factor();
        }
    }

    private void factor() {
        if (currentToken != null && currentToken.matches("\\d+")) {
            advanceToken();
        } else if (matchToken("(")) {
            expression();
            if (!matchToken(")")) {

            }
        } else {

        }
    }

    private void ifStatement() {
        if (matchToken("if")) {
            if (!matchToken("(")) {

            }
            expression();
            if (!matchToken(")")) {

            }
            ifStatement();
            if (matchToken("else")) {
                ifStatement();
            }
        }
    }

    public void parse() {
        ifStatement();
        if (currentToken != null) {

        } else {
            System.out.println("Análise sintática concluída com sucesso!");
        }
    }

    public boolean verificaFuncoes(List<String> tokens) {
        Pattern patternFunc = Pattern
                .compile("func\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(([^)]*)\\)(\\s*[^\\s{]+)?\\s*\\{");
        Matcher matcher;

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (token.equals("func")) {

                StringBuilder funcDeclaration = new StringBuilder();
                while (i < tokens.size() && !tokens.get(i).equals("{")) {
                    funcDeclaration.append(tokens.get(i++)).append(" ");
                }

                matcher = patternFunc.matcher(funcDeclaration.toString().trim());
                if (!matcher.matches()) {
                    return false;
                }

                if (i < tokens.size() && tokens.get(i).equals("{")) {
                    i++;
                }
            }
        }

        return true;
    }
}
