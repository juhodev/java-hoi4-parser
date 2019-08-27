package dev.juho.hoi4.savegame;

import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.savegame.country.Country;
import dev.juho.hoi4.savegame.country.data.production.GeneralLine;
import dev.juho.hoi4.savegame.country.data.production.MilitaryLine;
import dev.juho.hoi4.savegame.country.data.production.NavalLine;
import dev.juho.hoi4.savegame.country.data.units.Division;
import dev.juho.hoi4.utils.Logger;

import java.util.Iterator;
import java.util.Map;

public class SaveGameUtils {

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
