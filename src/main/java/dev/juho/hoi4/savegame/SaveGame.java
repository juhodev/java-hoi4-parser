package dev.juho.hoi4.savegame;

import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.PropertyNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.StringNode;
import dev.juho.hoi4.savegame.country.Country;
import dev.juho.hoi4.savegame.country.data.combat.CombatDataEntry;
import dev.juho.hoi4.savegame.country.data.combat.CombatHistory;
import dev.juho.hoi4.savegame.map.HOIMap;
import dev.juho.hoi4.savegame.map.States;
import dev.juho.hoi4.utils.ArgsParser;
import dev.juho.hoi4.utils.Logger;
import dev.juho.hoi4.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class SaveGame {

	private String startDate;

	private List<ASTNode> nodes;
	private HashMap<CountryTag, Country> countries;
	private CombatHistory combatHistory;
	private States states;

	public SaveGame(List<ASTNode> nodes) {
		this.nodes = nodes;
		this.countries = new HashMap<>();
		this.combatHistory = new CombatHistory();
		this.states = new States();
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

		if (ArgsParser.getInstance().has(ArgsParser.Argument.MAP)) {
			writeImage();
		}
	}

	public String getStartDate() {
		return startDate;
	}

	public HashMap<CountryTag, Country> getCountries() {
		return countries;
	}

	public CombatHistory getCombatHistory() {
		return combatHistory;
	}

	private void readProperty(PropertyNode propertyNode) {
		switch (propertyNode.getKey()) {
			case "start_date":
				startDate = ((StringNode) propertyNode.getValue()).getValue();
				break;

			case "countries":
				buildCountries((ObjectNode) propertyNode.getValue());
				break;

			case "combat_data_entry":
				buildCombatDataEntry((ObjectNode) propertyNode.getValue());
				break;

			case "states":
				buildStates((ObjectNode) propertyNode.getValue());
				break;
		}
	}

	private void buildCountries(ObjectNode countries) {
		Iterator it = countries.getChildren().entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();

			if (Utils.hasEnum(CountryTag.values(), pair.getKey())) {
				CountryTag tag = CountryTag.valueOf(pair.getKey());
				Country country = new Country(tag);
				country.build((ObjectNode) pair.getValue());
				this.countries.put(tag, country);
			} else {
				Logger.getInstance().log(Logger.WARNING, "Couldn't find country tag " + pair.getKey());
			}
		}
	}

	private void writeImage() {
		HOIMap map = new HOIMap();
		map.init();
		Image mapImage = map.createMap(states.getStateList());
		try {
			ImageIO.write((RenderedImage) mapImage, "PNG", new File("map.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void buildCombatDataEntry(ObjectNode combatDataEntry) {
		CombatDataEntry entry = new CombatDataEntry();
		entry.build(combatDataEntry);
		combatHistory.add(entry);
	}

	private void buildStates(ObjectNode statesNode) {
		states.build(statesNode);
	}

}
