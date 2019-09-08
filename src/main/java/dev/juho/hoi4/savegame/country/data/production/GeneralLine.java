package dev.juho.hoi4.savegame.country.data.production;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.savegame.country.data.HOIData;
import dev.juho.hoi4.utils.Utils;

import java.util.HashMap;

public class GeneralLine implements HOIData {

	private int id, type, activeFactories, priority, amount, toRepair;
	private double speed, cost;
	private Building building;

	public GeneralLine() {
		this.id = -1;
		this.type = -1;
		this.activeFactories = -1;
		this.priority = -1;
		this.amount = -1;
		this.toRepair = -1;

		this.speed = -1;
		this.cost = -1;

		this.building = new Building();
	}

	public void build(ObjectNode node) {
		if (node.has("id")) {
			ObjectNode idNode = (ObjectNode) node.get("id");
			IntegerNode id = (IntegerNode) idNode.get("id");
			this.id = id.getValue();
			IntegerNode type = (IntegerNode) idNode.get("type");
			this.type = type.getValue();
		}

		if (node.has("active_factories")) {
			IntegerNode activeFactories = (IntegerNode) node.get("active_factories");
			this.activeFactories = activeFactories.getValue();
		}
		if (node.has("priority")) {
			IntegerNode priority = (IntegerNode) node.get("priority");
			this.priority = priority.getValue();
		}
		if (node.has("amount")) {
			IntegerNode amount = (IntegerNode) node.get("amount");
			this.amount = amount.getValue();
		}
		if (node.has("speed")) {
			DoubleNode speed = (DoubleNode) node.get("speed");
			this.speed = speed.getValue();
		}
		if (node.has("cost")) {
			DoubleNode cost = (DoubleNode) node.get("cost");
			this.cost = cost.getValue();
		}
		if (node.has("building")) {
			ObjectNode building = (ObjectNode) node.get("building");
			readBuilding(building);
		}
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public int getPriority() {
		return priority;
	}

	public int getActiveFactories() {
		return activeFactories;
	}

	public int getAmount() {
		return amount;
	}

	public double getSpeed() {
		return speed;
	}

	public double getCost() {
		return cost;
	}

	public Building getBuilding() {
		return building;
	}

	public int getToRepair() {
		return toRepair;
	}

	private void readBuilding(ObjectNode obj) {
		if (obj.has("type")) {
			StringNode type = (StringNode) obj.get("type");
			building.setType(type.getValue());
		}

		if (obj.has("state")) {
			IntegerNode state = (IntegerNode) obj.get("state");
			building.setState(state.getValue());
		}
	}

	@Override
	public String asJSON() {
		return "{\"_type\": \"generalLine\", \"id\": " + id + ", \"type\": \"" + type + "\", \"activeFactories\": " + activeFactories + ", \"priority\": " + priority + ", \"amount\": " + amount + ", \"toRepair\": " + toRepair + ", \"speed\": " + speed + ", \"cost\": " + cost + ", \"building\": " + building.asJSON() + "}";
	}

	private class Building implements HOIData {

		private String type;
		private int state;

		public Building() {
			this.type = "";
			this.state = -1;
		}

		public void setState(int state) {
			this.state = state;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getState() {
			return state;
		}

		public String getType() {
			return type;
		}


		@Override
		public String asJSON() {
			return "{\"_type\": \"building\", \"type\": \"" + type + "\", \"state\": " + state + "}";
		}
	}

}
