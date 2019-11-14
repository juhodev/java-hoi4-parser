package dev.juho.hoi4.parser.textparser.gamefile.nodes;

import dev.juho.hoi4.parser.textparser.gamefile.GFNode;

public class PropertyNode extends GFNode {

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
