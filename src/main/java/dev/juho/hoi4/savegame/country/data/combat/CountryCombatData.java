package dev.juho.hoi4.savegame.country.data.combat;

import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.savegame.country.data.HOIData;
import dev.juho.hoi4.savegame.country.data.misc.Equipment;
import dev.juho.hoi4.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CountryCombatData implements HOIData {

	private List<Equipment> lostEquipment;
	private int leaderId, leaderType, manpowerLost;
	private List<ModifierHour> modifierHours;
	private List<CountryTag> tags;

	public CountryCombatData() {
		this.lostEquipment = new ArrayList<>();
		this.leaderId = -1;
		this.leaderType = -1;
		this.manpowerLost = -1;
		this.modifierHours = new ArrayList<>();
		this.tags = new ArrayList<>();
	}

	public void build(ObjectNode countryCombatData) {
		if (countryCombatData.has("equipment_lost")) {
			ObjectNode equipmentLost = (ObjectNode) countryCombatData.get("equipment_lost");
			ListNode equipmentList;

			if (equipmentLost.get("equipment") instanceof ObjectNode) {
				ObjectNode equipmentObject = (ObjectNode) equipmentLost.get("equipment");
				equipmentList = equipmentObject.toListNode();
			} else {
				equipmentList = (ListNode) equipmentLost.get("equipment");
			}

			readLostEquipment(equipmentList);
		}

		if (countryCombatData.has("leader")) {
			readLeader((ObjectNode) countryCombatData.get("leader"));
		}

		if (countryCombatData.has("modifier_hours")) {
			readModifierHours((ListNode) countryCombatData.get("modifier_hours"));
		}

		if (countryCombatData.has("manpower_lost")) {
			manpowerLost = ((IntegerNode) countryCombatData.get("manpower_lost")).getValue();
		}

		if (countryCombatData.has("tags")) {
			readTags((ListNode) countryCombatData.get("tags"));
		}
	}

	public int getLeaderId() {
		return leaderId;
	}

	public int getLeaderType() {
		return leaderType;
	}

	public int getManpowerLost() {
		return manpowerLost;
	}

	public List<CountryTag> getTags() {
		return tags;
	}

	public List<Equipment> getLostEquipment() {
		return lostEquipment;
	}

	public List<ModifierHour> getModifierHours() {
		return modifierHours;
	}

	@Override
	public String asJSON() {
		return "{\"lostEquipment\": " + lostEquipmentJSON() + ", \"modifierHours\": " + modifierHoursJSON() + ", \"tags\": " + tagsJSON() + ", \"leaderId\": " + leaderId + ", \"leaderType\": " + leaderType + ", \"manpowerLost\": " + manpowerLost + "}";
	}

	private String lostEquipmentJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");

		for (Equipment lost : lostEquipment) {
			builder.append(lost.asJSON()).append(",");
		}

		builder.delete(builder.length() - 1, builder.length());
		builder.append("]");
		return builder.toString();
	}

	private String modifierHoursJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");

		for (ModifierHour hour : modifierHours) {
			builder.append(hour.asJSON()).append(",");
		}

		builder.delete(builder.length() - 1, builder.length());
		builder.append("]");
		return builder.toString();
	}

	private String tagsJSON() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");

		for (CountryTag tag : tags) {
			builder.append(tag).append(",");
		}

		builder.delete(builder.length() - 1, builder.length());
		builder.append("]");
		return builder.toString();
	}

	private void readLostEquipment(ListNode lostEquipment) {
		for (ASTNode node : lostEquipment.getChildren()) {
			ObjectNode child = (ObjectNode) node;

			ObjectNode idNode = (ObjectNode) child.get("id");
			IntegerNode id = (IntegerNode) idNode.get("id");
			IntegerNode type = (IntegerNode) idNode.get("type");

			DoubleNode amount = (DoubleNode) child.get("amount");

			Equipment equipment = new Equipment(id.getValue(), type.getValue(), amount.getValue());
			this.lostEquipment.add(equipment);
		}
	}

	private void readLeader(ObjectNode leader) {
		IntegerNode id = (IntegerNode) leader.get("id");
		IntegerNode type = (IntegerNode) leader.get("type");

		leaderId = id.getValue();
		leaderType = type.getValue();
	}

	private void readModifierHours(ListNode hours) {
		for (ASTNode child : hours.getChildren()) {
			ObjectNode obj = (ObjectNode) child;

			IntegerNode modifier = (IntegerNode) obj.get("modifier");
			IntegerNode time = (IntegerNode) obj.get("time");

			ModifierHour modifierHour = new ModifierHour(modifier.getValue(), time.getValue());
			modifierHours.add(modifierHour);
		}
	}

	private void readTags(ListNode tagsList) {
		for (ASTNode tag : tagsList.getChildren()) {
			StringNode tagNode = (StringNode) tag;

			if (Utils.hasEnum(CountryTag.values(), tagNode.getValue())) {
				CountryTag countryTag = CountryTag.valueOf(tagNode.getValue());
				tags.add(countryTag);
			}
		}
	}

	private static class ModifierHour implements HOIData {
		private int modifier, time;

		public ModifierHour(int modifier, int time) {
			this.modifier = modifier;
			this.time = time;
		}

		public int getModifier() {
			return modifier;
		}

		public int getTime() {
			return time;
		}


		@Override
		public String asJSON() {
			return "{\"modifier\": " + modifier + ", \"time\": " + time + "}";
		}
	}

}
