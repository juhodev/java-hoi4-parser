package dev.juho.hoi4.savegame.country.data.technology;

import dev.juho.hoi4.savegame.country.data.HOIData;

public class Technology implements HOIData {

	private String name;
	private int level;
	private double researchPoints;

	public Technology(String name, int level, double researchPoints) {
		this.name = name;
		this.level = level;
		this.researchPoints = researchPoints;
	}

	public String getName() {
		return name;
	}

	public double getResearchPoints() {
		return researchPoints;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public String asJSON() {
		return "{\"_type\": \"technology\", \"name\": \"" + name + "\", \"level\": " + level + ", \"researchPoints\": " + researchPoints + "}";
	}
}
