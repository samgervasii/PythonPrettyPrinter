import java.io.*;
import antlr.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class PythonPrettyPrinter extends PythonParserBaseListener { // Extends GrammarNameBaseListener

    private String _target = ""; // code will be here
    private static final int _IND = 4; // constant of indents increment
    private int _indents = 0; // number of indents applied, multiple of IND
    private boolean _stmt_parent = false; // keeping trace of stmt being the parent
    private boolean _error = false; // starts with no error occured

    protected void addToTarget(String target) {
        this._target += target;
    }

    protected void addIndents() {
        this._indents += _IND;
    }

    protected void cutIndents() {
        this._indents -= _IND;
    }

    protected int getIndents() {
        return this._indents;
    }

    protected void setStmtParent(boolean stmt_parent) {
        this._stmt_parent = stmt_parent;
    }

    protected boolean getStmtParent() {
        return this._stmt_parent;
    }

    protected void setError(boolean error) {
        this._error = error;
    }

    protected boolean getError() {
        return this._error;
    }

    protected void removeLastChar() {
        this._target = _target.substring(0, _target.length() - 1);
    }

    protected void applyIndents() {
        for (int i = 0; i < getIndents(); i++) {
            addToTarget(" "); // add white space based on number on indents, that can only be a multiple of
                              // IND (4)
        }
    }

    public String getTarget() { 
        return this._target; 
    }

    // Override methods parserRules in GrammarNameBaseListener

    @Override
    public void enterStmt(PythonParser.StmtContext ctx) {
        if (getIndents() > 0) {
            applyIndents(); // the parent is a suite
            setStmtParent(true); // walk in suite and then stmt
        }
    }

    @Override
    public void exitStmt(PythonParser.StmtContext ctx) {
        addToTarget("\n"); // newline on every statement
        setStmtParent(false);
    }

    @Override
    public void enterSuite(PythonParser.SuiteContext ctx) {
        addToTarget("\n"); // newline on on every clause that indent
        addIndents();
    }

    @Override
    public void exitSuite(PythonParser.SuiteContext ctx) {
        cutIndents();
        removeLastChar(); // we got new line from statement so we remove the last char for having a better
                          // formatted code
    }

    @Override
    public void enterSimple_stmt(PythonParser.Simple_stmtContext ctx) {
        if (!getStmtParent()) { // simple_stmt can have suite as a direct parent, so we have to indent without
            // walk on stmt
            applyIndents();
        }
    }

    @Override
    public void enterElif_clause(PythonParser.Elif_clauseContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            applyIndents(); // to align with other indents if they exist. This is true for every clause
        }
    }

    @Override
    public void enterElse_clause(PythonParser.Else_clauseContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            applyIndents(); // to align with other indents if they exist. This is true for every clause
        }
    }

    @Override
    public void enterExcept_clause(PythonParser.Except_clauseContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            applyIndents(); // to align with other indents if they exist. This is true for every clause
        }
    }

    @Override
    public void enterFinally_clause(PythonParser.Finally_clauseContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            applyIndents(); // to align with other indents if they exist. This is true for every clause
        }
    }

    @Override
    public void exitDecorator(PythonParser.DecoratorContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            applyIndents(); // to align with other indents if they exist. This is true for every clause
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
        String output_path = args[1];
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
