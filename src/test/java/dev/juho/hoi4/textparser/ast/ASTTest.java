package dev.juho.hoi4.textparser.ast;

import dev.juho.hoi4.parser.textparser.ast.AST;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.parser.textparser.token.TextParserInputStream;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ASTTest {

	@Test
	public void parsePropertyNodeTest() {
		String str = "test=\"value\"";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		AST ast = new AST();
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		Assert.assertEquals(true, ((PropertyNode) nodes.get(0)).getValue() instanceof ASTNode);
	}

	@Test
	public void parseStringNodeTest() {
		String str = "test=\"value\"";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		AST ast = new AST();
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertEquals("test", node.getKey());
		Assert.assertEquals("value", ((StringNode) node.getValue()).getValue());
	}

	@Test
	public void parseObjectNode() {
		String str = "test={a=\"b\"}";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		AST ast = new AST();
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertEquals(ASTNode.Type.OBJECT, ((ASTNode) node.getValue()).getType());

		ObjectNode objectNode = (ObjectNode) ((PropertyNode) nodes.get(0)).getValue();
		List<ASTNode> objectChildren = objectNode.getChildren();
		Assert.assertEquals(ASTNode.Type.PROPERTY, objectChildren.get(0).getType());
		PropertyNode childNode = (PropertyNode) objectChildren.get(0);
		Assert.assertEquals("a", childNode.getKey());
		Assert.assertEquals("b", ((StringNode) childNode.getValue()).getValue());
	}

	@Test
	public void parseListNode() {
		String str = "test={ 1 2 }";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		AST ast = new AST();
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertEquals(ASTNode.Type.LIST, ((ASTNode) node.getValue()).getType());

		ListNode listNode = (ListNode) (node.getValue());
		List<ASTNode> childNodes = listNode.getChildren();
		IntegerNode firstChild = (IntegerNode) childNodes.get(0);
		IntegerNode secondChild = (IntegerNode) childNodes.get(1);
		Assert.assertEquals(1, firstChild.getValue());
		Assert.assertEquals(2, secondChild.getValue());
	}

	@Test
	public void parseObjectInsideList() {
		String str = "test={{ a=\"b\" }}";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		AST ast = new AST();
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertEquals(ASTNode.Type.LIST, ((ASTNode) node.getValue()).getType());

		ListNode listNode = (ListNode) node.getValue();
		List<ASTNode> childNodes = listNode.getChildren();
		ObjectNode objectNode = (ObjectNode) childNodes.get(0);
		List<ASTNode> objectChildren = objectNode.getChildren();
		PropertyNode propertyNode = (PropertyNode) objectChildren.get(0);
		Assert.assertEquals("a", propertyNode.getKey());
		Assert.assertEquals("b", ((StringNode) propertyNode.getValue()).getValue());
	}

	@Test
	public void parseListInsideList() {
		String str = "test={{ 1 2 }}";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		AST ast = new AST();
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertEquals(ASTNode.Type.LIST, ((ASTNode) node.getValue()).getType());

		ListNode listNode = (ListNode) node.getValue();
		List<ASTNode> childNodes = listNode.getChildren();
		ListNode childList = (ListNode) childNodes.get(0);
		Assert.assertEquals(ASTNode.Type.LIST, childList.getType());

		List<ASTNode> childChildNodes = childList.getChildren();
		IntegerNode firstChild = (IntegerNode) childChildNodes.get(0);
		IntegerNode secondChild = (IntegerNode) childChildNodes.get(1);
		Assert.assertEquals(1, firstChild.getValue());
		Assert.assertEquals(2, secondChild.getValue());
	}

}
