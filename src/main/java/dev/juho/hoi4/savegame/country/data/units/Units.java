package dev.juho.hoi4.savegame.country.data.units;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.PropertyNode;

import java.util.ArrayList;
import java.util.List;

public class Units {

	private List<Division> divisions;

	public Units() {
		this.divisions = new ArrayList<>();
	}

	public void build(PropertyNode unitsNode) {
		ObjectNode obj = (ObjectNode) unitsNode.getValue();

		for (ASTNode child : obj.getChildren()) {
			PropertyNode propNode = (PropertyNode) child;

			switch (propNode.getKey()) {
				case "division":
					readDivision((ObjectNode) propNode.getValue());
					break;
			}
		}
	}

	public List<Division> getDivisions() {
		return divisions;
	}

	private void readDivision(ObjectNode node) {
		Division div = new Division();
		div.build(node);
		divisions.add(div);
	}

}
