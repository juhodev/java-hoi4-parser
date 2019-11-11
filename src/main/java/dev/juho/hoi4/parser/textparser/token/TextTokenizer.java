package dev.juho.hoi4.parser.textparser.token;

import dev.juho.hoi4.parser.ParserInputStream;
import dev.juho.hoi4.utils.CharArray;

public class TextTokenizer {

	private static final char NEW_LINE = '\n';
	private static final char TAB = '\t';
	private static final char WHITESPACE = ' ';
	private static final char STRING_START = '"';
	private static final char COMMENT_START = '#';
	private static final char K_V_SEPARATOR = '=';
	private static final char LINE_RETURN = '\r';
	private static final char START_OBJECT = '{';
	private static final char END_OBJECT = '}';
	private static final char EQUALS = '=';

	private boolean isKey;
	private TextParserToken[] tokens;
	private int tokensCapacity;
	private int tokensSize;

	private int position;

	private ParserInputStream in;

	private boolean hasSeenFileIdentifier;

	public TextTokenizer(ParserInputStream in, int capacity) {
		this.in = in;
		this.isKey = true;
		this.tokens = new TextParserToken[capacity];
		this.tokensCapacity = capacity;
		this.tokensSize = 0;
		this.position = 0;
		this.hasSeenFileIdentifier = false;
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
		return in.eof();
	}

	public int getPosition() {
		return position;
	}

	public TextParserToken[] getTokens() {
		return tokens;
	}

	private TextParserToken read() {
		char nextChar = in.peekChar();

		if (nextChar == WHITESPACE || nextChar == TAB || nextChar == NEW_LINE || nextChar == LINE_RETURN)
			return ignoreUntilSomethingNotStupid();
		if (nextChar == COMMENT_START) return readComment();
		if (nextChar == START_OBJECT) return readOpenObject();
		if (nextChar == END_OBJECT) return readEndObject();
		if (isKey) return readKey();
		if (nextChar == STRING_START) {
			CharArray chars = readString(true);
			return new TextParserToken(TextParserToken.Type.STRING, chars.chars(), chars.size());
		}
		if (nextChar == EQUALS) return readEquals();
		if (Character.isLetter(nextChar)) return readEnumOrBoolean();
		if (Character.isDigit(nextChar) || nextChar == '-') return readNumber();
		return null;
	}

	private TextParserToken readOpenObject() {
		char nextChar = in.nextChar();
		isKey = true;

		return new TextParserToken(TextParserToken.Type.START_OBJECT, new char[]{nextChar}, 1);
	}

	private TextParserToken readEndObject() {
		char nextChar = in.nextChar();
		isKey = true;

		return new TextParserToken(TextParserToken.Type.END_OBJECT, new char[]{nextChar}, 1);
	}

	private TextParserToken readEquals() {
		char nextChar = in.nextChar();

		return new TextParserToken(TextParserToken.Type.OPERATION, new char[]{nextChar}, 1);
	}

	private TextParserToken readKey() {
		boolean isProbablyAValue = false;
		CharArray chars = new CharArray(25);

		while (true) {
			char next = in.peekChar();
			if (next == STRING_START) {
				CharArray strArray = readString(false);
				char peek = in.peekChar();

				if (peek == WHITESPACE || peek == NEW_LINE || peek == LINE_RETURN || peek == END_OBJECT) {
					return new TextParserToken(TextParserToken.Type.STRING, strArray.copy(1, strArray.size() - 1), strArray.size() - 1);
				}

				strArray.append('"');
				chars = strArray;
				continue;
			}

			if (next == K_V_SEPARATOR) {
				isKey = false;
				break;
			} else if (next == WHITESPACE || next == NEW_LINE || next == LINE_RETURN || next == END_OBJECT) {
				isKey = false;
				isProbablyAValue = true;
				break;
			} else {
				chars.append(in.nextChar());
			}
		}

		if (isProbablyAValue) {
			TextParserToken.Type type = arrType(chars);
			if (type == TextParserToken.Type.INTEGER) {
				if (chars.size() > 9) {
					return new TextParserToken(TextParserToken.Type.LONG, chars.chars(), chars.size());
				} else {
					return new TextParserToken(TextParserToken.Type.INTEGER, chars.chars(), chars.size());
				}
			} else if (type == TextParserToken.Type.DOUBLE) {
				return new TextParserToken(TextParserToken.Type.DOUBLE, chars.chars(), chars.size());
			} else if (chars.startsWith('y', 'e', 's') || chars.startsWith('n', 'o')) {
				return new TextParserToken(TextParserToken.Type.BOOLEAN, chars.chars(), chars.size());
			} else {
				if (!hasSeenFileIdentifier) {
					if (chars.startsWith('H', 'O', 'I', '4', 't', 'x', 't')) {
						hasSeenFileIdentifier = true;
						return null;
					}
				}

				return new TextParserToken(TextParserToken.Type.STRING, chars.chars(), chars.size());
			}
		}
		return new TextParserToken(TextParserToken.Type.KEY, chars.chars(), chars.size());
	}

	private TextParserToken readEnumOrBoolean() {
		CharArray chars = new CharArray(3);
		while (true) {
			char next = in.peekChar();

			if (next == K_V_SEPARATOR || next == WHITESPACE || next == NEW_LINE || next == LINE_RETURN) {
				isKey = true;
				break;
			} else {
				chars.append(in.nextChar());
			}
		}

		if (chars.size() <= 3) {
			if (chars.startsWith('y', 'e', 's') || chars.startsWith('n', 'o')) {
				return new TextParserToken(TextParserToken.Type.BOOLEAN, chars.chars(), chars.size());
			}
		}

		return new TextParserToken(TextParserToken.Type.STRING, chars.chars(), chars.size());
	}

	private TextParserToken ignoreUntilSomethingNotStupid() {
		in.nextChar();

		while (true) {
			char next = in.peekChar();

			if (next != WHITESPACE && next != TAB) {
				isKey = true;
				break;
			}
			in.nextChar();
		}

		return null;
	}

	private TextParserToken readComment() {
//		Skip #
		in.nextChar();
		while (true) {
			char next = in.nextChar();

			if (next == NEW_LINE) break;
		}

		return null;
	}

	private CharArray readString(boolean skip) {
		CharArray chars = new CharArray(30);
		// Skip "
		if (skip) in.nextChar();
		else chars.append(in.nextChar());

		while (true) {
			char next = in.nextChar();

			if (next == STRING_START) {
				isKey = true;
				break;
			} else {
				chars.append(next);
			}
		}

		return chars;
	}

	private TextParserToken readNumber() {
		TextParserToken.Type type = TextParserToken.Type.INTEGER;
		CharArray chars = new CharArray(12);

		while (true) {
			char next = in.nextChar();

			if (Character.isDigit(next) || next == '-') {
				chars.append(next);
			} else if (next == '.') {
				chars.append(next);
				type = TextParserToken.Type.DOUBLE;
			} else {
				isKey = true;
				break;
			}
		}

		if (type == TextParserToken.Type.INTEGER) {
			if (chars.size() <= 9) {
				return new TextParserToken(type, chars.chars(), chars.size());
			} else {
				return new TextParserToken(TextParserToken.Type.LONG, chars.chars(), chars.size());
			}
		} else {
			return new TextParserToken(type, chars.chars(), chars.size());
		}
	}

	private TextParserToken.Type arrType(CharArray array) {
		char[] arr = array.chars();
		boolean hasDot = false;

		for (int i = 0; i < array.size(); i++) {
			char c = arr[i];

			if (Character.isDigit(c)) {
				continue;
			}

			if (c == '.') {
				hasDot = true;
				continue;
			}

			return TextParserToken.Type.STRING;
		}

		if (hasDot) {
			return TextParserToken.Type.DOUBLE;
		}

		return TextParserToken.Type.INTEGER;
	}

}
