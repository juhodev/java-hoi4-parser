package dev.juho.hoi4.parser.textparser.gamefile;

import dev.juho.hoi4.parser.textparser.gamefile.nodes.*;
import dev.juho.hoi4.parser.textparser.token.TextParserToken;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import dev.juho.hoi4.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameFile {

	private HashMap<String, Object> nodes;
	private byte[] buffer;

	public GameFile() {
		this.nodes = new HashMap<>();
	}

	public void build(TextTokenizer tokenizer) {
		buffer = tokenizer.getBuffer();
		while (!tokenizer.eof()) {
			GFNode node = read(tokenizer);

			if (node != null) {
				PropertyNode propertyNode = (PropertyNode) node;
				add(propertyNode.getKey(), propertyNode.getValue());
			}
		}
	}

	public HashMap<String, Object> getNodes() {
		return nodes;
	}

	private void add(String key, Object object) {
		if (key.equalsIgnoreCase("countries")) {
			StringDataNode dataNode = new StringDataNode();
			dataNode.generate((ObjectNode) object);
			nodes.put(key, dataNode);
			return;
		}

		if (!nodes.containsKey(key)) {
			nodes.put(key, object);
		} else {
			Object storedValue = nodes.get(key);

			ListNode listNode;
			if (storedValue instanceof ListNode) {
				listNode = (ListNode) storedValue;
			} else {
				listNode = new ListNode();
				listNode.add(storedValue);
			}

			listNode.add(object);
			nodes.put(key, listNode);
		}
	}

	private GFNode read(TextTokenizer tokenizer) {
		// This must be copied because the next tokenizer.peek() might create new tokens which would modify this token
		TextParserToken next = tokenizer.next().copy();
		TextParserToken afterNext = tokenizer.peek();

		if (afterNext.getType() == TextParserToken.Type.OPERATION) {
			return readProperty(next, tokenizer);
		}

		String asString = new String(buffer, next.getStart(), next.getLength());

		if (asString.equalsIgnoreCase("hoi4txt")) {
			return null;
		}

//		if (next.getType() == TextParserToken.Type.KEY) return readProperty(tokenizer);
		Logger.getInstance().log(Logger.ERROR, "Couldn't read next token " + next.getType() + " - " + asString + " at " + tokenizer.getPosition() + "! Start with -debug for more info");
		Logger.getInstance().log(Logger.DEBUG, tokenizer.getTokens(), buffer, Math.max(tokenizer.getPosition() - 10, 0), 15);
		System.exit(0);
		return null;
	}

	private GFNode readProperty(TextParserToken key, TextTokenizer tokenizer) {
//		skip =
		tokenizer.next();

		TextParserToken next = tokenizer.peek();

		Object value = null;
		if (next.getType() == TextParserToken.Type.START_OBJECT) {
			tokenizer.next();
			value = readObject(tokenizer);
		} else if (next.getType() == TextParserToken.Type.STRING) {
			value = readString(tokenizer);
		}

		return new PropertyNode(new String(buffer, key.getStart(), key.getLength()), value);
	}

	private Object readObject(TextTokenizer tokenizer) {
		// This must be copied because the next tokenizer.peek() might create new tokens which would modify this token
		TextParserToken next = tokenizer.next().copy();

		// Not sure if I should return an empty list or an empty object
		if (next.getType() == TextParserToken.Type.END_OBJECT) {
			return new ObjectNode(new HashMap<>());
		}

		TextParserToken afterNext = tokenizer.peek();

		if (afterNext.getType() != TextParserToken.Type.OPERATION) {
			return readList(next, tokenizer);
		}

		final ObjectNode objectNode = new ObjectNode();

		while (next.getType() != TextParserToken.Type.END_OBJECT) {
			tokenizer.next();
			TextParserToken value = tokenizer.peek();
			Object node;
			if (value.getType() == TextParserToken.Type.START_OBJECT) {
				tokenizer.next();
				node = readObject(tokenizer);
			} else {
				node = readString(tokenizer);
			}

			String key = new String(buffer, next.getStart(), next.getLength());
			objectNode.add(key, node);

			next = tokenizer.next();
		}

		return objectNode;
	}

	private GFNode readList(TextParserToken firstElement, TextTokenizer tokenizer) {
		List<Object> children = new ArrayList<>();
		if (firstElement.getType() == TextParserToken.Type.START_OBJECT) {
			children.add(readObject(tokenizer));
		} else {
			children.add(readString(firstElement));
		}

		TextParserToken next = tokenizer.peek();
		while (next.getType() != TextParserToken.Type.END_OBJECT) {
			if (next.getType() == TextParserToken.Type.START_OBJECT) {
				tokenizer.next();
				children.add(readObject(tokenizer));
			} else {
				children.add(readString(tokenizer));
			}

			next = tokenizer.peek();
		}

		tokenizer.next();
		return new ListNode(children);
	}

	private Object readString(TextTokenizer tokenizer) {
		return readString(tokenizer.next());
	}

	private Object readString(TextParserToken next) {
		int length = next.getLength();

		GFNode.Type numberType = getNumberType(buffer, next.getStart(), length);
		if (numberType == GFNode.Type.DOUBLE)
			return charArrayToDouble(buffer, next.getStart(), length);
		if (numberType == GFNode.Type.INTEGER)
			return charArrayToInt(buffer, next.getStart(), length);
		if (numberType == GFNode.Type.LONG)
			return charArrayToLong(buffer, next.getStart(), length);
		if (buffer[next.getStart()] == 'n' && buffer[next.getStart() + 1] == 'o' || buffer[next.getStart()] == 'y' && buffer[next.getStart() + 1] == 'e' && buffer[next.getStart() + 2] == 's')
			return readBoolean(next);
		return new String(buffer, next.getStart(), length);
	}

	private boolean readBoolean(TextParserToken token) {
		return token.getLength() == 3;
	}

	private GFNode.Type getNumberType(byte[] value, int start, int length) {
		boolean alreadySeenDot = false;
		for (int i = start; i < start + length; i++) {
			if (value[i] == '-' && i == start) continue;
			if (Character.isDigit(value[i])) continue;
			if (value[i] == '.') {
				if (alreadySeenDot) {
					return GFNode.Type.STRING;
				} else {
					alreadySeenDot = true;
					continue;
				}
			}

			return GFNode.Type.STRING;
		}

		if (alreadySeenDot) {
			return GFNode.Type.DOUBLE;
		} else {
			if (length >= 9) {
				return GFNode.Type.LONG;
			} else {
				return GFNode.Type.INTEGER;
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
