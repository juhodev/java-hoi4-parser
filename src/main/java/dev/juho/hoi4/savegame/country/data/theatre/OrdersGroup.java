package dev.juho.hoi4.savegame.country.data.theatre;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.IntegerNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ListNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.StringNode;

import java.util.ArrayList;
import java.util.List;

public class OrdersGroup {

	private int id, type;
	private String name;
	private List<Member> members;

	public OrdersGroup() {
		this.id = -1;
		this.type = -1;
		this.name = "";
		this.members = new ArrayList<>();
	}

	public void build(ObjectNode ordersGroupObject) {
		ObjectNode idNode = (ObjectNode) ordersGroupObject.get("id");
		IntegerNode id = (IntegerNode) idNode.get("id");
		this.id = id.getValue();

		IntegerNode type = (IntegerNode) idNode.get("type");
		this.type = type.getValue();

		StringNode name = (StringNode) ordersGroupObject.get("name");
		this.name = name.getValue();

		if (ordersGroupObject.has("member")) {
			ASTNode node = (ASTNode) ordersGroupObject.get("member");

			if (node instanceof ObjectNode) {
				readMember((ObjectNode) node);
			} else {
				ListNode listNode = (ListNode) node;

				for (ASTNode child : listNode.getChildren()) {
					readMember((ObjectNode) child);
				}
			}
		}
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public List<Member> getMembers() {
		return members;
	}

	private void readMember(ObjectNode memberNode) {
		ObjectNode unitNode = (ObjectNode) memberNode.get("unit");

		IntegerNode id = (IntegerNode) unitNode.get("id");
		IntegerNode type = (IntegerNode) unitNode.get("type");
		members.add(new Member(id.getValue(), type.getValue()));
	}

	private class Member {
		private int unitId, unitType;

		public Member(int unitId, int unitType) {
			this.unitId = unitId;
			this.unitType = unitType;
		}

		public int getUnitId() {
			return unitId;
		}

		public int getUnitType() {
			return unitType;
		}
	}

}
