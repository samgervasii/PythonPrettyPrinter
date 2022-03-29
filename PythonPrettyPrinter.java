import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class PythonPrettyPrinter extends PythonParserBaseVisitor<String> { // Extends GrammarNameBaseVisitor
  protected final int _IND = 4; // costant that tells us how many spaces
  protected int _indents = 0; // number of indents applied, multiple of IND

  protected String applyIndents() { // return a string of n spaces where n is _indents
    String tabs = "";
    for (int i = 0; i < _indents; i++) {
      tabs += " ";
    }
    return tabs;
  }

  // override visitor methods

  @Override
  public String visitStmt(PythonParser.StmtContext ctx) { // if indents are setted, apply. In every cases add a new line
    if (_indents > 0) {
      return applyIndents() + visitChildren(ctx) + "\n";
    }
    return visitChildren(ctx) + "\n";
  }

  @Override
  public String visitSuite_new_line(PythonParser.Suite_new_lineContext ctx) { // suite in new line has to introduce an
                                                                              // increment of indents
    _indents += _IND;
    String suite = "\n" + visitChildren(ctx);
    suite = suite.substring(0, suite.length() - 1); // delete redundant NEW LINE
    _indents -= _IND;
    return suite;
  }

  @Override
  public String visitElif_clause(PythonParser.Elif_clauseContext ctx) { // same as Stmt but the new line is added before
                                                                        // the visitChildren (can be indented but not by
                                                                        // the last indenter), that's valid for all the
                                                                        // clauses
    if (_indents > 0) {
      return "\n" + applyIndents() + visitChildren(ctx);
    }
    return "\n" + visitChildren(ctx);
  }

  @Override
  public String visitElse_clause(PythonParser.Else_clauseContext ctx) {
    if (_indents > 0) {
      return "\n" + applyIndents() + visitChildren(ctx);
    }
    return "\n" + visitChildren(ctx);
  }

  @Override
  public String visitExcept_clause(PythonParser.Except_clauseContext ctx) {
    if (_indents > 0) {
      return "\n" + applyIndents() + visitChildren(ctx);
    }
    return "\n" + visitChildren(ctx);
  }

  @Override
  public String visitFinally_clause(PythonParser.Finally_clauseContext ctx) {
    if (_indents > 0) {
      return "\n" + applyIndents() + visitChildren(ctx);
    }
    return "\n" + visitChildren(ctx);
  }

  @Override
  public String visitDecorator(PythonParser.DecoratorContext ctx) { // just like Stmt
    if (_indents > 0) {
      return applyIndents() + visitChildren(ctx) + "\n";
    }
    return visitChildren(ctx) + "\n";
  }

  @Override
  public String visitChildren(RuleNode node) { // the original visitChildren had an aggregate method with result and
                                               // childResult, the effect was the return of the only last child.
                                               // Modified with a += operator
    String result = defaultResult();
    if (result == null)
      result = "";
    int n = node.getChildCount();
    for (int i = 0; i < n; i++) {
      if (!shouldVisitNextChild(node, result)) {
        break;
      }
      ParseTree c = node.getChild(i);
      String childResult = c.accept(this);
      result += childResult;
    }
    return result;
  }

  @Override
  public String visitTerminal(TerminalNode node) {
    if (node.getText().equalsIgnoreCase("<EOF>") || node.getText().equalsIgnoreCase(""))
      return "";
    return node.getText() + " ";
  }

  @Override
  public String visitErrorNode(ErrorNode node){
    System.out.println(node.getText()+" : error node text");
    System.exit(1);
    return node.getText();
  }

  public static void main(String[] args) throws IOException {
    // objects declaration
    String input_path = args[0]; // from commands line, otherwise "IO\\input.py";
    String output_path = args[1]; //from commands line, otherwise "OP\\output.py"

    FileWriter targetWriter = new FileWriter(output_path); //file writer
    PythonLexer lexer = new PythonLexer(CharStreams.fromFileName(input_path)); // GrammarNameLexer lexer = new ..
    CommonTokenStream tokens = new CommonTokenStream(lexer); // Tokens stream from lexer
    PythonParser parser = new PythonParser(tokens); // GrammarNameParser parser = new GrammarNameParser from tokens
    ParseTree tree = parser.root(); // parser.StarterRule() for ParseTree
    PythonPrettyPrinter visitor = new PythonPrettyPrinter(); // main listener

    // actions
    String c = visitor.visit(tree); // we recover the string target completed
    targetWriter.write(c);
    System.out.println("code written on file succesfully");
    targetWriter.close();
  }
}
