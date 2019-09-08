package dev.juho.hoi4.savegame.country.data.fuel;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.DoubleNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ListNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.savegame.country.data.HOIData;

import java.util.ArrayList;
import java.util.List;

public class Fuel implements HOIData {

	private double fuel, maxFuel, fuelGain, fuelGainPerOil, fuelGainFromStates;
	private List<FuelConsumerData> fuelConsumerDataList;

	public Fuel() {
		this.fuel = -1;
		this.maxFuel = -1;
		this.fuelGain = -1;
		this.fuelGainPerOil = -1;
		this.fuelGainFromStates = -1;

		this.fuelConsumerDataList = new ArrayList<>();
	}

	public void build(ObjectNode fuelObject) {
		this.fuel = ((DoubleNode) fuelObject.get("fuel")).getValue();
		this.maxFuel = ((DoubleNode) fuelObject.get("max_fuel")).getValue();
		this.fuelGain = ((DoubleNode) fuelObject.get("fuel_gain")).getValue();
		this.fuelGainPerOil = ((DoubleNode) fuelObject.get("fuel_gain_per_oil")).getValue();
		this.fuelGainFromStates = ((DoubleNode) fuelObject.get("fuel_gain_from_states")).getValue();

		if (fuelObject.has("fuel_consumer_data")) {
			ASTNode fuelConsumerData = (ASTNode) fuelObject.get("fuel_consumer_data");
			readFuelConsumerData(fuelConsumerData);
		}

//		TODO: read history
	}

	public double getFuel() {
		return fuel;
	}

	public double getFuelGain() {
		return fuelGain;
	}

	public double getFuelGainFromStates() {
		return fuelGainFromStates;
	}

	public double getFuelGainPerOil() {
		return fuelGainPerOil;
	}

	public double getMaxFuel() {
		return maxFuel;
	}

	public List<FuelConsumerData> getFuelConsumerDataList() {
		return fuelConsumerDataList;
	}

	private void readFuelConsumerData(ASTNode node) {
		if (node instanceof ObjectNode) {
			FuelConsumerData data = new FuelConsumerData();
			data.build(node);
			fuelConsumerDataList.add(data);
		} else if (node instanceof ListNode) {
			ListNode listNode = (ListNode) node;

			for (ASTNode child : listNode.getChildren()) {
				FuelConsumerData data = new FuelConsumerData();
				data.build(child);
				fuelConsumerDataList.add(data);
			}
		}
	}

	private String fuelConsumersAsJSON() {
		StringBuilder builder = new StringBuilder();

		builder.append("[ ");

		for (FuelConsumerData consumerData : fuelConsumerDataList) {
			builder.append(consumerData.asJSON()).append(",");
		}

		builder.delete(builder.length() - 1, builder.length());
		builder.append("]");

		return builder.toString();
	}

	@Override
	public String asJSON() {
		return "{\"_type\": \"fuel\", \"fuel\": " + fuel + ", \"maxFuel\": " + maxFuel + ", \"fuelGain\": " + fuelGain + ", \"fuelGainPerOil\": " + fuelGainPerOil + ", \"fuelGainFromStates\": " + fuelGainFromStates + ", \"fuelConsumerList\": " + fuelConsumersAsJSON() + "}";
	}
}
