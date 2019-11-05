package dev.juho.hoi4.savegame.map;

import dev.juho.hoi4.utils.CSVLoader;
import dev.juho.hoi4.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapAnalyzer {

	private File mapFile;
	private HashMap<Integer, Province> csvData;

	public MapAnalyzer(File mapFile) {
		this.mapFile = mapFile;
		this.csvData = new HashMap<>();
	}

	public void create(HOIMap map) {
		Logger.getInstance().time("Creating province data from map " + mapFile.getAbsolutePath());
		BufferedImage image = null;
		try {
			image = loadImage();
		} catch (IOException e) {
			e.printStackTrace();
		}

		loadProvinces();
		loadStates(map);
		int[] pixels = getPixels(image);
		createData(pixels, map);
		Logger.getInstance().timeEnd(Logger.DEBUG, "Creating province data from map " + mapFile.getAbsolutePath());
	}

	private void createData(int[] pixels, HOIMap map) {
		int currentValue = -1;
		int currentCount = 0;

		for (int i = 0; i < pixels.length; i++) {
			int pixel = pixels[i];

			if (currentValue == -1) {
				currentValue = pixel;
			}

			if (currentValue != pixel) {
				map.addProvinceData(csvData.get(currentValue), i - currentCount, currentCount);
				currentCount = 0;
				currentValue = pixel;
			}

			currentCount++;
		}
	}

	private BufferedImage loadImage() throws IOException {
		return ImageIO.read(mapFile);
	}

	private int[] getPixels(BufferedImage image) {
		BufferedImage newRGB = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		newRGB.createGraphics().drawImage(image, 0, 0, HOIMap.MAP_WIDTH, HOIMap.MAP_HEIGHT, null);

		return ((DataBufferInt) newRGB.getRaster().getDataBuffer()).getData();
	}

	private void loadStates(HOIMap map) {
		CSVLoader loader = new CSVLoader(new File("states.csv"), ",");

		List<String[]> rows = new ArrayList<>();
		try {
			rows = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String[] row : rows) {
			int stateId = Integer.parseInt(row[0]);
			List<Integer> provinces = new ArrayList<>();

			for (int i = 1; i < row.length; i++) {
				provinces.add(Integer.parseInt(row[i]));
			}

			map.addStateData(stateId, provinces);
		}
	}

	private void loadProvinces() {
		CSVLoader loader = new CSVLoader(new File("provinces.csv"), ";");

		List<String[]> rows = new ArrayList<>();
		try {
			rows = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String[] row : rows) {
			int provinceId = Integer.parseInt(row[0]);
			int r = Integer.parseInt(row[1]);
			int g = Integer.parseInt(row[2]);
			int b = Integer.parseInt(row[3]);
			int colorValue = RGBtoInt(r, g, b);
			String type = row[4];

			Province province = new Province();
			province.setColorValue(colorValue);
			province.setId(provinceId);
			province.setType(type);
			csvData.put(colorValue, province);
		}
	}

	//	https://stackoverflow.com/a/4801397
	private int RGBtoInt(int r, int g, int b) {
		int value = r;
		value = (value << 8) + g;
		value = (value << 8) + b;
		return value;
	}

}
