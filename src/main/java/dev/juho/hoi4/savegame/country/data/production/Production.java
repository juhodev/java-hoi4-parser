package dev.juho.hoi4.savegame.country.data.production;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.DoubleNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.IntegerNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.PropertyNode;
import dev.juho.hoi4.savegame.country.data.misc.Equipment;
import dev.juho.hoi4.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Production {

	private List<MilitaryLine> militaryLines;
	private List<NavalLine> navalLines;
	private List<GeneralLine> generalLines;
	private List<Equipment> equipment;

	public Production() {
		this.militaryLines = new ArrayList<>();
		this.navalLines = new ArrayList<>();
		this.generalLines = new ArrayList<>();
		this.equipment = new ArrayList<>();
	}

	public void build(PropertyNode productionNode) {
		ObjectNode obj = (ObjectNode) productionNode.getValue();

		for (ASTNode node : obj.getChildren()) {
			PropertyNode propNode = (PropertyNode) node;

			switch (propNode.getKey()) {
				case "military_lines":
					readMilitaryLine((ObjectNode) propNode.getValue());
					break;

				case "naval_lines":
					readNavalLine((ObjectNode) propNode.getValue());
					break;

				case "general_lines":
					readGeneralLines((ObjectNode) propNode.getValue());
					break;

				case "equipments":
					readEquipments((ObjectNode) propNode.getValue());
					break;
			}
		}
	}

	public List<MilitaryLine> getMilitaryLines() {
		return militaryLines;
	}

	public List<Equipment> getEquipment() {
		return equipment;
	}

	public List<GeneralLine> getGeneralLines() {
		return generalLines;
	}

	public List<NavalLine> getNavalLines() {
		return navalLines;
	}

	private void readMilitaryLine(ObjectNode node) {
		MilitaryLine line = new MilitaryLine();
		line.build(node);
		militaryLines.add(line);
	}

	private void readNavalLine(ObjectNode node) {
		NavalLine line = new NavalLine();
		line.build(node);
		navalLines.add(line);
	}

	private void readGeneralLines(ObjectNode node) {
		for (ASTNode child : node.getChildren()) {
			readGeneralLine((ObjectNode) ((PropertyNode) child).getValue());
		}
	}

	private void readGeneralLine(ObjectNode node) {
		GeneralLine line = new GeneralLine();
		line.build(node);
		generalLines.add(line);
	}

	private void readEquipments(ObjectNode node) {
		for (ASTNode child : node.getChildren()) {
			readEquipment((ObjectNode) ((PropertyNode) child).getValue());
		}
	}

	private void readEquipment(ObjectNode node) {
		HashMap<String, Object> nodeChildren = Utils.getObjectChildren(node);

		int id = -1;
		int type = -1;
		double amount = -1;

		if (nodeChildren.containsKey("id")) {
			HashMap<String, Object> propChildren = Utils.getObjectChildren((ObjectNode) nodeChildren.get("id"));

			id = ((IntegerNode) propChildren.get("id")).getValue();
			type = ((IntegerNode) propChildren.get("type")).getValue();
		}

		if (nodeChildren.containsKey("amount")) {
			amount = ((DoubleNode) nodeChildren.get("amount")).getValue();
		}

		equipment.add(new Equipment(id, type, amount));
	}
}
