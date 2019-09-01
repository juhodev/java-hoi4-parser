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

	public void build(Type personType, ObjectNode personNode) {
		this.personType = personType;

		if (personNode.has("id")) {
			ObjectNode idNode = (ObjectNode) personNode.get("id");
			IntegerNode id = (IntegerNode) idNode.get("id");
			this.id = id.getValue();
			IntegerNode type = (IntegerNode) idNode.get("type");
			this.type = type.getValue();
		}

		if (personNode.has("name")) {
			StringNode name = (StringNode) personNode.get("name");
			this.name = name.getValue();
		}
		if (personNode.has("picture")) {
			StringNode picture = (StringNode) personNode.get("picture");
			this.picture = picture.getValue();
		}
		if (personNode.has("skill")) {
			IntegerNode skill = (IntegerNode) personNode.get("skill");
			this.skill = skill.getValue();
		}
		if (personNode.has("experience")) {
			DoubleNode experience = (DoubleNode) personNode.get("experience");
			this.experience = experience.getValue();
		}
		if (personNode.has("attack_skill")) {
			IntegerNode attackSkill = (IntegerNode) personNode.get("attack_skill");
			this.attackSkill = attackSkill.getValue();
		}
		if (personNode.has("defense_skill")) {
			IntegerNode defenseSkill = (IntegerNode) personNode.get("defense_skill");
			this.defenseSkill = defenseSkill.getValue();
		}
		if (personNode.has("planning_skill")) {
			IntegerNode planningSkill = (IntegerNode) personNode.get("planning_skill");
			this.planningSkill = planningSkill.getValue();
		}
		if (personNode.has("logistics_skill")) {
			IntegerNode logisticsSkill = (IntegerNode) personNode.get("logistics_skill");
			this.logisticsSkill = logisticsSkill.getValue();
		}
		if (personNode.has("traits")) {
			ListNode traitsList = (ListNode) personNode.get("traits");
			readTraits(traitsList);
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

	private void readTraits(ListNode node) {
		for (ASTNode child : node.getChildren()) {
			String trait = ((StringNode) child).getValue();
			traits.add(trait);
		}
	}

	public enum Type {
		CORPS_COMMANDER,
		FIELD_MARSHAL,
		NAVY_LEADER,
	}

}
