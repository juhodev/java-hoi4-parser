package dev.juho.hoi4.textparser.ast;

import dev.juho.hoi4.parser.textparser.ast.AST;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.parser.ParserInputStream;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import dev.juho.hoi4.utils.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ASTTest {

	@BeforeClass
	public static void before() {
		Logger.LOG_LEVEL = Logger.DEBUG;
	}

	@Test
	public void parsePropertyNodeTest() {
		String str = "test=\"value\"";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()), 128);
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST();
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		Assert.assertTrue(((PropertyNode) nodes.get(0)).getValue() instanceof ASTNode);
	}

	@Test
	public void parseStringNodeTest() {
		String str = "test=\"value\"";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()), 128);
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

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

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()), 128);
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST();
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertEquals(ASTNode.Type.OBJECT, ((ASTNode) node.getValue()).getType());

		ObjectNode objectNode = (ObjectNode) ((PropertyNode) nodes.get(0)).getValue();
		Assert.assertTrue(objectNode.has("a"));
		Assert.assertEquals("b", ((StringNode) objectNode.get("a")).getValue());
	}

	@Test
	public void parseListNode() {
		String str = "test={ 1 2 }";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()), 128);
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

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

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()), 128);
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST();
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertEquals(ASTNode.Type.LIST, ((ASTNode) node.getValue()).getType());

		ListNode listNode = (ListNode) node.getValue();
		List<ASTNode> childNodes = listNode.getChildren();
		ObjectNode objectNode = (ObjectNode) childNodes.get(0);
		Assert.assertTrue(objectNode.has("a"));
		Assert.assertEquals("b", ((StringNode) objectNode.get("a")).getValue());
	}

	@Test
	public void parseListInsideList() {
		String str = "test={{ 1 2 }}";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()), 128);
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

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
