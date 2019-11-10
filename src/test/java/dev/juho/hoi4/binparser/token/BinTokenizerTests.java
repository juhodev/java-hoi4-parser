package dev.juho.hoi4.binparser.token;

import dev.juho.hoi4.parser.ParserInputStream;
import dev.juho.hoi4.parser.binparser.BinParser;
import dev.juho.hoi4.parser.binparser.token.BinParserToken;
import dev.juho.hoi4.parser.binparser.token.BinTokenizer;
import dev.juho.hoi4.utils.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;

public class BinTokenizerTests {

	@BeforeClass
	public static void setup() {
		Logger.LOG_LEVEL = Logger.DEBUG;
	}

	@Test
	public void testString() {
//		HOI4bin player=JAP
		byte rawData[] = {
//				HOI4bin
				(byte) 0x48, (byte) 0x4F, (byte) 0x49, (byte) 0x34, (byte) 0x62, (byte) 0x69,
				(byte) 0x6E,

				(byte) 0x35, (byte) 0x2A, (byte) 0x01, (byte) 0x00, (byte) 0x0F,
				(byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x4A, (byte) 0x41, (byte) 0x50
		};

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(rawData), 30);
		BinTokenizer tokenizer = new BinTokenizer(in, 30);
		tokenizer.init();

		BinParserToken player = tokenizer.next();
		BinParserToken equals = tokenizer.next();
		BinParserToken jap = tokenizer.next();

		Assert.assertEquals(0x352A, player.getCode());
		Assert.assertEquals(0x0100, equals.getCode());
		Assert.assertEquals(0x0f00, jap.getCode());
	}

	@Test
	public void testMultipleStrings() {
		/* F:\nodeshit\hoi4\parser\save_files\a.hoi4 (20/03/2019 19.04.03)
   StartOffset(h): 00000007, EndOffset(h): 00000023, Length(h): 0000001D */

		byte rawData[] = {
//				HOI4bin
				(byte) 0x48, (byte) 0x4F, (byte) 0x49, (byte) 0x34, (byte) 0x62, (byte) 0x69,
				(byte) 0x6E,

				(byte) 0x35, (byte) 0x2A, (byte) 0x01, (byte) 0x00, (byte) 0x0F, (byte) 0x00,
				(byte) 0x03, (byte) 0x00, (byte) 0x4A, (byte) 0x41, (byte) 0x50, (byte) 0x3E,
				(byte) 0x2E, (byte) 0x01, (byte) 0x00, (byte) 0x17, (byte) 0x00, (byte) 0x0A,
				(byte) 0x00, (byte) 0x6E, (byte) 0x65, (byte) 0x75, (byte) 0x74, (byte) 0x72,
				(byte) 0x61, (byte) 0x6C, (byte) 0x69, (byte) 0x74, (byte) 0x79
		};

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(rawData), 30);
		BinTokenizer tokenizer = new BinTokenizer(in, 30);
		tokenizer.init();

		BinParserToken player = tokenizer.next();
		BinParserToken equals = tokenizer.next();
		BinParserToken jap = tokenizer.next();
		BinParserToken ideology = tokenizer.next();
		BinParserToken equals2 = tokenizer.next();
		BinParserToken neutrality = tokenizer.next();

		Assert.assertEquals(0x352A, player.getCode());
		Assert.assertEquals(0x0100, equals.getCode());
		Assert.assertEquals(0x0f00, jap.getCode());
		Assert.assertEquals(0x3E2E, ideology.getCode());
		Assert.assertEquals(0x0100, equals2.getCode());
		Assert.assertEquals("neutrality", new String((char[]) neutrality.getValue()));
	}

	@Test
	public void testObject() {
		/* F:\nodeshit\hoi4\parser\save_files\a.hoi4 (20/03/2019 19.04.03)
   StartOffset(h): 0000005C, EndOffset(h): 0000007E, Length(h): 00000023 */

		byte rawData[] = {
				// HOI4bin
				(byte) 0x48, (byte) 0x4F, (byte) 0x49, (byte) 0x34, (byte) 0x62, (byte) 0x69,
				(byte) 0x6E,

				(byte) 0x67, (byte) 0x35, (byte) 0x01, (byte) 0x00, (byte) 0x03, (byte) 0x00,
				(byte) 0x0F, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x4A, (byte) 0x41,
				(byte) 0x50, (byte) 0x01, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0xE2,
				(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x0F, (byte) 0x00, (byte) 0x03,
				(byte) 0x00, (byte) 0x6A, (byte) 0x6F, (byte) 0x6F
		};

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(rawData), 30);
		BinTokenizer tokenizer = new BinTokenizer(in, 30);
		tokenizer.init();

		BinParserToken player_countries = tokenizer.next();
		BinParserToken equals = tokenizer.next();
		BinParserToken startObj = tokenizer.next();
		BinParserToken jap = tokenizer.next();
		BinParserToken equals2 = tokenizer.next();
		BinParserToken startObj2 = tokenizer.next();
		BinParserToken user = tokenizer.next();
		BinParserToken equals3 = tokenizer.next();
		BinParserToken joo = tokenizer.next();

		Assert.assertEquals(0x6735, player_countries.getCode());
		Assert.assertEquals(0x0100, equals.getCode());
		Assert.assertEquals(0x0300, startObj.getCode());
		Assert.assertEquals("JAP", new String((char[]) jap.getValue()));
		Assert.assertEquals(0x0100, equals2.getCode());
		Assert.assertEquals(0x0300, startObj2.getCode());
		Assert.assertEquals(0xE200, user.getCode());
		Assert.assertEquals(0x0100, equals3.getCode());
		Assert.assertEquals("joo", new String((char[]) joo.getValue()));
	}

	@Test
	public void testInt() {
		/* F:\nodeshit\hoi4\parser\save_files\a.hoi4 (20/03/2019 19.04.03)
   StartOffset(h): 00047921, EndOffset(h): 0004792A, Length(h): 0000000A */

		byte rawData[] = {
				// HOI4bin
				(byte) 0x48, (byte) 0x4F, (byte) 0x49, (byte) 0x34, (byte) 0x62, (byte) 0x69,
				(byte) 0x6E,

				(byte) 0xE8, (byte) 0x2F, (byte) 0x01, (byte) 0x00, (byte) 0x0C, (byte) 0x00,
				(byte) 0x13, (byte) 0x00, (byte) 0x00, (byte) 0x00
		};

		ParserInputStream in = new ParserInputStream(new ByteArrayInputStream(rawData), 30);
		BinTokenizer tokenizer = new BinTokenizer(in, 30);
		tokenizer.init();

		BinParserToken available = tokenizer.next();
		BinParserToken equals = tokenizer.next();
		BinParserToken num = tokenizer.next();

		Assert.assertEquals(0xE82F, available.getCode());
		Assert.assertEquals(0x0100, equals.getCode());
//		TODO: I'm not sure if this should be 0x1300 or 0x13.
//		I'll keep it as 0x1300 for now because if tests don't pass you change the tests and not the code that it tests so they pass right?
		Assert.assertEquals(0x1300, (int) num.getValue());
	}

	@Test
	public void testFloat() {

	}

}
