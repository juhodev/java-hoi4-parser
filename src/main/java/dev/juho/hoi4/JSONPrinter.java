package dev.juho.hoi4;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.utils.ArgsParser;
import dev.juho.hoi4.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONPrinter {

	private List<ASTNode> nodeList;

	public JSONPrinter(List<ASTNode> nodeList) {
		this.nodeList = nodeList;
	}

	public void print() {
		int limit = Integer.MAX_VALUE;
		if (ArgsParser.getInstance().has(ArgsParser.Argument.JSON_LIMIT))
			limit = ArgsParser.getInstance().getInt(ArgsParser.Argument.JSON_LIMIT);

		JSONObject obj = new JSONObject();

		for (ASTNode node : nodeList) {
			PropertyNode propertyNode = (PropertyNode) node;
			String key = propertyNode.getKey();
			Object value = propertyNode.getValue();

			if (value instanceof ObjectNode) {
				JSONObject objJSON = ((ObjectNode) value).toJSON();
				if (objJSON.length() <= limit) {
					obj.put(key, objJSON);
				}
			} else if (value instanceof ListNode) {
				JSONArray listJSON = ((ListNode) value).toJSON();
				if (listJSON.length() <= limit) {
					obj.put(key, listJSON);
				}
			} else if (value instanceof PropertyNode) {
				obj.put(key, value);
			}
		}

		Logger.getInstance().log(Logger.INFO, "Writing json to HOI4-game.json");
		File outJSON = new File("HOI4-game.json");
		try {
			FileWriter writer = new FileWriter(outJSON);
			writer.write(obj.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Logger.getInstance().log(Logger.INFO, "Game written to HOI4-game.json");
	}

}
