/* Generated By:JJTree: Do not edit this line. ASTMemberSelector.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.orbisgis.plugins.core.javaManager.parser;

public class ASTMemberSelector extends SimpleNode {
	public ASTMemberSelector(int id) {
		super(id);
	}

	public ASTMemberSelector(JavaParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(JavaParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=23b5b7f6a2b138f5067b46d7b9a4d463 (do not edit this
 * line)
 */