package FlareMessage;

import java.nio.ByteBuffer;

/**
 * Abstract base class for FlareMessages. Flare messages will be the main method
 * of client/server communication
 *
 * @author Brian Parra
 */
public abstract class FlareMessage {

    protected byte flareOpCode;

    protected int messageLength;
    protected int dataLength;

    protected final static int HEADER_LENGTH = 5;

    //Header is Opcode 1 byte
    //Followed by messageLength 1 int
    /**
     * Convert the message contents to binary array
     *
     * @return binary representation of message
     */
    public abstract byte[] toBinary();

    /**
     * Adds an int to a data array without copying
     *
     * @param data data array to add int to
     * @param offset position in array to add
     * @param input input integer
     */
    public static void intToData(byte[] data, int offset, int input) {
        data[offset] = (byte) (input >> 24);
        data[offset + 1] = (byte) (input >> 16);
        data[offset + 2] = (byte) (input >> 8);
        data[offset + 3] = (byte) input;
    }

    /**
     * Adds double to binary array at offset
     *
     * @param data data to add double to
     * @param offset integer offset of the array
     * @param input the double to add
     */
    public static void doubleToData(byte[] data, int offset, double input) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(input);

        for (int n = 0; n < 8; n++) {
            data[offset + n] = bytes[n];
        }
    }

}
