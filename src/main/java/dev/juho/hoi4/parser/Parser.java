package dev.juho.hoi4.parser;

import dev.juho.hoi4.savegame.SaveGame;

import java.io.File;
import java.io.IOException;

public abstract class Parser {

	private File file;

	public Parser(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public abstract void parse() throws IOException;

	public abstract SaveGame getSaveGame();
}
