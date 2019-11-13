package dev.juho.hoi4.parser.textparser.token;

import dev.juho.hoi4.parser.ParserInputStream;

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
			int[] position = readString(true, STRING_START);
			return new TextParserToken(TextParserToken.Type.STRING, position[0], position[1]);
		}
		if (nextChar == EQUALS) return readEquals();
		int[] position = readString(false, EQUALS, WHITESPACE, TAB, NEW_LINE, LINE_RETURN);
		return new TextParserToken(TextParserToken.Type.STRING, position[0], position[1]);
	}

	private TextParserToken readOpenObject() {
		in.nextChar();
		return new TextParserToken(TextParserToken.Type.START_OBJECT, in.getPosition() - 1, 1);
	}

	private TextParserToken readEndObject() {
		in.nextChar();
		return new TextParserToken(TextParserToken.Type.END_OBJECT, in.getPosition() - 1, 1);
	}

	private TextParserToken readEquals() {
		in.nextChar();
		return new TextParserToken(TextParserToken.Type.OPERATION, in.getPosition() - 1, 1);
	}

	private void readComment() {
//		Skip #
		in.nextChar();
		while (true) {
			char next = in.nextChar();

			if (next == NEW_LINE) break;
		}
	}

	/**
	 * @return index 0: start of the string, index: 1 end of the string
	 */
	private int[] readString(boolean skip, char... end) {
		int start = in.getPosition();
		int length = 1;

		in.nextChar();
		// Skip "
		if (skip) {
			start = in.getPosition();
			length = 0;
		}

		if (in.eof()) {
			return new int[]{start, length};
		}

		char next = in.peekChar();

		while (!containsChar(end, next)) {
			in.nextChar();
			length++;
			if (!in.eof()) {
				next = in.peekChar();
			} else {
				break;
			}
		}

//		This will only happen when parsing a string that starts and ends with "
		if (skip && next == STRING_START) in.nextChar();

		return new int[]{start, length};
	}

	private boolean containsChar(char[] array, char c) {
		for (char x : array) {
			if (x == c) return true;
		}

		return false;
	}

}
