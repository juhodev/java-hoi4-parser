package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;

public class PropertyNode extends ASTNode {

	private String key;
	private Object value;

	public PropertyNode(String key, Object value) {
		super(Type.PROPERTY);

		this.key = key;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public String getKey() {
		return key;
	}
}
