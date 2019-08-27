package dev.juho.hoi4.savegame.country.data.technology;

public class Technology {

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
}
