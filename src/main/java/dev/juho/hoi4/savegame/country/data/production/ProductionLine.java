package dev.juho.hoi4.savegame.country.data.production;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.savegame.country.data.resources.ResourceData;
import dev.juho.hoi4.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductionLine {

	private int id, type, activeFactories, priority, amount, requestedFactories;
	private double produced, speed, cost;
	private List<Double> factoryEfficiencies;
	private ResourceData resources;

	public ProductionLine() {
		this.id = -1;
		this.type = -1;
		this.activeFactories = -1;
		this.priority = -1;
		this.amount = -1;
		this.requestedFactories = -1;

		this.produced = -1;
		this.speed = -1;
		this.cost = -1;

		this.factoryEfficiencies = new ArrayList<>();
		this.resources = new ResourceData();
	}

	public void build(ObjectNode obj) {
		if (obj.has("id")) {
			ObjectNode idNode = (ObjectNode) obj.get("id");

			IntegerNode id = (IntegerNode) idNode.get("id");
			this.id = id.getValue();

			IntegerNode type = (IntegerNode) idNode.get("type");
			this.type = type.getValue();
		}

		if (obj.has("produced")) {
			DoubleNode produced = (DoubleNode) obj.get("produced");
			this.produced = produced.getValue();
		}

		if (obj.has("active_factories")) {
			IntegerNode activeFactories = (IntegerNode) obj.get("active_factories");
			this.activeFactories = activeFactories.getValue();
		}

		if (obj.has("priority")) {
			IntegerNode priority = (IntegerNode) obj.get("priority");
			this.priority = priority.getValue();
		}

		if (obj.has("amount")) {
			IntegerNode amount = (IntegerNode) obj.get("amount");
			this.amount = amount.getValue();
		}

		if (obj.has("speed")) {
			DoubleNode speed = (DoubleNode) obj.get("speed");
			this.speed = speed.getValue();
		}

		if (obj.has("cost")) {
			DoubleNode cost = (DoubleNode) obj.get("cost");
			this.cost = cost.getValue();
		}

		if (obj.has("requested_factories")) {
			IntegerNode requestedFactories = (IntegerNode) obj.get("requested_factories");
			this.requestedFactories = requestedFactories.getValue();
		}

		if (obj.has("factory_efficiencies")) {
			ListNode factoryEfficiencies = (ListNode) obj.get("factory_efficiencies");
			readFactoryEfficiencies(factoryEfficiencies);
		}

		if (obj.has("resources")) {
			ObjectNode resources = (ObjectNode) obj.get("resources");
			readResources(resources);
		}
	}

	public double getCost() {
		return cost;
	}

	public double getProduced() {
		return produced;
	}

	public double getSpeed() {
		return speed;
	}

	public int getActiveFactories() {
		return activeFactories;
	}

	public int getAmount() {
		return amount;
	}

	public int getId() {
		return id;
	}

	public int getPriority() {
		return priority;
	}

	public int getRequestedFactories() {
		return requestedFactories;
	}

	public int getType() {
		return type;
	}

	public List<Double> getFactoryEfficiencies() {
		return factoryEfficiencies;
	}

	public ResourceData getResources() {
		return resources;
	}

	private void readFactoryEfficiencies(ListNode list) {
		for (ASTNode node : list.getChildren()) {
			factoryEfficiencies.add(((DoubleNode) node).getValue());
		}
	}

	private void readResources(ObjectNode node) {
		if (node.has("oil")) {
			DoubleNode dNode = (DoubleNode) node.get("oil");
			resources.setOil(dNode.getValue());
		}

		if (node.has("aluminium")) {
			DoubleNode dNode = (DoubleNode) node.get("aluminium");
			resources.setAluminium(dNode.getValue());
		}

		if (node.has("rubber")) {
			DoubleNode dNode = (DoubleNode) node.get("rubber");
			resources.setRubber(dNode.getValue());
		}

		if (node.has("tungsten")) {
			DoubleNode dNode = (DoubleNode) node.get("tungsten");
			resources.setTungsten(dNode.getValue());
		}

		if (node.has("steel")) {
			DoubleNode dNode = (DoubleNode) node.get("steel");
			resources.setSteel(dNode.getValue());
		}

		if (node.has("chromium")) {
			DoubleNode dNode = (DoubleNode) node.get("chromium");
			resources.setChromium(dNode.getValue());
		}
	}

}
