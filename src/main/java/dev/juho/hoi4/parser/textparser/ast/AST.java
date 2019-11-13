package dev.juho.hoi4.parser.textparser.ast;

import dev.juho.hoi4.parser.ParserInputStream;
import dev.juho.hoi4.parser.textparser.TextParser;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.parser.textparser.token.TextParserToken;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import dev.juho.hoi4.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AST {

	private List<ASTNode> nodes;
	private ParserInputStream in;

	public AST(ParserInputStream in) {
		this.in = in;
		this.nodes = new ArrayList<>();
	}

	public void build(TextTokenizer tokenizer) {
		while (!tokenizer.eof()) {
			ASTNode node = read(tokenizer);

			if (node != null) {
				nodes.add(node);
			}
		}
	}

	public List<ASTNode> getNodes() {
		return nodes;
	}

	private ASTNode read(TextTokenizer tokenizer) {
		TextParserToken next = tokenizer.next();
		TextParserToken afterNext = tokenizer.peek();

		if (afterNext.getType() == TextParserToken.Type.OPERATION) {
			return readProperty(next, tokenizer);
		}

		String asString = new String(in.getBuffer(), next.getStart(), next.getLength());

		if (asString.equalsIgnoreCase("hoi4txt")) {
			return null;
		}

//		if (next.getType() == TextParserToken.Type.KEY) return readProperty(tokenizer);
		Logger.getInstance().log(Logger.ERROR, "Couldn't read next token " + next.getType() + " - " + asString + " at " + tokenizer.getPosition() + "! Start with -debug for more info");
		Logger.getInstance().log(Logger.DEBUG, tokenizer.getTokens(), in, Math.max(tokenizer.getPosition() - 10, 0), 15);
		System.exit(0);
		return null;
	}

	private ASTNode readProperty(TextParserToken key, TextTokenizer tokenizer) {
//		skip =
		tokenizer.next();

		TextParserToken next = tokenizer.peek();

		ASTNode value = null;
		if (next.getType() == TextParserToken.Type.START_OBJECT) {
			tokenizer.next();
			value = readObject(tokenizer);
		}
		if (next.getType() == TextParserToken.Type.STRING)
			value = readString(tokenizer);

		return new PropertyNode(new String(in.getBuffer(), key.getStart(), key.getLength()), value);
	}

	private ASTNode readObject(TextTokenizer tokenizer) {
		TextParserToken next = tokenizer.next();

		// Not sure if I should return an empty list or an empty object
		if (next.getType() == TextParserToken.Type.END_OBJECT) {
			return new ObjectNode(new HashMap<>());
		}

		TextParserToken afterNext = tokenizer.peek();

		if (afterNext.getType() != TextParserToken.Type.OPERATION) {
			return readList(next, tokenizer);
		}

		HashMap<String, ASTNode> children = new HashMap<>();

		while (next.getType() != TextParserToken.Type.END_OBJECT) {
			tokenizer.next();
			TextParserToken value = tokenizer.peek();
			ASTNode nodeValue;
			if (value.getType() == TextParserToken.Type.START_OBJECT) {
				tokenizer.next();
				nodeValue = readObject(tokenizer);
			} else {
				nodeValue = readString(tokenizer);
			}

			children.put(new String(in.getBuffer(), next.getStart(), next.getLength()), nodeValue);
			next = tokenizer.next();
		}

		return new ObjectNode(children);
	}

	private ASTNode readList(TextParserToken firstElement, TextTokenizer tokenizer) {
		List<ASTNode> children = new ArrayList<>();
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

	private ASTNode readString(TextTokenizer tokenizer) {
		return readString(tokenizer.next());
	}

	private ASTNode readString(TextParserToken next) {
		int length = next.getLength();

		ASTNode.Type numberType = getNumberType(in.getBuffer(), next.getStart(), length);
		if (numberType == ASTNode.Type.DOUBLE)
			return new DoubleNode(charArrayToDouble(in.getBuffer(), next.getStart(), length));
		if (numberType == ASTNode.Type.INTEGER)
			return new IntegerNode(charArrayToInt(in.getBuffer(), next.getStart(), length));
		if (numberType == ASTNode.Type.LONG)
			return new LongNode(charArrayToLong(in.getBuffer(), next.getStart(), length));
		if (in.getBuffer()[next.getStart()] == 'n' && in.getBuffer()[next.getStart() + 1] == 'o' || in.getBuffer()[next.getStart()] == 'y' && in.getBuffer()[next.getStart() + 1] == 'e' && in.getBuffer()[next.getStart() + 2] == 's')
			return readBoolean(next);
		return new StringNode(new String(in.getBuffer(), next.getStart(), length));
	}

	private ASTNode readBoolean(TextParserToken token) {
		return new BooleanNode(token.getLength() == 3);
	}

	private ASTNode.Type getNumberType(byte[] value, int start, int length) {
		boolean alreadySeenDot = false;
		for (int i = start; i < start + length; i++) {
			if (value[i] == '-' && i == start) continue;
			if (Character.isDigit(value[i])) continue;
			if (value[i] == '.') {
				if (alreadySeenDot) {
					return ASTNode.Type.STRING;
				} else {
					alreadySeenDot = true;
					continue;
				}
			}

			return ASTNode.Type.STRING;
		}

		if (alreadySeenDot) {
			return ASTNode.Type.DOUBLE;
		} else {
			if (length >= 9) {
				return ASTNode.Type.LONG;
			} else {
				return ASTNode.Type.INTEGER;
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
