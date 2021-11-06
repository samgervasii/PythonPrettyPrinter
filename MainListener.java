import java.io.*;
import antlr.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class MainListener extends PythonParserBaseListener { // Extends GrammarNameBaseListener

    private String target = ""; // code will be here
    public int indents = 0; // number of indents applied, multiple of IND
    public static final int IND = 4; // constant of indents increment
    public boolean ind_stmt = false; // bool check for direct parent of a small stmt
    public boolean error = false; // starts with no error occured

    public void addToTarget(String target) {
        this.target += target;
    }

    public String getTarget() {
        return this.target;
    }

    public void removeLastChar() {
        this.target = target.substring(0, target.length() - 1);
    }

    public void addIndents() {
        for (int i = 0; i < indents; i++) {
            addToTarget(" "); // add white space based on number on indents, that can only be a multiple of
                              // IND (4)
        }
    }

    // Override methods parserRules in GrammarNameBaseListener

    @Override
    public void enterStmt(PythonParser.StmtContext ctx) {
        if (indents > 0) {
            addIndents(); // the parent is a suite
            ind_stmt = true; // walk in suite and then stmt
        }
    }

    @Override
    public void exitStmt(PythonParser.StmtContext ctx) {
        addToTarget("\n"); // newline on every statement
        ind_stmt = false;
    }

    @Override
    public void enterSuite(PythonParser.SuiteContext ctx) {
        addToTarget("\n"); // newline on on every clause that indent
        indents += IND;
    }

    @Override
    public void exitSuite(PythonParser.SuiteContext ctx) {
        indents -= IND; // reset variable that counts spaces
        removeLastChar(); // we got new line from statement so we remove the last char for having a better
                          // formatted code
    }

    @Override
    public void enterSimple_stmt(PythonParser.Simple_stmtContext ctx) {
        if (!ind_stmt) { // simple_stmt can have suite as a direct parent, so we have to indent without
                         // walk on stmt
            addIndents();
        }
    }

    @Override
    public void enterElif_clause(PythonParser.Elif_clauseContext ctx) {
        addToTarget("\n");
        if (indents > 0) {
            addIndents();
        }
    }

    @Override
    public void enterElse_clause(PythonParser.Else_clauseContext ctx) {
        addToTarget("\n");
        if (indents > 0) {
            addIndents();
        }
    }

    @Override
    public void enterExcept_clause(PythonParser.Except_clauseContext ctx) {
        addToTarget("\n");
        if (indents > 0) {
            addIndents();
        }
    }

    @Override
    public void enterFinally_clause(PythonParser.Finally_clauseContext ctx) {
        addToTarget("\n");
        if (indents > 0) {
            addIndents();
        }
    }

    @Override
    public void exitDecorator(PythonParser.DecoratorContext ctx) {
        addToTarget("\n");
        if (indents > 0) {
            addIndents();
        }
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        if (node.getText() != "<EOF>") { // do not print <EOF>
            addToTarget(node.getText() + " "); // every terminal node is whitespaced
            if (node.getText() == "") { // redundant
                removeLastChar();
            }
        }
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        error = true;
    }

    // Main
    public static void main(String[] args) throws IOException {
        // objects declaration
        String input_path = args[0]; // from commands line, otherwise "IO\\input.py";
        String output_path = "IO\\output.py";
        String code = "";
        FileWriter myWriter = new FileWriter(output_path); // write on the output_path
        PythonLexer lexer = new PythonLexer(CharStreams.fromFileName(input_path)); // GrammarNameLexer lexer = new ..
        CommonTokenStream tokens = new CommonTokenStream(lexer); // Tokens stream from lexer
        PythonParser parser = new PythonParser(tokens); // GrammarNameParser parser = new GrammarNameParser from tokens
        ParseTree tree = parser.root(); // parser.StarterRule() for ParseTree
        ParseTreeWalker walker = new ParseTreeWalker(); // walker
        MainListener listener = new MainListener(); // main listener

        // actions
        walker.walk(listener, tree); // walker walks the ParseTree using the final listener
        code = listener.getTarget(); // we recover the string code completed
        if (listener.error) {
            System.out.println("Parsing error occured, please check your input code");
        } else {
            myWriter.write(code);// we write on file
            System.out.println("code wrote successfully");
        }
        myWriter.close(); // close the file

    }
}
