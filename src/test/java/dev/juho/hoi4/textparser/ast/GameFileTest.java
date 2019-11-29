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
import java.util.HashMap;
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

		HashMap<String, Object> nodes = gameFile.getNodes();

		Assert.assertNotNull(nodes.get("test"));
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

		HashMap<String, Object> nodes = gameFile.getNodes();

		Assert.assertNotNull(nodes.get("test"));
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

		HashMap<String, Object> nodes = gameFile.getNodes();

		Assert.assertNotNull(nodes.get("test"));
		Assert.assertEquals("value", nodes.get("test"));
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

		HashMap<String, Object> nodes = gameFile.getNodes();

		Assert.assertNotNull(nodes.get("test"));
		Assert.assertTrue(nodes.get("test") instanceof ObjectNode);

		ObjectNode objectNode = (ObjectNode) nodes.get("test");
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

		HashMap<String, Object> nodes = gameFile.getNodes();

		Assert.assertNotNull(nodes.get("test"));
		Assert.assertEquals(GFNode.Type.LIST, ((GFNode) nodes.get("test")).getType());

		ListNode listNode = (ListNode) nodes.get("test");
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

		HashMap<String, Object> nodes = gameFile.getNodes();
		ListNode listNode = (ListNode) nodes.get("test");
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

		HashMap<String, Object> nodes = gameFile.getNodes();

		Assert.assertTrue(nodes.get("test") instanceof ListNode);

		ListNode listNode = (ListNode) nodes.get("test");
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

		HashMap<String, Object> nodes = gameFile.getNodes();

		Assert.assertEquals(GFNode.Type.LIST, ((GFNode) nodes.get("test")).getType());

		ListNode listNode = (ListNode) nodes.get("test");
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
		HashMap<String, Object> nodes = gameFile.getNodes();

		ListNode listNode = (ListNode) nodes.get("test");
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
		HashMap<String, Object> nodes = gameFile.getNodes();

		ListNode listNode = (ListNode) nodes.get("test");
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
		HashMap<String, Object> nodes = gameFile.getNodes();

		ObjectNode objectNode = (ObjectNode) nodes.get("test");

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

		HashMap<String, Object> nodes = gameFile.getNodes();

		ListNode outsideList = (ListNode) nodes.get("test");
		ListNode insideListOne = (ListNode) outsideList.getChildren().get(0);
		ListNode insideListTwo = (ListNode) outsideList.getChildren().get(1);

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

		HashMap<String, Object> nodes = gameFile.getNodes();
		Assert.assertTrue(nodes.get("test") instanceof ObjectNode);
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

		HashMap<String, Object> nodes = gameFile.getNodes();
		Assert.assertEquals(100.0, (double) nodes.get("test"), 0);
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

		HashMap<String, Object> nodes = gameFile.getNodes();
		ObjectNode objectNode = (ObjectNode) nodes.get("test");
		GFNode shouldBeListNode = (GFNode) objectNode.get("a");
		Assert.assertEquals(shouldBeListNode.getType(), GFNode.Type.LIST);
	}

}
