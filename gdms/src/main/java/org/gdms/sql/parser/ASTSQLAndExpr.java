/* Generated By:JJTree: Do not edit this line. ASTSQLAndExpr.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.gdms.sql.parser;

public
class ASTSQLAndExpr extends SimpleNode {
  public ASTSQLAndExpr(int id) {
    super(id);
  }

  public ASTSQLAndExpr(SQLEngine p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SQLEngineVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e694c93cb952934ee968db5d257822fb (do not edit this line) */