package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;

import java.util.List;

public class ListNode extends ASTNode {

	private List<ASTNode> children;

	public ListNode(List<ASTNode> children) {
		super(Type.LIST);

		this.children = children;
	}

	public List<ASTNode> getChildren() {
		return children;
	}
}
