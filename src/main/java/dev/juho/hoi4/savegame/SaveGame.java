package dev.juho.hoi4.savegame;

import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.PropertyNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.StringNode;
import dev.juho.hoi4.savegame.country.Country;
import dev.juho.hoi4.utils.Logger;
import dev.juho.hoi4.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SaveGame {

	private String startDate;

	private List<ASTNode> nodes;
	private HashMap<CountryTag, Country> countries;

	public SaveGame(List<ASTNode> nodes) {
		this.nodes = nodes;
		this.countries = new HashMap<>();
	}

	public void build() {
		Logger.getInstance().time("building save game");
		for (ASTNode node : nodes) {
			switch (node.getType()) {
				case PROPERTY:
					readProperty((PropertyNode) node);
					break;
			}
		}
		this.nodes.clear();
		Logger.getInstance().timeEnd(Logger.INFO, "building save game");
	}

	public String getStartDate() {
		return startDate;
	}

	public HashMap<CountryTag, Country> getCountries() {
		return countries;
	}

	private void readProperty(PropertyNode propertyNode) {
		switch (propertyNode.getKey()) {
			case "start_date":
				startDate = ((StringNode) propertyNode.getValue()).getValue();
				break;

			case "countries":
				buildCountries((ObjectNode) propertyNode.getValue());
				break;
		}
	}

	private void buildCountries(ObjectNode countries) {
		for (ASTNode node : countries.getChildren()) {
			PropertyNode propNode = (PropertyNode) node;
			if (Utils.hasEnum(CountryTag.values(), propNode.getKey())) {
				CountryTag tag = CountryTag.valueOf(propNode.getKey());
				Country country = new Country(tag);
				country.build((ObjectNode) propNode.getValue());
				this.countries.put(tag, country);
			} else {
				Logger.getInstance().log(Logger.WARNING, "Couldn't find country tag " + propNode.getKey());
			}
		}
	}

}
