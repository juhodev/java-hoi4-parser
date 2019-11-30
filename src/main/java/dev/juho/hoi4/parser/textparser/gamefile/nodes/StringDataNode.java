package dev.juho.hoi4.parser.textparser.gamefile.nodes;

import dev.juho.hoi4.parser.textparser.gamefile.GFNode;

import java.util.Iterator;
import java.util.Map;

public class StringDataNode extends GFNode {

	private StringBuilder data;

	public StringDataNode() {
		super(Type.STRING_DATA);

		this.data = new StringBuilder();
	}

	public void generate(ObjectNode objectNode) {
		Iterator it = objectNode.getChildren().entrySet().iterator();
		boolean first = true;
		data.append("{");

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			String key = (String) pair.getKey();
			Object value = pair.getValue();
			if (!first) {
				data.append(",");
			} else {
				first = false;
			}

			data.append("\"").append(key).append("\":");

			if (value instanceof ObjectNode) {
				((ObjectNode) value).appendToStringBuilder(data);
			} else if (value instanceof ListNode) {
				((ListNode) value).appendToStringBuilder(data);
			} else if (value instanceof String) {
				data.append("\"").append(value).append("\"");
			} else {
				data.append(value);
			}
		}

		data.append("}");
	}

	public String getData() {
		return data.toString();
	}

}
