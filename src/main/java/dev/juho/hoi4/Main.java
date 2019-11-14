package dev.juho.hoi4;

import dev.juho.hoi4.parser.Parser;
import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.parser.textparser.TextParser;
import dev.juho.hoi4.parser.textparser.gamefile.GFNode;
import dev.juho.hoi4.utils.ArgsParser;
import dev.juho.hoi4.utils.Logger;
import dev.juho.hoi4.utils.Utils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static String gameFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/Paradox Interactive/Hearts of Iron IV";
	public static String gameName = "";

	public static void main(String[] args) {
		Logger.LOG_LEVEL = Logger.INFO;

		ArgsParser.getInstance().add(ArgsParser.Argument.GAME, ArgsParser.Type.STRING, true, "-game");

		ArgsParser.getInstance().add(ArgsParser.Argument.COUNTRY, ArgsParser.Type.LIST, false, "-country");
		ArgsParser.getInstance().add(ArgsParser.Argument.FOLDER, ArgsParser.Type.STRING, false, "-folder", "-hoi4_folder");
		ArgsParser.getInstance().add(ArgsParser.Argument.JSON, ArgsParser.Type.NONE, false, "-json");
		ArgsParser.getInstance().add(ArgsParser.Argument.JSON_LIMIT, ArgsParser.Type.STRING, false, "-json_limit");
		ArgsParser.getInstance().add(ArgsParser.Argument.SAVE_GAME, ArgsParser.Type.STRING, false, "-save_game");
		ArgsParser.getInstance().add(ArgsParser.Argument.MAP, ArgsParser.Type.NONE, false, "-map");
		ArgsParser.getInstance().add(ArgsParser.Argument.HELP, ArgsParser.Type.NONE, false, "-help");
		ArgsParser.getInstance().add(ArgsParser.Argument.DEBUG, ArgsParser.Type.NONE, false, "-debug");

		ArgsParser.getInstance().parse(args);

		gameName = ArgsParser.getInstance().getString(ArgsParser.Argument.GAME);

		if (ArgsParser.getInstance().has(ArgsParser.Argument.HELP)) {
			Logger.getInstance().log(Logger.INFO, "Usage: java -jar HOI4.jar [options...]");
			Logger.getInstance().log(Logger.INFO, "\t-country <TAG> ...    Analyze countries specified by <TAG>");
			Logger.getInstance().log(Logger.INFO, "\t-debug                Show debug logs");
			Logger.getInstance().log(Logger.INFO, "\t-game <name>          File name or path to file .hoi4 file");
			Logger.getInstance().log(Logger.INFO, "\t-hoi4_folder <path>   Path to HOI4 folder");
			Logger.getInstance().log(Logger.INFO, "\t-json                 Saves the .hoi4 as .json file");
			Logger.getInstance().log(Logger.INFO, "\t-json_limit <limit>   When saving as JSON ignores objects larger than <limit> values");
			Logger.getInstance().log(Logger.INFO, "\t-map                  Creates a map with all -map_<option> options");
			Logger.getInstance().log(Logger.INFO, "\t-map_highlight        Highlight countries specified with -country");
			Logger.getInstance().log(Logger.INFO, "");
			Logger.getInstance().log(Logger.INFO, "Examples and more option info: https://github.com/juhodev/java-hoi4-parser");
			System.exit(0);
		}

		if (ArgsParser.getInstance().has(ArgsParser.Argument.COUNTRY)) {
			List<String> countries = ArgsParser.getInstance().getList(ArgsParser.Argument.COUNTRY);
			List<CountryTag> countryTags = new ArrayList<>();

			for (String country : countries) {
				if (Utils.hasEnum(CountryTag.values(), country)) {
					countryTags.add(CountryTag.valueOf(country));
				} else {
					Logger.getInstance().log(Logger.ERROR, "Couldn't find a country " + country);
				}
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
		Parser parser = new TextParser(file);
		try {
			parser.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<GFNode> astNodes = parser.getNodes();

		if (ArgsParser.getInstance().has(ArgsParser.Argument.JSON)) {
			JSONPrinter jsonPrinter = new JSONPrinter(astNodes);
			jsonPrinter.print();
		}
	}

}
