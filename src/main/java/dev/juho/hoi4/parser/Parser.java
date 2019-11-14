package dev.juho.hoi4.parser;

import dev.juho.hoi4.parser.textparser.gamefile.GFNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class Parser {

	private File file;

	public Parser(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public abstract void parse() throws IOException;

	public abstract List<GFNode> getNodes();
}
