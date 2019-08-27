package dev.juho.hoi4.savegame.country.data.resources;

import dev.juho.hoi4.parser.textparser.ast.nodes.DoubleNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.PropertyNode;
import dev.juho.hoi4.utils.Logger;
import dev.juho.hoi4.utils.Utils;

import java.util.HashMap;

public class Resources {

	private ResourceData produced, imported, toUse, toExport, baseExport, exported, consumed;

	public Resources() {
		this.produced = new ResourceData();
		this.imported = new ResourceData();
		this.toUse = new ResourceData();
		this.toExport = new ResourceData();
		this.baseExport = new ResourceData();
		this.exported = new ResourceData();
		this.consumed = new ResourceData();
	}

	public void build(PropertyNode resourceNode) {
		ObjectNode obj = (ObjectNode) resourceNode.getValue();

		HashMap<String, Object> children = Utils.getObjectChildren(obj);

		if (children.containsKey("produced")) {
			ObjectNode objNode = (ObjectNode) children.get("produced");
			readResourceData(objNode, produced);
		}

		if (children.containsKey("imported")) {
			ObjectNode objNode = (ObjectNode) children.get("imported");
			readResourceData(objNode, imported);
		}

		if (children.containsKey("to_use")) {
			ObjectNode objNode = (ObjectNode) children.get("to_use");
			readResourceData(objNode, toUse);
		}

		if (children.containsKey("to_export")) {
			ObjectNode objNode = (ObjectNode) children.get("to_export");
			readResourceData(objNode, toExport);
		}

		if (children.containsKey("base_export")) {
			ObjectNode objNode = (ObjectNode) children.get("base_export");
			readResourceData(objNode, baseExport);
		}

		if (children.containsKey("exported")) {
			ObjectNode objNode = (ObjectNode) children.get("exported");
			readResourceData(objNode, exported);
		}

		if (children.containsKey("consumed")) {
			ObjectNode objNode = (ObjectNode) children.get("consumed");
			readResourceData(objNode, consumed);
		}
	}

	public ResourceData getBaseExport() {
		return baseExport;
	}

	public ResourceData getConsumed() {
		return consumed;
	}

	public ResourceData getExported() {
		return exported;
	}

	public ResourceData getImported() {
		return imported;
	}

	public ResourceData getProduced() {
		return produced;
	}

	public ResourceData getToExport() {
		return toExport;
	}

	public ResourceData getToUse() {
		return toUse;
	}

	private void readResourceData(ObjectNode node, ResourceData data) {
		HashMap<String, Object> children = Utils.getObjectChildren(node);

		if (children.containsKey("oil")) {
			DoubleNode dNode = (DoubleNode) children.get("oil");
			data.setOil(dNode.getValue());
		}

		if (children.containsKey("aluminium")) {
			DoubleNode dNode = (DoubleNode) children.get("aluminium");
			data.setAluminium(dNode.getValue());
		}

		if (children.containsKey("rubber")) {
			DoubleNode dNode = (DoubleNode) children.get("rubber");
			data.setRubber(dNode.getValue());
		}

		if (children.containsKey("tungsten")) {
			DoubleNode dNode = (DoubleNode) children.get("tungsten");
			data.setTungsten(dNode.getValue());
		}

		if (children.containsKey("steel")) {
			DoubleNode dNode = (DoubleNode) children.get("steel");
			data.setSteel(dNode.getValue());
		}

		if (children.containsKey("chromium")) {
			DoubleNode dNode = (DoubleNode) children.get("chromium");
			data.setChromium(dNode.getValue());
		}

		Logger.getInstance().log(Logger.DEBUG, "resources: oil: " + data.getOil() + ", aluminium: " + data.getAluminium() + ", rubber: " + data.getRubber() + ", tungsten: " + data.getTungsten() + ", steel: " + data.getSteel() + ", chromium: " + data.getChromium());
	}

}
