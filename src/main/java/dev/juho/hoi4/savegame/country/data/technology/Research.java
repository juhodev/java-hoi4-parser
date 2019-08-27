package dev.juho.hoi4.savegame.country.data.technology;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.DoubleNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.IntegerNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.PropertyNode;
import dev.juho.hoi4.utils.Logger;
import dev.juho.hoi4.utils.Utils;

import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Research {

	private List<Technology> technology;
	private List<ResearchSlot> slots;

	public Research() {
		this.technology = new ArrayList<>();
		this.slots = new ArrayList<>();
	}

	public void build(PropertyNode technologyNode) {
		ObjectNode techValues = (ObjectNode) technologyNode.getValue();

		PropertyNode technologies = (PropertyNode) techValues.getChildren().get(0);
		PropertyNode slots = (PropertyNode) techValues.getChildren().get(1);

		readTechnologies(((ObjectNode) technologies.getValue()).getChildren());
		readResearchSlots(((ObjectNode) slots.getValue()).getChildren());
	}

	private void readTechnologies(List<ASTNode> techChildren) {
		for (ASTNode child : techChildren) {
			PropertyNode childProp = (PropertyNode) child;

			String techName = childProp.getKey();

			HashMap<String, Object> children = Utils.getObjectChildren((ObjectNode) childProp.getValue());

			int level = -1;
			if (children.containsKey("level")) level = ((IntegerNode) children.get("level")).getValue();
			double researchPoints = -1;
			if (children.containsKey("research_points"))
				researchPoints = ((DoubleNode) children.get("research_points")).getValue();

			Logger.getInstance().log(Logger.DEBUG, "adding research: " + techName + ", level: " + level + ", research points: " + researchPoints);
			technology.add(new Technology(techName, level, researchPoints));
		}
	}

	private void readResearchSlots(List<ASTNode> slotsChildren) {
		for (ASTNode child : slotsChildren) {
			PropertyNode childProp = (PropertyNode) child;

			String current = childProp.getKey();
//			TODO: You might want to clean this up
			ObjectNode objNode = (ObjectNode) childProp.getValue();
			double points = -1;
			if (objNode.getChildren().size() > 0) {
				PropertyNode pointsProp = (PropertyNode) objNode.getChildren().get(0);
				DoubleNode doubleNode = (DoubleNode) pointsProp.getValue();
				points = doubleNode.getValue();
			}


			ResearchSlot slot = new ResearchSlot(current, points);
			Logger.getInstance().log(Logger.DEBUG, "adding research slot: " + slot.getCurrent() + ", points: " + slot.getPoints());
			slots.add(slot);
		}
	}

}
