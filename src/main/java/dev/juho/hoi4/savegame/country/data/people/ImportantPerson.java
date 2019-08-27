package dev.juho.hoi4.savegame.country.data.people;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImportantPerson {

	private Type personType;
	private int id, type, skill, attackSkill, defenseSkill, planningSkill, logisticsSkill;
	private String name, picture;
	private double experience;
	private List<String> traits;

	public ImportantPerson() {
		this.id = -1;
		this.type = -1;
		this.skill = -1;
		this.attackSkill = -1;
		this.defenseSkill = -1;
		this.planningSkill = -1;
		this.logisticsSkill = -1;

		this.name = "";
		this.picture = "";

		this.experience = -1;

		this.traits = new ArrayList<>();
	}

	public void build(PropertyNode personNode) {
		ObjectNode node = (ObjectNode) personNode.getValue();

		if (personNode.getKey().equalsIgnoreCase("corps_commander")) {
			personType = Type.CORPS_COMMANDER;
		} else if (personNode.getKey().equalsIgnoreCase("field_marshal")) {
			personType = Type.FIELD_MARSHAL;
		} else if (personNode.getKey().equalsIgnoreCase("navy_leader")) {
			personType = Type.NAVY_LEADER;
		}

		for (ASTNode child : node.getChildren()) {
			PropertyNode propNode = (PropertyNode) child;

			switch (propNode.getKey()) {
				case "id":
					readId((ObjectNode) propNode.getValue());
					break;

				case "name":
					name = ((StringNode) propNode.getValue()).getValue();
					break;

				case "picture":
					picture = ((StringNode) propNode.getValue()).getValue();
					break;

				case "skill":
					skill = ((IntegerNode) propNode.getValue()).getValue();
					break;

				case "experience":
					experience = ((DoubleNode) propNode.getValue()).getValue();
					break;

				case "attack_skill":
					attackSkill = ((IntegerNode) propNode.getValue()).getValue();
					break;

				case "defense_skill":
					defenseSkill = ((IntegerNode) propNode.getValue()).getValue();
					break;

				case "planning_skill":
					planningSkill = ((IntegerNode) propNode.getValue()).getValue();
					break;

				case "logistics_skill":
					logisticsSkill = ((IntegerNode) propNode.getValue()).getValue();
					break;

				case "traits":
					readTraits((ListNode) propNode.getValue());
					break;
			}
		}
	}

	public int getType() {
		return type;
	}

	public Type getPersonType() {
		return personType;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getExperience() {
		return experience;
	}

	public int getAttackSkill() {
		return attackSkill;
	}

	public int getDefenseSkill() {
		return defenseSkill;
	}

	public int getLogisticsSkill() {
		return logisticsSkill;
	}

	public int getPlanningSkill() {
		return planningSkill;
	}

	public int getSkill() {
		return skill;
	}

	public List<String> getTraits() {
		return traits;
	}

	public String getPicture() {
		return picture;
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

	private void readTraits(ListNode node) {
		for (ASTNode child : node.getChildren()) {
			String trait = ((StringNode) child).getValue();
			traits.add(trait);
		}
	}

	private enum Type {
		CORPS_COMMANDER,
		FIELD_MARSHAL,
		NAVY_LEADER,
	}

}
