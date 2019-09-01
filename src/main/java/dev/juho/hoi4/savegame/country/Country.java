package dev.juho.hoi4.savegame.country;

import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ListNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.PropertyNode;
import dev.juho.hoi4.savegame.country.data.people.ImportantPerson;
import dev.juho.hoi4.savegame.country.data.production.Production;
import dev.juho.hoi4.savegame.country.data.resources.Resources;
import dev.juho.hoi4.savegame.country.data.technology.Research;
import dev.juho.hoi4.savegame.country.data.units.Units;

import java.util.ArrayList;
import java.util.List;

public class Country {

	private CountryTag tag;

	private Research research;
	private Resources resources;
	private Production production;
	private Units units;
	private List<ImportantPerson> importantPeople;

	public Country(CountryTag tag) {
		this.tag = tag;
		this.research = new Research();
		this.resources = new Resources();
		this.production = new Production();
		this.units = new Units();
		this.importantPeople = new ArrayList<>();
	}

	public CountryTag getTag() {
		return tag;
	}

	public void build(ObjectNode countryNode) {
		ObjectNode researchNode = (ObjectNode) countryNode.get("technology");
		research.build(researchNode);

		ObjectNode resourceNode = (ObjectNode) countryNode.get("resources");
		resources.build(resourceNode);

		ObjectNode productionNode = (ObjectNode) countryNode.get("production");
		production.build(productionNode);

		ASTNode corpsCommander = (ASTNode) countryNode.get("corps_commander");
		if (corpsCommander instanceof ObjectNode) {
			readPerson(ImportantPerson.Type.CORPS_COMMANDER, (ObjectNode) corpsCommander);
		} else if (corpsCommander instanceof ListNode) {
			ListNode listNode = (ListNode) corpsCommander;

			for (ASTNode child : listNode.getChildren()) {
				readPerson(ImportantPerson.Type.CORPS_COMMANDER, (ObjectNode) child);
			}
		}

		ASTNode fieldMarshal = (ASTNode) countryNode.get("field_marshal");
		if (fieldMarshal instanceof ObjectNode) {
			readPerson(ImportantPerson.Type.FIELD_MARSHAL, (ObjectNode) fieldMarshal);
		} else if (fieldMarshal instanceof ListNode) {
			ListNode listNode = (ListNode) fieldMarshal;

			for (ASTNode child : listNode.getChildren()) {
				readPerson(ImportantPerson.Type.FIELD_MARSHAL, (ObjectNode) child);
			}
		}

		ASTNode navyLeader = (ASTNode) countryNode.get("navy_leader");
		if (navyLeader instanceof ObjectNode) {
			readPerson(ImportantPerson.Type.NAVY_LEADER, (ObjectNode) navyLeader);
		} else if (navyLeader instanceof ListNode) {
			ListNode listNode = (ListNode) navyLeader;

			for (ASTNode child : listNode.getChildren()) {
				readPerson(ImportantPerson.Type.FIELD_MARSHAL, (ObjectNode) child);
			}
		}

		if (countryNode.has("units")) {
			ObjectNode unitsNode = (ObjectNode) countryNode.get("units");
			units.build(unitsNode);
		}
	}

	public List<ImportantPerson> getImportantPeople() {
		return importantPeople;
	}

	public Units getUnits() {
		return units;
	}

	public Production getProduction() {
		return production;
	}

	public Research getResearch() {
		return research;
	}

	public Resources getResources() {
		return resources;
	}

	private void readPerson(ImportantPerson.Type type, ObjectNode node) {
		ImportantPerson person = new ImportantPerson();
		person.build(type, node);
		importantPeople.add(person);
	}

}
