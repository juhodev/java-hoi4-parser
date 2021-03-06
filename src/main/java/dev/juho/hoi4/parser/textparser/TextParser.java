package dev.juho.hoi4.parser.textparser;

import dev.juho.hoi4.parser.Parser;
import dev.juho.hoi4.parser.textparser.gamefile.GameFile;
import dev.juho.hoi4.profiler.Profiler;
import dev.juho.hoi4.utils.Logger;

import java.io.*;
import java.util.HashMap;

public class TextParser extends Parser {

	private HashMap<String, Object> nodes;

	public TextParser(File file) {
		super(file);
	}

	@Override
	public void parse() throws IOException {
		Logger.getInstance().log(Logger.INFO, "Starting parsing " + getFile().getName());
		Logger.getInstance().time("Parsing " + getFile().getName());
		Profiler.getInstance().start("parse_file");

		TextTokenizer tokenizer = new TextTokenizer();
		tokenizer.read(new FileInputStream(getFile()));
		GameFile gameFile = new GameFile();

		gameFile.build(tokenizer);
		Profiler.getInstance().end("parse_file");
		Logger.getInstance().timeEnd(Logger.INFO, "Parsing " + getFile().getName());
		nodes = gameFile.getNodes();
	}

	@Override
	public HashMap<String, Object> getNodes() {
		return nodes;
	}
}
