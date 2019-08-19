package dev.juho.hoi4.parser.textparser.token;

import dev.juho.hoi4.parser.data.HOIEnum;
import dev.juho.hoi4.utils.Utils;


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

	private StringBuilder strBuilder;
	private TextParserInputStream in;

	public TextTokenizer(TextParserInputStream in, int capacity) {
		this.in = in;
		this.isKey = true;
		this.tokens = new TextParserToken[capacity];
		this.tokensCapacity = capacity;
		this.tokensSize = 0;
		this.position = 0;
		this.strBuilder = new StringBuilder();
	}

	private void createTokens(TextParserInputStream in) {
		while (!in.eof()) {
			TextParserToken token = read();

			if (token != null) {
				tokens[tokensSize++] = token;
//				Logger.getInstance().log(Logger.DEBUG, token.getType() + " - " + token.getValue().toString());
			}

//			if (tokens.size() % 1000000 == 0) {
//				Logger.getInstance().log(Logger.DEBUG, tokens.size() + " tokens");
//			}
		}
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
		if (isStartOfKey()) return readKey();
		if (nextChar == STRING_START) return readString();
		if (nextChar == EQUALS) return readEquals();
		if (isStartOfKeyEnumOrBoolean(nextChar)) return readEnumOrBoolean();
		if (isDigit(nextChar)) return readNumber();
		return null;
	}

	private TextParserToken readOpenObject() {
		char nextChar = in.next();
		isKey = true;

		return new TextParserToken<>(TextParserToken.Type.START_OBJECT, nextChar);
	}

	private TextParserToken readEndObject() {
		char nextChar = in.next();
		isKey = true;

		return new TextParserToken<>(TextParserToken.Type.END_OBJECT, nextChar);
	}

	private TextParserToken readEquals() {
		char nextChar = in.next();

		return new TextParserToken<>(TextParserToken.Type.OPERATION, nextChar);
	}

	private TextParserToken readKey() {
		boolean isProbablyAValue = false;

		while (!in.eof()) {
			char next = in.peek();

			if (next == K_V_SEPARATOR) {
				isKey = false;
				break;
			} else if (next == WHITESPACE || next == NEW_LINE || next == LINE_RETURN || next == END_OBJECT) {
				isKey = false;
				isProbablyAValue = true;
				break;
			} else {
				strBuilder.append(in.next());
			}
		}

		if (isProbablyAValue) {
			String str = strBuilder.toString();
			strBuilder.setLength(0);

			if (str.matches("^[0-9]*$")) {
				if (str.length() > 9) {
					return new TextParserToken<>(TextParserToken.Type.LONG, Long.parseLong(str));
				} else {
					return new TextParserToken<>(TextParserToken.Type.INTEGER, Integer.parseInt(str));
				}
			} else if (str.matches("^([0-9]|\\.)*$")) {
				return new TextParserToken<>(TextParserToken.Type.DOUBLE, Double.parseDouble(str));
			} else if (str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("no")) {
				return new TextParserToken<>(TextParserToken.Type.BOOLEAN, str);
			} else if (Utils.hasEnum(HOIEnum.values(), str)) {
				return new TextParserToken<>(TextParserToken.Type.ENUM, HOIEnum.valueOf(str.toUpperCase()));
			} else {
				if (str.equalsIgnoreCase("HOI4txt")) return null;
				return new TextParserToken<>(TextParserToken.Type.STRING, str);
			}
		}
		String str = strBuilder.toString();
		strBuilder.setLength(0);
		return new TextParserToken<>(TextParserToken.Type.KEY, str);
	}

	private TextParserToken readEnumOrBoolean() {
		while (!in.eof()) {
			char next = in.peek();

			if (next == K_V_SEPARATOR || next == WHITESPACE || next == NEW_LINE || next == LINE_RETURN) {
				isKey = true;
				break;
			} else {
				strBuilder.append(in.next());
			}
		}

		String str = strBuilder.toString();
		strBuilder.setLength(0);

		if (str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("no")) {
			return new TextParserToken<>(TextParserToken.Type.BOOLEAN, str);
		} else if (Utils.hasEnum(HOIEnum.values(), str)) {
			return new TextParserToken<>(TextParserToken.Type.ENUM, HOIEnum.valueOf(str.toUpperCase()));
		} else {
			return new TextParserToken<>(TextParserToken.Type.STRING, str.toUpperCase());
		}
	}

	private TextParserToken ignoreUntilSomethingNotStupid() {
		in.next();

		while (!in.eof()) {
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
		while (!in.eof()) {
			char next = in.next();

			if (next == NEW_LINE) break;
		}

		return null;
	}

	private TextParserToken readString() {
		// Skip "
		in.next();
		while (!in.eof()) {
			char next = in.next();

			if (next == STRING_START) {
				isKey = true;
				break;
			} else {
				strBuilder.append(next);
			}
		}

		String str = strBuilder.toString();
		strBuilder.setLength(0);
		return new TextParserToken<>(TextParserToken.Type.STRING, str);
	}

	private TextParserToken readNumber() {
		TextParserToken.Type type = TextParserToken.Type.INTEGER;

		while (!in.eof()) {
			char next = in.next();

			if (isDigit(next)) {
				strBuilder.append(next);
			} else if (next == '.') {
				strBuilder.append(next);
				type = TextParserToken.Type.DOUBLE;
			} else {
				isKey = true;
				break;
			}
		}

		if (type == TextParserToken.Type.INTEGER) {
			String str = strBuilder.toString();
			strBuilder.setLength(0);
			if (str.length() <= 9) {
				return new TextParserToken<>(type, Integer.parseInt(str));
			} else {
				return new TextParserToken<>(TextParserToken.Type.LONG, Long.parseLong(str));
			}
		} else {
			String str = strBuilder.toString();
			strBuilder.setLength(0);
			return new TextParserToken<>(type, Double.parseDouble(str));
		}
	}

	private boolean isStartOfKey() {
		return isKey;
	}

	private boolean isStartOfKeyEnumOrBoolean(char c) {
		return Character.isLetter(c);
	}

	private boolean isDigit(char c) {
		return (Character.isDigit(c) || c == '-');
	}

}
