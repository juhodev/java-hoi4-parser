package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;

import java.util.*;

public class ObjectNode extends ASTNode {

	private HashMap<String, Object> children;

	public ObjectNode(HashMap<String, Object> children) {
		super(Type.OBJECT);

		this.children = children;
	}

	public void set(String key, ASTNode value) {
		children.put(key, value);
	}

	public Object get(String key) {
		return children.get(key);
	}

	public boolean has(String key) {
		return children.containsKey(key);
	}

	public ListNode toListNode() {
		ListNode listNode = new ListNode(new ArrayList<>());
		listNode.add(this);

		return listNode;
	}

	public HashMap<String, Object> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Iterator it = children.entrySet().iterator();
		builder.append("object with ").append(children.size()).append(" children:\n");

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

			builder.append(pair.getKey()).append(": ").append(pair.getValue()).append("\n");
		}

		return builder.toString();
	}
}
