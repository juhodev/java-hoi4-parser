package dev.juho.hoi4.parser.textparser.token;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

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

	private byte[] buffer;
	private int inputLength, readHead;

	public TextTokenizer(int capacity) {
		this.tokens = new TextParserToken[capacity];
		this.tokensCapacity = capacity;
		this.tokensSize = 0;
		this.position = 0;

		this.inputLength = -1;
		this.readHead = -1;
	}

	public void readInputStream(InputStream in) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(in);
		buffer = new byte[bis.available()];
		inputLength = bis.read(buffer, 0, buffer.length);
		readHead = 0;
	}

	private void createNewTokens() {
		tokensSize = 0;
		position = 0;
		while (readHead != inputLength) {
			TextParserToken token = read();
			tokens[tokensSize++] = token;

			if (tokensSize >= tokensCapacity) {
				break;
			}
		}
	}

	public byte[] getBuffer() {
		return buffer;
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
		byte nextChar = buffer[readHead];

		while (nextChar == WHITESPACE || nextChar == TAB || nextChar == NEW_LINE || nextChar == LINE_RETURN) {
			nextChar = buffer[++readHead];
		}

		if (nextChar == COMMENT_START) readComment();

		switch (nextChar) {
			case START_OBJECT:
				return new TextParserToken(TextParserToken.Type.START_OBJECT, readHead++, 1);

			case END_OBJECT:
				return new TextParserToken(TextParserToken.Type.END_OBJECT, readHead++, 1);

			case EQUALS:
				return new TextParserToken(TextParserToken.Type.OPERATION, readHead++, 1);

			case STRING_START:
				int[] quotedStrPositions = readString(true, STRING_START);
				return new TextParserToken(TextParserToken.Type.STRING, quotedStrPositions[0], quotedStrPositions[1]);

			default:
				int[] strPositions = readString(false, EQUALS, WHITESPACE, TAB, NEW_LINE, LINE_RETURN);
				return new TextParserToken(TextParserToken.Type.STRING, strPositions[0], strPositions[1]);
		}
	}

	private void readComment() {
//		Skip #
		readHead++;
		while (true) {
			byte next = buffer[readHead];

			if (next == NEW_LINE) break;
		}
	}

	/**
	 * @return index 0: start of the string, index: 1 end of the string
	 */
	private int[] readString(boolean skip, char... end) {
		int start = readHead++;
		int length = 1;

		// Skip "
		if (skip) {
			start = readHead;
			length = 0;
		}

		if (readHead == inputLength) {
			return new int[]{start, length};
		}

		byte next = buffer[readHead];

		while (!containsChar(end, next)) {
			readHead++;
			length++;
			if (readHead != inputLength) {
				next = buffer[readHead];
			} else {
				break;
			}
		}

//		This will only happen when parsing a string that starts and ends with "
		if (skip && next == STRING_START) readHead++;

		return new int[]{start, length};
	}

	private boolean containsChar(char[] array, byte c) {
		for (char x : array) {
			if (x == c) return true;
		}

		return false;
	}

}
