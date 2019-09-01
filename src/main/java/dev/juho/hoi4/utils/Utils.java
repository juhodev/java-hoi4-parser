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
}
