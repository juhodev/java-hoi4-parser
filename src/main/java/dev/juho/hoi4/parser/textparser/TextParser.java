package dev.juho.hoi4.parser.textparser;

import dev.juho.hoi4.parser.Parser;
import dev.juho.hoi4.parser.textparser.ast.AST;
import dev.juho.hoi4.parser.textparser.token.TextParserInputStream;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import dev.juho.hoi4.savegame.SaveGame;
import dev.juho.hoi4.utils.Logger;

import java.io.*;

public class TextParser extends Parser {

	private HOIType types;
	private SaveGame saveGame;

	public TextParser(File file) {
		super(file);
	}

	@Override
	public void parse() throws IOException {
		Logger.getInstance().time("Parsing " + getFile().getName());

		TextParserInputStream in = new TextParserInputStream(new FileInputStream(getFile()));

		TextTokenizer tokenizer = new TextTokenizer(in, 4096 * 2);
		AST ast = new AST();

		ast.build(tokenizer);
		Logger.getInstance().timeEnd(Logger.INFO, "Parsing " + getFile().getName());
		this.saveGame = new SaveGame(ast.getNodes());
	}

	@Override
	public SaveGame getSaveGame() {
		return saveGame;
	}
}
