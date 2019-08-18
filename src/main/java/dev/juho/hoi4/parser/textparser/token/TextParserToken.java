package dev.juho.hoi4.parser.textparser.token;

public class TextParserToken<T> {

	private Type type;
	private T value;
	public int[] position;

	public TextParserToken(int[] position, Type type, T value) {
		this.position = position;
		this.type = type;
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public T getValue() {
		return value;
	}

	public int[] getPosition() {
		return position;
	}

	public enum Type {
		OPERATION,
		LONG,
		DOUBLE,
		INTEGER,
		START_OBJECT,
		END_OBJECT,
		BOOLEAN,
		ENUM,
		KEY,
		STRING,
	}

}
