/* Generated By:JJTree: Do not edit this line. ASTEnumDeclaration.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.orbisgis.core.javaManager.parser;

public class ASTEnumDeclaration extends SimpleNode {
	public ASTEnumDeclaration(int id) {
		super(id);
	}

	public ASTEnumDeclaration(JavaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=f528a0cd2ae1ec6cde5b43379d01fcb4 (do not edit this
 * line)
 */