package dev.juho.hoi4.savegame.country;

import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;
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
		for (ASTNode node : countryNode.getChildren()) {
			PropertyNode propNode = (PropertyNode) node;
			switch (propNode.getKey()) {
				case "technology":
					research.build(propNode);
					break;

				case "resources":
					resources.build(propNode);
					break;

				case "production":
					production.build(propNode);
					break;

				case "corps_commander":
				case "field_marshal":
				case "navy_leader":
					readPerson(propNode);
					break;

				case "units":
					units.build(propNode);
					break;
			}
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

	private void readPerson(PropertyNode node) {
		ImportantPerson person = new ImportantPerson();
		person.build(node);
		importantPeople.add(person);
	}

}
