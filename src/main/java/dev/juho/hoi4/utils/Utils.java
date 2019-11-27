package dev.juho.hoi4.utils;

import java.io.File;
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

	// TODO: This probably shouldn't only check if the file exists
	public static boolean isFullPath(String path) {
		return new File(path).exists();
	}

}
