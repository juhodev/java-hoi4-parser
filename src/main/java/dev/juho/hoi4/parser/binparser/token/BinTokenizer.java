package dev.juho.hoi4.parser.binparser.token;

import dev.juho.hoi4.parser.ParserInputStream;
import dev.juho.hoi4.utils.CSVLoader;
import dev.juho.hoi4.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BinTokenizer {

	private final static int SPECIAL_EQUALS = 0x0100;
	private final static int SPECIAL_START_OBJ = 0x0300;
	private final static int SPECIAL_END_OBJ = 0x0400;
	private final static int SPECIAL_INTEGER = 0x0C00;
	private final static int SPECIAL_FLOAT = 0x0D00;
	private final static int SPECIAL_BOOLEAN = 0x0E00;
	private final static int SPECIAL_STRING = 0x0F00;
	private final static int SPECIAL_INTEGER_2 = 0x1400;
	private final static int SPECIAL_STRING_2 = 0x1700;
	private final static int SPECIAL_FLOAT5 = 0x6701;
	private final static int SPECIAL_FLOAT5_2 = 0x9001;
	private final static int SPECIAL_IRONMAN = 0x132D;
	// No idea how the ones with _2 in the end are different from the ones without _2

	private HashMap<Integer, BinData> data;

	private ParserInputStream in;
	private BinParserToken[] tokens;
	private int tokensCapacity;
	private int tokensSize;
	private int position;

	private boolean hasSeenFileIdentifier;

	public BinTokenizer(ParserInputStream in, int capacity) {
		this.data = new HashMap<>();
		this.in = in;
		this.tokens = new BinParserToken[capacity];
		this.tokensCapacity = capacity;
		this.tokensSize = 0;
		this.position = 0;
		this.hasSeenFileIdentifier = false;
	}

	public void init() {
		loadIronmeltData();
	}

	public boolean eof() {
		return in.eof();
	}

	public BinParserToken peek() {
		if (position == tokensSize) {
			createNewTokens();
		}

		return tokens[position];
	}

	public BinParserToken next() {
		if (position == tokensSize) {
			createNewTokens();
		}

		return tokens[position++];
	}

	private void createNewTokens() {
		tokensSize = 0;
		position = 0;
		while (!in.eof()) {
			BinParserToken token = read();

			if (token != null) {
				tokens[tokensSize++] = token;
			}

			if (tokensSize >= tokensCapacity) {
				break;
			}
		}
	}

	private BinParserToken read() {
		if (!hasSeenFileIdentifier) {
			in.nextByte(); // H
			in.nextByte(); // O
			in.nextByte(); // I
			in.nextByte(); // 4
			in.nextByte(); // b
			in.nextByte(); // i
			in.nextByte(); // n
			hasSeenFileIdentifier = true;
		}

//		Turn these into ints because java doesn't have unsigned bytes so bytes larger than 127 turn into negative numbers
		int first = in.nextByte() & 0xff;
		int second = in.nextByte() & 0xff;
		int appended = appendBytes(first, second);
		BinData lookup = data.get(appended);
		if (lookup == null) {
			Logger.getInstance().log(Logger.ERROR, "Couldn't find bin data for " + Integer.toHexString(appended));
			System.exit(0);
			return null;
		}

		BinData.DataType dataType = lookup.getDataType();

		switch (dataType) {
			case STRING:
			case INTEGER:
				return readKey(lookup);

			case SPECIAL:
				return readSpecial(lookup);

			case UNSPECIFIED:
				return readUnspecified(lookup);

			default:
				return null;
		}
	}

	private BinParserToken readKey(BinData data) {
		return new BinParserToken<>(BinParserToken.Type.KEY, data.getCode(), data.getText());
	}

	private BinParserToken readUnspecified(BinData data) {
		return new BinParserToken<>(BinParserToken.Type.KEY, data.getCode(), data.getText());
	}

	private BinParserToken readSpecial(BinData data) {
		switch (data.getCode()) {
			case SPECIAL_EQUALS:
				return new BinParserToken<>(BinParserToken.Type.OPERATION, SPECIAL_EQUALS, data.getText());

			case SPECIAL_START_OBJ:
				return new BinParserToken<>(BinParserToken.Type.START_OBJECT, SPECIAL_START_OBJ, data.getText());

			case SPECIAL_END_OBJ:
				return new BinParserToken<>(BinParserToken.Type.END_OBJECT, SPECIAL_END_OBJ, data.getText());

//				SPECIAL_INTEGER ehkä käyttääki vaan 1 byte niistä neljäst?
			case SPECIAL_INTEGER:
			case SPECIAL_INTEGER_2:
//				I'm pretty sure integers are 4 bytes but _only two_ bytes are used
				return new BinParserToken<>(BinParserToken.Type.INTEGER, SPECIAL_INTEGER, readInt());

			case SPECIAL_FLOAT5:
			case SPECIAL_FLOAT5_2:
			case SPECIAL_FLOAT:
				return new BinParserToken<>(BinParserToken.Type.DOUBLE, SPECIAL_FLOAT, readFloat());
			case SPECIAL_BOOLEAN:
				return new BinParserToken<>(BinParserToken.Type.BOOLEAN, SPECIAL_BOOLEAN, data.getText());

			case SPECIAL_STRING:
			case SPECIAL_STRING_2:
				return new BinParserToken<>(BinParserToken.Type.STRING, SPECIAL_STRING, readString());

			case SPECIAL_IRONMAN:
				return new BinParserToken<>(BinParserToken.Type.STRING, SPECIAL_IRONMAN, data.getText());

			default:
				break;
		}

		return null;
	}

	private char[] readString() {
		byte aLength = in.nextByte();
		in.nextByte(); // I'm pretty sure the other part of length is never used
		char[] str = new char[(int) aLength];

		for (int i = 0; i < (int) aLength; i++) {
			str[i] = in.nextChar();
		}

		return str;
	}

	private int readInt() {
//		IIRC ints use 4 bytes but only the first two bytes are used
//		Yep, I was correct, if the int is not a date it uses only two bytes of the four allocated to it
//		BUT if it is a date it will use all four.
		int INTEGER_LENGTH = 4;
		int USED_BYTES = 2;
		int FILL_BYTES = INTEGER_LENGTH - USED_BYTES;

		ByteBuffer buffer = ByteBuffer.allocate(INTEGER_LENGTH);
		for (int i = 0; i < FILL_BYTES; i++) {
			buffer.put((byte) 0);
		}

		for (int i = 0; i < USED_BYTES; i++) {
			buffer.put(in.nextByte());
		}

		for (int i = 0; i < FILL_BYTES; i++) {
			in.nextByte();
		}

		return buffer.getInt(0);
	}

	private double readFloat() {
//		I'll just read all floats as doubles
		int FLOAT_LENGTH = 4;
		ByteBuffer buffer = ByteBuffer.allocate(FLOAT_LENGTH);

		for (int i = 0; i < FLOAT_LENGTH; i++) {
			buffer.put(in.nextByte());
		}

		return buffer.getDouble(0);
	}

	/**
	 * Combines two bytes into an int
	 * <p>
	 * Reading the HOI4 save file one byte a time will only give you half of the hex code
	 * combining them will give you the full hex code that can be used to lookup what that code means in the save file
	 *
	 * @param a first part of a hex code
	 * @param b second part of a hex code
	 * @return hex codes appended
	 */
	private int appendBytes(int a, int b) {
		return b | (a << 8);
	}

	private void loadIronmeltData() {
		CSVLoader loader = new CSVLoader(new File("hoi4bin_ironmelt.csv"), ";", true);
		List<String[]> lines = new ArrayList<>();
		try {
			lines = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String[] csv : lines) {
			if (csv.length > 1) {
				int code = Integer.decode(csv[0]);
				String text = csv[1];
				String dataType = csv[2];
				boolean quoted = csv[3].length() == 3;
				data.put(code, new BinData(code, text, BinData.DataType.valueOf(dataType.toUpperCase()), quoted));
			}
		}
	}

}
