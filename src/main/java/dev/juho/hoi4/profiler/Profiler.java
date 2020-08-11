package dev.juho.hoi4.profiler;

import java.util.HashMap;

public class Profiler {

	private static Profiler instance;

	// If disabled is set to true, profiler will not save any data. This should be set to true when deploying to production.
	private boolean disabled;

	private HashMap<String, TimingHistory> timingHistory;

	public Profiler() {
		this.disabled = false;
		this.timingHistory = new HashMap<>();
	}

	public static Profiler getInstance() {
		if (instance == null) {
			instance = new Profiler();
		}

		return instance;
	}

	public void disable() {
		disabled = true;
	}

	/**
	 * Before starting profiling you must first register a name of a profile
	 *
	 * @param name Name of a profile
	 */
	public void register(String name) {
		if (disabled) {
			return;
		}
		timingHistory.put(name, new TimingHistory(name));
	}

	public void start(String name) {
		if (disabled) {
			return;
		}
		long currentTime = System.nanoTime();
		TimingHistory history = timingHistory.get(name);
		history.add(currentTime);
		timingHistory.put(name, history);
	}

	public void end(String name) {
		if (disabled) {
			return;
		}
		long currentTime = System.nanoTime();

		TimingHistory history = timingHistory.get(name);
		history.add(currentTime);
		timingHistory.put(name, history);
	}

	public String getChromeFormat() {
		if (disabled) {
			return "[]";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("[");

		timingHistory.forEach((key, data) -> {
			builder.append(data.getChromeFormat());
		});

		builder.delete(builder.length() - 1, builder.length());
		builder.append("]");
		return builder.toString();
	}
}
