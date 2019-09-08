package dev.juho.hoi4.savegame.country.data.technology;

import dev.juho.hoi4.savegame.country.data.HOIData;

public class ResearchSlot implements HOIData {

	private String current;
	private double points;

	public ResearchSlot(String current, double points) {
		this.current = current;
		this.points = points;
	}

	public double getPoints() {
		return points;
	}

	public String getCurrent() {
		return current;
	}

	@Override
	public String asJSON() {
		return "{\"_type\": \"researchSlot\", \"current\": \"" + current + "\", \"points\": " + points + "}";
	}
}
