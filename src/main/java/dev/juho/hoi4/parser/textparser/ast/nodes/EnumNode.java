package dev.juho.hoi4.parser.textparser.ast.nodes;

import dev.juho.hoi4.parser.data.HOIEnum;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;

public class EnumNode extends ASTNode {

	private HOIEnum value;

	public EnumNode(HOIEnum value) {
		super(Type.ENUM);

		this.value = value;
	}

	public HOIEnum getValue() {
		return value;
	}
}
