package dev.juho.hoi4.savegame.map;

import dev.juho.hoi4.parser.data.CountryTag;

public class Province {

	private int colorValue, id;
	private CountryTag owner;
	private String type;

	public Province() {
		this.colorValue = -1;
		this.id = -1;
		this.owner = CountryTag.ALL;
		this.type = "";
	}

	public void setOwner(CountryTag owner) {
		this.owner = owner;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setColorValue(int colorValue) {
		this.colorValue = colorValue;
	}

	public CountryTag getOwner() {
		return owner;
	}

	public int getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public int getColorValue() {
		return colorValue;
	}
}
