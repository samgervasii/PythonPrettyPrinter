import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

//Extends GrammarNameBaseVisitor
public class PythonPrettyPrinter extends PythonParserBaseVisitor<String> { 
  //costant that tells us how many spaces
  protected final int _IND = 4;
  //number of spaces to apply, multiple of IND
  protected int _indents = 0; 

  // return a string of n spaces where n is _indents
  protected String applyIndents() { 
    String tabs = "";
    for (int i = 0; i < _indents; i++) {
      tabs += " ";
    }
    return tabs;
  }

  // override visitor methods

  //if indents are setted, apply. In every cases add a new line
  @Override
  public String visitStmt(PythonParser.StmtContext ctx) { 
    if (_indents > 0) {
      return applyIndents() + visitChildren(ctx) + "\n";
    }
    return visitChildren(ctx) + "\n";
  }

  //suite in new line has to introduce an increment of indents
  @Override
  public String visitSuite_new_line(PythonParser.Suite_new_lineContext ctx) { 
    _indents += _IND;
    String suite = "\n" + visitChildren(ctx);
    //delete redundant NEW LINE
    suite = suite.substring(0, suite.length() - 1); 
    _indents -= _IND;
    return suite;
  }

  //same as Stmt but the new line is added before
  //the visitChildren (can be indented but not by
  //the last indenter), that's valid for all the clauses
  @Override
  public String visitElif_clause(PythonParser.Elif_clauseContext ctx) { 
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

  //just like Stmt
  @Override
  public String visitDecorator(PythonParser.DecoratorContext ctx) { 
    if (_indents > 0) {
      return applyIndents() + visitChildren(ctx) + "\n";
    }
    return visitChildren(ctx) + "\n";
  }

  //the original visitChildren had an aggregate method with result and
  //childResult, the effect was the return of the last child only.
  //Modified with a += operator
  @Override
  public String visitChildren(RuleNode node) {                                                                                          
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
    //objects declaration
    String input_path = args[0]; 
    String output_path = args[1]; 

    //file writer
    FileWriter targetWriter = new FileWriter(output_path);
    //GrammarNameLexer lexer = new ..
    PythonLexer lexer = new PythonLexer(CharStreams.fromFileName(input_path)); 
    //Tokens stream from lexer
    CommonTokenStream tokens = new CommonTokenStream(lexer); 
    //GrammarNameParser parser = new GrammarNameParser from tokens
    PythonParser parser = new PythonParser(tokens); 
    //parser.StarterRule() for ParseTree
    ParseTree tree = parser.root();
    //main visitor 
    PythonPrettyPrinter visitor = new PythonPrettyPrinter(); 

    //actions
    //we recover the string target completed
    String c = visitor.visit(tree); 
    targetWriter.write(c);
    System.out.println("code written on file succesfully");
    targetWriter.close();
  }
}
