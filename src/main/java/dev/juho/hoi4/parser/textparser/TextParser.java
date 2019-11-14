package dev.juho.hoi4.parser.textparser;

import dev.juho.hoi4.parser.Parser;
import dev.juho.hoi4.parser.ParserInputStream;
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

		ParserInputStream in = new ParserInputStream(new FileInputStream(getFile()));

		TextTokenizer tokenizer = new TextTokenizer(in, 4096 * 8);
		GameFile gameFile = new GameFile(in);

		gameFile.build(tokenizer);
		Logger.getInstance().timeEnd(Logger.INFO, "Parsing " + getFile().getName());
		nodes = gameFile.getNodes();
	}

	@Override
	public List<GFNode> getNodes() {
		return nodes;
	}
}
