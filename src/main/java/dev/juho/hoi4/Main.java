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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

	public static String gameFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/Paradox Interactive/Hearts of Iron IV";
	public static String gameName = "";

	public static void main(String[] args) {
		Logger.LOG_LEVEL = Logger.INFO;

		ArgsParser.getInstance().add(ArgsParser.Argument.GAME, ArgsParser.Type.STRING, true, "-game");
		ArgsParser.getInstance().add(ArgsParser.Argument.COUNTRY, ArgsParser.Type.LIST, true, "-country");

		ArgsParser.getInstance().add(ArgsParser.Argument.FOLDER, ArgsParser.Type.STRING, false, "-folder", "-hoi4_folder");
		ArgsParser.getInstance().add(ArgsParser.Argument.OUTFILE, ArgsParser.Type.STRING, false, "-outFile");
		ArgsParser.getInstance().add(ArgsParser.Argument.OUT, ArgsParser.Type.NONE, false, "-out");
		ArgsParser.getInstance().add(ArgsParser.Argument.MAP, ArgsParser.Type.NONE, false, "-map");
		ArgsParser.getInstance().add(ArgsParser.Argument.DEBUG, ArgsParser.Type.NONE, false, "-debug");

		ArgsParser.getInstance().parse(args);

		gameName = ArgsParser.getInstance().getString(ArgsParser.Argument.GAME);

		List<String> countries = ArgsParser.getInstance().getList(ArgsParser.Argument.COUNTRY);
		List<CountryTag> countryTags = new ArrayList<>();

		for (String country : countries) {
			if (Utils.hasEnum(CountryTag.values(), country)) {
				countryTags.add(CountryTag.valueOf(country));
			} else {
				Logger.getInstance().log(Logger.ERROR, "Couldn't find a country " + country);
			}
		}

		if (ArgsParser.getInstance().has(ArgsParser.Argument.FOLDER)) {
			gameFolder = ArgsParser.getInstance().getString(ArgsParser.Argument.FOLDER);
		} else {
			Logger.getInstance().log(Logger.INFO, "Using default HOI4 folder located here: " + gameFolder);
		}

		if (ArgsParser.getInstance().has(ArgsParser.Argument.DEBUG)) {
			Logger.LOG_LEVEL = Logger.DEBUG;
			Logger.getInstance().log(Logger.DEBUG, "DEBUG MODE ENABLED");
		}

		File file = new File(gameFolder + "/save games/" + gameName);
//		File realFile = new File("C:\\Users\\Juho\\Documents\\Paradox Interactive\\Hearts of Iron IV\\save games\\SOV_1949_07_19_14.hoi4");
		File testFile = new File("test.hoi4");
		Parser parser = new TextParser(file);
		try {
			parser.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}

		parser.getSaveGame().build();

		if (ArgsParser.getInstance().has(ArgsParser.Argument.OUT)) {
			if (!ArgsParser.getInstance().has(ArgsParser.Argument.OUTFILE)) {
				try {
					SaveGameUtils.saveToFile(parser.getSaveGame(), countryTags);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					SaveGameUtils.saveToFile(parser.getSaveGame(), countryTags, ArgsParser.getInstance().getString(ArgsParser.Argument.OUTFILE));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		Logger.getInstance().log(Logger.INFO, "Average military factories in use: " + SaveGameUtils.getAverageMilitaryFactoriesInUse(parser.getSaveGame()));
		double[] avrgDataThing = SaveGameUtils.getAverageDivisionsAndManpowerCount(parser.getSaveGame());

		Logger.getInstance().log(Logger.INFO, "Average divisions: " + avrgDataThing[0] + ", total manpower: " + String.format("%.0f", avrgDataThing[1]));
		SaveGameUtils.printCountry(parser.getSaveGame(), CountryTag.GER);
	}

}
