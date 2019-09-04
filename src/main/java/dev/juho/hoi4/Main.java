package dev.juho.hoi4;

import dev.juho.hoi4.parser.Parser;
import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.parser.textparser.TextParser;
import dev.juho.hoi4.savegame.SaveGame;
import dev.juho.hoi4.savegame.SaveGameUtils;
import dev.juho.hoi4.utils.ArgsParser;
import dev.juho.hoi4.utils.Logger;
import dev.juho.hoi4.utils.Utils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Main {

	public static String gameFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\Paradox Interactive\\Hearts of Iron IV";
	public static String gameName = "";

	public static void main(String[] args) {
		Logger.getInstance().LOG_LEVEL = Logger.INFO;

		ArgsParser argsParser = new ArgsParser(new String[]{"-game"});
		HashMap<String, String> argsMap = argsParser.parse(args);
		gameName = argsMap.get("-game");

		if (argsMap.containsKey("-folder")) {
			gameFolder = argsMap.get("-folder");
		} else {
			Logger.getInstance().log(Logger.INFO, "Using default HOI4 folder located here: " + gameFolder);
		}

		if (argsMap.containsKey("-debug")) {
			Logger.getInstance().LOG_LEVEL = Logger.DEBUG;
			Logger.getInstance().log(Logger.DEBUG, "DEBUG MODE ENABLED");
		}

		File file = new File(gameFolder + "\\save games\\" + gameName);
//		File realFile = new File("C:\\Users\\Juho\\Documents\\Paradox Interactive\\Hearts of Iron IV\\save games\\SOV_1949_07_19_14.hoi4");
		File testFile = new File("test.hoi4");
		Parser parser = new TextParser(file);
		try {
			parser.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}

		parser.getSaveGame().build();

		Logger.getInstance().log(Logger.INFO, "Average military factories in use: " + SaveGameUtils.getAverageMilitaryFactoriesInUse(parser.getSaveGame()));
		double[] avrgDataThing = SaveGameUtils.getAverageDivisionsAndManpowerCount(parser.getSaveGame());

		Logger.getInstance().log(Logger.INFO, "Average divisions: " + avrgDataThing[0] + ", total manpower: " + String.format("%.0f", avrgDataThing[1]));
		SaveGameUtils.printCountry(parser.getSaveGame(), CountryTag.GER);
	}

}
