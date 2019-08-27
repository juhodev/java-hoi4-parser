package dev.juho.hoi4.savegame.country.data.resources;

public class ResourceData {

	private double oil, aluminium, rubber, tungsten, steel, chromium;

	public ResourceData() {
		this.oil = -1;
		this.aluminium = -1;
		this.rubber = -1;
		this.tungsten = -1;
		this.steel = -1;
		this.chromium = -1;
	}

	public ResourceData(double oil, double aluminium, double rubber, double tungsten, double steel, double chromium) {
		this.oil = oil;
		this.aluminium = aluminium;
		this.rubber = rubber;
		this.tungsten = tungsten;
		this.steel = steel;
		this.chromium = chromium;
	}

	public void setAluminium(double aluminium) {
		this.aluminium = aluminium;
	}

	public void setChromium(double chromium) {
		this.chromium = chromium;
	}

	public void setOil(double oil) {
		this.oil = oil;
	}

	public void setRubber(double rubber) {
		this.rubber = rubber;
	}

	public void setSteel(double steel) {
		this.steel = steel;
	}

	public void setTungsten(double tungsten) {
		this.tungsten = tungsten;
	}

	public double getAluminium() {
		return aluminium;
	}

	public double getChromium() {
		return chromium;
	}

	public double getOil() {
		return oil;
	}

	public double getRubber() {
		return rubber;
	}

	public double getSteel() {
		return steel;
	}

	public double getTungsten() {
		return tungsten;
	}
}
