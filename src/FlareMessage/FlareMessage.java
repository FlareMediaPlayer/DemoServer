/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlareMessage;

/**
 *
 * @author mac
 */
public abstract class FlareMessage {
    
    protected byte flareOpCode;
    
    protected int messageLength;
    protected int dataLength;
    
    protected final static int HEADER_LENGTH = 5;
    
    //Header is Opcode 1 byte
    //Followed by messageLength 1 int
    
    public abstract byte[] toBinary();
    
    /**
     * Adds an int to a data array without copying 
     * @param data data array to add int to
     * @param position position in array to add
     * @param input input integer
     */
    public static void intToData(byte[] data, int offset, int input){
        data[offset] = (byte) (input >> 24);
        data[offset + 1] = (byte) (input >> 16);
        data[offset + 2] = (byte) (input >> 8);
        data[offset + 3] = (byte) input;
    }
    
}
