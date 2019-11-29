package dev.juho.hoi4.parser.textparser;

import dev.juho.hoi4.parser.Parser;
import dev.juho.hoi4.parser.textparser.gamefile.GFNode;
import dev.juho.hoi4.parser.textparser.gamefile.GameFile;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import dev.juho.hoi4.utils.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class TextParser extends Parser {

	private HashMap<String, Object> nodes;

	public TextParser(File file) {
		super(file);
	}

	@Override
	public void parse() throws IOException {
		Logger.getInstance().time("Parsing " + getFile().getName());

		TextTokenizer tokenizer = new TextTokenizer(4096 * 4);
		tokenizer.readInputStream(new FileInputStream(getFile()));
		GameFile gameFile = new GameFile();

		gameFile.build(tokenizer);
		Logger.getInstance().timeEnd(Logger.INFO, "Parsing " + getFile().getName());
		nodes = gameFile.getNodes();
	}

	@Override
	public HashMap<String, Object> getNodes() {
		return nodes;
	}
}
