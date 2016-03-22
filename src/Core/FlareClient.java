/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import Network.WebSocket;
import Network.WebSocketParser;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 *
 * @author josesfval
 */
public class FlareClient implements Runnable {

    // Client Variables
    private String sessionId;
    private WebSocket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private DataInputStream dataInputStream;
    private boolean running = true;
    BufferedReader in;

    public FlareClient(String sessionId, WebSocket clientSocket) throws IOException {
        this.sessionId = sessionId;
        this.clientSocket = clientSocket;

        inputStream = clientSocket.getInputStream();
        outputStream = clientSocket.getOutputStream();
        dataInputStream = new DataInputStream(inputStream);
        in = new BufferedReader(new InputStreamReader(inputStream));

    }

    public String getId() {
        return sessionId;
    }

    @Override
    public void run() {
        byte c;
        int opCode;
        byte mask[] = new byte[4];
        byte data[];
        int dataLength;
        int maskBit;
        
        while (running) {
            try {

                c = dataInputStream.readByte(); 
     
                opCode = c & 0x0f; 
                System.out.println("is Final :" +WebSocketParser.getBit(c, 7));
                System.out.println("Op Code: "+ opCode);
                
                c = dataInputStream.readByte();
                maskBit = (c >> 7) & 0x1;
                
                dataLength = c & 0x7f;
                System.out.println("Mask Bit is :" + maskBit);
                System.out.println("Data Length is :" + dataLength);
                
                
                mask[0] = dataInputStream.readByte();
                mask[1] = dataInputStream.readByte();
                mask[2] = dataInputStream.readByte();
                mask[3] = dataInputStream.readByte();
                
                data = new byte[dataLength];
                int maskIndex;
                for(int n = 0; n < dataLength; n++){
                    maskIndex = n % 4;
                    data[n] = (byte) (dataInputStream.readByte() ^ mask[maskIndex]);
                }
                String test = new String(data);
                System.out.println("Data is :" + test);
                System.out.println();
                
                try{
                String aMessage = "hello";
                WebSocketParser.send(outputStream, aMessage.getBytes(StandardCharsets.US_ASCII));
                }catch(Exception e){
                    System.out.println("could not send message back");
                }
 
          

            } catch (Exception e) {

            }

        }
    }

}
