package dev.juho.hoi4.parser.textparser;

import dev.juho.hoi4.parser.Parser;
import dev.juho.hoi4.parser.textparser.gamefile.GFNode;
import dev.juho.hoi4.parser.textparser.gamefile.GameFile;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import dev.juho.hoi4.utils.Logger;

import java.io.*;
import java.util.List;

public class TextParser extends Parser {

	private List<GFNode> nodes;

	public TextParser(File file) {
		super(file);
	}

	@Override
	public void parse() throws IOException {
		Logger.getInstance().time("Parsing " + getFile().getName());

		TextTokenizer tokenizer = new TextTokenizer(4096 * 8);
		tokenizer.readInputStream(new FileInputStream(getFile()));
		GameFile gameFile = new GameFile();

		gameFile.build(tokenizer);
		Logger.getInstance().timeEnd(Logger.INFO, "Parsing " + getFile().getName());
		nodes = gameFile.getNodes();
	}

	@Override
	public List<GFNode> getNodes() {
		return nodes;
	}
}
