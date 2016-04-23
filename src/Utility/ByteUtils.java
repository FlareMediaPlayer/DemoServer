/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import java.nio.ByteBuffer;

/**
 *
 * @author mac
 */
public class ByteUtils {
    
    public static byte[] intToByteArray(int input){
        return ByteBuffer.allocate(4).putInt(input).array();
    }
    
    public static byte[] shortToByteArray(short input){
        return ByteBuffer.allocate(2).putShort(input).array();
    }
    
}
