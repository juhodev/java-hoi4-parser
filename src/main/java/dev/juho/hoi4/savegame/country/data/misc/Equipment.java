package dev.juho.hoi4.savegame.country.data.misc;

import dev.juho.hoi4.savegame.country.data.HOIData;

public class Equipment implements HOIData {

	private int id, type;
	private double amount;

	public Equipment(int id, int type, double amount) {
		this.id = id;
		this.type = type;
		this.amount = amount;
	}

	public int getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public double getAmount() {
		return amount;
	}

	@Override
	public String asJSON() {
		return "{\"_type\": \"Equipment\", \"id\": " + id + ", \"type\": " + type + ", \"amount\": " + amount + "}";
	}
}
