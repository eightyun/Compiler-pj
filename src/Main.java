import org.antlr.v4.runtime.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Input path is required");
            return;
        }
        String source = args[0];
        CharStream input = CharStreams.fromFileName(source);
        SysYLexer lexer = new SysYLexer(input);

        MyErrorListener errorListener = new MyErrorListener();
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SysYParser parser = new SysYParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        parser.program();

        if (errorListener.hasErrors()) {
            errorListener.printErrorInformation();
        } else {
            if (args.length > 1 && args[1].equals("highlight")) {
                SysYHighlighter.highlight(input);
            } else if (args.length > 1 && args[1].equals("format")) {
                SysYFormatter.format(input);
            } else {
                System.out.println("No syntax errors detected.");
            }
        }
    }
}