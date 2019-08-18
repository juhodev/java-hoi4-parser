package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;

import java.util.List;

public class ObjectNode extends ASTNode {

	private List<ASTNode> children;

	public ObjectNode(List<ASTNode> children) {
		super(Type.OBJECT);

		this.children = children;
	}

	public List<ASTNode> getChildren() {
		return children;
	}
}
