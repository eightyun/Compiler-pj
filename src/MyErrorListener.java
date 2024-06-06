import org.antlr.v4.runtime.*;

public class MyErrorListener extends BaseErrorListener {
    private boolean hasErrors = false;
    private StringBuilder errorMessages = new StringBuilder();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg,
                            RecognitionException e) {
        hasErrors = true;
        errorMessages.append("Error type A at Line ").append(line).append(":").append(msg).append("\n");
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    public void printLexerErrorInformation() {
        System.err.print(errorMessages.toString());
    }
}