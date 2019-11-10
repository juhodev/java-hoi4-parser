package dev.juho.hoi4.parser.binparser;

import dev.juho.hoi4.parser.Parser;
import dev.juho.hoi4.parser.ParserInputStream;
import dev.juho.hoi4.parser.binparser.token.BinTokenizer;
import dev.juho.hoi4.savegame.SaveGame;
import dev.juho.hoi4.utils.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BinParser extends Parser {

	private SaveGame saveGame;

	public BinParser(File file) {
		super(file);
	}

	@Override
	public void parse() throws IOException {
		Logger.getInstance().time("Parsing " + getFile().getName());

		ParserInputStream in = new ParserInputStream(new FileInputStream(getFile()), 4096 * 8);

		BinTokenizer tokenizer = new BinTokenizer(in, 4096 * 8);
		tokenizer.init();
		while (!tokenizer.eof()) {
			tokenizer.next();
		}
		Logger.getInstance().timeEnd(Logger.INFO, "Parsing " + getFile().getName());
	}

	@Override
	public SaveGame getSaveGame() {
		return null;
	}
}
