package dev.juho.hoi4.utils;

import java.util.HashMap;

public class ArgsParser {

	private String[] required;

	public ArgsParser(String[] required) {
		this.required = required;
	}

	public HashMap<String, String> parse(String[] args) {
		HashMap<String, String> argMap = createHashMapFromArgs(args);

		for (String s : required) {
			if (!argMap.containsKey(s)) {
				Logger.getInstance().log(Logger.ERROR, s + " is required!");
				System.exit(1);
			}
		}

		return argMap;
	}

	private HashMap<String, String> createHashMapFromArgs(String[] args) {
		HashMap<String, String> kv = new HashMap<>();

		String key = "";
		StringBuilder builder = new StringBuilder();

		for (String s : args) {
			if (s.startsWith("-")) {
				kv.put(key, builder.toString().trim());
				builder.delete(0, builder.length());
				key = s;
			} else {
				builder.append(s).append(" ");
			}
		}

		if (!key.isEmpty()) {
			kv.put(key, builder.toString().trim());
		}

		return kv;
	}

}
