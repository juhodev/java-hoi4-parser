package dev.juho.hoi4.savegame.country.data.fuel;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.DoubleNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.IntegerNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ListNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.ObjectNode;

public class FuelConsumerData {

	private int index, priority;
	private double received, requested;

	public FuelConsumerData() {
		this.index = -1;
		this.priority = -1;
		this.received = -1;
		this.requested = -1;
	}

	public void build(ASTNode fuelConsumerData) {
		readData((ObjectNode) fuelConsumerData);
	}

	public int getPriority() {
		return priority;
	}

	public double getReceived() {
		return received;
	}

	public double getRequested() {
		return requested;
	}

	public int getIndex() {
		return index;
	}

	private void readData(ObjectNode consumerData) {
		IntegerNode index = (IntegerNode) consumerData.get("index");
		IntegerNode priority = (IntegerNode) consumerData.get("priority");
		DoubleNode received = (DoubleNode) consumerData.get("received");
		DoubleNode requested = (DoubleNode) consumerData.get("requested");

		this.index = index.getValue();
		this.priority = priority.getValue();
		this.received = received.getValue();
		this.requested = requested.getValue();
	}
}
