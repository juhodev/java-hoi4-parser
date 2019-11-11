package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class ListNode extends ASTNode {

	private List<ASTNode> children;

	public ListNode(List<ASTNode> children) {
		super(Type.LIST);

		this.children = children;
	}

	public void add(ASTNode child) {
		children.add(child);
	}

	public List<ASTNode> getChildren() {
		return children;
	}

	public JSONArray toJSON() {
		JSONArray array = new JSONArray();

		for (ASTNode value : children) {
			switch (value.getType()) {
				case STRING:
					array.put(((StringNode) value).getValue());
					break;

				case LONG:
					array.put(((LongNode) value).getValue());
					break;

				case DOUBLE:
					array.put(((DoubleNode) value).getValue());
					break;

				case INTEGER:
					array.put(((IntegerNode) value).getValue());
					break;

				case BOOLEAN:
					array.put(((BooleanNode) value).getValue());
					break;

				case OBJECT:
					array.put(((ObjectNode) value).toJSON());
					break;

				case LIST:
					array.put(((ListNode) value).toJSON());
					break;
			}
		}

		return array;
	}
}
