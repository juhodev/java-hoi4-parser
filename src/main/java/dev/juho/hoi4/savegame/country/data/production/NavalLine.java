package dev.juho.hoi4.savegame.country.data.production;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.IntegerNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ListNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.PropertyNode;
import dev.juho.hoi4.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NavalLine extends ProductionLine {

	private int deploymentBase;
	private List<Name> names;

	public NavalLine() {
		this.deploymentBase = -1;
		this.names = new ArrayList();
	}

	@Override
	public void build(ObjectNode obj) {
//		for (ASTNode node : obj.getChildren()) {
//			PropertyNode propNode = (PropertyNode) node;
//
//			switch (propNode.getKey()) {
//				case "deployment":
////					readDeployment((ObjectNode) propNode.getValue());
//					break;
//
//				case "names":
//
//				default:
//					break;
//			}
//		}

		super.build(obj);
	}

	private void readDeployment(ObjectNode node) {
		if (node.getChildren().size() > 0) {
//			TODO: fix ObjectNode cannot be cast to IntegerNode
			IntegerNode intNode = (IntegerNode) ((PropertyNode) node.getChildren().get(0)).getValue();
			deploymentBase = intNode.getValue();
		}
	}

	private void readNames(ListNode node) {
		for (ASTNode child : node.getChildren()) {
			ObjectNode childObj = (ObjectNode) child;

//			TODO: finish making this I can't do this right now
		}
	}

	private class Name {

		int type;
		int equipmentId;
		int equipmentType;

		public Name(int type, int equipmentId, int equipmentType) {
			this.type = type;
			this.equipmentId = equipmentId;
			this.equipmentType = equipmentType;
		}

		public int getType() {
			return type;
		}

		public int getEquipmentId() {
			return equipmentId;
		}

		public int getEquipmentType() {
			return equipmentType;
		}
	}

}
