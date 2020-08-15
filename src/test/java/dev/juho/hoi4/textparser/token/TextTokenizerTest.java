package dev.juho.hoi4.textparser.token;

import dev.juho.hoi4.profiler.Profiler;
import dev.juho.hoi4.utils.Logger;
import org.junit.BeforeClass;

public class TextTokenizerTest {

	@BeforeClass
	public static void init() {
		Logger.LOG_LEVEL = Logger.DEBUG;
		Profiler.getInstance().disable();
	}

//	@Test
//	public void testTokenizerKeyValueParsing() {
//		String str = "test=\"value\"";
//
//		TextTokenizer tokenizer = new TextTokenizer(128);
//		try {
//			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		TextParserToken firstToken = tokenizer.next();
//		TextParserToken secondToken = tokenizer.next();
//		TextParserToken thirdToken = tokenizer.next();
//
//		Assert.assertEquals(TextParserToken.Type.STRING, firstToken.getType());
//		Assert.assertEquals(TextParserToken.Type.OPERATION, secondToken.getType());
//		Assert.assertEquals(TextParserToken.Type.STRING, thirdToken.getType());
//	}
//
//	@Test
//	public void testTokenizerKeyNumberParsing() {
//		String str = "test=1";
//
//		TextTokenizer tokenizer = new TextTokenizer(128);
//		try {
//			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		TextParserToken firstToken = tokenizer.next();
//		TextParserToken secondToken = tokenizer.next();
//		TextParserToken thirdToken = tokenizer.next();
//
//		Assert.assertEquals(TextParserToken.Type.STRING, firstToken.getType());
//		Assert.assertEquals(TextParserToken.Type.OPERATION, secondToken.getType());
//		Assert.assertEquals(TextParserToken.Type.STRING, thirdToken.getType());
//	}
//
//	@Test
//	public void testTokenizerKeyObjectParsing() {
//		String str = "test={ a = 3 }";
//
//		TextTokenizer tokenizer = new TextTokenizer(128);
//		try {
//			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.OPERATION, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.START_OBJECT, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.OPERATION, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.END_OBJECT, tokenizer.next().getType());
//	}
//
//	@Test
//	public void testTokenizerQuotedKeyParsing() {
//		String str = "\"test\"={ a = 3 }";
//
//		TextTokenizer tokenizer = new TextTokenizer(128);
//		try {
//			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.OPERATION, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.START_OBJECT, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.OPERATION, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.END_OBJECT, tokenizer.next().getType());
//	}
//
//	@Test
//	public void testTokenizerSkipComments() {
//		String str = "# this comment should be ignored\n\r#secondcomment\na=2";
//
//		TextTokenizer tokenizer = new TextTokenizer(128);
//		try {
//			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.OPERATION, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
//	}
//
//	@Test
//	public void testTokenizerEndsWithWhitespace() {
//		String str = "a=2\r\n ";
//
//		TextTokenizer tokenizer = new TextTokenizer(128);
//		try {
//			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.OPERATION, tokenizer.next().getType());
//		Assert.assertEquals(TextParserToken.Type.STRING, tokenizer.next().getType());
//	}
//
//	@Test
//	public void testTokenSwap() {
//		String str = "a={ a = 3 }";
//
//		TextTokenizer tokenizer = new TextTokenizer(2);
//		try {
//			tokenizer.readInputStream(new ByteArrayInputStream(str.getBytes()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		TextParserToken first = tokenizer.next();
//		TextParserToken second = tokenizer.next();
//		TextParserToken thirdPeek = tokenizer.peek();
//
//		Assert.assertEquals(TextParserToken.Type.STRING, first.getType());
//		Assert.assertEquals(TextParserToken.Type.OPERATION, second.getType());
//		Assert.assertEquals(TextParserToken.Type.START_OBJECT, thirdPeek.getType());
//
//		first.forget();
//		second.forget();
//		thirdPeek.forget();
//	}

}
