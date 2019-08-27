package dev.juho.hoi4.savegame.country.data.units;

import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.savegame.country.data.misc.Equipment;
import dev.juho.hoi4.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Division {

	private int id, type, movePriority, location, templateId, templateType, manpower, manpowerNeeded;
	private double movementProgress, supplies, maxSupply, organisation, strength, experience;
	private String lastCombatDate;
	private CountryTag logicalCountry;
	private List<Equipment> equipmentList;

	public Division() {
		this.id = -1;
		this.type = -1;
		this.movePriority = -1;
		this.location = -1;
		this.templateId = -1;
		this.templateType = -1;
		this.manpower = -1;
		this.manpowerNeeded = -1;

		this.movementProgress = -1;
		this.supplies = -1;
		this.maxSupply = -1;
		this.organisation = -1;
		this.strength = -1;
		this.experience = -1;

		this.lastCombatDate = "";

		this.equipmentList = new ArrayList<>();
	}

	public void build(ObjectNode node) {
		for (ASTNode child : node.getChildren()) {
			PropertyNode propNode = (PropertyNode) child;

			switch (propNode.getKey()) {
				case "id":
					readId((ObjectNode) propNode.getValue());
					break;

				case "last_combat_date":
					lastCombatDate = ((StringNode) propNode.getValue()).getValue();
					break;

				case "movement_progress":
					movementProgress = ((DoubleNode) propNode.getValue()).getValue();
					break;

				case "move_priority":
					movePriority = ((IntegerNode) propNode.getValue()).getValue();
					break;

				case "location":
					location = ((IntegerNode) propNode.getValue()).getValue();
					break;

				case "logical_country":
					logicalCountry = CountryTag.valueOf(((StringNode) propNode.getValue()).getValue());
					break;

				case "division_template_id":
					readDivisionTemplateId((ObjectNode) propNode.getValue());
					break;

				case "supplies":
					supplies = ((DoubleNode) propNode.getValue()).getValue();
					break;

				case "max_supply":
					maxSupply = ((DoubleNode) propNode.getValue()).getValue();
					break;

				case "organisation":
					organisation = ((DoubleNode) propNode.getValue()).getValue();
					break;

				case "strength":
					strength = ((DoubleNode) propNode.getValue()).getValue();
					break;

				case "army_manpower":
					readManpower((ObjectNode) propNode.getValue());
					break;
			}
		}
	}

	public double getExperience() {
		return experience;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public CountryTag getLogicalCountry() {
		return logicalCountry;
	}

	public double getMaxSupply() {
		return maxSupply;
	}

	public double getMovementProgress() {
		return movementProgress;
	}

	public double getOrganisation() {
		return organisation;
	}

	public double getStrength() {
		return strength;
	}

	public double getSupplies() {
		return supplies;
	}

	public int getLocation() {
		return location;
	}

	public int getManpower() {
		return manpower;
	}

	public int getManpowerNeeded() {
		return manpowerNeeded;
	}

	public int getMovePriority() {
		return movePriority;
	}

	public int getTemplateId() {
		return templateId;
	}

	public int getTemplateType() {
		return templateType;
	}

	public List<Equipment> getEquipmentList() {
		return equipmentList;
	}

	public String getLastCombatDate() {
		return lastCombatDate;
	}

	private void readManpower(ObjectNode node) {
		HashMap<String, Object> children = Utils.getObjectChildren(node);

		if (children.containsKey("army_manpower_value")) {
			ObjectNode objNode = (ObjectNode) children.get("army_manpower_value");
			manpower = readManpowerValue(objNode);
		}

		if (children.containsKey("army_manpower_need")) {
			ObjectNode objNode = (ObjectNode) children.get("army_manpower_need");
			manpowerNeeded = readManpowerValue(objNode);
		}
	}

	private int readManpowerValue(ObjectNode node) {
		HashMap<String, Object> children = Utils.getObjectChildren(node);

		if (children.containsKey("value")) {
			HashMap<String, Object> childrenOfChildren = Utils.getObjectChildren((ObjectNode) children.get("value"));

			return ((IntegerNode) childrenOfChildren.get("value")).getValue();
		}

		return -1;
	}

	private void readDivisionTemplateId(ObjectNode node) {
		HashMap<String, Object> children = Utils.getObjectChildren(node);

		if (children.containsKey("id")) {
			IntegerNode idNode = (IntegerNode) children.get("id");
			templateId = idNode.getValue();
		}

		if (children.containsKey("type")) {
			IntegerNode typeNode = (IntegerNode) children.get("type");
			templateType = typeNode.getValue();
		}
	}

	private void readId(ObjectNode node) {
		HashMap<String, Object> children = Utils.getObjectChildren(node);

		if (children.containsKey("id")) {
			IntegerNode idNode = (IntegerNode) children.get("id");
			id = idNode.getValue();
		}

		if (children.containsKey("type")) {
			IntegerNode typeNode = (IntegerNode) children.get("type");
			type = typeNode.getValue();
		}
	}

}
