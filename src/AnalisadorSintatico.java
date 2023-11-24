import java.io.IOException;
import java.util.*;

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

                // Verifica se o próximo token é um identificador válido
                if (i < tokens.size() && tokens.get(i).matches("[a-zA-Z_]+")) {
                    i++;
                } else {
                    return false;
                }

                // Verifica se o próximo token é um tipo válido (int, string, etc.)
                if (i < tokens.size() && (tokens.get(i).equals("int") || tokens.get(i).equals("string")
                        || tokens.get(i).equals("char") || tokens.get(i).equals("boolean"))

                ) {
                    i++;
                } else {
                    return false;
                }

            } else {
                i++;
            }
        }

        return true; // Se percorreu todos os tokens sem problemas, retorna true
    }

    public boolean verificaFuncMain() {
        boolean temMain = false;
        advanceToken();

        while (lex.hasNextToken()) {
            String token = lex.nextToken();

            // Verifica se encontrou a palavra-chave "func"
            if (token.equals("func")) {
                // Verifica se o próximo token é "main"
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
                // Verifica se há um identificador após a palavra-chave 'func'
                if (lex.hasNextToken() && lex.tokenType(lex.nextToken()).equals("identifier")) {
                    estruturaCorreta = true; // Estrutura func + nome está correta
                } else {
                    return false; // Nome de função ausente ou inválido após 'func'
                }
            }
        }

        return estruturaCorreta; // Retorna true se a estrutura func + nome for encontrada
    }

    public boolean verificaChavesParantesesFechados() {
        Stack<String> pilha = new Stack<>();

        while (lex.hasNextToken()) {
            String token = lex.nextToken();

            if (token.equals("{") || token.equals("[") || token.equals("(")) {
                pilha.push(token);
            } else if (token.equals("}") || token.equals("]") || token.equals(")")) {
                if (pilha.isEmpty()) {
                    return false; // Encontrou um fechamento sem correspondência de abertura
                }

                String topo = pilha.peek();
                if ((token.equals("}") && topo.equals("{")) ||
                        (token.equals("]") && topo.equals("[")) ||
                        (token.equals(")") && topo.equals("("))) {
                    pilha.pop();
                } else {
                    return false; // Fechamento incompatível com o topo da pilha
                }
            }
        }

        return pilha.isEmpty(); // Verifica se todos os símbolos foram fechados corretamente
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

    // Método para a regra 'expression'
    private void expression() {
        term();
        while (currentToken != null && (currentToken.equals("+") || currentToken.equals("-"))) {
            advanceToken();
            term();
        }
    }

    // Método para a regra 'term'
    private void term() {
        factor();
        while (currentToken != null && (currentToken.equals("*") || currentToken.equals("/"))) {
            advanceToken();
            factor();
        }
    }

    // Método para a regra 'factor'
    private void factor() {
        if (currentToken != null && currentToken.matches("\\d+")) {
            advanceToken();
        } else if (matchToken("(")) {
            expression();
            if (!matchToken(")")) {
                // Erro: Esperado ')' após expressão
                // Tratamento de erro aqui
            }
        } else {
            // Erro: Token inesperado
            // Tratamento de erro aqui
        }
    }

    // Método para a regra 'ifStatement'
    private void ifStatement() {
        if (matchToken("if")) {
            if (!matchToken("(")) {
                // Erro: Esperado '(' após 'if'
                // Tratamento de erro aqui
            }
            expression();
            if (!matchToken(")")) {
                // Erro: Esperado ')' após expressão do 'if'
                // Tratamento de erro aqui
            }
            ifStatement();
            if (matchToken("else")) {
                ifStatement();
            }
        }
            }

    // Método para iniciar a análise sintática
    public void parse() {
        ifStatement();
        if (currentToken != null) {
            // Erro: Tokens extras no final
            // Tratamento de erro aqui
        } else {
            System.out.println("Análise sintática concluída com sucesso!");
        }
    }

}
