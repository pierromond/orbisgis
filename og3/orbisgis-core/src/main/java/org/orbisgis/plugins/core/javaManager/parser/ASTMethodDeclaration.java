/* Generated By:JJTree: Do not edit this line. ASTMethodDeclaration.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.orbisgis.plugins.core.javaManager.parser;

public class ASTMethodDeclaration extends SimpleNode {
	public ASTMethodDeclaration(int id) {
		super(id);
	}

	public ASTMethodDeclaration(JavaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=7aa45a2c8270a28214a513c5b059785f (do not edit this
 * line)
 */