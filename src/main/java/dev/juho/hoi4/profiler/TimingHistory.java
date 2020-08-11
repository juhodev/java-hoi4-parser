package dev.juho.hoi4.profiler;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class TimingHistory {

	private String name;
	private LinkedList<Long> history;

	public TimingHistory(String name) {
		this.name = name;
		this.history = new LinkedList<>();
	}

	public void add(long x) {
		history.add(x);
	}

	public String getChromeFormat() {
		StringBuilder builder = new StringBuilder();

		AtomicInteger count = new AtomicInteger();
		history.forEach(x -> {
			builder.append(timeToChrome(x, count.get())).append(",");
			count.getAndIncrement();
		});

		return builder.toString();
	}

	private String timeToChrome(long time, int count) {
		return "{\"name\": \"" + name + "\", \"cat\": \"PERF\", \"ph\": " + (count % 2 == 0 ? "\"B\"" : "\"E\"") + ", \"pid\": -1, \"tid\": -1, \"ts\": " + time + "}";
	}

}
