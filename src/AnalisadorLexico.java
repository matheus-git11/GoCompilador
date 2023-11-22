import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalisadorLexico {
    private final Pattern regex = Pattern.compile("\".*\"|\\d+|[a-zA-Z_]+[a-zA-Z0-9_]*|[+|*|/|\\-|{|}|(|)|\\[|\\]|\\.|,|;|<|>|=|~|&]");
    private final List<String> keywords = Arrays.asList(
            "class", "constructor", "function", "method", "field", "static",
            "var", "int", "char", "boolean", "void", "true", "false", "null",
            "this", "let", "do", "if", "else", "while", "return"
    );

    private final String symbol = "[+|*|/|{|}|(|)|\\.|,|;|<|>|=|~]";
    private final String identifier = "[a-zA-Z_]+[a-zA-Z0-9_]*";
    private final String integer = "\\d+";
    private final String string = "\".*\"";

    private List<String> tokens;
    private int index;

    public AnalisadorLexico() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\mathe\\IdeaProjects\\CompiladorGo\\src\\CodigoGo.txt"));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();

        String arquivo = content.toString().replaceAll("//.*", " ").replaceAll("/\\*(.|\\n)*?\\*/", " ");
        Matcher matcher = regex.matcher(arquivo);
        tokens = new ArrayList<>();
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        index = 0;
    }

    public boolean hasNextToken() {
        return index < tokens.size();
    }

    public String nextToken() {
        return tokens.get(index++);
    }

    public String tokenType(String token) {
        if (token.matches(identifier)) {
            return keywords.contains(token) ? "keyword" : "identifier";
        } else if (token.matches(symbol)) {
            return "symbol";
        } else if (token.matches(integer)) {
            return "integer";
        } else if (token.matches(string)) {
            return "string";
        }
        return "unknown";
    }
}