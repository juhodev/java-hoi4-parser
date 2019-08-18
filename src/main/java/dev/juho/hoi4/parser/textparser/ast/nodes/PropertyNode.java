package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;

public class PropertyNode<T> extends ASTNode {

	private String key;
	private T value;

	public PropertyNode(String key, T value) {
		super(Type.PROPERTY);

		this.key = key;
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public String getKey() {
		return key;
	}
}
