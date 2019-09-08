package dev.juho.hoi4.utils;

import java.util.HashMap;

public class ArgsParser {

	private String[] required;
	private String[] optional;

	public ArgsParser(String[] required, String[] optional) {
		this.required = required;
		this.optional = optional;
	}

	public HashMap<String, String> parse(String[] args) {
		HashMap<String, String> argMap = createHashMapFromArgs(args);

		for (String s : required) {
			if (!argMap.containsKey(s)) {
				Logger.getInstance().log(Logger.ERROR, s + " is required!");
				System.exit(1);
			}
		}

		if (argMap.containsKey("-help")) {
			StringBuilder builder = new StringBuilder();
			for (String s : required) {
				builder.append(s).append(" ");
			}
			Logger.getInstance().log(Logger.INFO, "Required args: " + builder.toString());

			builder.delete(0, builder.length());

			for (String s : optional) {
				builder.append(s).append(" ");
			}

			Logger.getInstance().log(Logger.INFO, "Optional args: " + builder.toString());
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
