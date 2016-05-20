package Utility;

import java.nio.ByteBuffer;

/**
 * Helper utility class for converting to byte arrays
 * @author Brian Parra
 */
public class ByteUtils {
    
    /**
     * Converts an int to byte array
     * @param input integer to convert
     * @return binary representation of int
     */
    public static byte[] intToByteArray(int input){
        return ByteBuffer.allocate(4).putInt(input).array();
    }
    
    /**
     * Converts a short to binary
     * @param input short to convert
     * @return short in binary representation
     */
    public static byte[] shortToByteArray(short input){
        return ByteBuffer.allocate(2).putShort(input).array();
    }
    
}
