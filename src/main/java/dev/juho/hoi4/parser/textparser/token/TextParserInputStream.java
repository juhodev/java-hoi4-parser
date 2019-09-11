package dev.juho.hoi4.parser.textparser.token;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TextParserInputStream {

	private BufferedInputStream is;
	private byte[] buffer;

	private int currentPos;
	private int read;

	private int capacity;

	public TextParserInputStream(InputStream is, int capacity) {
		this.is = new BufferedInputStream(is);
		this.currentPos = 0;
		this.capacity = capacity;
		this.buffer = new byte[capacity];

		readToBuffer();
	}

	public char next() {
		if (currentPos + 1 > read) {
			readToBuffer();
		}

		char next = (char) buffer[currentPos++];
		return next;
	}

	public char peek() {
		if (currentPos + 1 > read) {
			readToBuffer();
		}

		char next = (char) buffer[currentPos];
		return next;
	}

	public boolean eof() {
		return read < capacity && currentPos == read;
	}

	private void readToBuffer() {
		try {
			read = is.read(buffer, 0, capacity);
			currentPos = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
