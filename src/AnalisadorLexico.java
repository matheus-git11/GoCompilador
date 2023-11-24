import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalisadorLexico {
    private final Pattern regex = Pattern
            .compile("\".*\"|\\d+|[a-zA-Z_]+[a-zA-Z0-9_]*|[+|*|/|\\-|{|}|(|)|\\[|\\]|\\.|,|;|<|>|=|~|&]");
    private final List<String> keywords = Arrays.asList(
        "map","import","interface", "switch", "go", "goto", "defer", "for",
        "var", "chan", "type", "else", "return", "break", "const","case", "continue",
        "default", "falltrough", "defer", "package", "range","if",
        "select", "struct", "return", "func");

        private final String arithmeticOperator = "[+|*|/|-|/=|%]";
        private final String logicalOperator = "(&|\\||\\^|<<|>>|&\\^|&&|\\|\\|)";
        private final String comparisonOperator = "[<|>|==|!=|<=|>=|!]";
        private final String assignmentOperator = "(\\+=|-=|\\*=|/=|%|=|&=|\\|=|\\^=|<<=|>>=|&\\^=|=)";
        private final String identifier = "[a-zA-Z_]+[a-zA-Z0-9_]*";
        private final String integer = "\\d+";
        private final String string = "\".*\"";
        private final String delimiter = "[(){}\\[\\].,]";

    private List<String> tokens;
    private int index;

    public AnalisadorLexico() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/CodigoGo.txt"));
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
        } else if (index < tokens.size() && token.matches("[+|-|*|/]") && tokens.get(index).equals("=")) {
            return "assignment operator";
        } else if (token.matches(arithmeticOperator)) {
            return "arithmetic operator";
        } else if (token.matches(logicalOperator)) {
            return "logical operator";
        } else if (token.matches(comparisonOperator)) {
            return "comparison operator";
        } else if (token.matches(assignmentOperator)) {
            return "assignment operator";
        } else if (token.matches(integer)) {
            return "integer";
        } else if (token.matches(string)) {
            return "string";
        } else if (token.matches(delimiter)) {
            return "delimiter";
        }
        return "unknown";
    }
}