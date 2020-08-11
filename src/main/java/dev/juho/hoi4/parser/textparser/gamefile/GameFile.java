package dev.juho.hoi4.parser.textparser.gamefile;

import dev.juho.hoi4.parser.textparser.gamefile.nodes.*;
import dev.juho.hoi4.parser.textparser.token.TextParserToken;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import dev.juho.hoi4.profiler.Profiler;
import dev.juho.hoi4.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameFile {

	private HashMap<String, Object> nodes;

	public GameFile() {
		this.nodes = new HashMap<>();
	}

	public void build(TextTokenizer tokenizer) {
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
		Profiler.getInstance().start("gamefile_read");
		TextTokenizer.Type next = tokenizer.peek();

		TextTokenizer.Type afterNext = tokenizer.peek(1);

		if (afterNext == TextTokenizer.Type.EQUALS) {
			return readProperty(tokenizer.readString(), tokenizer);
		}

		String asString = tokenizer.readString();

		if (asString.equalsIgnoreCase("hoi4txt")) {
			return null;
		}

		Logger.getInstance().log(Logger.ERROR, "Couldn't read next token " + next + " - " + asString + " at " + tokenizer.getPosition() + "! Start with -debug for more info");
		System.exit(0);
		return null;
	}

	private GFNode readProperty(String key, TextTokenizer tokenizer) {
		tokenizer.skip();

		TextTokenizer.Type next = tokenizer.peek();

		Object value = null;
		if (next == TextTokenizer.Type.OPEN_BRACKET) {
			tokenizer.skip();
			value = readObject(tokenizer);
		} else if (next == TextTokenizer.Type.STRING) {
			value = tokenizer.readString();
		}

		Profiler.getInstance().end("gamefile_read");
		return new PropertyNode(key, value);
	}

	private Object readObject(TextTokenizer tokenizer) {
		TextTokenizer.Type next = tokenizer.peek();

		// Not sure if I should return an empty list or an empty object
		if (next == TextTokenizer.Type.CLOSED_BRACKET) {
			tokenizer.skip();
			return new ObjectNode(new HashMap<>());
		}

		TextTokenizer.Type afterNext = tokenizer.peek(1);

		if (afterNext != TextTokenizer.Type.EQUALS) {
			if (next == TextTokenizer.Type.OPEN_BRACKET) {
				return readList(next, tokenizer);
			} else {
				return readList(tokenizer.readString(), tokenizer);
			}
		}

		final ObjectNode objectNode = new ObjectNode();

		while (next != TextTokenizer.Type.CLOSED_BRACKET) {
			String key = tokenizer.readString();
			tokenizer.skip();
			TextTokenizer.Type value = tokenizer.peek();
			Object node;
			if (value == TextTokenizer.Type.OPEN_BRACKET) {
				tokenizer.skip();
				node = readObject(tokenizer);
			} else {
				node = tokenizer.readString();
			}

			objectNode.add(key, node);

			next = tokenizer.peek();
		}

		tokenizer.skip();
		return objectNode;
	}

	private GFNode readList(Object firstElement, TextTokenizer tokenizer) {
		List<Object> children = new ArrayList<>();
		if (firstElement instanceof TextTokenizer.Type) {
			tokenizer.skip();
			children.add(readObject(tokenizer));
		} else {
			children.add(firstElement);
		}

		TextTokenizer.Type next = tokenizer.peek();
		while (next != TextTokenizer.Type.CLOSED_BRACKET) {
			if (next == TextTokenizer.Type.OPEN_BRACKET) {
				tokenizer.skip();
				children.add(readObject(tokenizer));
			} else {
				children.add(tokenizer.readString());
			}

			next = tokenizer.peek();
		}

		tokenizer.skip();
		return new ListNode(children);
	}

//	private Object readString(TextParserToken next) {
//		int length = next.getLength();
//
//		GFNode.Type numberType = getNumberType(buffer, next.getStart(), length);
//		if (numberType == GFNode.Type.DOUBLE)
//			return charArrayToDouble(buffer, next.getStart(), length);
//		if (numberType == GFNode.Type.INTEGER)
//			return charArrayToInt(buffer, next.getStart(), length);
//		if (numberType == GFNode.Type.LONG)
//			return charArrayToLong(buffer, next.getStart(), length);
//		if (buffer[next.getStart()] == 'n' && buffer[next.getStart() + 1] == 'o' || buffer[next.getStart()] == 'y' && buffer[next.getStart() + 1] == 'e' && buffer[next.getStart() + 2] == 's')
//			return readBoolean(next);
//		next.forget();
//		return new String(buffer, next.getStart(), length);
//	}

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
