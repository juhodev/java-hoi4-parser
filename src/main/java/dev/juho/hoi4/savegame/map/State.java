package dev.juho.hoi4.savegame.map;

import dev.juho.hoi4.parser.data.CountryTag;

public class State {

	private int id;
	private CountryTag owner, controller;
	private ManpowerPool manpowerPool;
	private boolean demilitarized, isBorderConflict;
	private String stateCategory;
//	TODO: This is still missing buildings, resources, resistance, flags, variables

	public State() {
		this.id = -1;
		this.owner = CountryTag.ALL;
		this.controller = CountryTag.ALL;
		this.manpowerPool = new ManpowerPool();
		this.demilitarized = false;
		this.isBorderConflict = false;
		this.stateCategory = "";
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setController(CountryTag controller) {
		this.controller = controller;
	}

	public void setBorderConflict(boolean borderConflict) {
		isBorderConflict = borderConflict;
	}

	public void setDemilitarized(boolean demilitarized) {
		this.demilitarized = demilitarized;
	}

	public void setManpowerPool(ManpowerPool manpowerPool) {
		this.manpowerPool = manpowerPool;
	}

	public void setOwner(CountryTag owner) {
		this.owner = owner;
	}

	public void setStateCategory(String stateCategory) {
		this.stateCategory = stateCategory;
	}

	public CountryTag getOwner() {
		return owner;
	}

	public ManpowerPool getManpowerPool() {
		return manpowerPool;
	}

	public String getStateCategory() {
		return stateCategory;
	}

	public int getId() {
		return id;
	}

	public CountryTag getController() {
		return controller;
	}

	public static class ManpowerPool {
		public int available, locked, total;

		public ManpowerPool() {
			this.available = -1;
			this.locked = -1;
			this.total = -1;
		}
	}
}
