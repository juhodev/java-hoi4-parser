package dev.juho.hoi4.parser.textparser.token;

import dev.juho.hoi4.Main;
import dev.juho.hoi4.parser.textparser.TextParser;
import dev.juho.hoi4.utils.Logger;

import javax.crypto.spec.DESedeKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TextParserInputStream {

	private InputStream is;
	private byte[] buffer;

	private int currentPos;
	private int read;

	private int capacity;

	public TextParserInputStream(InputStream is, int capacity) {
		this.is = is;
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
