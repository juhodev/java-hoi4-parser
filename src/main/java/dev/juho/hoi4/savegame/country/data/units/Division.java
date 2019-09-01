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
		ObjectNode idNode = (ObjectNode) node.get("id");
		IntegerNode id = (IntegerNode) idNode.get("id");
		this.id = id.getValue();

		IntegerNode type = (IntegerNode) idNode.get("type");
		this.type = type.getValue();

		StringNode lastCombatDate = (StringNode) node.get("last_combat_date");
		this.lastCombatDate = lastCombatDate.getValue();

		if (node.has("movement_progress")) {
			DoubleNode movementProgress = (DoubleNode) node.get("movement_progress");
			this.movementProgress = movementProgress.getValue();
		}

		if (node.has("move_priority")) {
			IntegerNode movePriority = (IntegerNode) node.get("move_priority");
			this.movePriority = movePriority.getValue();
		}

		IntegerNode location = (IntegerNode) node.get("location");
		this.location = location.getValue();

		StringNode logicalCountry = (StringNode) node.get("logical_country");
		this.logicalCountry = CountryTag.valueOf(logicalCountry.getValue());

		if (node.has("division_template_id")) {
			ObjectNode divisionTemplateId = (ObjectNode) node.get("division_template_id");
			IntegerNode templateId = (IntegerNode) divisionTemplateId.get("id");
			this.templateId = templateId.getValue();
			IntegerNode templateType = (IntegerNode) divisionTemplateId.get("type");
			this.templateType = templateType.getValue();
		}

		DoubleNode supplies = (DoubleNode) node.get("supplies");
		this.supplies = supplies.getValue();

		DoubleNode maxSupply = (DoubleNode) node.get("max_supply");
		this.maxSupply = maxSupply.getValue();

		DoubleNode organisation = (DoubleNode) node.get("organisation");
		this.organisation = organisation.getValue();

		DoubleNode strength = (DoubleNode) node.get("strength");
		this.strength = strength.getValue();

		ObjectNode armyManpower = (ObjectNode) node.get("army_manpower");
		readManpower(armyManpower);
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
		ObjectNode manpowerValue = (ObjectNode) node.get("army_manpower_value");
		ObjectNode manpowerNeed = (ObjectNode) node.get("army_manpower_need");

		this.manpower = readManpowerValue(manpowerValue);
		this.manpowerNeeded = readManpowerValue(manpowerNeed);
	}

	private int readManpowerValue(ObjectNode node) {
		ObjectNode value = (ObjectNode) node.get("value");
		IntegerNode intNode = (IntegerNode) value.get("value");
		return intNode.getValue();
	}

}
