package dev.juho.hoi4.parser.textparser.token;

import dev.juho.hoi4.profiler.Profiler;
import dev.juho.hoi4.utils.Logger;
import jdk.internal.jline.internal.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TextTokenizer {

	private byte[] fileContent;
	private int totalRead;

	private int bufferRead;

	private byte[] equalsArray;
	private byte[] openBracketArray;
	private byte[] closedBracketArray;
	private byte[] whitespaceArray;
	private byte[] escapedChars;
	private byte[] quoteArray;
	private byte[] stringArray;

	public TextTokenizer() {
		this.totalRead = 0;
		this.bufferRead = 0;
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

		equalsArray = new byte[fileContent.length];
		openBracketArray = new byte[fileContent.length];
		closedBracketArray = new byte[fileContent.length];
		whitespaceArray = new byte[fileContent.length];
		escapedChars = new byte[fileContent.length];
		quoteArray = new byte[fileContent.length];

		process();
		Logger.getInstance().timeEnd(Logger.INFO, "tokenizer read");
	}

	public void process() {
		findSpecialChars();
		this.stringArray = findStrings();
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
		if (bufferRead + length >= fileContent.length) {
			return Type.NONE;
		}

		// This seems like a bad idea but I guess it works
		if (length != 0 && stringArray[bufferRead + length] == 1) {
			return peekAfterString();
		}

		if (quoteArray[bufferRead + length] == 1) {
			length += 1;

			if (bufferRead + length >= fileContent.length) {
				return Type.NONE;
			}
		}

		if (equalsArray[bufferRead + length] == 1) {
			return Type.EQUALS;
		}

		if (openBracketArray[bufferRead + length] == 1) {
			return Type.OPEN_BRACKET;
		}

		if (closedBracketArray[bufferRead + length] == 1) {
			return Type.CLOSED_BRACKET;
		}

		if (stringArray[bufferRead + length] == 1) {
			return Type.STRING;
		}

		return lookForNext(length);
	}

	public int getPosition() {
		return bufferRead;
	}

	public String readString() {
		if (quoteArray[bufferRead] == 1) {
			bufferRead++;
		}

		int strLength = 0;
		int strStart = bufferRead;
		for (int i = bufferRead; i < stringArray.length; i++) {
			if (stringArray[i] == 1) {
				strLength++;
				continue;
			}

			break;
		}

		bufferRead += strLength;

		if (quoteArray[bufferRead] == 1) {
			bufferRead++;
		}

		return new String(fileContent, strStart, strLength);
	}

	public boolean eof() {
		return peek() == Type.NONE;
	}

	public void skip() {
		bufferRead++;
	}

	private Type peekAfterString() {
		for (int i = bufferRead; i < fileContent.length; i++) {
			if (stringArray[i] != 1) {
				return peek(i - bufferRead);
			}
		}

		return Type.NONE;
	}

	private Type lookForNext(int off) {
		for (int i = bufferRead + off; i < fileContent.length; i++) {
			if (equalsArray[i] == 1) {
				if (off == 0) {
					bufferRead = i;
				}
				return Type.EQUALS;
			}

			if (openBracketArray[i] == 1) {
				if (off == 0) {
					bufferRead = i;
				}
				return Type.OPEN_BRACKET;
			}

			if (closedBracketArray[i] == 1) {
				if (off == 0) {
					bufferRead = i;
				}
				return Type.CLOSED_BRACKET;
			}

			if (stringArray[i] == 1) {
				if (off == 0) {
					bufferRead = i;
				}
				return Type.STRING;
			}
		}

		return Type.NONE;
	}

	private byte[] findStrings() {
		byte[] result = new byte[fileContent.length];

		boolean insideString = false;

		for (int i = 0; i < fileContent.length; i++) {
			if (quoteArray[i] == 1) insideString = !insideString;

			byte b = (byte) ((equalsArray[i] | openBracketArray[i] | closedBracketArray[i] | whitespaceArray[i] | quoteArray[i]) ^ 1);
			if (b == 1) {
				boolean escaped = escapedChars[i] == 1;
				if (escaped) {
					result[i] = 0;
					continue;
				}
			}

			if (whitespaceArray[i] == 1 && insideString) {
				result[i] = 1;
				continue;
			}

			result[i] = b;
		}

		return result;
	}

	private void findSpecialChars() {
		final byte equals = (byte) '=';
		final byte openBracket = (byte) '{';
		final byte closedBracket = (byte) '}';
		final byte whitespace = (byte) ' ';
		final byte quote = (byte) '"';

		for (int i = 0; i < fileContent.length; i++) {
			byte b = fileContent[i];

			if (b >= 9 && b <= 13) {
				escapedChars[i] = 1;
				continue;
			}

			if (b == equals) {
				equalsArray[i] = 1;
				continue;
			}

			if (b == openBracket) {
				openBracketArray[i] = 1;
				continue;
			}

			if (b == closedBracket) {
				closedBracketArray[i] = 1;
				continue;
			}

			if (b == whitespace) {
				whitespaceArray[i] = 1;
				continue;
			}

			if (b == quote) {
				quoteArray[i] = 1;
			}
		}
	}

}
