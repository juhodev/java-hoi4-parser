package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.utils.ArgsParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListNode extends ASTNode {

	private List<Object> children;

	public ListNode(List<Object> children) {
		super(Type.LIST);

		this.children = children;
	}

	public ListNode() {
		super(Type.LIST);

		this.children = new ArrayList<>();
	}

	public ListNode add(Object child) {
		children.add(child);
		return this;
	}

	public List<Object> getChildren() {
		return children;
	}

	public JSONArray toJSON() {
		JSONArray array = new JSONArray();

		int limit = Integer.MAX_VALUE;
		if (ArgsParser.getInstance().has(ArgsParser.Argument.JSON_LIMIT))
			limit = ArgsParser.getInstance().getInt(ArgsParser.Argument.JSON_LIMIT);

		for (Object value : children) {
			if (value instanceof String || value instanceof Boolean || value instanceof Double || value instanceof Integer) {
				array.put(value);
			} else if (value instanceof ListNode) {
				JSONArray listJSON = ((ListNode) value).toJSON();
				if (listJSON.length() <= limit) {
					array.put(listJSON);
				}
			} else if (value instanceof ObjectNode) {
				JSONObject objJSON = ((ObjectNode) value).toJSON();
				if (objJSON.length() <= limit) {
					array.put(objJSON);
				}
			}
		}

		return array;
	}
}
