import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;
import java.util.*;

public class SysYHighlighter {
    private static final String BRIGHT_CYAN = "\u001B[96m";
    private static final String BRIGHT_RED = "\u001B[91m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String BRIGHT_YELLOW = "\u001B[93m";
    private static final String RESET = "\u001B[0m";

    private static final String[] BRACKET_COLORS = {
            "\u001B[91m", // BrightRed
            "\u001B[92m", // BrightGreen
            "\u001B[93m", // BrightYellow
            "\u001B[94m", // BrightBlue
            "\u001B[95m", // BrightMagenta
            "\u001B[96m"  // BrightCyan
    };

    private static Stack<Integer> bracketLevels = new Stack<>();
    private static Set<Integer> functionTokenIndices;

    public static void highlight(CharStream input) throws IOException {
        SysYLexer lexer = new SysYLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SysYParser parser = new SysYParser(tokens);

        ParseTree tree = parser.program();

        ParseTreeWalker walker = new ParseTreeWalker();
        FunctionNameCollector collector = new FunctionNameCollector();
        walker.walk(collector, tree);

        functionTokenIndices = collector.getFunctionTokenIndices();

        tokens.fill();
        List<Token> allTokens = tokens.getTokens();
        for (Token t : allTokens) {
            printSysYTokenInformation(t);
        }
    }

    public static void printSysYTokenInformation(Token t) {
        String tokenType = SysYLexer.VOCABULARY.getSymbolicName(t.getType());
        String tokenText = t.getText();

        if (tokenType.equals("INTEGR_CONST")) {
            if (tokenText.matches("0[xX][0-9a-fA-F]+")) {
                tokenText = String.valueOf(Integer.parseInt(tokenText.substring(2), 16));
            } else if (tokenText.matches("0[0-7]+")) {
                tokenText = String.valueOf(Integer.parseInt(tokenText.substring(1), 8));
            }
        }

        String highlightedText = tokenText;
        switch (tokenType) {
            case "CONST":
            case "INT":
            case "VOID":
            case "IF":
            case "ELSE":
            case "WHILE":
            case "BREAK":
            case "CONTINUE":
            case "RETURN":
                highlightedText = BRIGHT_CYAN + tokenText + RESET;
                break;
            case "PLUS":
            case "MINUS":
            case "MUL":
            case "DIV":
            case "MOD":
            case "ASSIGN":
            case "EQ":
            case "NEQ":
            case "LT":
            case "GT":
            case "LE":
            case "GE":
            case "NOT":
            case "AND":
            case "OR":
            case "COMMA":
            case "SEMICOLON":
                highlightedText = BRIGHT_RED + tokenText + RESET;
                break;
            case "INTEGR_CONST":
                highlightedText = MAGENTA + tokenText + RESET;
                break;
            case "L_PAREN":
            case "R_PAREN":
            case "L_BRACKT":
            case "R_BRACKT":
            case "L_BRACE":
            case "R_BRACE":
                int level = bracketLevels.size();
                String color = BRACKET_COLORS[level % BRACKET_COLORS.length];
                if (tokenType.startsWith("L_")) {
                    bracketLevels.push(level);
                } else {
                    bracketLevels.pop();
                }
                highlightedText = color + tokenText + RESET;
                break;
            default:
                if (isFunctionName(t)) {
                    highlightedText = BRIGHT_YELLOW + tokenText + RESET;
                }
                break;
        }

        System.out.print(highlightedText);
    }

    public static boolean isFunctionName(Token t) {
        return functionTokenIndices.contains(t.getTokenIndex());
    }
}

class FunctionNameCollector extends SysYParserBaseListener {
    private Set<Integer> functionTokenIndices = new HashSet<>();

    @Override
    public void enterFuncDef(SysYParser.FuncDefContext ctx) {
        functionTokenIndices.add(ctx.IDENT().getSymbol().getTokenIndex());
    }

    public Set<Integer> getFunctionTokenIndices() {
        return functionTokenIndices;
    }
}