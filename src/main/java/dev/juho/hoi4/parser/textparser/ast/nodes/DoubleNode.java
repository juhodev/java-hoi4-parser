package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;

public class DoubleNode extends ASTNode {

	private double value;

	public DoubleNode(double value) {
		super(Type.DOUBLE);

		this.value = value;
	}

	public double getValue() {
		return value;
	}
}
