package dev.juho.hoi4.savegame.country.data.theatre;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.IntegerNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ListNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public class Theatre {

	private int id, type;
	private int[] area;
	private List<OrdersGroup> ordersGroups;

	public Theatre() {
		this.id = -1;
		this.type = -1;
		this.area = new int[0];
		this.ordersGroups = new ArrayList<>();
	}

	public void build(ObjectNode theatreObject) {
		ObjectNode idNode = (ObjectNode) theatreObject.get("id");
		IntegerNode id = (IntegerNode) idNode.get("id");
		this.id = id.getValue();

		IntegerNode type = (IntegerNode) idNode.get("type");
		this.type = type.getValue();

		readArea((ListNode) theatreObject.get("area"));
		readOrdersGroup((ASTNode) theatreObject.get("orders_group"));
	}

	public int getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public int[] getArea() {
		return area;
	}

	public List<OrdersGroup> getOrdersGroups() {
		return ordersGroups;
	}

	private void readArea(ListNode areaList) {
		int[] newArea = new int[areaList.getChildren().size()];

		for (int i = 0; i < areaList.getChildren().size(); i++) {
			IntegerNode intNode = (IntegerNode) areaList.getChildren().get(i);
			newArea[i] = intNode.getValue();
		}

		this.area = newArea;
	}

	private void readOrdersGroup(ASTNode ordersGroups) {
		if (ordersGroups instanceof ObjectNode) {
			OrdersGroup group = new OrdersGroup();
			group.build((ObjectNode) ordersGroups);
			this.ordersGroups.add(group);
		} else if (ordersGroups instanceof ListNode) {
			ListNode listNode = (ListNode) ordersGroups;

			for (ASTNode child : listNode.getChildren()) {
				OrdersGroup group = new OrdersGroup();
				group.build((ObjectNode) child);
				this.ordersGroups.add(group);
			}
		}
	}

}
