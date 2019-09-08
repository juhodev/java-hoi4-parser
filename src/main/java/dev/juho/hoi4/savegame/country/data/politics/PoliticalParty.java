package dev.juho.hoi4.savegame.country.data.politics;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.savegame.country.data.HOIData;

import java.util.ArrayList;
import java.util.List;

public class PoliticalParty implements HOIData {

	private String type, name, longName;
	private double popularity;
	private CountryLeader countryLeader;

	public PoliticalParty() {
		this.type = "";
		this.name = "";
		this.longName = "";
		this.popularity = -1;
		this.countryLeader = new CountryLeader();
	}

	public void build(String type, ObjectNode partyObject) {
		this.type = type;

		StringNode name = (StringNode) partyObject.get("name");
		StringNode longName = (StringNode) partyObject.get("long_name");
		DoubleNode popularity = (DoubleNode) partyObject.get("popularity");

		if (partyObject.get("country_leader") instanceof ObjectNode) {
			readCountryLeader((ObjectNode) partyObject.get("country_leader"));
		} else {
			ListNode listNode = (ListNode) partyObject.get("country_leader");

			for (ASTNode child : listNode.getChildren()) {
				readCountryLeader((ObjectNode) child);
			}
		}

		this.name = name.getValue();
		this.longName = longName.getValue();
		this.popularity = popularity.getValue();
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public CountryLeader getCountryLeader() {
		return countryLeader;
	}

	public double getPopularity() {
		return popularity;
	}

	public String getLongName() {
		return longName;
	}

	private void readCountryLeader(ObjectNode leader) {
		ObjectNode idNode = (ObjectNode) leader.get("id");
		IntegerNode id = (IntegerNode) idNode.get("id");
		this.countryLeader.setId(id.getValue());

		IntegerNode type = (IntegerNode) idNode.get("type");
		this.countryLeader.setType(type.getValue());

		StringNode name = (StringNode) leader.get("name");
		this.countryLeader.setName(name.getValue());

		if (leader.has("desc")) {
			StringNode desc = (StringNode) leader.get("desc");
			this.countryLeader.setDesc(desc.getValue());
		}

		if (leader.has("picture")) {
			StringNode picture = (StringNode) leader.get("picture");
			this.countryLeader.setPicture(picture.getValue());
		}

		StringNode ideology = (StringNode) leader.get("ideology");
		this.countryLeader.setIdeology(ideology.getValue());

		StringNode expire = (StringNode) leader.get("expire");
		this.countryLeader.setExpire(expire.getValue());
	}

	@Override
	public String asJSON() {
		return "{\"_type\": \"politicalParty\", \"type\": \"" + type + "\", \"name\": \"" + name + "\", \"longName\": \"" + longName + "\", \"popularity\": " + popularity + ", \"countryLeader\": " + countryLeader.asJSON() + "}";
	}

	private class CountryLeader implements HOIData {

		private int id, type;
		private String name, desc, picture, ideology, expire;
		private List<String> traits;

		public CountryLeader() {
			this.id = -1;
			this.type = -1;
			this.name = "";
			this.desc = "";
			this.picture = "";
			this.ideology = "";
			this.expire = "";
			this.traits = new ArrayList<>();
		}

		public int getType() {
			return type;
		}

		public int getId() {
			return id;
		}

		public String getPicture() {
			return picture;
		}

		public String getName() {
			return name;
		}

		public List<String> getTraits() {
			return traits;
		}

		public String getDesc() {
			return desc;
		}

		public String getExpire() {
			return expire;
		}

		public String getIdeology() {
			return ideology;
		}

		public void setType(int type) {
			this.type = type;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public void setExpire(String expire) {
			this.expire = expire;
		}

		public void setIdeology(String ideology) {
			this.ideology = ideology;
		}

		public void setId(int id) {
			this.id = id;
		}

		public void setPicture(String picture) {
			this.picture = picture;
		}

		public void setTraits(List<String> traits) {
			this.traits = traits;
		}

		private String traitsAsJSON() {
			StringBuilder builder = new StringBuilder();

			builder.append("[ ");

			for (String s : traits) {
				builder.append(s).append(",");
			}

			builder.delete(builder.length() - 1, builder.length());
			builder.append("]");
			return builder.toString();
		}

		@Override
		public String asJSON() {
			return "{\"_type\": \"countryLeader\", \"id\": " + id + ", \"type\": " + type + ", \"name\": \"" + name + "\", \"desc\": \"" + desc + "\", \"picture\": \"" + picture + "\", \"ideology\": \"" + ideology + "\", \"expire\": \"" + expire + "\", \"traits\": " + traitsAsJSON() + "}";
		}
	}

}
