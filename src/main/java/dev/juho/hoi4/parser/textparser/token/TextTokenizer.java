package dev.juho.hoi4.parser.textparser.token;

import dev.juho.hoi4.parser.textparser.gamefile.GFNode;
import dev.juho.hoi4.profiler.Profiler;
import dev.juho.hoi4.utils.Logger;
import jdk.internal.jline.internal.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TextTokenizer {

	private byte[] fileContent;
	private int totalRead;


	private static final short STRING = 0;
	private static final short EQUALS = 2;
	private static final short OPEN_BRACKET = 3;
	private static final short CLOSED_BRACKET = 4;

	private static final byte NUMBER_TYPE_INT = 0;
	private static final byte NUMBER_TYPE_LONG = 1;
	private static final byte NUMBER_TYPE_DOUBLE = 2;
	private static final byte NUMBER_TYPE_STRING = 3;

	private long[] tokens;
	private int tokensLength, tokensRead;

	public TextTokenizer() {
		this.tokens = new long[1024 * 4 * 4];
		this.tokensLength = 0;
		this.tokensRead = 0;

		this.totalRead = 0;
	}

	public void read(InputStream is) throws IOException {
		Logger.getInstance().time("tokenizer read");
		BufferedInputStream bis = new BufferedInputStream(is);

		fileContent = new byte[bis.available()];
		byte[] buffer = new byte[4096 * 4];

		int read;
		while ((read = bis.read(buffer)) != -1) {
			System.arraycopy(buffer, 0, fileContent, totalRead, read);
			totalRead += read;
		}

		process();
		Logger.getInstance().timeEnd(Logger.DEBUG, "tokenizer read");
	}

	public void process() {
		final byte equals = (byte) '=';
		final byte openBracket = (byte) '{';
		final byte closedBracket = (byte) '}';
		final byte whitespace = (byte) ' ';
		final byte quote = (byte) '"';

		int strStart = -1;
		boolean quotedStr = false;

		for (int i = 0; i < fileContent.length; i++) {
			byte b = fileContent[i];

			if (b >= 9 && b <= 13) {
				if (strStart != -1) {
					addToken(strStart, (short) (i - strStart), STRING);
					strStart = -1;
				}
				continue;
			}

			if (b == equals) {
				if (strStart != -1) {
					addToken(strStart, (short) (i - strStart), STRING);
					strStart = -1;
				}
				addToken(i, (short) 1, EQUALS);
				continue;
			}

			if (b == openBracket) {
				if (strStart != -1) {
					addToken(strStart, (short) (i - strStart), STRING);
					strStart = -1;
				}
				addToken(i, (short) 1, OPEN_BRACKET);
				continue;
			}

			if (b == closedBracket) {
				if (strStart != -1) {
					addToken(strStart, (short) (i - strStart), STRING);
					strStart = -1;
				}
				addToken(i, (short) 1, CLOSED_BRACKET);
				continue;
			}

			if (b == whitespace) {
				if (strStart != -1 && !quotedStr) {
					addToken(strStart, (short) (i - strStart), STRING);
					strStart = -1;
				}
				continue;
			}

			if (b == quote) {
				if (strStart != -1 && quotedStr) {
					addToken(strStart, (short) (i - strStart), STRING);
					strStart = -1;
					quotedStr = false;
					continue;
				}
				quotedStr = true;
				strStart = i + 1;
				continue;
			}

			if (strStart == -1) {
				strStart = i;
			}
		}

		if (strStart != -1) {
			addToken(strStart, (short) (fileContent.length - strStart), STRING);
		}
	}

	public enum Type {
		EQUALS,
		OPEN_BRACKET,
		CLOSED_BRACKET,
		STRING,
		NONE,
	}

	public Type peek() {
		return peek(0);
	}

	public Type peek(int length) {
		if (tokensRead + length >= tokensLength) {
			return Type.NONE;
		}

		long token = tokens[tokensRead + length];
		short type = (short) token;

		switch (type) {
			case STRING:
				return Type.STRING;

			case EQUALS:
				return Type.EQUALS;

			case OPEN_BRACKET:
				return Type.OPEN_BRACKET;

			case CLOSED_BRACKET:
				return Type.CLOSED_BRACKET;

			default:
				return Type.NONE;
		}
	}

	public int getPosition() {
		return tokensRead;
	}

	public Object readString() {
		final byte zeroChar = (byte) '0';
		final byte nineChar = (byte) '9';
		final byte dashChar = (byte) '-';

		final byte yChar = (byte) 'y';
		final byte eChar = (byte) 'e';
		final byte sChar = (byte) 's';
		final byte nChar = (byte) 'n';
		final byte oChar = (byte) 'o';

		long curr = tokens[tokensRead++];

		int currStart = (int) (curr >>> (16 + 16));
		short currLength = (short) (curr >>> 16);

		// This means the string is actually a number (starts with either a "-" or a number)
		if ((fileContent[currStart] == dashChar && (fileContent[currStart + 1] >= zeroChar && fileContent[currStart + 1] <= nineChar)) || (fileContent[currStart] >= zeroChar && fileContent[currStart] <= nineChar)) {
			byte numberType = getNumberType(fileContent, currStart, currLength);

			if (numberType == NUMBER_TYPE_INT) {
				return charArrayToInt(fileContent, currStart, currLength);
			}

			if (numberType == NUMBER_TYPE_LONG) {
				return charArrayToLong(fileContent, currStart, currLength);
			}

			if (numberType == NUMBER_TYPE_DOUBLE) {
				return charArrayToDouble(fileContent, currStart, currLength);
			}
		}

		if (currLength == 2 || currLength == 3) {
			boolean stringIsYes = fileContent[currStart] == yChar && fileContent[currStart + 1] == eChar && fileContent[currStart + 2] == sChar;
			boolean stringIsNo = fileContent[currStart] == nChar && fileContent[currStart + 1] == oChar;

			if (stringIsYes) {
				return true;
			}

			if (stringIsNo) {
				return false;
			}
		}

		return new String(fileContent, currStart, currLength);
	}

	public boolean eof() {
		return peek() == Type.NONE;
	}

	public void skip() {
		tokensRead++;
	}

	private void addToken(int start, short length, short type) {
		long packed = pack(start, length, type);

		if (tokensLength == tokens.length - 1) {
			resizeTokens();
		}

		tokens[tokensLength++] = packed;
	}

	private long pack(int start, short length, short type) {
		return (long) start << (16 + 16) | (long) length << 16 | (long) type;
	}

	private void resizeTokens() {
		long[] newTokens = new long[tokens.length * 2];
		System.arraycopy(tokens, 0, newTokens, 0, tokensLength);
		tokens = newTokens;
	}

	private byte getNumberType(byte[] value, int start, int length) {
		boolean alreadySeenDot = false;
		for (int i = start; i < start + length; i++) {
			if (value[i] == '-' && i == start) continue;
			if (Character.isDigit(value[i])) continue;
			if (value[i] == '.') {
				if (alreadySeenDot) {
					return NUMBER_TYPE_STRING;
				} else {
					alreadySeenDot = true;
					continue;
				}
			}

			return NUMBER_TYPE_STRING;
		}

		if (alreadySeenDot) {
			return NUMBER_TYPE_DOUBLE;
		} else {
			if (length >= 9) {
				return NUMBER_TYPE_LONG;
			} else {
				return NUMBER_TYPE_INT;
			}
		}
	}

	//	https://stackoverflow.com/a/12297485
	private int charArrayToInt(byte[] arr, int start, int length) {
		int result = 0;

		for (int i = start; i < start + length; i++) {
			int digit = (int) arr[i] - (int) '0';
			result *= 10;
			result += digit;
		}

		return result;
	}

	private long charArrayToLong(byte[] arr, int start, int length) {
		long result = 0;

		for (int i = start; i < start + length; i++) {
			int digit = (int) arr[i] - (int) '0';
			result *= 10;
			result += digit;
		}

		return result;
	}

	private double charArrayToDouble(byte[] arr, int start, int length) {
		double result = 0;
		boolean seenDot = false;
		int count = 1;

		for (int i = start; i < start + length; i++) {
			if (arr[i] == '.') {
				seenDot = true;
				continue;
			}

			int digit = (int) arr[i] - (int) '0';
			if (seenDot) {
				double num = (double) digit / count;
				count *= 10;
				result += num;
			} else {
				result *= 10;
				result += digit;
			}
		}

		return result;
	}

}
