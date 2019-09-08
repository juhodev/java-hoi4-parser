package dev.juho.hoi4.savegame.country.data.resources;

import dev.juho.hoi4.parser.textparser.ast.nodes.DoubleNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;
import dev.juho.hoi4.savegame.country.data.HOIData;
import dev.juho.hoi4.utils.Logger;

public class Resources implements HOIData {

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

	public void build(ObjectNode resourceNode) {
		if (resourceNode.has("produced")) {
			readResourceData((ObjectNode) resourceNode.get("produced"), produced);
		}

		if (resourceNode.has("imported")) {
			readResourceData((ObjectNode) resourceNode.get("imported"), imported);
		}

		if (resourceNode.has("to_use")) {
			readResourceData((ObjectNode) resourceNode.get("to_use"), toUse);
		}

		if (resourceNode.has("to_export")) {
			readResourceData((ObjectNode) resourceNode.get("to_export"), toExport);
		}

		if (resourceNode.has("base_export")) {
			readResourceData((ObjectNode) resourceNode.get("base_export"), baseExport);
		}

		if (resourceNode.has("exported")) {
			readResourceData((ObjectNode) resourceNode.get("exported"), exported);
		}

		if (resourceNode.has("consumed")) {
			readResourceData((ObjectNode) resourceNode.get("consumed"), consumed);
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
		if (node.has("oil")) {
			DoubleNode dNode = (DoubleNode) node.get("oil");
			data.setOil(dNode.getValue());
		}

		if (node.has("aluminium")) {
			DoubleNode dNode = (DoubleNode) node.get("aluminium");
			data.setAluminium(dNode.getValue());
		}

		if (node.has("rubber")) {
			DoubleNode dNode = (DoubleNode) node.get("rubber");
			data.setRubber(dNode.getValue());
		}

		if (node.has("tungsten")) {
			DoubleNode dNode = (DoubleNode) node.get("tungsten");
			data.setTungsten(dNode.getValue());
		}

		if (node.has("steel")) {
			DoubleNode dNode = (DoubleNode) node.get("steel");
			data.setSteel(dNode.getValue());
		}

		if (node.has("chromium")) {
			DoubleNode dNode = (DoubleNode) node.get("chromium");
			data.setChromium(dNode.getValue());
		}

		Logger.getInstance().log(Logger.DEBUG, "resources: oil: " + data.getOil() + ", aluminium: " + data.getAluminium() + ", rubber: " + data.getRubber() + ", tungsten: " + data.getTungsten() + ", steel: " + data.getSteel() + ", chromium: " + data.getChromium());
	}

	@Override
	public String asJSON() {
		return "{\"_type\": \"resources\", \"produced\": " + produced.asJSON() + ", \"imported\": " + imported.asJSON() + ", \"toUse\": " + toUse.asJSON() + ", \"toExport\": " + toExport.asJSON() + ", \"baseExport\": " + baseExport.asJSON() + ", \"exported\": " + exported.asJSON() + ", \"consumed\": " + consumed.asJSON() + "}";
	}
}
