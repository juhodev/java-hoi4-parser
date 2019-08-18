package dev.juho.hoi4.parser.textparser.token;

import dev.juho.hoi4.parser.data.HOIEnum;
import dev.juho.hoi4.utils.Logger;
import dev.juho.hoi4.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TextTokenizer {

	public static final char NEW_LINE = '\n';
	public static final char TAB = '\t';
	public static final char WHITESPACE = ' ';
	public static final char STRING_START = '"';
	public static final char COMMENT_START = '#';
	public static final char K_V_SEPARATOR = '=';
	public static final char LINE_RETURN = '\r';
	public static final char START_OBJECT = '{';
	public static final char END_OBJECT = '}';
	public static final String OPERATIONS = "=";

	private boolean isKey;
	private List<TextParserToken> tokens;

	private int position;

	public TextTokenizer() {
		this.isKey = true;
		this.tokens = new ArrayList<>();
		this.position = 0;
	}

	public void createTokens(TextParserInputStream in) {
		while (!in.eof()) {
			TextParserToken token = read(in);

			if (token != null) {
				tokens.add(token);
//				Logger.getInstance().log(Logger.DEBUG, token.getType() + " - " + token.getValue().toString());
			}

//			if (tokens.size() % 1000000 == 0) {
//				Logger.getInstance().log(Logger.DEBUG, tokens.size() + " tokens");
//			}
		}
	}

	public TextParserToken peek() {
		return tokens.get(position);
	}

	public TextParserToken next() {
		return tokens.get(position++);
	}

	public boolean eof() {
		return position >= tokens.size();
	}

	public int getPosition() {
		return position;
	}

	public List<TextParserToken> getTokens() {
		return tokens;
	}

	private TextParserToken read(TextParserInputStream in) {
		char nextChar = in.peek();

		if (nextChar == WHITESPACE || nextChar == TAB || nextChar == NEW_LINE || nextChar == LINE_RETURN)
			return ignoreUntilSomethingNotStupid(in);
		if (nextChar == COMMENT_START) return readComment(in);
		if (nextChar == START_OBJECT) return readOpenObject(in);
		if (nextChar == END_OBJECT) return readEndObject(in);
		if (isStartOfKey()) return readKey(in);
		if (nextChar == STRING_START) return readString(in);
		if (isOperation(nextChar)) return readOperation(in);
		if (isStartOfKeyEnumOrBoolean(nextChar)) return readEnumOrBoolean(in);
		if (isDigit(nextChar)) return readNumber(in);
		return null;
	}

	private TextParserToken readOpenObject(TextParserInputStream in) {
		char nextChar = in.next();
		isKey = true;

		return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.START_OBJECT, nextChar);
	}

	private TextParserToken readEndObject(TextParserInputStream in) {
		char nextChar = in.next();
		isKey = true;

		return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.END_OBJECT, nextChar);
	}

	private TextParserToken readOperation(TextParserInputStream in) {
		char nextChar = in.next();

		return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.OPERATION, nextChar);
	}

	private TextParserToken readKey(TextParserInputStream in) {
		StringBuilder builder = new StringBuilder();
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
				builder.append(in.next());
			}
		}

		if (isProbablyAValue) {
			String str = builder.toString();

//			match both

			if (str.matches("^[0-9]*$")) {
				if (str.length() > 9) {
					return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.LONG, Long.parseLong(str));
				} else {
					return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.INTEGER, Integer.parseInt(str));
				}
			} else if (str.matches("^([0-9]|\\.)*$")) {
				return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.DOUBLE, Double.parseDouble(builder.toString()));
			} else if (str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("no")) {
				return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.BOOLEAN, str);
			} else if (Utils.hasEnum(HOIEnum.values(), str)) {
				return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.ENUM, HOIEnum.valueOf(str.toUpperCase()));
			} else {
				if (str.equalsIgnoreCase("HOI4txt")) return null;
				return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.STRING, str);
			}
		}
		return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.KEY, builder.toString());
	}

	private TextParserToken readEnumOrBoolean(TextParserInputStream in) {
		StringBuilder builder = new StringBuilder();

		while (!in.eof()) {
			char next = in.peek();

			if (next == K_V_SEPARATOR || next == WHITESPACE || next == NEW_LINE || next == LINE_RETURN) {
				isKey = true;
				break;
			} else {
				builder.append(in.next());
			}
		}

		String str = builder.toString();

		if (str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("no")) {
			return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.BOOLEAN, str);
		} else if (Utils.hasEnum(HOIEnum.values(), str)) {
			return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.ENUM, HOIEnum.valueOf(str.toUpperCase()));
		} else {
			return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.STRING, str.toUpperCase());
		}
	}

	private TextParserToken ignoreUntilSomethingNotStupid(TextParserInputStream in) {
		in.next();

		while (!in.eof()) {
			char next = in.peek();

			if (next != WHITESPACE || next != TAB || next == NEW_LINE || next == LINE_RETURN) {
				isKey = true;
				break;
			}
			in.next();
		}

		return null;
	}

	private TextParserToken readComment(TextParserInputStream in) {
//		Skip #
		in.next();
		while (!in.eof()) {
			char next = in.next();

			if (next == NEW_LINE) break;
		}

		return null;
	}

	private TextParserToken readString(TextParserInputStream in) {
		StringBuilder builder = new StringBuilder();

		// Skip "
		in.next();
		while (!in.eof()) {
			char next = in.next();

			if (next == STRING_START) {
				isKey = true;
				break;
			} else {
				builder.append(next);
			}
		}

		return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.STRING, builder.toString());
	}

	private TextParserToken readNumber(TextParserInputStream in) {
		StringBuilder builder = new StringBuilder();
		TextParserToken.Type type = TextParserToken.Type.INTEGER;

		while (!in.eof()) {
			char next = in.next();

			if (isDigit(next)) {
				builder.append(next);
			} else if (next == '.') {
				builder.append(next);
				type = TextParserToken.Type.DOUBLE;
			} else {
				isKey = true;
				break;
			}
		}

		if (type == TextParserToken.Type.INTEGER) {
			String str = builder.toString();
			if (str.length() <= 9) {
				return new TextParserToken(new int[]{in.getLine(), in.getCol()}, type, Integer.parseInt(str));
			} else {
				return new TextParserToken(new int[]{in.getLine(), in.getCol()}, TextParserToken.Type.LONG, Long.parseLong(str));
			}
		} else {
			return new TextParserToken(new int[]{in.getLine(), in.getCol()}, type, Double.parseDouble(builder.toString()));
		}
	}

	private boolean isStartOfKey() {
		return isKey;
	}

	private boolean isStartOfKeyEnumOrBoolean(char c) {
		return Character.isLetter(c);
	}

	private boolean isOperation(char c) {
		return OPERATIONS.indexOf(c) != -1;
	}

	private boolean isDigit(char c) {
		return (Character.isDigit(c) || c == '-');
	}

}
