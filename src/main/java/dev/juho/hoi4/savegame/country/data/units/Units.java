package dev.juho.hoi4.savegame.country.data.units;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ListNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.PropertyNode;
import dev.juho.hoi4.savegame.country.data.HOIData;

import java.util.ArrayList;
import java.util.List;

public class Units implements HOIData {

	private List<Division> divisions;

	public Units() {
		this.divisions = new ArrayList<>();
	}

	public void build(ObjectNode node) {
		ASTNode divisionNode = (ASTNode) node.get("division");

		if (divisionNode instanceof ObjectNode) {
			readDivision((ObjectNode) divisionNode);
		} else if (divisionNode instanceof ListNode) {
			ListNode listNode = (ListNode) divisionNode;

			for (ASTNode child : listNode.getChildren()) {
				readDivision((ObjectNode) child);
			}
		}
	}

	public double getAverageStrength() {
		double totalStrength = 0;

		for (Division division : divisions) {
			totalStrength += division.getStrength();
		}

		return totalStrength / divisions.size();
	}

	public double getAverageOrganisation() {
		double totalOrganisation = 0;

		for (Division division : divisions) {
			totalOrganisation += division.getOrganisation();
		}

		return totalOrganisation / divisions.size();
	}

	public List<Division> getDivisions() {
		return divisions;
	}

	private void readDivision(ObjectNode node) {
		Division div = new Division();
		div.build(node);
		divisions.add(div);
	}

	@Override
	public String asJSON() {
		StringBuilder builder = new StringBuilder();

		builder.append("{\"_type\": \"units\", \"divisions\": [ ");

		for (Division division : divisions) {
			builder.append(division.asJSON()).append(",");
		}

		builder.delete(builder.length() - 1, builder.length());
		builder.append("]}");

		return builder.toString();
	}
}
