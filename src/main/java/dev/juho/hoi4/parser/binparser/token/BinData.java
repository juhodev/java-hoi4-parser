package dev.juho.hoi4.parser.binparser.token;

public class BinData {

	private int code;
	private String text;
	private DataType dataType;
	private boolean quoted;

	public BinData(int code, String text, DataType dataType, boolean quoted) {
		this.code = code;
		this.text = text;
		this.dataType = dataType;
		this.quoted = quoted;
	}

	public int getCode() {
		return code;
	}

	public DataType getDataType() {
		return dataType;
	}

	public String getText() {
		return text;
	}

	public boolean isQuoted() {
		return quoted;
	}

	public enum DataType {
		SPECIAL,
		INTEGER,
		STRING,
		VARIABLE,
		FLOAT,
		UNSPECIFIED,
		BOOLEAN,
		DATE,
		FLOAT5,
	}

}
