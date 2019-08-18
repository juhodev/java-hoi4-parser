package dev.juho.hoi4.parser.textparser;

import dev.juho.hoi4.parser.data.PlayerCountry;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class HOIType {

	private HashMap<HOIKey, Type> typeMap;

	public HOIType() {
		this.typeMap = new HashMap<>();
	}

	public void populate() {
		typeMap.put(HOIKey.DATE, Type.STRING);
		typeMap.put(HOIKey.DIFFICULTY, Type.STRING);
		typeMap.put(HOIKey.VERSION, Type.STRING);
		typeMap.put(HOIKey.TUTORIAL, Type.BOOLEAN);
		typeMap.put(HOIKey.PLAYER_COUNTRIES, Type.PLAYER_COUNTRIES);
		typeMap.put(HOIKey.SAVE_VERSION, Type.INTEGER);
		typeMap.put(HOIKey.DLCS, Type.INTEGER);
		typeMap.put(HOIKey.MODS, Type.LIST);
		typeMap.put(HOIKey.SESSION, Type.INTEGER);
		typeMap.put(HOIKey.SPEED, Type.INTEGER);
		typeMap.put(HOIKey.GAME_UNIQUE_SEED, Type.INTEGER);
		typeMap.put(HOIKey.GAME_UNIQUE_ID, Type.STRING);
	}

	public Type getType(HOIKey key) {
		return typeMap.get(key);
	}

	public enum Type {
		STRING,
		BOOLEAN,
		PLAYER_COUNTRIES,
		INTEGER,
		LIST,
	}

}
