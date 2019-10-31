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

		for (Object obj : children.values()) {
			listNode.add((ASTNode) obj);
		}

		return listNode;
	}

	public HashMap<String, Object> getChildren() {
		return children;
	}
}
