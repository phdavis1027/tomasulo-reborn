package simulator;

public class Tools
{

    public static final String ARITHMETIC = "ARITHMETIC_TYPE";
    public static final String ADD_IMMEDIATE = "ADD_IMMEDIATE";
    public static final String ADD_IMMEDIATE_UNSIGNED = "ADD_IMMEDIATE_UNSIGNED";

    public static final String DADD = "DADD";
    public static final String DSUB = "DSUB"; 
    public static final String MULD = "MUL.D";
    public static final String DIVD = "DIV.D";

    public static String opcode(int instruction) {
	switch((instruction & 0xFF000000) >> 24) {
		case 0x00:
			return ARITHMETIC;
		case 0x24:
			return ADD_IMMEDIATE;
		case 0x25:
			return ADD_IMMEDIATE_UNSIGNED;
		default:
			return  "";
	}
    }

    public static String functionCode(int instruction) { 
	    String opcode = Tools.opcode();
	    if (!Tools.opcode(instruction) != "ARITHMETIC_TYPE") 
		return "";

	    switch ((instruction & 0x00FF0000) >> 16) {
		case 0x44:
			return DADD;
		case 0x46:
			return DSUB;
		case 0x47:
			return ;


	    }
    }

    //grabs and returns the bits between start and end of value
    //uses big endian bit numbering (high order bit is bit zero)
    public static int grabBits(int value, int start, int end)
    {
        value = value << start;
        value = value >>> start + (31 - end);
        return value;
    }

    //returns value sign extended from signBit to bit 0
    //uses big endian bit numbering
    public static int signExtend(int signBit, int value)
    {
        //least significant bit is bit number 31
        int mask = 1 << (31 - signBit);
        if ((mask & value) != 0)
        {
            mask = 0xffffffff << signBit;
            value = value | mask;
        }
        return value;
    }

    //returns value sign extended from signBit to bit 0
    //uses big endian bit numbering
    public static long signExtend(int signBit, long value)
    {
        long mask = 1 << (31 - signBit);
        if ((mask & value) != 0)
        {
            mask = 0xffffffffffffffffL << signBit;
            value = value | mask;
        }
        return value;
    }

    //pads either the left side or the right side of field with
    //the pad character so that the String returned is len characters
    public static String pad(String field, int len, String padChar,
                             Direction dir)
    {
        int i;
        String padding = padChar;
        int count = len - field.length();
        for (i = 0; i < count - 1; i++) padding = padding + padChar;
        if (count != 0 && dir == Direction.LEFT) field = field + padding;
        if (count != 0 && dir == Direction.RIGHT) field = padding + field;
        return field;
    }
}

