package dev.juho.hoi4.textparser.token;

import dev.juho.hoi4.parser.textparser.token.TextParserToken;
import dev.juho.hoi4.parser.textparser.token.TextTokenizer;
import dev.juho.hoi4.utils.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;

public class TextTokenizerTest {

	@BeforeClass
	public static void init() {
		Logger.LOG_LEVEL = Logger.DEBUG;
	}

	@Test
	public void testTokenizerKeyValueParsing() {
		String str = "test=\"value\"";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		TextParserToken firstToken = tokenizer.next();
		TextParserToken secondToken = tokenizer.next();
		TextParserToken thirdToken = tokenizer.next();

		Assert.assertEquals(TextParserToken.Type.STRING, firstToken.getType());
		Assert.assertEquals(TextParserToken.Type.OPERATION, secondToken.getType());
		Assert.assertEquals(TextParserToken.Type.STRING, thirdToken.getType());
	}

	@Test
	public void testTokenizerKeyNumberParsing() {
		String str = "test=1";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		TextParserToken firstToken = tokenizer.next();
		TextParserToken secondToken = tokenizer.next();
		TextParserToken thirdToken = tokenizer.next();

		Assert.assertEquals(TextParserToken.Type.STRING, firstToken.getType());
		Assert.assertEquals(TextParserToken.Type.OPERATION, secondToken.getType());
		Assert.assertEquals(TextParserToken.Type.STRING, thirdToken.getType());
	}

	@Test
	public void testTokenizerKeyObjectParsing() {
		String str = "test={ a = 3 }";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.START_OBJECT, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.END_OBJECT, tokenizer.next().getType());
	}

	@Test
	public void testTokenizerQuotedKeyParsing() {
		String str = "\"test\"={ a = 3 }";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.START_OBJECT, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.END_OBJECT, tokenizer.next().getType());
	}

	@Test
	public void testTokenizerSkipComments() {
		String str = "# this comment should be ignored\n\r#secondcomment\na=2";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
	}

	@Test
	public void testTokenizerEndsWithWhitespace() {
		String str = "a=2\r\n ";

		TextTokenizer tokenizer = new TextTokenizer(128);
		try {
			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.OPERATION, tokenizer.next().getType());
		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
	}

}
