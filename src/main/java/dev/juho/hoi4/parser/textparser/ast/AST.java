package dev.juho.hoi4.parser.textparser.ast;

import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.parser.textparser.token.TextParserToken;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import dev.juho.hoi4.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AST {

	private List<ASTNode> nodes;

	public AST() {
		this.nodes = new ArrayList<>();
	}

	public void build(TextTokenizer tokenizer) {
		List<ASTNode> astNodes = new ArrayList<>();

		while (!tokenizer.eof()) {
			ASTNode node = read(tokenizer);

			if (node != null) {
				astNodes.add(node);
			}
		}

		this.nodes = astNodes;
	}

	public List<ASTNode> getNodes() {
		return nodes;
	}

	private ASTNode read(TextTokenizer tokenizer) {
		TextParserToken next = tokenizer.peek();

		if (next.getType() == TextParserToken.Type.KEY) return readProperty(tokenizer);
		Logger.getInstance().log(Logger.ERROR, "Couldn't read next token " + next.getType() + " - " + next.getValue() + " at " + tokenizer.getPosition());
		System.exit(0);
		return null;
	}

	private ASTNode readProperty(TextTokenizer tokenizer) {
		TextParserToken next = tokenizer.next();

		TextParserToken operation = tokenizer.peek();
		if (operation.getType() == TextParserToken.Type.OPERATION) {
			tokenizer.next();
			if (next.getType() == TextParserToken.Type.KEY) {
				TextParserToken value = tokenizer.peek();

				switch (value.getType()) {
					case STRING:
						return new PropertyNode<>(next.getValue().toString(), readString(tokenizer));

					case INTEGER:
						return new PropertyNode<>(next.getValue().toString(), readInt(tokenizer));

					case DOUBLE:
						return new PropertyNode<>(next.getValue().toString(), readDouble(tokenizer));

					case LONG:
						return new PropertyNode<>(next.getValue().toString(), readLong(tokenizer));

					case BOOLEAN:
						return new PropertyNode<>(next.getValue().toString(), readBoolean(tokenizer));

					case START_OBJECT:
						tokenizer.next();
						return new PropertyNode<>(next.getValue().toString(), readObjectOrList(tokenizer));
				}
			} else {
				Logger.getInstance().log(Logger.ERROR, "Couldn't read next token " + next.getType() + " - " + next.getValue() + " at " + tokenizer.getPosition());
				System.exit(0);
			}
		} else {
			switch (next.getType()) {
				case STRING:
					return new StringNode(next.getValue().toString());

				case INTEGER:
					return new IntegerNode((int) next.getValue());

				case DOUBLE:
					return new DoubleNode((double) next.getValue());

				case LONG:
					return new LongNode((long) next.getValue());

				case BOOLEAN:
					return new BooleanNode(next.getValue().toString().equalsIgnoreCase("yes"));

				case START_OBJECT:
					return readObjectOrList(tokenizer);
			}
		}

		Logger.getInstance().log(Logger.DEBUG, "HUH? " + next.getType() + "next: " + next.getType());
		return null;
	}

	private LongNode readLong(TextTokenizer tokenizer) {
		return new LongNode((long) tokenizer.next().getValue());
	}

	private BooleanNode readBoolean(TextTokenizer tokenizer) {
		return new BooleanNode(tokenizer.next().getValue().toString().equalsIgnoreCase("yes"));
	}

	private StringNode readString(TextTokenizer tokenizer) {
		return new StringNode(tokenizer.next().getValue().toString());
	}

	private IntegerNode readInt(TextTokenizer tokenizer) {
		return new IntegerNode((int) tokenizer.next().getValue());
	}

	private DoubleNode readDouble(TextTokenizer tokenizer) {
		return new DoubleNode((double) tokenizer.next().getValue());
	}

	private ASTNode readObjectOrList(TextTokenizer tokenizer) {
		HashMap<String, Object> children = new HashMap<>();

		String key = "";
		List<ASTNode> childrenList = new ArrayList<>();
//		tokenizer.next();
		ASTNode.Type type = ASTNode.Type.OBJECT;

		TextParserToken token = tokenizer.peek();
		while (token.getType() != TextParserToken.Type.END_OBJECT) {
			ASTNode property = readProperty(tokenizer);

			if (property.getType() != ASTNode.Type.PROPERTY) {
				type = ASTNode.Type.LIST;
				childrenList.add(property);
			} else {
				PropertyNode propNode = (PropertyNode) property;

				if (children.containsKey(propNode.getKey())) {
					if (children.get(propNode.getKey()) instanceof ListNode) {
						ListNode listNode = (ListNode) children.get(propNode.getKey());
						listNode.add((ASTNode) propNode.getValue());
						children.put(propNode.getKey(), listNode);
					} else {
						key = propNode.getKey();
						List<ASTNode> listNodes = new ArrayList<>();
						listNodes.add((ASTNode) children.get(propNode.getKey()));
						listNodes.add((ASTNode) propNode.getValue());
						ListNode listNode = new ListNode(listNodes);
						children.put(propNode.getKey(), listNode);
					}
				} else {
					children.put(propNode.getKey(), propNode.getValue());
				}
			}
			token = tokenizer.peek();
		}

		tokenizer.next();
		if (type == ASTNode.Type.OBJECT) {
			return new ObjectNode(children);
		} else if (key.isEmpty()) {
			return new ListNode(childrenList);
		} else {
			ListNode listNode = new ListNode(childrenList);
			return new PropertyNode<>(key, listNode);
		}
	}

}
