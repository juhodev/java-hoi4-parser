package dev.juho.hoi4.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader {

	private File file;
	private String separator;

	public CSVLoader(File file, String separator) {
		this.file = file;
		this.separator = separator;
	}

	public List<String[]> load() throws IOException {
		List<String[]> rows = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		String line;
		while ((line = reader.readLine()) != null) {
			if (line.length() > 2) {
				rows.add(line.split(separator));
			}
		}

		return rows;
	}

}
