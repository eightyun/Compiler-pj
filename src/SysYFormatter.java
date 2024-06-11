import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.IOException;

public class SysYFormatter {
    private static int indentLevel = 0;
    private static StringBuilder formattedCode = new StringBuilder();

    public static void format(CharStream input) throws IOException {
        SysYLexer lexer = new SysYLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SysYParser parser = new SysYParser(tokens);

        ParseTree tree = parser.program();

        ParseTreeWalker walker = new ParseTreeWalker();
        SysYFormatterListener listener = new SysYFormatterListener();
        walker.walk(listener, tree);

        System.out.print(formattedCode.toString());
    }

    private static class SysYFormatterListener extends SysYParserBaseListener {
        @Override
        public void enterFuncDef(SysYParser.FuncDefContext ctx) {
            if (formattedCode.length() > 0) {
                formattedCode.append("\n");
            }
            addIndent();
            formattedCode.append(ctx.funcType().getText()).append(" ")
                    .append(ctx.IDENT().getText()).append(" (");
            if (ctx.funcFParams() != null) {
                formattedCode.append(ctx.funcFParams().getText());
            }
            formattedCode.append(") ");
        }

        @Override
        public void exitFuncDef(SysYParser.FuncDefContext ctx) {
            formattedCode.append("\n");
            super.exitFuncDef(ctx);
        }

        @Override
        public void enterBlock(SysYParser.BlockContext ctx) {
            if (ctx.getParent() instanceof SysYParser.FuncDefContext) {
                formattedCode.append("{\n");
            } else {
                addIndent();
                formattedCode.append("{\n");
            }
            indentLevel++;
        }

        @Override
        public void exitBlock(SysYParser.BlockContext ctx) {
            indentLevel--;
            addIndent();
            formattedCode.append("}\n");
        }

        @Override
        public void enterStmt(SysYParser.StmtContext ctx) {
            if (!(ctx.getParent() instanceof SysYParser.BlockContext)) {
                indentLevel++;
            }
            addIndent();
        }

        @Override
        public void exitStmt(SysYParser.StmtContext ctx) {
            if (!(ctx.getParent() instanceof SysYParser.BlockContext)) {
                indentLevel--;
            }
            formattedCode.append("\n");
        }

        @Override
        public void enterDecl(SysYParser.DeclContext ctx) {
            addIndent();
        }

        @Override
        public void exitDecl(SysYParser.DeclContext ctx) {
            formattedCode.append("\n");
        }

        private void addIndent() {
            for (int i = 0; i < indentLevel; i++) {
                formattedCode.append("    ");
            }
        }
    }
}