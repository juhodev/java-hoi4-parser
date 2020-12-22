package dev.juho.hoi4.utils;

import java.text.DecimalFormat;
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

	private HashMap<String, Long> times;
	private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

	public Logger() {
		times = new HashMap<>();
	}

	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}

		return instance;
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


		if (LOG_LEVEL >= level && !ArgsParser.getInstance().has(ArgsParser.Argument.ONLY_PRINT_OUT_FILE)) {
			System.out.println(timePrefix + " " + logLevelPrefix + ": " + message);
		}
	}

	/**
	 * The same as console.time & console.timeEnd in JavaScript
	 */

	public void time(String name) {
		times.put(name, System.nanoTime());
	}

	public void timeEnd(int level, String name) {
		if (!times.containsKey(name)) {
			log(Logger.WARNING, name + " starting time not found");
			return;
		}

		long startingDate = times.get(name);
		double elapsedTime = (System.nanoTime() - startingDate) / 1000000.0;

		log(level, name + " " + decimalFormat.format(elapsedTime) + "ms");
		times.remove(name);
	}

}
