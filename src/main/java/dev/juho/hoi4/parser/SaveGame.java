package dev.juho.hoi4.parser;

import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.parser.data.PlayerCountry;
import dev.juho.hoi4.parser.textparser.HOIKey;

import java.util.HashMap;
import java.util.List;

public class SaveGame {

	public HashMap<HOIKey, Object> save;

	public SaveGame() {
		save = new HashMap<>();
	}
}
