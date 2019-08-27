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
		for (ASTNode node : obj.getChildren()) {
			PropertyNode propNode = (PropertyNode) node;

			switch (propNode.getKey()) {
				case "id":
					readId((ObjectNode) propNode.getValue());
					break;

				case "produced":
					produced = ((DoubleNode) propNode.getValue()).getValue();
					break;

				case "active_factories":
					activeFactories = ((IntegerNode) propNode.getValue()).getValue();
					break;

				case "priority":
					priority = ((IntegerNode) propNode.getValue()).getValue();
					break;

				case "amount":
					amount = ((IntegerNode) propNode.getValue()).getValue();
					break;

				case "speed":
					speed = ((DoubleNode) propNode.getValue()).getValue();
					break;

				case "cost":
					cost = ((DoubleNode) propNode.getValue()).getValue();
					break;

				case "requested_factories":
					requestedFactories = ((IntegerNode) propNode.getValue()).getValue();
					break;

				case "factory_efficiencies":
					readFactoryEfficiencies((ListNode) propNode.getValue());
					break;

				case "resources":
					readResources((ObjectNode) propNode.getValue());
					break;
			}
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

	private void readId(ObjectNode obj) {
		HashMap<String, Object> children = Utils.getObjectChildren(obj);

		id = ((IntegerNode) children.get("id")).getValue();
		type = ((IntegerNode) children.get("type")).getValue();
	}

	private void readFactoryEfficiencies(ListNode list) {
		for (ASTNode node : list.getChildren()) {
			factoryEfficiencies.add(((DoubleNode) node).getValue());
		}
	}

	private void readResources(ObjectNode node) {
		HashMap<String, Object> children = Utils.getObjectChildren(node);

		if (children.containsKey("oil")) {
			DoubleNode dNode = (DoubleNode) children.get("oil");
			resources.setOil(dNode.getValue());
		}

		if (children.containsKey("aluminium")) {
			DoubleNode dNode = (DoubleNode) children.get("aluminium");
			resources.setAluminium(dNode.getValue());
		}

		if (children.containsKey("rubber")) {
			DoubleNode dNode = (DoubleNode) children.get("rubber");
			resources.setRubber(dNode.getValue());
		}

		if (children.containsKey("tungsten")) {
			DoubleNode dNode = (DoubleNode) children.get("tungsten");
			resources.setTungsten(dNode.getValue());
		}

		if (children.containsKey("steel")) {
			DoubleNode dNode = (DoubleNode) children.get("steel");
			resources.setSteel(dNode.getValue());
		}

		if (children.containsKey("chromium")) {
			DoubleNode dNode = (DoubleNode) children.get("chromium");
			resources.setChromium(dNode.getValue());
		}
	}

}
