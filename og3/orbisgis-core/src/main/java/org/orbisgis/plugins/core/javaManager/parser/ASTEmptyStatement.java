/* Generated By:JJTree: Do not edit this line. ASTEmptyStatement.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.orbisgis.plugins.core.javaManager.parser;

public class ASTEmptyStatement extends SimpleNode {
	public ASTEmptyStatement(int id) {
		super(id);
	}

	public ASTEmptyStatement(JavaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=1efee76ae3106f0c75689b9f0455fcfa (do not edit this
 * line)
 */