package dev.juho.hoi4.parser.binparser.token;

public class BinParserToken<T> {

	private Type type;
	private int code;
	private T value;

	public BinParserToken(Type type, int code, T value){
		this.type = type;
		this.code = code;
		this.value = value;
	}

	public int getCode() {
		return code;
	}

	public T getValue() {
		return value;
	}

	public Type getType() {
		return type;
	}

	public enum Type {
		OPERATION,
		LONG,
		DOUBLE,
		INTEGER,
		START_OBJECT,
		END_OBJECT,
		BOOLEAN,
		KEY,
		STRING,
	}

}
