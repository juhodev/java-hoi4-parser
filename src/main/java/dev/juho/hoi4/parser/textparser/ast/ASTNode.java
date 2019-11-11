package dev.juho.hoi4.parser.textparser.ast;


public abstract class ASTNode {

	private Type type;

	public ASTNode(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public enum Type {
		LONG,
		BOOLEAN,
		ENUM,
		PROPERTY,
		STRING,
		INTEGER,
		DOUBLE,
		OBJECT,
		LIST,
	}

}
