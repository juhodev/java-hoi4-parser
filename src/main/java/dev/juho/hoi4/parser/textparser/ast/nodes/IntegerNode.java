package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;

public class IntegerNode extends ASTNode {

	private int value;

	public IntegerNode(int value) {
		super(Type.INTEGER);

		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
