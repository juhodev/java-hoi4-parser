package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.textparser.ast.ASTNode;

public class LongNode extends ASTNode {

	private long value;

	public LongNode(long value) {
		super(Type.LONG);

		this.value = value;
	}

	public long getValue() {
		return value;
	}
}
