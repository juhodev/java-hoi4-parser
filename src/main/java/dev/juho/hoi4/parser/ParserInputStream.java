package dev.juho.hoi4.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ParserInputStream {

	private BufferedInputStream is;
	private byte[] buffer;

	private int currentPos;
	private int read;

	public ParserInputStream(InputStream is) {
		this.is = new BufferedInputStream(is);
		this.currentPos = 0;

		readToBuffer();
	}

	public byte nextByte() {
		return buffer[currentPos++];
	}

	public byte peekByte() {
		return buffer[currentPos];
	}

	public char nextChar() {
		char next = (char) buffer[currentPos++];
		return next;
	}

	public char peekChar() {
		char next = (char) buffer[currentPos];
		return next;
	}

	public int getPosition() {
		return currentPos;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public boolean eof() {
		return currentPos == read;
	}

	private void readToBuffer() {
		int available = -1;
		try {
			available = is.available();
		} catch (IOException e) {
			e.printStackTrace();
		}

		buffer = new byte[available];
		try {
			read = is.read(buffer, 0, available);
			currentPos = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
