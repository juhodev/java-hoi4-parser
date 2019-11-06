package dev.juho.hoi4.parser.textparser.token;

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

	private TextParserInputStream in;

	private boolean hasSeenFileIdentifier;

	public TextTokenizer(TextParserInputStream in, int capacity) {
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
		char nextChar = in.peek();

		if (nextChar == WHITESPACE || nextChar == TAB || nextChar == NEW_LINE || nextChar == LINE_RETURN)
			return ignoreUntilSomethingNotStupid();
		if (nextChar == COMMENT_START) return readComment();
		if (nextChar == START_OBJECT) return readOpenObject();
		if (nextChar == END_OBJECT) return readEndObject();
		if (isKey) return readKey();
		if (nextChar == STRING_START) return readString();
		if (nextChar == EQUALS) return readEquals();
		if (Character.isLetter(nextChar)) return readEnumOrBoolean();
		if (Character.isDigit(nextChar) || nextChar == '-') return readNumber();
		return null;
	}

	private TextParserToken readOpenObject() {
		char nextChar = in.next();
		isKey = true;

		return new TextParserToken(TextParserToken.Type.START_OBJECT, new char[]{nextChar}, 1);
	}

	private TextParserToken readEndObject() {
		char nextChar = in.next();
		isKey = true;

		return new TextParserToken(TextParserToken.Type.END_OBJECT, new char[]{nextChar}, 1);
	}

	private TextParserToken readEquals() {
		char nextChar = in.next();

		return new TextParserToken(TextParserToken.Type.OPERATION, new char[]{nextChar}, 1);
	}

	private TextParserToken readKey() {
		boolean isProbablyAValue = false;
		CharArray chars = new CharArray(20);

		while (true) {
			char next = in.peek();

			if (next == K_V_SEPARATOR) {
				isKey = false;
				break;
			} else if (next == WHITESPACE || next == NEW_LINE || next == LINE_RETURN || next == END_OBJECT) {
				isKey = false;
				isProbablyAValue = true;
				break;
			} else {
				chars.append(in.next());
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
		CharArray chars = new CharArray(30);

		while (true) {
			char next = in.peek();

			if (next == K_V_SEPARATOR || next == WHITESPACE || next == NEW_LINE || next == LINE_RETURN) {
				isKey = true;
				break;
			} else {
				chars.append(in.next());
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
		in.next();

		while (true) {
			char next = in.peek();

			if (next != WHITESPACE && next != TAB) {
				isKey = true;
				break;
			}
			in.next();
		}

		return null;
	}

	private TextParserToken readComment() {
//		Skip #
		in.next();
		while (true) {
			char next = in.next();

			if (next == NEW_LINE) break;
		}

		return null;
	}

	private TextParserToken readString() {
		// Skip "
		in.next();

		CharArray chars = new CharArray(30);
		while (true) {
			char next = in.next();

			if (next == STRING_START) {
				isKey = true;
				break;
			} else {
				chars.append(next);
			}
		}

		return new TextParserToken(TextParserToken.Type.STRING, chars.chars(), chars.size());
	}

	private TextParserToken readNumber() {
		TextParserToken.Type type = TextParserToken.Type.INTEGER;
		CharArray chars = new CharArray(12);

		while (true) {
			char next = in.next();

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
