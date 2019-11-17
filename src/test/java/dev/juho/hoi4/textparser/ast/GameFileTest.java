package dev.juho.hoi4.textparser.ast;

import dev.juho.hoi4.parser.textparser.gamefile.GFNode;
import dev.juho.hoi4.parser.textparser.gamefile.GameFile;
import dev.juho.hoi4.parser.textparser.gamefile.nodes.*;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import dev.juho.hoi4.utils.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

public class GameFileTest {

	@BeforeClass
	public static void before() {
		Logger.LOG_LEVEL = Logger.DEBUG;
	}

	@Test
	public void skipHOI4Txt() {
		String str = "HOI4txt\ntest=\"value\"";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);

		List<GFNode> nodes = gameFile.getNodes();

		Assert.assertEquals(GFNode.Type.PROPERTY, nodes.get(0).getType());
		Assert.assertNotNull(((PropertyNode) nodes.get(0)).getValue());
	}

	@Test
	public void parsePropertyNodeTest() {
		String str = "test=\"value\"";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);

		List<GFNode> nodes = gameFile.getNodes();

		Assert.assertEquals(GFNode.Type.PROPERTY, nodes.get(0).getType());
		Assert.assertNotNull(((PropertyNode) nodes.get(0)).getValue());
	}

	@Test
	public void parseStringNodeTest() {
		String str = "test=\"value\"";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);

		List<GFNode> nodes = gameFile.getNodes();

		Assert.assertEquals(GFNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertEquals("test", node.getKey());
		Assert.assertEquals("value", node.getValue());
	}

	@Test
	public void parseObjectNode() {
		String str = "test={a=\"b\"}";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);

		List<GFNode> nodes = gameFile.getNodes();

		Assert.assertEquals(GFNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertTrue(node.getValue() instanceof ObjectNode);

		ObjectNode objectNode = (ObjectNode) ((PropertyNode) nodes.get(0)).getValue();
		Assert.assertTrue(objectNode.has("a"));
		Assert.assertEquals("b", objectNode.getString("a"));
	}

	@Test
	public void parseListNode() {
		String str = "test={ 1 2 }";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);

		List<GFNode> nodes = gameFile.getNodes();

		Assert.assertEquals(GFNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertEquals(GFNode.Type.LIST, ((GFNode) node.getValue()).getType());

		ListNode listNode = (ListNode) (node.getValue());
		List<Object> childNodes = listNode.getChildren();
		Object firstChild = childNodes.get(0);
		Object secondChild = childNodes.get(1);
		Assert.assertEquals(1, (int) firstChild);
		Assert.assertEquals(2, (int) secondChild);
	}

	@Test
	public void parseListWithASingleElement() {
		String str = "test={ 1 }";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);

		List<GFNode> nodes = gameFile.getNodes();
		PropertyNode node = (PropertyNode) nodes.get(0);
		ListNode listNode = (ListNode) node.getValue();
		Assert.assertNotNull(listNode.getChildren().get(0));
		Object child = listNode.getChildren().get(0);
		Assert.assertEquals(1, (int) child);
	}

	@Test
	public void parseObjectInsideList() {
		String str = "test={{ a=\"b\" }}";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);

		List<GFNode> nodes = gameFile.getNodes();

		Assert.assertEquals(GFNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertTrue(node.getValue() instanceof ListNode);

		ListNode listNode = (ListNode) node.getValue();
		List<Object> childNodes = listNode.getChildren();
		ObjectNode objectNode = (ObjectNode) childNodes.get(0);
		Assert.assertTrue(objectNode.has("a"));
		Assert.assertEquals("b", objectNode.getString("a"));
	}

	@Test
	public void parseListInsideList() {
		String str = "test={{ 1 2 }}";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);

		List<GFNode> nodes = gameFile.getNodes();

		Assert.assertEquals(GFNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		Assert.assertEquals(GFNode.Type.LIST, ((GFNode) node.getValue()).getType());

		ListNode listNode = (ListNode) node.getValue();
		List<Object> childNodes = listNode.getChildren();
		ListNode childList = (ListNode) childNodes.get(0);
		Assert.assertEquals(GFNode.Type.LIST, childList.getType());

		List<Object> childChildNodes = childList.getChildren();
		Object firstChild = childChildNodes.get(0);
		Object secondChild = childChildNodes.get(1);
		Assert.assertEquals(1, (int) firstChild);
		Assert.assertEquals(2, (int) secondChild);
	}

	@Test
	public void parseStringInsideList() {
		String str = "test={ \"Coloured Buttons\" }";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);
		List<GFNode> nodes = gameFile.getNodes();

		Assert.assertEquals(GFNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		ListNode listNode = (ListNode) node.getValue();
		Object firstChild = listNode.getChildren().get(0);

		Assert.assertEquals("Coloured Buttons", firstChild);
	}

	@Test
	public void parseStringListWithMultipleStrings() {
		String str = "test={ \"Coloured Buttons\" \"test\" }";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);
		List<GFNode> nodes = gameFile.getNodes();

		Assert.assertEquals(GFNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		ListNode listNode = (ListNode) node.getValue();
		Object firstChild = listNode.getChildren().get(0);
		Object secondChild = listNode.getChildren().get(1);

		Assert.assertEquals("Coloured Buttons", firstChild);
		Assert.assertEquals("test", secondChild);
	}

	@Test
	public void parseObjectWithQuoteKeys() {
		String str = "test={ \"1\"={ 4 } \"2\"={ 5 } }";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);
		List<GFNode> nodes = gameFile.getNodes();

		Assert.assertEquals(GFNode.Type.PROPERTY, nodes.get(0).getType());
		PropertyNode node = (PropertyNode) nodes.get(0);
		ObjectNode objectNode = (ObjectNode) node.getValue();

		Assert.assertNotNull(objectNode.get("1"));
		Assert.assertNotNull(objectNode.get("2"));

		ListNode childOne = (ListNode) objectNode.get("1");
		ListNode childTwo = (ListNode) objectNode.get("2");

		Assert.assertEquals(4, (int) childOne.getChildren().get(0));
		Assert.assertEquals(5, (int) childTwo.getChildren().get(0));
	}

	@Test
	public void parseListWithTwoLists() {
		String str = "test={{ 1 } { 2 }}";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);

		List<GFNode> nodes = gameFile.getNodes();
		PropertyNode propertyNode = (PropertyNode) nodes.get(0);
		ListNode outsideList = (ListNode) propertyNode.getValue();
		ListNode insideListOne = (ListNode) outsideList.getChildren().get(0);
		ListNode insideListTwo = (ListNode) outsideList.getChildren().get(1);

		Assert.assertEquals(propertyNode.getType(), GFNode.Type.PROPERTY);
		Assert.assertEquals(outsideList.getType(), GFNode.Type.LIST);
		Assert.assertEquals(insideListOne.getType(), GFNode.Type.LIST);
		Assert.assertEquals(insideListTwo.getType(), GFNode.Type.LIST);
	}

	@Test
	public void parseEmptyList() {
		String str = "test={}";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);

		List<GFNode> nodes = gameFile.getNodes();
		PropertyNode propertyNode = (PropertyNode) nodes.get(0);
		Assert.assertEquals(propertyNode.getType(), GFNode.Type.PROPERTY);
		Assert.assertTrue(propertyNode.getValue() instanceof ObjectNode);
	}

	@Test
	public void parseDouble() {
		String str = "test=100.00000";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);

		List<GFNode> nodes = gameFile.getNodes();
		PropertyNode propertyNode = (PropertyNode) nodes.get(0);
		Assert.assertEquals(100.0, (double) propertyNode.getValue(), 0);
	}

	@Test
	public void parseTwoSameKeysInObject() {
		String str = "test={ a=2 a=5 }";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameFile gameFile = new GameFile();
		gameFile.build(tokenizer);

		List<GFNode> nodes = gameFile.getNodes();
		PropertyNode propertyNode = (PropertyNode) nodes.get(0);
		Assert.assertEquals(propertyNode.getType(), GFNode.Type.PROPERTY);
		ObjectNode objectNode = (ObjectNode) propertyNode.getValue();
		GFNode shouldBeListNode = (GFNode) objectNode.get("a");
		Assert.assertEquals(shouldBeListNode.getType(), GFNode.Type.LIST);
	}

}
