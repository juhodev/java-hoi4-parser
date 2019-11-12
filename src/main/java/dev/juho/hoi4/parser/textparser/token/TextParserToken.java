package dev.juho.hoi4.parser.textparser.token;

public class TextParserToken {

	private Type type;
	private char[] value;
	private int length;

	public TextParserToken(Type type, char[] value, int length) {
		this.type = type;
		this.value = value;
		this.length = length;
	}

	public Type getType() {
		return type;
	}

	public char[] getValue() {
		return value;
	}

	public int getLength() {
		return length;
	}

	public enum Type {
		OPERATION,
		START_OBJECT,
		END_OBJECT,
		STRING,
	}

}
