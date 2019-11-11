package dev.juho.hoi4.parser.textparser;

import dev.juho.hoi4.parser.Parser;
import dev.juho.hoi4.parser.textparser.ast.AST;
import dev.juho.hoi4.parser.ParserInputStream;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import dev.juho.hoi4.savegame.SaveGame;
import dev.juho.hoi4.utils.Logger;

import java.io.*;
import java.util.List;

public class TextParser extends Parser {

	private List<ASTNode> nodes;

	public TextParser(File file) {
		super(file);
	}

	@Override
	public void parse() throws IOException {
		Logger.getInstance().time("Parsing " + getFile().getName());

		ParserInputStream in = new ParserInputStream(new FileInputStream(getFile()));

		TextTokenizer tokenizer = new TextTokenizer(in, 4096 * 8);
		AST ast = new AST();

		ast.build(tokenizer);
		Logger.getInstance().timeEnd(Logger.INFO, "Parsing " + getFile().getName());
		nodes = ast.getNodes();
	}

	@Override
	public List<ASTNode> getNodes() {
		return nodes;
	}
}
