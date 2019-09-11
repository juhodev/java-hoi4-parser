package dev.juho.hoi4.savegame;

import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.savegame.country.Country;
import dev.juho.hoi4.savegame.country.data.production.GeneralLine;
import dev.juho.hoi4.savegame.country.data.production.MilitaryLine;
import dev.juho.hoi4.savegame.country.data.production.NavalLine;
import dev.juho.hoi4.savegame.country.data.units.Division;
import dev.juho.hoi4.utils.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class SaveGameUtils {

	public static void saveToFile(SaveGame game, CountryTag tag) throws IOException {
		saveToFile(game, tag, tag + "-" + new Date().getTime() + "-save-game.json");
	}

	public static void saveToFile(SaveGame game, CountryTag tag, String fileName) throws IOException {
		Logger.getInstance().log(Logger.INFO, "Saving " + tag + " to " + fileName);
		Country country = game.getCountries().get(tag);

		PrintWriter writer = new PrintWriter(new FileWriter(new File(fileName)));
		writer.write(country.asJSON());
		writer.flush();
		writer.close();
	}

	public static void printCountry(SaveGame game, CountryTag tag) {
		Country country = game.getCountries().get(tag);

		Logger.getInstance().log(Logger.INFO, "Country info for " + tag);
		Logger.getInstance().log(Logger.INFO, "Politics: ");
		Logger.getInstance().log(Logger.INFO, "\tRuling political party: " + country.getPolitics().getRulingParty());
		Logger.getInstance().log(Logger.INFO, "\tLast election date: " + country.getPolitics().getLastElection());
		Logger.getInstance().log(Logger.INFO, "");
		Logger.getInstance().log(Logger.INFO, "Production: ");
		Logger.getInstance().log(Logger.INFO, "\tFactories used for buildings: " + country.getProduction().getFactoriesUedByGeneralLines());
		Logger.getInstance().log(Logger.INFO, "\tFactories used for military stuff: " + country.getProduction().getFactoriesUsedByMilitary());
		Logger.getInstance().log(Logger.INFO, "\tFactories used for navy stuff: " + country.getProduction().getFactoriesUsedByNavy());
		Logger.getInstance().log(Logger.INFO, "\tAll active factories: " + country.getProduction().getTotalUsedFactoryCount());
		Logger.getInstance().log(Logger.INFO, "");
		Logger.getInstance().log(Logger.INFO, "Divisions: ");
		Logger.getInstance().log(Logger.INFO, "\tDivision count: " + country.getUnits().getDivisions().size());
		Logger.getInstance().log(Logger.INFO, "\tDivision average strength: " + Math.round(country.getUnits().getAverageStrength()));
	}

	public static double getAverageMilitaryFactoriesInUse(SaveGame game) {
		Iterator it = game.getCountries().entrySet().iterator();

		int totalFactoriesInUse = 0;
		int totalCountries = 0;

		while (it.hasNext()) {
			Map.Entry<CountryTag, Country> pair = (Map.Entry) it.next();
			Country country = pair.getValue();

			int factoriesInUse = 0;

			for (MilitaryLine line : country.getProduction().getMilitaryLines()) {
				factoriesInUse += line.getActiveFactories();
			}

			totalFactoriesInUse += factoriesInUse;
			totalCountries++;
		}

		Logger.getInstance().log(Logger.INFO, "totalFactories: " + totalFactoriesInUse + ", totalCountries: " + totalCountries);
		return (double) totalFactoriesInUse / (double) totalCountries;
	}

	public static double[] getAverageDivisionsAndManpowerCount(SaveGame game) {
		Iterator it = game.getCountries().entrySet().iterator();

		int totalDivisions = 0;
		int totalManpower = 0;
		int totalCountries = 0;

		while (it.hasNext()) {
			Map.Entry<CountryTag, Country> pair = (Map.Entry<CountryTag, Country>) it.next();
			Country country = pair.getValue();

			int divisionCount = 0;
			int manpowerCount = 0;

			for (Division div : country.getUnits().getDivisions()) {
				divisionCount++;
				manpowerCount += div.getManpower();
			}

			totalCountries++;
			totalDivisions += divisionCount;
			totalManpower += manpowerCount;
		}

		return new double[]{(double) totalDivisions / (double) totalCountries, totalManpower};
	}

}
