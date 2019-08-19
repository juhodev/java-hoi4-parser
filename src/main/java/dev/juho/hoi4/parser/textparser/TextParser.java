package dev.juho.hoi4.parser.textparser;

import dev.juho.hoi4.Main;
import dev.juho.hoi4.parser.Parser;
import dev.juho.hoi4.parser.SaveGame;
import dev.juho.hoi4.parser.data.HOIEnum;
import dev.juho.hoi4.parser.textparser.ast.AST;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.token.TextParserInputStream;
import dev.juho.hoi4.parser.textparser.token.TextParserToken;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import dev.juho.hoi4.utils.Logger;
import dev.juho.hoi4.utils.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

		Logger.getInstance().log(Logger.INFO, "nodes in ast: " + ast.getNodes().size());

//		List<ASTNode> nodes = ast.getNodes();
//
//		for (ASTNode node : nodes) {
//			Logger.getInstance().log(Logger.DEBUG, node);
//		}
	}


	@Override
	public SaveGame getSaveGame() {
		return saveGame;
	}
}
