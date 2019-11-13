package dev.juho.hoi4.utils;

import dev.juho.hoi4.parser.ParserInputStream;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.parser.textparser.token.TextParserToken;

import java.util.*;

public class Logger {

	public static final int ERROR = 0;
	public static final int INFO = 1;
	public static final int WARNING = 2;
	public static final int DEBUG = 3;

	public static int LOG_LEVEL = INFO;

	private final String PREFIX_LOG_ERROR = "[ERROR]";
	private final String PREFIX_LOG_WARNING = "[WARNING]";
	private final String PREFIX_LOG_INFO = "[INFO]";
	private final String PREFIX_LOG_DEBUG = "[DEBUG]";

	private static Logger instance;

	private HashMap<String, Date> times;

	public Logger() {
		times = new HashMap<>();
	}

	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}

		return instance;
	}

	public void log(int level, TextParserToken[] tokens, ParserInputStream in, int start, int length) {
		log(level, "Starting from " + start + " ending at " + length);
		for (int i = start; i < start + length; i++) {
			log(level, i + ": " + tokens[i].getType() + ", " + tokens[i].getStart() + "-" + tokens[i].getLength());
			log(level, "\t" + tokens[i].getStart() + ": " + new String(in.getBuffer(), tokens[i].getStart(), tokens[i].getLength()));
		}
	}

	public void log(int level, TextParserToken token) {
		log(level, "token: " + token.getType() + ", " + token.getStart() + "-" + token.getLength());
	}

	public void log(int level, ASTNode node) {
		log(level, node, "");
	}

	private void log(int level, ASTNode node, String tabs) {
		switch (node.getType()) {
			case DOUBLE:
				log(level, tabs + ((DoubleNode) node).getValue());
				break;

			case INTEGER:
				log(level, tabs + ((IntegerNode) node).getValue());
				break;

			case LONG:
				log(level, tabs + ((LongNode) node).getValue());
				break;

			case BOOLEAN:
				log(level, tabs + ((BooleanNode) node).getValue());
				break;

			case STRING:
				log(level, tabs + ((StringNode) node).getValue());
				break;

			case OBJECT:
				ObjectNode objectNode = (ObjectNode) node;

				Iterator it = objectNode.getChildren().entrySet().iterator();

				while (it.hasNext()) {
					Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();
					log(level, (ASTNode) pair.getValue(), tabs + "\t");
				}
				break;

			case LIST:
				ListNode listNode = (ListNode) node;
				for (ASTNode listChild : listNode.getChildren()) {
					log(level, listChild, tabs + "\t");
				}
				break;

			case PROPERTY:
				PropertyNode propertyNode = (PropertyNode) node;
				log(level, tabs + "Key: " + propertyNode.getKey());
				log(level, (ASTNode) propertyNode.getValue(), tabs + "\t");
		}
	}

	public void log(int level, byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		builder.append("bytes: ");

		for (byte b : bytes) {
			builder.append(b + " ");
		}

		log(level, builder.toString());
	}

	public void log(int level, char c) {
		log(level, Character.toString(c));
	}

	public void log(int level, double x) {
		log(level, String.valueOf(x));
	}

	public void log(int level, int x) {
		log(level, String.valueOf(x));
	}

	public void log(int level, String message) {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);

		String timePrefix = "[" + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR) + " "
				+ (hour < 10 ? ("0" + hour) : hour) + ":"
				+ (minute < 10 ? "0" + minute : minute) + ":"
				+ (second < 10 ? "0" + second : second) + "]";

		String logLevelPrefix;

		switch (level) {
			case ERROR:
				logLevelPrefix = PREFIX_LOG_ERROR;
				break;

			case WARNING:
				logLevelPrefix = PREFIX_LOG_WARNING;
				break;

			case INFO:
				logLevelPrefix = PREFIX_LOG_INFO;
				break;

			case DEBUG:
				logLevelPrefix = PREFIX_LOG_DEBUG;
				break;

			default:
				logLevelPrefix = "[??]";
				break;
		}


		if (LOG_LEVEL >= level) {
			System.out.println(timePrefix + " " + logLevelPrefix + ": " + message);
		}
	}

	/**
	 * The same as console.time & console.timeEnd in JavaScript
	 */

	public void time(String name) {
		times.put(name, new Date());
	}

	public void timeEnd(int level, String name) {
		if (!times.containsKey(name)) {
			log(Logger.WARNING, name + " starting time not found");
			return;
		}

		Date startingDate = times.get(name);
		long elapsedTime = new Date().getTime() - startingDate.getTime();

		log(level, name + " " + elapsedTime + "ms");
		times.remove(name);
	}

}
