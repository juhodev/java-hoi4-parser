package dev.juho.hoi4;

import dev.juho.hoi4.parser.Parser;
import dev.juho.hoi4.parser.textparser.TextParser;
import dev.juho.hoi4.savegame.SaveGameUtils;
import dev.juho.hoi4.utils.Logger;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		Logger.getInstance().LOG_LEVEL = Logger.INFO;

		File realFile = new File("C:\\Users\\Juho\\Documents\\Paradox Interactive\\Hearts of Iron IV\\save games\\SOV_1949_07_19_14.hoi4");
		File testFile = new File("test.hoi4");
		Parser parser = new TextParser(realFile);
		try {
			parser.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}

		parser.getSaveGame().build();

		Logger.getInstance().log(Logger.INFO, "Average factories in use: " + SaveGameUtils.getAverageMilitaryFactoriesInUse(parser.getSaveGame()));
		double[] avrgDataThing = SaveGameUtils.getAverageDivisionsAndManpowerCount(parser.getSaveGame());

		Logger.getInstance().log(Logger.INFO, "Average divisions: " + avrgDataThing[0] + ", total manpower: " + String.format("%.0f", avrgDataThing[1]));
	}

}
