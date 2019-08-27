package dev.juho.hoi4.savegame.country.data.technology;

public class ResearchSlot {

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
}
