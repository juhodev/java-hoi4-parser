package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;

public class BooleanNode extends ASTNode {

	private boolean value;

	public BooleanNode(boolean value) {
		super(Type.BOOLEAN);

		this.value = value;
	}

	public boolean getValue() {
		return value;
	}

}
