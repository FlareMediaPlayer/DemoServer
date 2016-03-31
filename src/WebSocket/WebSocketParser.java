/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebSocket;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author mac
 */
public class WebSocketParser {

    public static int getBit(byte input, int position) {
        return (input >> position) & 1;
    }
    
    public static void send(OutputStream outputStream, byte data[]) throws IOException{
        //Break data into Frames then write it to the output stream
        
        
        //This is only for a single frame right now. Need to finish to send variable length data
        //Also doesnt have mask on either
        
        byte opCode = 1;
        byte isFinal = (byte) (1 << 7);

        //mask bit is 0 for now
        byte dataLength = (byte) data.length;
 
        
        byte firstHeader = (byte) ((byte)opCode | (byte)isFinal);
        
    
        
        outputStream.write(firstHeader); //First write frame headers
        outputStream.write(dataLength); // 
        outputStream.write(data); // Here is frame data to send
 
    }

}
