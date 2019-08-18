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
	private int offset;
	private int read;

	private int line;
	private int col;

	public TextParserInputStream(InputStream is) {
		this.is = is;
		this.currentPos = 0;
		this.buffer = new byte[4096 * 2];
		this.line = 0;
		this.col = 0;

		readToBuffer();
	}

	public int getLine() {
		if (Main.DEBUG_MODE) return 0;
		return line;
	}

	public int getCol() {
		if (Main.DEBUG_MODE) return 0;
		return col;
	}

	public char next() {
		if (currentPos + 1 > read) {
			readToBuffer();
		}

		char next = (char) buffer[currentPos++];

		if (Main.DEBUG_MODE) {
			if (next == '\n') {
				line++;
				col = 0;
			} else {
				col++;
			}
		}

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
		return read < buffer.length && currentPos == read;
	}

	private void readToBuffer() {
		try {
			read = is.read(buffer, offset, buffer.length);
			currentPos = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
