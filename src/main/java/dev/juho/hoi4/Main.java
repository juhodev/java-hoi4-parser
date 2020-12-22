package dev.juho.hoi4;

import com.sun.org.apache.bcel.internal.generic.Type;
import dev.juho.hoi4.parser.Parser;
import dev.juho.hoi4.parser.textparser.TextParser;
import dev.juho.hoi4.profiler.Profiler;
import dev.juho.hoi4.utils.ArgsParser;
import dev.juho.hoi4.utils.Logger;
import dev.juho.hoi4.utils.Utils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Main {

	public static String gameFolder = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "/Paradox Interactive/Hearts of Iron IV";
	public static String gameName = "";

	public static void main(String[] args) {
		Logger.LOG_LEVEL = Logger.INFO;

		ArgsParser.getInstance().add(ArgsParser.Argument.FILE, ArgsParser.Type.STRING, true, "-file");

		ArgsParser.getInstance().add(ArgsParser.Argument.FOLDER, ArgsParser.Type.STRING, false, "-folder", "-hoi4_folder");
		ArgsParser.getInstance().add(ArgsParser.Argument.JSON, ArgsParser.Type.NONE, false, "-json");
		ArgsParser.getInstance().add(ArgsParser.Argument.JSON_LIMIT, ArgsParser.Type.STRING, false, "-json_limit");
		ArgsParser.getInstance().add(ArgsParser.Argument.HELP, ArgsParser.Type.NONE, false, "-help");
		ArgsParser.getInstance().add(ArgsParser.Argument.DEBUG, ArgsParser.Type.NONE, false, "-debug");
		ArgsParser.getInstance().add(ArgsParser.Argument.OUT_FILE, ArgsParser.Type.STRING, false, "-out", "-out_file", "-outfile");
		ArgsParser.getInstance().add(ArgsParser.Argument.ONLY_PRINT_OUT_FILE, ArgsParser.Type.NONE, false, "-only_out_file", "-ouf");

		ArgsParser.getInstance().parse(args);

		gameName = ArgsParser.getInstance().getString(ArgsParser.Argument.FILE);

		if (ArgsParser.getInstance().has(ArgsParser.Argument.HELP)) {
			Logger.getInstance().log(Logger.INFO, "Usage: java -jar HOI4.jar [options...]");
			Logger.getInstance().log(Logger.INFO, "\t-debug                Show debug logs");
			Logger.getInstance().log(Logger.INFO, "\t-file <path>          File name or path to a .hoi4 (or .txt) file");
			Logger.getInstance().log(Logger.INFO, "\t-hoi4_folder <path>   Path to HOI4 folder");
			Logger.getInstance().log(Logger.INFO, "\t-json                 Saves the .hoi4 as .json file");
			Logger.getInstance().log(Logger.INFO, "\t-json_limit <limit>   When saving as JSON ignores objects larger than <limit> values");
			Logger.getInstance().log(Logger.INFO, "\t-out                  File name for the output file (when -json is used)");
			Logger.getInstance().log(Logger.INFO, "\t-only_out_file        Only prints out the out file path after the file .hoi4 has been parsed");
			Logger.getInstance().log(Logger.INFO, "");
			Logger.getInstance().log(Logger.INFO, "Examples and more option info: https://github.com/juhodev/java-hoi4-parser");
			System.exit(0);
		}

		if (ArgsParser.getInstance().has(ArgsParser.Argument.FOLDER)) {
			gameFolder = ArgsParser.getInstance().getString(ArgsParser.Argument.FOLDER);
		} else {
			Logger.getInstance().log(Logger.INFO, "Using default HOI4 folder located here: " + gameFolder);
		}

		if (ArgsParser.getInstance().has(ArgsParser.Argument.DEBUG)) {
			Logger.LOG_LEVEL = Logger.DEBUG;
			Logger.getInstance().log(Logger.DEBUG, "DEBUG MODE ENABLED");
		} else {
			Profiler.getInstance().disable();
		}

		Profiler.getInstance().register("parse_file");
		Profiler.getInstance().register("write_json");
		Profiler.getInstance().register("read_file");
		Profiler.getInstance().register("tokenizer_read");
		Profiler.getInstance().register("gamefile_read");

		boolean isFullPath = Utils.isFullPath(gameName);

		File file;
		if (isFullPath) {
			file = new File(gameName);
		} else {
			file = new File(gameFolder + "/save games/" + gameName);
		}
		Parser parser = new TextParser(file);
		try {
			parser.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}

		HashMap<String, Object> nodes = parser.getNodes();

		if (ArgsParser.getInstance().has(ArgsParser.Argument.JSON)) {
			printJSON(nodes, isFullPath, file);
		}

		File chromeFormat = new File("chrome_format.json");
		try {
			FileWriter writer = new FileWriter(chromeFormat);
			writer.write(Profiler.getInstance().getChromeFormat());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void printJSON(HashMap<String, Object> nodes, boolean isFullPath, File gameFile) {
		Logger.getInstance().time("Write JSON");
		Profiler.getInstance().start("write_json");

		JSONPrinter jsonPrinter = new JSONPrinter(nodes);
		String outFileName;

		if (ArgsParser.getInstance().has(ArgsParser.Argument.OUT_FILE)) {
			outFileName = ArgsParser.getInstance().getString(ArgsParser.Argument.OUT_FILE);
		} else {
			outFileName = isFullPath ? gameFile.getName() : gameName;
		}

		try {
			jsonPrinter.print(outFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Profiler.getInstance().end("write_json");
		Logger.getInstance().timeEnd(Logger.INFO, "Write JSON");
	}

}
