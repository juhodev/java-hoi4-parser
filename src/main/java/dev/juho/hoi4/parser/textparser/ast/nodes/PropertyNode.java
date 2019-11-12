package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;

public class PropertyNode extends ASTNode {

	private String key;
	private ASTNode value;

	public PropertyNode(String key, ASTNode value) {
		super(Type.PROPERTY);

		this.key = key;
		this.value = value;
	}

	public ASTNode getValue() {
		return value;
	}

	public String getKey() {
		return key;
	}
}
