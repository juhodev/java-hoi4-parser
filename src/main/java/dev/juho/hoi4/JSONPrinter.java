package dev.juho.hoi4;

import dev.juho.hoi4.parser.textparser.gamefile.GFNode;
import dev.juho.hoi4.parser.textparser.gamefile.nodes.*;
import dev.juho.hoi4.utils.ArgsParser;
import dev.juho.hoi4.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONPrinter {

	private HashMap<String, Object> nodeList;

	public JSONPrinter(HashMap<String, Object> nodeList) {
		this.nodeList = nodeList;
	}

	public void print(String gameName) throws IOException {
		File outJSON = new File(gameName + ".json");
		Logger.getInstance().log(Logger.INFO, "Writing json to " + outJSON.getAbsolutePath());
		BufferedWriter writer = new BufferedWriter(new FileWriter(outJSON));
		writer.write("{");
		StringBuilder builder = new StringBuilder();
		boolean first = true;

//		Now that we print the json out like this it leads to duplicate keys in the root object
//		For an example look at saved_event_target
		Iterator it = nodeList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			String key = (String) pair.getKey();
			Object value = pair.getValue();
			if (!first) {
				builder.append(",");
			} else {
				first = false;
			}

			builder.append("\"").append(key).append("\":");

			if (value instanceof ObjectNode) {
				String objJSON = ((ObjectNode) value).toJSONString();
				builder.append(objJSON);
			} else if (value instanceof ListNode) {
				String listJSON = ((ListNode) value).toJSONString();
				builder.append(listJSON);
			} else if (value instanceof String) {
				builder.append("\"").append(value).append("\"");
			} else if (value instanceof StringDataNode) {
				String data = ((StringDataNode) value).getData();
				builder.append(data);
			} else {
				builder.append(value);
			}

			writer.write(builder.toString());
			builder.setLength(0);
		}

		writer.write("}");
		writer.flush();
		writer.close();
		Logger.getInstance().log(Logger.INFO, "Game written to " + outJSON.getAbsolutePath());
	}

}
