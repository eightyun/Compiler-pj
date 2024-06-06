import org.antlr.v4.runtime.*;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("input path is required");
            return;
        }
        String source = args[0];
        CharStream input = CharStreams.fromFileName(source);
        SysYLexer sysYLexer = new SysYLexer(input);

        // 第三步：添加自定义的ErrorListener
        sysYLexer.removeErrorListeners();
        MyErrorListener myErrorListener = new MyErrorListener();
        sysYLexer.addErrorListener(myErrorListener);

        // 第四步：获取所有token并触发错误检查
        List<? extends Token> myTokens = sysYLexer.getAllTokens();

        // 第五步：根据是否有错误信息输出相应内容
        if (myErrorListener.hasErrors()) {
            myErrorListener.printLexerErrorInformation();
        } else {
            for (Token t : myTokens) {
                printSysYTokenInformation(t);
            }
        }
    }

    // 输出Token信息的函数
    public static void printSysYTokenInformation(Token t) {
        String tokenType = SysYLexer.VOCABULARY.getSymbolicName(t.getType());
        String tokenText = t.getText();

        // 将十六进制和八进制转换为十进制
        if (tokenType.equals("INTEGR_CONST")) {
            if (tokenText.matches("0[xX][0-9a-fA-F]+")) {
                tokenText = String.valueOf(Integer.parseInt(tokenText.substring(2), 16));
            } else if (tokenText.matches("0[0-7]+")) {
                tokenText = String.valueOf(Integer.parseInt(tokenText.substring(1), 8));
            }
        }

        System.err.println(tokenType + " " + tokenText + " at Line " + t.getLine() + ".");
    }
}

