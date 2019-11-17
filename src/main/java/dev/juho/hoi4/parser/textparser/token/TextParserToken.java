package dev.juho.hoi4.parser.textparser.token;

public class TextParserToken {

	private Type type;
	private int start, length;

	public TextParserToken() {
		this.type = Type.NONE;
		this.start = -1;
		this.length = -1;
	}

	private TextParserToken(Type type, int start, int length) {
		this.type = type;
		this.start = start;
		this.length = length;
	}

	public TextParserToken copy() {
		return new TextParserToken(type, start, length);
	}

	public void setAll(TextParserToken.Type type, int start, int length) {
		this.type = type;
		this.start = start;
		this.length = length;
	}

	public Type getType() {
		return type;
	}

	public int getStart() {
		return start;
	}

	public int getLength() {
		return length;
	}

	public enum Type {
		OPERATION,
		START_OBJECT,
		END_OBJECT,
		STRING,
		NONE,
	}

}
