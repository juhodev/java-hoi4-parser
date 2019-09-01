package dev.juho.hoi4.savegame.country.data.technology;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.utils.Logger;
import dev.juho.hoi4.utils.Utils;

import java.io.ObjectOutput;
import java.util.*;

public class Research {

	private List<Technology> technology;
	private List<ResearchSlot> slots;

	public Research() {
		this.technology = new ArrayList<>();
		this.slots = new ArrayList<>();
	}

	public void build(ObjectNode technologyNode) {
		ObjectNode technologies = (ObjectNode) technologyNode.get("technologies");

		if (technologies.has("technologies"))
			readTechnologies((ObjectNode) technologies.get("technologies"));
		if (technologies.has("slots"))
			readResearchSlots((ObjectNode) technologies.get("slots"));
	}

	private void readTechnologies(ObjectNode technologiesNode) {
		Iterator it = technologiesNode.getChildren().entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();

			if (pair.getValue() instanceof ObjectNode) {
				ObjectNode childNode = (ObjectNode) pair.getValue();
				readTechObject(pair.getKey(), childNode);
			} else if (pair.getValue() instanceof ListNode) {
				ListNode listNode = (ListNode) pair.getValue();
				for (ASTNode node : listNode.getChildren()) {
					ObjectNode childNode = (ObjectNode) node;
					readTechObject(pair.getKey(), childNode);
				}
			}
		}
	}

	private void readResearchSlots(ObjectNode slots) {
		Iterator it = slots.getChildren().entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();

			if (pair.getValue() instanceof ObjectNode) {
				readSlotObject(pair.getKey(), (ObjectNode) pair.getValue());
			} else if (pair.getValue() instanceof ListNode) {
				ListNode listNode = (ListNode) pair.getValue();

				for (ASTNode childNode : listNode.getChildren()) {
					readSlotObject(pair.getKey(), (ObjectNode) childNode);
				}
			}
		}
	}

	private void readSlotObject(String slot, ObjectNode objectNode) {
		DoubleNode points = (DoubleNode) objectNode.get("points");
		slots.add(new ResearchSlot(slot, points.getValue()));
	}

	private void readTechObject(String techName, ObjectNode objectNode) {
		IntegerNode level = (IntegerNode) objectNode.get("level");
		DoubleNode researchPoints = (DoubleNode) objectNode.get("research_points");

		technology.add(new Technology(techName, level.getValue(), researchPoints.getValue()));
	}

}
