package dev.juho.hoi4.parser.textparser.token;

public class TextParserToken {

	private boolean inUse;
	private Type type;
	private int start, length;

	public TextParserToken() {
		this.inUse = false;
		this.type = Type.NONE;
		this.start = -1;
		this.length = -1;
	}

	public TextParserToken(Type type, int start, int length) {
		this.type = type;
		this.start = start;
		this.length = length;
		this.inUse = false;
	}

	public TextParserToken copy() {
		return new TextParserToken(type, start, length);
	}

	public void setAll(TextParserToken.Type type, int start, int length) {
		this.type = type;
		this.start = start;
		this.length = length;
		this.inUse = false;
	}

	public boolean isInUse() {
		return inUse;
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

	public TextParserToken use() {
		this.inUse = true;
		return this;
	}

	public void forget() {
		this.inUse = false;
	}

	public enum Type {
		OPERATION,
		START_OBJECT,
		END_OBJECT,
		STRING,
		NONE,
	}

}
