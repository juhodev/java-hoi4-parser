package dev.juho.hoi4.utils;

import dev.juho.hoi4.parser.textparser.HOIKey;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.PropertyNode;

import java.util.HashMap;

public class Utils {

	public static boolean hasEnum(Enum[] enumArray, String enumStr) {
		for (Enum e : enumArray) {
			if (e.toString().equalsIgnoreCase(enumStr)) {
				return true;
			}
		}

		return false;
	}

	public static HashMap<String, String> getArgs(String[] args) {
		HashMap<String, String> map = new HashMap<>();

		String key = "";
		String value = "";

		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-")) {
				if (!key.isEmpty()) {
					map.put(key, value.trim());
				}

				key = args[i];
			} else {
				value += args[i] + " ";
			}
		}

		if (!key.isEmpty()) {
			map.put(key, value.trim());
		}

		return map;
	}
}
