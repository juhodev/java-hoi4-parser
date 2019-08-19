package dev.juho.hoi4.parser.textparser.token;

public class TextParserToken<T> {

	private Type type;
	private T value;

	public TextParserToken(Type type, T value) {
		this.type = type;
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public T getValue() {
		return value;
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
