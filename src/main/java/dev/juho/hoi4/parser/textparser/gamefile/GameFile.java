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
		short next = tokenizer.peek();

		short afterNext = tokenizer.peek(1);

		if (afterNext == TextTokenizer.TYPE_EQUALS) {
			return readProperty(tokenizer.readString(), tokenizer);
		}

		Object asString = tokenizer.readString();

		if (asString instanceof String && ((String) asString).equalsIgnoreCase("hoi4txt")) {
			return null;
		}

		Logger.getInstance().log(Logger.ERROR, "Couldn't read next token " + next + " - " + asString + " at " + tokenizer.getPosition() + "! Start with -debug for more info");
		System.exit(0);
		return null;
	}

	private GFNode readProperty(Object key, TextTokenizer tokenizer) {
		tokenizer.skip();

		short next = tokenizer.peek();

		Object value = null;
		if (next == TextTokenizer.TYPE_OPEN_BRACKET) {
			tokenizer.skip();
			value = readObject(tokenizer);
		} else if (next == TextTokenizer.TYPE_STRING) {
			value = tokenizer.readString();
		}

		Profiler.getInstance().end("gamefile_read");
		return new PropertyNode(key.toString(), value);
	}

	private Object readObject(TextTokenizer tokenizer) {
		short next = tokenizer.peek();

		// Not sure if I should return an empty list or an empty object
		if (next == TextTokenizer.TYPE_CLOSED_BRACKET) {
			tokenizer.skip();
			return new ObjectNode(new HashMap<>());
		}

		short afterNext = tokenizer.peek(1);

		if (afterNext != TextTokenizer.TYPE_EQUALS) {
			if (next == TextTokenizer.TYPE_OPEN_BRACKET) {
				return readList(next, tokenizer);
			} else {
				return readList(tokenizer.readString(), tokenizer);
			}
		}

		final ObjectNode objectNode = new ObjectNode();

		while (next != TextTokenizer.TYPE_CLOSED_BRACKET) {
			Object key = tokenizer.readString();
			tokenizer.skip();
			short value = tokenizer.peek();
			Object node;
			if (value == TextTokenizer.TYPE_OPEN_BRACKET) {
				tokenizer.skip();
				node = readObject(tokenizer);
			} else {
				node = tokenizer.readString();
			}

			objectNode.add(key.toString(), node);

			next = tokenizer.peek();
		}

		tokenizer.skip();
		return objectNode;
	}

	private GFNode readList(Object firstElement, TextTokenizer tokenizer) {
		List<Object> children = new ArrayList<>();
		if (firstElement instanceof Short) {
			tokenizer.skip();
			children.add(readObject(tokenizer));
		} else {
			children.add(firstElement);
		}

		short next = tokenizer.peek();
		while (next != TextTokenizer.TYPE_CLOSED_BRACKET) {
			if (next == TextTokenizer.TYPE_OPEN_BRACKET) {
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
}
