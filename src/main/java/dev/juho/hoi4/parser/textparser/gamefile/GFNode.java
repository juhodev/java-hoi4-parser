package dev.juho.hoi4.parser.textparser.gamefile;


public abstract class GFNode {

	private Type type;

	public GFNode(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public enum Type {
		LONG,
		BOOLEAN,
		PROPERTY,
		STRING,
		INTEGER,
		DOUBLE,
		OBJECT,
		LIST,
	}

}
