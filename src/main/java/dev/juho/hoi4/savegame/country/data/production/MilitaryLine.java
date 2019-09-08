package dev.juho.hoi4.savegame.country.data.production;

import dev.juho.hoi4.savegame.country.data.HOIData;

public class MilitaryLine extends ProductionLine implements HOIData {

	@Override
	public String asJSON() {
		return "{\"_type\": \"navalLine\", \"id\": " + getId() + ", \"type\": " + getType() + ", \"activeFactories\": " + getActiveFactories() + ", \"priority\": " + getPriority() +
				", \"amount\": " + getAmount() + ", \"requestedFactories\": " + getRequestedFactories() + ", \"produced\": " + getProduced() + ", \"speed\": " + getSpeed() + ", \"cost\": " + getCost() +
				", \"factoryEfficiencies\": [], \"resources\": " + getResources().asJSON() + "}";
	}
}
