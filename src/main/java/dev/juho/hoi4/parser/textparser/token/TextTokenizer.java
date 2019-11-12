package dev.juho.hoi4.parser.textparser.token;

import dev.juho.hoi4.parser.ParserInputStream;
import dev.juho.hoi4.utils.CharArray;

public class TextTokenizer {

	private static final char NEW_LINE = '\n';
	private static final char TAB = '\t';
	private static final char WHITESPACE = ' ';
	private static final char STRING_START = '"';
	private static final char COMMENT_START = '#';
	private static final char LINE_RETURN = '\r';
	private static final char START_OBJECT = '{';
	private static final char END_OBJECT = '}';
	private static final char EQUALS = '=';

	private TextParserToken[] tokens;
	private int tokensCapacity;
	private int tokensSize;

	private int position;

	private ParserInputStream in;

	public TextTokenizer(ParserInputStream in, int capacity) {
		this.in = in;
		this.tokens = new TextParserToken[capacity];
		this.tokensCapacity = capacity;
		this.tokensSize = 0;
		this.position = 0;
	}

	private void createNewTokens() {
		tokensSize = 0;
		position = 0;
		while (!in.eof()) {
			TextParserToken token = read();

			if (token != null) {
				tokens[tokensSize++] = token;
			}

			if (tokensSize >= tokensCapacity) {
				break;
			}
		}
	}

	public TextParserToken peek() {
		if (position == tokensSize) {
			createNewTokens();
		}

		return tokens[position];
	}

	public TextParserToken next() {
		if (position == tokensSize) {
			createNewTokens();
		}

		return tokens[position++];
	}

	public boolean eof() {
		return position > 0 && position == tokensSize && tokensSize != tokensCapacity;
	}

	public int getPosition() {
		return position;
	}

	public TextParserToken[] getTokens() {
		return tokens;
	}

	private TextParserToken read() {
		char nextChar = in.peekChar();

		while (nextChar == WHITESPACE || nextChar == TAB || nextChar == NEW_LINE || nextChar == LINE_RETURN) {
			in.nextChar();
			nextChar = in.peekChar();
		}

		if (nextChar == COMMENT_START) readComment();
		if (nextChar == START_OBJECT) return readOpenObject();
		if (nextChar == END_OBJECT) return readEndObject();
		if (nextChar == STRING_START) {
			CharArray chars = readString(true, STRING_START);
			return new TextParserToken(TextParserToken.Type.STRING, chars.chars(), chars.size());
		}
		if (nextChar == EQUALS) return readEquals();
		CharArray chars = readString(false, EQUALS, WHITESPACE, TAB, NEW_LINE, LINE_RETURN);
		return new TextParserToken(TextParserToken.Type.STRING, chars.chars(), chars.size());
	}

	private TextParserToken readOpenObject() {
		char nextChar = in.nextChar();

		return new TextParserToken(TextParserToken.Type.START_OBJECT, new char[]{nextChar}, 1);
	}

	private TextParserToken readEndObject() {
		char nextChar = in.nextChar();

		return new TextParserToken(TextParserToken.Type.END_OBJECT, new char[]{nextChar}, 1);
	}

	private TextParserToken readEquals() {
		char nextChar = in.nextChar();

		return new TextParserToken(TextParserToken.Type.OPERATION, new char[]{nextChar}, 1);
	}

	private void readComment() {
//		Skip #
		in.nextChar();
		while (true) {
			char next = in.nextChar();

			if (next == NEW_LINE) break;
		}
	}

	private CharArray readString(boolean skip, char... end) {
		CharArray chars = new CharArray(30);
		// Skip "
		if (skip) in.nextChar();
		else chars.append(in.nextChar());

		if (in.eof()) {
			return chars;
		}

		char next = in.peekChar();

		while (!containsChar(end, next)) {
			chars.append(in.nextChar());
			if (!in.eof()) {
				next = in.peekChar();
			} else {
				break;
			}
		}

//		This will only happen when parsing a string that starts and ends with "
		if (skip && next == STRING_START) in.nextChar();

		return chars;
	}

	private boolean containsChar(char[] array, char c) {
		for (char x : array) {
			if (x == c) return true;
		}

		return false;
	}

}
