/* Generated By:JJTree: Do not edit this line. ASTSQLProductSymbol.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.gdms.sql.parser;

public
class ASTSQLProductSymbol extends SimpleNode {
  public ASTSQLProductSymbol(int id) {
    super(id);
  }

  public ASTSQLProductSymbol(SQLEngine p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SQLEngineVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ab0db37ee0e099a8250abcf7d5b89384 (do not edit this line) */