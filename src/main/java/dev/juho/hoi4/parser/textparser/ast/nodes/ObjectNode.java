package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.utils.ArgsParser;
import org.json.JSONArray;
import org.json.JSONObject;

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

	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		int limit = Integer.MAX_VALUE;
		if (ArgsParser.getInstance().has(ArgsParser.Argument.JSON_LIMIT))
			limit = ArgsParser.getInstance().getInt(ArgsParser.Argument.JSON_LIMIT);

		Iterator it = children.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			String key = (String) pair.getKey();
			ASTNode value = (ASTNode) pair.getValue();

			switch (value.getType()) {
				case STRING:
					obj.put(key, ((StringNode) value).getValue());
					break;

				case LONG:
					obj.put(key, ((LongNode) value).getValue());
					break;

				case DOUBLE:
					obj.put(key, ((DoubleNode) value).getValue());
					break;

				case INTEGER:
					obj.put(key, ((IntegerNode) value).getValue());
					break;

				case LIST:
					JSONArray listJSON = ((ListNode) value).toJSON();
					if (listJSON.length() <= limit) {
						obj.put(key, listJSON);
					}
					break;

				case BOOLEAN:
					obj.put(key, ((BooleanNode) value).getValue());
					break;

				case OBJECT:
					JSONObject objJSON = ((ObjectNode) value).toJSON();
					if (objJSON.length() <= limit) {
						obj.put(key, objJSON);
					}
					break;
			}
		}

		return obj;
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
