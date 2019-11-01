package dev.juho.hoi4.savegame.map;

import dev.juho.hoi4.parser.data.CountryTag;

public class Province {

	private int id;
	private CountryTag controller;

	public Province() {
		this.id = -1;
		this.controller = CountryTag.ALL;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setController(CountryTag controller) {
		this.controller = controller;
	}

	public int getId() {
		return id;
	}

	public CountryTag getController() {
		return controller;
	}
}
