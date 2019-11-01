package dev.juho.hoi4.savegame.map;

import dev.juho.hoi4.parser.data.CountryTag;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.StringNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Provinces {

	private List<Province> provinceList;

	public Provinces() {
		this.provinceList = new ArrayList<>();
	}

	public void build(ObjectNode provincesNode) {
		Iterator it = provincesNode.getChildren().entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

			String key = (String) pair.getKey();
			ObjectNode obj = (ObjectNode) pair.getValue();

			if (obj.has("controller")) {
				StringNode controllerNode = (StringNode) obj.get("controller");

				Province province = new Province();
				province.setId(Integer.parseInt(key));
				province.setController(CountryTag.valueOf(controllerNode.getValue()));
				provinceList.add(province);
			}
		}
	}

	public List<Province> getProvinceList() {
		return provinceList;
	}
}
