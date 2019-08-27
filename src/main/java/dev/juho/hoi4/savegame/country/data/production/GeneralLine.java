package dev.juho.hoi4.savegame.country.data.production;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.utils.Utils;

import java.util.HashMap;

public class GeneralLine {

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
		HashMap<String, Object> children = Utils.getObjectChildren(node);

		if (children.containsKey("id")) {
			readId((ObjectNode) children.get("id"));
		}

		if (children.containsKey("active_factories")) {
			activeFactories = ((IntegerNode) children.get("active_factories")).getValue();
		}

		if (children.containsKey("priority")) {
			priority = ((IntegerNode) children.get("priority")).getValue();
		}

		if (children.containsKey("amount")) {
			amount = ((IntegerNode) children.get("amount")).getValue();
		}

		if (children.containsKey("speed")) {
			speed = ((DoubleNode) children.get("speed")).getValue();
		}

		if (children.containsKey("cost")) {
			cost = ((DoubleNode) children.get("cost")).getValue();
		}

		if (children.containsKey("building")) {
			readBuilding((ObjectNode) children.get("building"));
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

	private void readId(ObjectNode obj) {
		HashMap<String, Object> idChildren = Utils.getObjectChildren(obj);
		IntegerNode id = (IntegerNode) idChildren.get("id");
		IntegerNode type = (IntegerNode) idChildren.get("type");

		this.id = id.getValue();
		this.type = type.getValue();
	}

	private void readBuilding(ObjectNode obj) {
		HashMap<String, Object> children = Utils.getObjectChildren(obj);
		if (children.containsKey("type")) {
			StringNode typeNode = (StringNode) children.get("type");
			building.setType(typeNode.getValue());
		}

		if (children.containsKey("state")) {
			IntegerNode stateNode = (IntegerNode) children.get("state");
			building.setState(stateNode.getValue());
		}
	}

	private class Building {

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
	}

}
