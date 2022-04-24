import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

//Extends GrammarNameBaseListener
public class PythonPrettyPrinterListener extends PythonParserBaseListener { 
    //code will be here
    private String _target = "";
    //constant of indents increment 
    private static final int _IND = 4;
    //number of indents applied, multiple of IND 
    private int _indents = 0;
    //keeping trace of stmt being the parent 
    private boolean _stmt_parent = false;
    //starts with no error occured 
    private boolean _error = false; 

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

    //add white space based on number on indents, that can only be a multiple of IND (4)
    protected void applyIndents() {
        for (int i = 0; i < getIndents(); i++) {
            addToTarget(" ");                                
        }
    }

    public String getTarget() { 
        return this._target; 
    }

    // Override methods parserRules in GrammarNameBaseListener

    @Override
    public void enterStmt(PythonParser.StmtContext ctx) {
        if (getIndents() > 0) {
            //the parent is a suite
            applyIndents();
            //walk in suite and then stmt
            setStmtParent(true); 
        }
    }

    @Override
    public void exitStmt(PythonParser.StmtContext ctx) {
        //newline on every statement
        addToTarget("\n"); 
        setStmtParent(false);
    }

    @Override
    public void enterSuite_new_line(PythonParser.Suite_new_lineContext ctx) {
        //newline on on every clause that indent
        addToTarget("\n"); 
        addIndents();
    }

    @Override
    public void exitSuite_new_line(PythonParser.Suite_new_lineContext ctx) {
        cutIndents();
        //we got new line from statement so we remove the last char for having a better formatted code
        removeLastChar();                
    }

    @Override
    public void enterSimple_stmt(PythonParser.Simple_stmtContext ctx) {
        //simple_stmt can have suite as a direct parent, so we have to indent without walk on stmt 
        if (!getStmtParent()) { 
            applyIndents();
        }
    }

    @Override
    public void enterElif_clause(PythonParser.Elif_clauseContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            //to align with other indents if they exist. This is true for every clause
            applyIndents(); 
        }
    }

    @Override
    public void enterElse_clause(PythonParser.Else_clauseContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            //to align with other indents if they exist. This is true for every clause
            applyIndents(); 
        }
    }

    @Override
    public void enterExcept_clause(PythonParser.Except_clauseContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            //to align with other indents if they exist. This is true for every clause
            applyIndents(); 
        }
    }

    @Override
    public void enterFinally_clause(PythonParser.Finally_clauseContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            //to align with other indents if they exist. This is true for every clause
            applyIndents(); 
        }
    }

    @Override
    public void exitDecorator(PythonParser.DecoratorContext ctx) {
        addToTarget("\n");
        if (getIndents() > 0) {
            //to align with other indents if they exist. This is true for every clause
            applyIndents(); 
        }
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        //do not print <EOF>
        if (node.getText() != "<EOF>") {
            //every terminal node is whitespaced
            addToTarget(node.getText() + " ");
            //redundant
            if (node.getText() == "") { 
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
        String input_path = args[0];
        String output_path = args[1];
        String code = "";
        //write on the output_path
        FileWriter myWriter = new FileWriter(output_path);
        //GrammarNameLexer lexer = new ..
        PythonLexer lexer = new PythonLexer(CharStreams.fromFileName(input_path)); 
        //Tokens stream from lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        //GrammarNameParser parser = new GrammarNameParser from tokens
        PythonParser parser = new PythonParser(tokens);
        //parser.StarterRule() for ParseTree
        ParseTree tree = parser.root(); 
        ParseTreeWalker walker = new ParseTreeWalker();
        PythonPrettyPrinterListener listener = new PythonPrettyPrinterListener();

        // actions
        //walker walks the ParseTree using the final listener
        walker.walk(listener, tree);
        //we recover the string target completed 
        code = listener.getTarget(); 
        if (listener.getError()) {
            System.out.println("Parsing error occured, please check your input code");
        } else {
            //we write on file
            myWriter.write(code);
            System.out.println("code wrote successfully");
        }
        //close the file
        myWriter.close(); 

    }
}
