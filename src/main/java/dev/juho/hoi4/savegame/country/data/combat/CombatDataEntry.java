package dev.juho.hoi4.savegame.country.data.combat;

import dev.juho.hoi4.parser.textparser.ast.nodes.IntegerNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.savegame.country.data.HOIData;

public class CombatDataEntry implements HOIData {

	private int id, refCount;
	private CombatData combatData;

	public CombatDataEntry() {
		this.id = -1;
		this.refCount = -1;
		this.combatData = new CombatData();
	}

	public void build(ObjectNode combatDataEntry) {
		IntegerNode idNode = (IntegerNode) combatDataEntry.get("id");
		id = idNode.getValue();

		IntegerNode refNode = (IntegerNode) combatDataEntry.get("ref_count");
		refCount = refNode.getValue();

		ObjectNode combatDataNode = (ObjectNode) combatDataEntry.get("combat_data");
		combatData.build(combatDataNode);
	}

	public int getId() {
		return id;
	}

	public CombatData getCombatData() {
		return combatData;
	}

	public int getRefCount() {
		return refCount;
	}

	@Override
	public String asJSON() {
		return "{\"id\": " + id + ", \"refCount\": " + refCount + ", \"combatData\": " + combatData.asJSON() + "}";
	}
}
