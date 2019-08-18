package dev.juho.hoi4.textparser.token;

import dev.juho.hoi4.parser.textparser.token.TextParserInputStream;
import dev.juho.hoi4.parser.textparser.token.TextParserToken;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

public class TextTokenizerTest {

	@Test
	public void propertyKeyStringTest() {
		String str = "test=\"value\"";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		List<TextParserToken> tokens = tokenizer.getTokens();

		Assert.assertEquals(TextParserToken.Type.KEY, tokens.get(0).getType());
		Assert.assertEquals("test", tokens.get(0).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokens.get(1).getType());
		Assert.assertEquals("=", tokens.get(1).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.STRING, tokens.get(2).getType());
		Assert.assertEquals("value", tokens.get(2).getValue().toString());
	}

	@Test
	public void propertyKeyIntegerTest() {
		String str = "test=2";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		List<TextParserToken> tokens = tokenizer.getTokens();

		Assert.assertEquals(TextParserToken.Type.KEY, tokens.get(0).getType());
		Assert.assertEquals("test", tokens.get(0).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokens.get(1).getType());
		Assert.assertEquals("=", tokens.get(1).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.INTEGER, tokens.get(2).getType());
		Assert.assertEquals(2, (int) tokens.get(2).getValue());
	}

	@Test
	public void propertyKeyDoubleTest() {
		String str = "test=2.3";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		List<TextParserToken> tokens = tokenizer.getTokens();

		Assert.assertEquals(TextParserToken.Type.KEY, tokens.get(0).getType());
		Assert.assertEquals("test", tokens.get(0).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokens.get(1).getType());
		Assert.assertEquals("=", tokens.get(1).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.DOUBLE, tokens.get(2).getType());
		Assert.assertEquals(2.3, (double) tokens.get(2).getValue(), 0);
	}

	@Test
	public void propertyKeyLongTest() {
		String str = "test=5000000000";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		List<TextParserToken> tokens = tokenizer.getTokens();

		Assert.assertEquals(TextParserToken.Type.KEY, tokens.get(0).getType());
		Assert.assertEquals("test", tokens.get(0).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokens.get(1).getType());
		Assert.assertEquals("=", tokens.get(1).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.LONG, tokens.get(2).getType());
		Assert.assertEquals(5000000000L, (long) tokens.get(2).getValue());
	}

	@Test
	public void propertyKeyBooleanTest() {
		String str = "test=yes";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		List<TextParserToken> tokens = tokenizer.getTokens();

		Assert.assertEquals(TextParserToken.Type.KEY, tokens.get(0).getType());
		Assert.assertEquals("test", tokens.get(0).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokens.get(1).getType());
		Assert.assertEquals("=", tokens.get(1).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.BOOLEAN, tokens.get(2).getType());
		Assert.assertEquals("yes", tokens.get(2).getValue().toString());
	}

	@Test
	public void propertyKeyObjectTest() {
		String str = "test={a=\"b\"}";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		List<TextParserToken> tokens = tokenizer.getTokens();

		Assert.assertEquals(TextParserToken.Type.KEY, tokens.get(0).getType());
		Assert.assertEquals("test", tokens.get(0).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokens.get(1).getType());
		Assert.assertEquals("=", tokens.get(1).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.START_OBJECT, tokens.get(2).getType());
		Assert.assertEquals("{", tokens.get(2).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.KEY, tokens.get(3).getType());
		Assert.assertEquals("a", tokens.get(3).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokens.get(4).getType());
		Assert.assertEquals("=", tokens.get(4).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.STRING, tokens.get(5).getType());
		Assert.assertEquals("b", tokens.get(5).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.END_OBJECT, tokens.get(6).getType());
		Assert.assertEquals("}", tokens.get(6).getValue().toString());
	}

	@Test
	public void propertyKeyListTest() {
		String str = "test={ 1 2 3 }";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		List<TextParserToken> tokens = tokenizer.getTokens();

		Assert.assertEquals(TextParserToken.Type.KEY, tokens.get(0).getType());
		Assert.assertEquals("test", tokens.get(0).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokens.get(1).getType());
		Assert.assertEquals("=", tokens.get(1).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.START_OBJECT, tokens.get(2).getType());
		Assert.assertEquals("{", tokens.get(2).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.INTEGER, tokens.get(3).getType());
		Assert.assertEquals(1, (int) tokens.get(3).getValue());
		Assert.assertEquals(TextParserToken.Type.INTEGER, tokens.get(4).getType());
		Assert.assertEquals(2, (int) tokens.get(4).getValue());
		Assert.assertEquals(TextParserToken.Type.INTEGER, tokens.get(5).getType());
		Assert.assertEquals(3, (int) tokens.get(5).getValue());
		Assert.assertEquals(TextParserToken.Type.END_OBJECT, tokens.get(6).getType());
		Assert.assertEquals("}", tokens.get(6).getValue().toString());
	}

	@Test
	public void propertyKeyListInsideObjectTest() {
		String str = "test={a={ 1 2 }}";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		List<TextParserToken> tokens = tokenizer.getTokens();

		Assert.assertEquals(TextParserToken.Type.KEY, tokens.get(0).getType());
		Assert.assertEquals("test", tokens.get(0).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokens.get(1).getType());
		Assert.assertEquals("=", tokens.get(1).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.START_OBJECT, tokens.get(2).getType());
		Assert.assertEquals("{", tokens.get(2).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.KEY, tokens.get(3).getType());
		Assert.assertEquals("a", tokens.get(3).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokens.get(4).getType());
		Assert.assertEquals("=", tokens.get(4).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.START_OBJECT, tokens.get(5).getType());
		Assert.assertEquals("{", tokens.get(5).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.INTEGER, tokens.get(6).getType());
		Assert.assertEquals(1, (int) tokens.get(6).getValue());
		Assert.assertEquals(TextParserToken.Type.INTEGER, tokens.get(7).getType());
		Assert.assertEquals(2, (int) tokens.get(7).getValue());
		Assert.assertEquals(TextParserToken.Type.END_OBJECT, tokens.get(8).getType());
		Assert.assertEquals("}", tokens.get(8).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.END_OBJECT, tokens.get(9).getType());
		Assert.assertEquals("}", tokens.get(9).getValue().toString());
	}

	@Test
	public void propertyKeyListInsideOfListTest() {
		String str = "test={{ 1 2 3 }}";

		TextTokenizer tokenizer = new TextTokenizer();
		TextParserInputStream in = new TextParserInputStream(new ByteArrayInputStream(str.getBytes()));
		tokenizer.createTokens(in);

		List<TextParserToken> tokens = tokenizer.getTokens();

		Assert.assertEquals(TextParserToken.Type.KEY, tokens.get(0).getType());
		Assert.assertEquals("test", tokens.get(0).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokens.get(1).getType());
		Assert.assertEquals("=", tokens.get(1).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.START_OBJECT, tokens.get(2).getType());
		Assert.assertEquals("{", tokens.get(2).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.START_OBJECT, tokens.get(3).getType());
		Assert.assertEquals("{", tokens.get(3).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.INTEGER, tokens.get(4).getType());
		Assert.assertEquals(1, (int) tokens.get(4).getValue());
		Assert.assertEquals(TextParserToken.Type.INTEGER, tokens.get(5).getType());
		Assert.assertEquals(2, (int) tokens.get(5).getValue());
		Assert.assertEquals(TextParserToken.Type.INTEGER, tokens.get(6).getType());
		Assert.assertEquals(3, (int) tokens.get(6).getValue());
		Assert.assertEquals(TextParserToken.Type.END_OBJECT, tokens.get(7).getType());
		Assert.assertEquals("}", tokens.get(7).getValue().toString());
		Assert.assertEquals(TextParserToken.Type.END_OBJECT, tokens.get(8).getType());
		Assert.assertEquals("}", tokens.get(8).getValue().toString());
	}

}
