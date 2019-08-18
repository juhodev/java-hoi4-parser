package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;

public class StringNode extends ASTNode {

	private String value;

	public StringNode(String value) {
		super(Type.STRING);

		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
