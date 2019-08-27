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

	public static HashMap<String, Object> getObjectChildren(ObjectNode node) {
		HashMap<String, Object> hashMap = new HashMap<>();

		// TODO: Check if this should be changed to HashMap<String, Integer> because I'm not if the HashMap stores all
		// child values as well, if that's the case we def want to just save the index

		for (ASTNode child : node.getChildren()) {
			PropertyNode childProp = (PropertyNode) child;
			hashMap.put(childProp.getKey(), childProp.getValue());
		}

		return hashMap;
	}
}
