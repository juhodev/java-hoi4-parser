package dev.juho.hoi4.textparser.ast;

import dev.juho.hoi4.parser.textparser.ast.AST;
import dev.juho.hoi4.parser.textparser.ast.ASTNode;
import dev.juho.hoi4.parser.textparser.ast.nodes.*;
import dev.juho.hoi4.parser.ParserInputStream;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import dev.juho.hoi4.utils.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ASTTest {

	@BeforeClass
	public static void before() {
		Logger.LOG_LEVEL = Logger.DEBUG;
	}

	@Test
	public void skipHOI4Txt() {
		String str = "HOI4txt\ntest=\"value\"";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		Assert.assertNotNull(((PropertyNode) nodes.get(0)).getValue());
	}

	@Test
	public void parsePropertyNodeTest() {
		String str = "test=\"value\"";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		Assert.assertNotNull(((PropertyNode) nodes.get(0)).getValue());
	}

	@Test
	public void parseStringNodeTest() {
		String str = "test=\"value\"";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
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

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertEquals(ASTNode.Type.OBJECT, node.getValue().getType());

		ObjectNode objectNode = (ObjectNode) ((PropertyNode) nodes.get(0)).getValue();
		Assert.assertTrue(objectNode.has("a"));
		Assert.assertEquals("b", ((StringNode) objectNode.get("a")).getValue());
	}

	@Test
	public void parseListNode() {
		String str = "test={ 1 2 }";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
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
	public void parseListWithASingleElement() {
		String str = "test={ 1 }";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();
		PropertyNode node = (PropertyNode) nodes.get(0);
		ListNode listNode = (ListNode) node.getValue();
		Assert.assertNotNull(listNode.getChildren().get(0));
		IntegerNode child = (IntegerNode) listNode.getChildren().get(0);
		Assert.assertEquals(1, child.getValue());
	}

	@Test
	public void parseObjectInsideList() {
		String str = "test={{ a=\"b\" }}";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertEquals(ASTNode.Type.LIST, node.getValue().getType());

		ListNode listNode = (ListNode) node.getValue();
		List<ASTNode> childNodes = listNode.getChildren();
		ObjectNode objectNode = (ObjectNode) childNodes.get(0);
		Assert.assertTrue(objectNode.has("a"));
		Assert.assertEquals("b", ((StringNode) objectNode.get("a")).getValue());
	}

	@Test
	public void parseListInsideList() {
		String str = "test={{ 1 2 }}";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
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

	@Test
	public void parseStringInsideList() {
		String str = "test={ \"Coloured Buttons\" }";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
		ast.build(tokenizer);
		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		ListNode listNode = (ListNode) node.getValue();
		StringNode firstChild = (StringNode) listNode.getChildren().get(0);

		Assert.assertEquals("Coloured Buttons", firstChild.getValue());
	}

	@Test
	public void parseStringListWithMultipleStrings() {
		String str = "test={ \"Coloured Buttons\" \"test\" }";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
		ast.build(tokenizer);
		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		ListNode listNode = (ListNode) node.getValue();
		StringNode firstChild = (StringNode) listNode.getChildren().get(0);
		StringNode secondChild = (StringNode) listNode.getChildren().get(1);

		Assert.assertEquals("Coloured Buttons", firstChild.getValue());
		Assert.assertEquals("test", secondChild.getValue());
	}

	@Test
	public void parseObjectWithQuoteKeys() {
		String str = "test={ \"1\"={ 4 } \"2\"={ 5 } }";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
		ast.build(tokenizer);
		List<ASTNode> nodes = ast.getNodes();

		Assert.assertEquals(ASTNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		ObjectNode objectNode = (ObjectNode) node.getValue();

		Assert.assertNotNull(objectNode.get("1"));
		Assert.assertNotNull(objectNode.get("2"));

		ListNode childOne = (ListNode) objectNode.get("1");
		ListNode childTwo = (ListNode) objectNode.get("2");

		Assert.assertEquals(4, ((IntegerNode) childOne.getChildren().get(0)).getValue());
		Assert.assertEquals(5, ((IntegerNode) childTwo.getChildren().get(0)).getValue());
	}

	@Test
	public void parseListWithTwoLists() {
		String str = "test={{ 1 } { 2 }}";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();
		PropertyNode propertyNode = (PropertyNode) nodes.get(0);
		ListNode outsideList = (ListNode) propertyNode.getValue();
		ListNode insideListOne = (ListNode) outsideList.getChildren().get(0);
		ListNode insideListTwo = (ListNode) outsideList.getChildren().get(1);

		Assert.assertEquals(propertyNode.getType(), ASTNode.Type.PROPERTY);
		Assert.assertEquals(outsideList.getType(), ASTNode.Type.LIST);
		Assert.assertEquals(insideListOne.getType(), ASTNode.Type.LIST);
		Assert.assertEquals(insideListTwo.getType(), ASTNode.Type.LIST);
	}

	@Test
	public void parseEmptyList() {
		String str = "test={}";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();
		PropertyNode propertyNode = (PropertyNode) nodes.get(0);
		Assert.assertEquals(propertyNode.getType(), ASTNode.Type.PROPERTY);
		Assert.assertEquals(propertyNode.getValue().getType(), ASTNode.Type.OBJECT);
	}

	@Test
	public void parseDouble() {
		String str = "test=100.00000";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();
		PropertyNode propertyNode = (PropertyNode) nodes.get(0);
		Assert.assertEquals(100.0, ((DoubleNode) propertyNode.getValue()).getValue(), 0);
	}

	@Test
	public void parseTwoSameKeysInObject() {
		String str = "test={ a=2 a=5 }";

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(str.getBytes()));
		TextTokenizer tokenizer = new TextTokenizer(in, 128);

		AST ast = new AST(in);
		ast.build(tokenizer);

		List<ASTNode> nodes = ast.getNodes();
		PropertyNode propertyNode = (PropertyNode) nodes.get(0);
		Assert.assertEquals(propertyNode.getType(), ASTNode.Type.PROPERTY);
		ObjectNode objectNode = (ObjectNode) propertyNode.getValue();
		ASTNode shouldBeListNode = (ASTNode) objectNode.get("a");
		Assert.assertEquals(shouldBeListNode.getType(), ASTNode.Type.LIST);
	}

}
