package dev.juho.hoi4.savegame.country.data.combat;

import dev.juho.hoi4.parser.textparser.ast.nodes.BooleanNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.IntegerNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.StringNode;
import dev.juho.hoi4.savegame.country.data.HOIData;

public class CombatData implements HOIData {

	private String date;
	private int province;
	private boolean defensiveVictory;
	private CountryCombatData defender, attacker;

	public CombatData() {
		this.date = "";
		this.province = -1;
		this.defensiveVictory = false;
		this.defender = new CountryCombatData();
		this.attacker = new CountryCombatData();
	}

	public void build(ObjectNode combatData) {
		ObjectNode defenderNode = (ObjectNode) combatData.get("defender");
		defender.build(defenderNode);

		ObjectNode attackerNode = (ObjectNode) combatData.get("attacker");
		attacker.build(attackerNode);

		StringNode dateNode = (StringNode) combatData.get("date");
		date = dateNode.getValue();

		IntegerNode provinceNode = (IntegerNode) combatData.get("province");
		province = provinceNode.getValue();

		if (combatData.has("defensive_victory")) {
			BooleanNode booleanNode = (BooleanNode) combatData.get("defensive_victory");
			defensiveVictory = booleanNode.getValue();
		}
	}

	public CountryCombatData getAttacker() {
		return attacker;
	}

	public CountryCombatData getDefender() {
		return defender;
	}

	public int getProvince() {
		return province;
	}

	public String getDate() {
		return date;
	}

	public boolean wasDefensiveVictory() {
		return defensiveVictory;
	}

	@Override
	public String asJSON() {
		return "{\"date\": \"" + date + "\", \"province\": " + province + ", \"defensiveVictory\": " + defensiveVictory + ", \"defender\": " + defender.asJSON() + ", \"attacker\": " + attacker.asJSON() + "}";
	}
}
