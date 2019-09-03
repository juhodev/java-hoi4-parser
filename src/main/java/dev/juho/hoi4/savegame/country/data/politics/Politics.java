package dev.juho.hoi4.savegame.country.data.politics;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Politics {

	private String rulingParty, lastElection;
	private int electionFrequency;
	private double politicalPower;
	private boolean electionsAllowed;
	private List<PoliticalParty> parties;
	private List<String> ideas;

	public Politics() {
		this.parties = new ArrayList<>();
		this.ideas = new ArrayList<>();
		this.electionFrequency = -1;
		this.politicalPower = -1;
		this.electionsAllowed = false;
	}

	public void build(ObjectNode politicsNode) {
		readParties((ObjectNode) politicsNode.get("parties"));
		readIdeas((ListNode) politicsNode.get("ideas"));
		rulingParty = ((StringNode) politicsNode.get("ruling_party")).getValue();
		lastElection = ((StringNode) politicsNode.get("last_election")).getValue();
		electionFrequency = ((IntegerNode) politicsNode.get("election_frequency")).getValue();
		electionsAllowed = ((BooleanNode) politicsNode.get("elections_allowed")).getValue();
		politicalPower = ((DoubleNode) politicsNode.get("political_power")).getValue();
	}

	public double getPoliticalPower() {
		return politicalPower;
	}

	public boolean isElectionsAllowed() {
		return electionsAllowed;
	}

	public int getElectionFrequency() {
		return electionFrequency;
	}

	public List<PoliticalParty> getParties() {
		return parties;
	}

	public List<String> getIdeas() {
		return ideas;
	}

	public String getLastElection() {
		return lastElection;
	}

	public String getRulingParty() {
		return rulingParty;
	}

	private void readIdeas(ListNode ideaList) {
		for (ASTNode child : ideaList.getChildren()) {
			StringNode childStr = (StringNode) child;
			ideas.add(childStr.getValue());
		}
	}

	private void readParties(ObjectNode partiesNode) {
		Iterator it = partiesNode.getChildren().entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();

			PoliticalParty party = new PoliticalParty();
			party.build(pair.getKey(), (ObjectNode) pair.getValue());
			parties.add(party);
		}
	}

}
