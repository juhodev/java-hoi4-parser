package dev.juho.hoi4.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader {

	private File file;
	private String separator;
	private boolean ignoreFirstLine;

	public CSVLoader(File file, String separator, boolean ignoreFirstLine) {
		this.file = file;
		this.separator = separator;
		this.ignoreFirstLine = ignoreFirstLine;
	}

	public List<String[]> load() throws IOException {
		List<String[]> rows = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		boolean ignored = false;
		String line;
		while ((line = reader.readLine()) != null) {
			if (ignoreFirstLine && !ignored) {
				ignored = true;
				continue;
			}

			if (line.length() > 2) {
				rows.add(line.split(separator));
			}
		}

		return rows;
	}

}
