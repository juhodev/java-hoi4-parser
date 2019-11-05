package dev.juho.hoi4.savegame.map;

import java.util.ArrayList;
import java.util.List;

public class ProvinceData {

	private Province province;
	private List<RowData> rows;
	private String type;

	public ProvinceData(Province province) {
		this.province = province;
		this.rows = new ArrayList<>();
	}

	public void add(int pos, int width) {
		rows.add(new RowData(pos, width));
	}

	public Province getProvince() {
		return province;
	}

	public List<RowData> getRows() {
		return rows;
	}

	public class RowData {
		int pos, width;

		public RowData(int pos, int width) {
			this.pos = pos;
			this.width = width;
		}
	}

}
