package dev.juho.hoi4.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ParserInputStream {

	private BufferedInputStream is;
	private byte[] buffer;

	private int currentPos;
	private int read;

	private int capacity;

	public ParserInputStream(InputStream is, int capacity) {
		this.is = new BufferedInputStream(is);
		this.currentPos = 0;
		this.capacity = capacity;
		this.buffer = new byte[capacity];

		readToBuffer();
	}

	public byte nextByte() {
		if (currentPos + 1 > read) {
			readToBuffer();
		}

		return buffer[currentPos++];
	}

	public byte peekByte() {
		if (currentPos + 1 > read) {
			readToBuffer();
		}

		return buffer[currentPos];
	}

	public char nextChar() {
		if (currentPos + 1 > read) {
			readToBuffer();
		}

		char next = (char) buffer[currentPos++];
		return next;
	}

	public char peekChar() {
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
