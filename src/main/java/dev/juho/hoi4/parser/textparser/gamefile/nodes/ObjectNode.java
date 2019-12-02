package dev.juho.hoi4.parser.textparser.gamefile.nodes;

import dev.juho.hoi4.parser.textparser.gamefile.GFNode;
import dev.juho.hoi4.utils.ArgsParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class ObjectNode extends GFNode {

	private HashMap<String, Object> children;
	// Consider making all objects lists by default

	public ObjectNode(HashMap<String, Object> children) {
		super(Type.OBJECT);

		this.children = children;
	}

	public ObjectNode() {
		super(Type.OBJECT);

		this.children = new HashMap<>();
	}

	public ObjectNode add(String key, Object value) {
		if (!children.containsKey(key)) {
			children.put(key, value);
			return this;
		}

		final Object obj = get(key);

		ListNode listNode;
		if (obj instanceof ListNode) {
			listNode = (ListNode) obj;
		} else {
			listNode = new ListNode();
			listNode.add(obj);
		}

		listNode.add(value);
		children.put(key, listNode);
		return this;
	}

	public Object get(String key) {
		return children.get(key);
	}

	public int getInt(String key) {
		final Object object = get(key);
		if (object instanceof Number) {
			return ((Number) object).intValue();
		}

		return -1;
	}

	public double getDouble(String key) {
		final Object object = get(key);
		if (object instanceof Number) {
			return ((Number) object).doubleValue();
		}

		return -1;
	}

	public long getLong(String key) {
		final Object object = get(key);
		if (object instanceof Number) {
			return ((Number) object).longValue();
		}

		return -1;
	}

	public String getString(String key) {
		final Object object = get(key);
		if (object instanceof String) {
			return (String) object;
		}

		return null;
	}

	public boolean getBoolean(String key) {
		final Object object = get(key);
		if (object instanceof Boolean) {
			return object.equals(Boolean.TRUE);
		}

		return false;
	}

	public ObjectNode getObjectNode(String key) {
		final Object object = get(key);
		if (object instanceof ObjectNode) {
			return (ObjectNode) object;
		}

		return null;
	}

	public ListNode getListNode(String key) {
		final Object object = get(key);
		if (object instanceof ListNode) {
			return (ListNode) object;
		}

		return null;
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
			Object value = pair.getValue();

			if (value instanceof String || value instanceof Long || value instanceof Boolean || value instanceof Double || value instanceof Integer) {
				obj.put(key, value);
			} else if (value instanceof ListNode) {
				JSONArray listJSON = ((ListNode) value).toJSON();
				if (listJSON.length() <= limit) {
					obj.put(key, listJSON);
				}
			} else if (value instanceof ObjectNode) {
				JSONObject objJSON = ((ObjectNode) value).toJSON();
				if (objJSON.length() <= limit) {
					obj.put(key, objJSON);
				}
			}
		}

		return obj;
	}

	public void appendToStringBuilder(StringBuilder builder) {
		if (children.size() == 0) {
			builder.append("{}");
			return;
		}

		builder.append("{");

		int limit = Integer.MAX_VALUE;
		if (ArgsParser.getInstance().has(ArgsParser.Argument.JSON_LIMIT))
			limit = ArgsParser.getInstance().getInt(ArgsParser.Argument.JSON_LIMIT);

		Iterator it = children.entrySet().iterator();
		int valueCount = 0;

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			String key = (String) pair.getKey();
			Object value = pair.getValue();

			builder.append("\"").append(key).append("\":");
			if (value instanceof String) {
				builder.append("\"").append(value).append("\"").append(",");
			} else if (value instanceof Long || value instanceof Boolean || value instanceof Double || value instanceof Integer) {
				builder.append(value).append(",");
			} else if (value instanceof ListNode) {
				ListNode listNode = (ListNode) value;
				listNode.appendToStringBuilder(builder);
				builder.append(",");
			} else if (value instanceof ObjectNode) {
				ObjectNode objectNode = (ObjectNode) value;
				objectNode.appendToStringBuilder(builder);
				builder.append(",");
			}

			valueCount++;
			if (valueCount == limit) {
				break;
			}
		}

		builder.delete(builder.length() - 1, builder.length());
		builder.append("}");
	}

	public String toJSONString() {
		StringBuilder builder = new StringBuilder();
		appendToStringBuilder(builder);
		return builder.toString();
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
