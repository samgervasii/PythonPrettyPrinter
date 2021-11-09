import java.io.*;
import antlr.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class PythonPrettyPrinter extends PythonParserBaseListener { // Extends GrammarNameBaseListener

    private String target = ""; // code will be here
    private static final int IND = 4; // constant of indents increment
    private int indents = 0; // number of indents applied, multiple of IND
    private boolean stmt_parent = false; // keeping trace of stmt being the parent
    private boolean error = false; // starts with no error occured

    public void addToTarget(String target) {
        this.target += target;
    }

    public String getTarget() {
        return this.target;
    }

    public void moreIndents() {
        this.indents += IND;
    }

    public void lessIndents() {
        this.indents -= IND;
    }

    public int getIndents() {
        return this.indents;
    }

    public void setStmt_Parent(boolean stmt_parent) {
        this.stmt_parent = stmt_parent;
    }

    public boolean getStmt_Parent() {
        return this.stmt_parent;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean getError() {
        return this.error;
    }

    public void removeLastChar() {
        this.target = target.substring(0, target.length() - 1);
    }

    public void addIndents() {
        for (int i = 0; i < getIndents(); i++) {
            addToTarget(" "); // add white space based on number on indents, that can only be a multiple of
                              // IND (4)
        }
    }

    // Override methods parserRules in GrammarNameBaseListener

    @Override
    public void enterStmt(PythonParser.StmtContext ctx) {
        if (getIndents() > 0) {
            addIndents(); // the parent is a suite
            setStmt_Parent(true); // walk in suite and then stmt
        }
    }

    @Override
    public void exitStmt(PythonParser.StmtContext ctx) {
        addToTarget("\n"); // newline on every statement
        setStmt_Parent(false);
    }

    @Override
    public void enterSuite(PythonParser.SuiteContext ctx) {
        addToTarget("\n"); // newline on on every clause that indent
        moreIndents();
    }

    @Override
    public void exitSuite(PythonParser.SuiteContext ctx) {
        lessIndents();
        removeLastChar(); // we got new line from statement so we remove the last char for having a better
                          // formatted code
    }

    @Override
    public void enterSimple_stmt(PythonParser.Simple_stmtContext ctx) {
        if (!getStmt_Parent()) { // simple_stmt can have suite as a direct parent, so we have to indent without
            // walk on stmt
            addIndents();
        }
    }

    @Override
    public void enterElif_clause(PythonParser.Elif_clauseContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            addIndents(); // to align with other indents if they exist. This is true for every clause
        }
    }

    @Override
    public void enterElse_clause(PythonParser.Else_clauseContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            addIndents(); // to align with other indents if they exist. This is true for every clause
        }
    }

    @Override
    public void enterExcept_clause(PythonParser.Except_clauseContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            addIndents(); // to align with other indents if they exist. This is true for every clause
        }
    }

    @Override
    public void enterFinally_clause(PythonParser.Finally_clauseContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            addIndents(); // to align with other indents if they exist. This is true for every clause
        }
    }

    @Override
    public void exitDecorator(PythonParser.DecoratorContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            addIndents(); // to align with other indents if they exist. This is true for every clause
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
        setError(true);
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
        PythonPrettyPrinter listener = new PythonPrettyPrinter(); // main listener

        // actions
        walker.walk(listener, tree); // walker walks the ParseTree using the final listener
        code = listener.getTarget(); // we recover the string target completed
        if (listener.getError()) {
            System.out.println("Parsing error occured, please check your input code");
        } else {
            myWriter.write(code);// we write on file
            System.out.println("code wrote successfully");
        }
        myWriter.close(); // close the file

    }
}
