package dev.juho.hoi4.savegame.map;

import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.utils.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HOIMap {

	public static final int MAP_WIDTH = 5632, MAP_HEIGHT = 2048;

	private HashMap<Integer, ProvinceData> provinceData;
	private HashMap<Integer, List<Integer>> stateToProvince;

	public HOIMap() {
		this.provinceData = new HashMap<>();
		this.stateToProvince = new HashMap<>();
	}

	public void init() {
		Logger.getInstance().log(Logger.INFO, "Creating map");
		File provinceData = new File("provinceData.csv");

		if (provinceData.exists()) {
//			Read
		} else {
			Logger.getInstance().log(Logger.INFO, "Creating map data, this might take a while");
			MapAnalyzer mapAnalyzer = new MapAnalyzer(new File("provinces.bmp"));
			mapAnalyzer.create(this);
		}
	}

	public void addProvinceData(Province province, int pos, int width) {
		ProvinceData data = provinceData.get(province.getId());
		if (data == null) {
			data = new ProvinceData(province);
		}

		data.add(pos, width);
		provinceData.put(province.getId(), data);
	}

	public void addStateData(int state, List<Integer> provinces) {
		stateToProvince.put(state, provinces);
	}

	public Image createMap(List<State> states) {
		Logger.getInstance().time("Creating map");
		int[] pixels = new int[MAP_WIDTH * MAP_HEIGHT];

		List<CountryTag> highlightCountries = Arrays.asList(CountryTag.GER, CountryTag.SOV);

		for (State state : states) {
			List<Integer> provincesInState = stateToProvince.get(state.getId());
			if (provincesInState == null) {
				Logger.getInstance().log(Logger.ERROR, "province data not found for state " + state.getId());
				continue;
			}

			boolean highlight = highlightCountries.contains(state.getOwner()) || highlightCountries.contains(state.getController());

			for (int province : provincesInState) {
				ProvinceData data = provinceData.get(province);
				if (data != null) {
					if (!data.getProvince().getType().equalsIgnoreCase("sea")) {
						for (ProvinceData.RowData row : data.getRows()) {
							if (highlight) {
								writeWidth(pixels, row.pos, row.width, 0xff0000);
							} else {
								writeWidth(pixels, row.pos, row.width, 0x777777);
							}
						}

						ProvinceData.RowData lastRow = data.getRows().get(data.getRows().size() - 1);
						writeWidth(pixels, lastRow.pos, lastRow.width, 0xcee3ed);
					}
				} else {
					Logger.getInstance().log(Logger.ERROR, "couldn't find province data for province " + state.getId() + " (controller: " + state.getController() + ")");
				}
			}
		}

		Logger.getInstance().timeEnd(Logger.INFO, "Creating map");
		return pixelsToImage(pixels);
	}

	private void writeWidth(int[] pixels, int pos, int width, int color) {
		for (int i = pos; i < pos + width; i++) {
			pixels[i] = color;
		}

		pixels[pos] = 0xcee3ed;
	}

	private Image pixelsToImage(int[] pixels) {
		BufferedImage img = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = img.getRaster();
		int[] newPixels = new int[pixels.length * 3];
		for (int i = 0; i < pixels.length; i++) {
			newPixels[3 * i] = (pixels[i] >> 16) & 0xff;
			newPixels[3 * i + 1] = (pixels[i] >> 8) & 0xff;
			newPixels[3 * i + 2] = (pixels[i]) & 0xff;
		}

		raster.setPixels(0, 0, MAP_WIDTH, MAP_HEIGHT, newPixels);
		return img;
	}

}
