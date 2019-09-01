package dev.juho.hoi4.savegame.country.data.production;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.savegame.country.data.misc.Equipment;
import dev.juho.hoi4.utils.Logger;
import dev.juho.hoi4.utils.Utils;

import java.util.*;

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

	public void build(ObjectNode productionNode) {
		Iterator it = productionNode.getChildren().entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();
			if (pair.getValue() instanceof ObjectNode || pair.getValue() instanceof ListNode) {
				switch (pair.getKey()) {
					case "military_lines":
						readMilitaryLine((ASTNode) pair.getValue());
						break;

					case "naval_lines":
						readNavalLine((ASTNode) pair.getValue());
						break;

					case "general_lines":
						readGeneralLines((ObjectNode) pair.getValue());
						break;

					case "equipments":
						readEquipments((ObjectNode) pair.getValue());
						break;
				}
			}
		}
	}

	/**
	 * @return factory count used military (used for producing tanks and stuff)
	 */
	public int getFactoriesUsedByMilitary() {
		int count = 0;
		for (MilitaryLine line : militaryLines) {
			count += line.getActiveFactories();
		}

		return count;
	}

	/**
	 * @return factory count used by the navy (used for producing ships)
	 */
	public int getFactoriesUsedByNavy() {
		int count = 0;
		for (NavalLine line : navalLines) {
			count += line.getActiveFactories();
		}

		return count;
	}

	/**
	 * @return factory count used for building/repairing buildings
	 */
	public int getFactoriesUedByGeneralLines() {
		int count = 0;
		for (GeneralLine line : generalLines) {
			count += line.getActiveFactories();
		}

		return count;
	}

	/**
	 * @return full active factory count
	 */
	public int getTotalUsedFactoryCount() {
		return getFactoriesUsedByMilitary() + getFactoriesUsedByNavy() + getFactoriesUedByGeneralLines();
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

	private void readMilitaryLine(ASTNode node) {
		MilitaryLine line = new MilitaryLine();

		if (node instanceof ObjectNode) {
			ObjectNode objNode = (ObjectNode) node;
			line.build(objNode);
			militaryLines.add(line);
		} else if (node instanceof ListNode) {
			ListNode listNode = (ListNode) node;

			for (ASTNode childNode : listNode.getChildren()) {
				ObjectNode objNode = (ObjectNode) childNode;
				line.build(objNode);
				militaryLines.add(line);
			}
		}
	}

	private void readNavalLine(ASTNode node) {
		NavalLine line = new NavalLine();

		if (node instanceof ObjectNode) {
			ObjectNode objNode = (ObjectNode) node;
			line.build(objNode);
			navalLines.add(line);
		} else if (node instanceof ListNode) {
			ListNode listNode = (ListNode) node;

			for (ASTNode childNode : listNode.getChildren()) {
				ObjectNode objNode = (ObjectNode) childNode;
				line.build(objNode);
				navalLines.add(line);
			}
		}
	}

	private void readGeneralLines(ObjectNode node) {
		ASTNode astNode = (ASTNode) node.get("line");

		if (astNode instanceof ObjectNode) {
			readGeneralLine((ObjectNode) astNode);
		} else if (astNode instanceof ListNode) {
			ListNode lineList = (ListNode) astNode;
			for (ASTNode child : lineList.getChildren()) {
				readGeneralLine((ObjectNode) child);
			}
		}
	}

	private void readGeneralLine(ObjectNode node) {
		GeneralLine line = new GeneralLine();
		line.build(node);
		generalLines.add(line);
	}

	private void readEquipments(ObjectNode node) {
		ASTNode childNode = (ASTNode) node.get("equipments");

		if (childNode instanceof ObjectNode) {
			ObjectNode objNode = (ObjectNode) childNode;
			readEquipment(objNode);
		} else if (childNode instanceof ListNode) {
			ListNode listNode = (ListNode) childNode;

			for (ASTNode listChild : listNode.getChildren()) {
				ObjectNode objNode = (ObjectNode) listChild;
				readEquipment(objNode);
			}
		}
	}

	private void readEquipment(ObjectNode node) {
		ObjectNode idNode = (ObjectNode) node.get("id");

		IntegerNode id = (IntegerNode) idNode.get("id");
		IntegerNode type = (IntegerNode) idNode.get("type");
		DoubleNode amount = (DoubleNode) node.get("amount");

		equipment.add(new Equipment(id.getValue(), type.getValue(), amount.getValue()));
	}
}
