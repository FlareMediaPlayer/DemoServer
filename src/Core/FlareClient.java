/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import Network.WebSocket;
import Network.WebSocketMessage;
import Network.WebSocketParser;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


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
        
        while (running) {
            try {
                

                WebSocketMessage message = clientSocket.getMessage();
                System.out.println(message.getText() + "\n");
                
                
                try{
                String aMessage = "hello";
                WebSocketParser.send(outputStream, aMessage.getBytes(StandardCharsets.US_ASCII));
                }catch(Exception e){
                    System.out.println("could not send message back");
                }
 
          

            } catch (IOException e) {

            }

        }
    }

}
