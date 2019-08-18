package dev.juho.hoi4.utils;

import dev.juho.hoi4.parser.textparser.HOIKey;

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
