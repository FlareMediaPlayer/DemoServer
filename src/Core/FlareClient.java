/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import WebSocket.WebSocket;
import WebSocket.Message.WebSocketMessage;
import WebSocket.Message.WebSocketTextMessage;
import WebSocket.WebSocketParser;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author josesfval
 */
public class FlareClient implements Runnable {

    // Client Variables
    private String sessionId;
    private WebSocket clientSocket;
    private InputStream inputStream;
    protected OutputStream outputStream;
    private DataInputStream dataInputStream;
    private boolean running = true;
    BufferedReader in;
    
    
    //This is our table to look up handlers for each WebSocketMessage
    private static final Map<Byte, Class> messageTable = initializeTable();

    private static Map<Byte, Class> initializeTable() {

        Map<Byte, Class> table = new HashMap<Byte, Class>();

        try {

            //table.put(OP_CODE.CONTINUATION, Class.forName("ProcessContinuationFrame"));
            table.put(WebSocket.OP_CODE.TEXT, FlareClient.TextMessageHandler.class);
            table.put(WebSocket.OP_CODE.BINARY, FlareClient.BinaryMessageHandler.class);
            //table.put(WebSocket.OP_CODE.CLOSE, WebSocket.ProcessCloseFrame.class);
            //table.put(WebSocket.OP_CODE.PING, WebSocket.ProcessPingFrame.class);
            //table.put(WebSocket.OP_CODE.PONG, WebSocket.ProcessPongFrame.class);

        } catch (Exception e) {

            System.out.println("couldnt add " + e.getMessage());
        }

        return Collections.unmodifiableMap(table);

    }

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
                
                WebSocketMessageHandler messageHandler = null;
                WebSocketMessage message = clientSocket.getMessage();
                Class messageClass = FlareClient.messageTable.get(message.getOpcode());
                
                try {

                    messageHandler = (FlareClient.WebSocketMessageHandler) messageClass.getConstructor(FlareClient.class).newInstance(this);
                    messageHandler.initialize(message);
                    messageHandler.process();

                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(WebSocket.class.getName()).log(Level.SEVERE, null, ex);
                }

                
                
               
                

            } catch (IOException e) {

            }

        }
    }
    
    
    
    
    public abstract class WebSocketMessageHandler {
        protected WebSocketMessage message;
        
        
        public abstract void process();
        
        public void initialize(WebSocketMessage _message){
            message = _message;
        }
    }
    
    public class TextMessageHandler extends WebSocketMessageHandler{
        
        
        //Put all logic for handling a text message in here.
        public void process(){
            System.out.println(((WebSocketTextMessage) message).getText() + "\n");
            
            try {
                
                    String aMessage = "{\"test\"  : \"hello\" }";
                    WebSocketParser.send(FlareClient.this.outputStream, aMessage.getBytes(StandardCharsets.US_ASCII));
                } catch (Exception e) {
                    System.out.println("could not send message back");
                }
            
        }
        
        
    }
    
    public class BinaryMessageHandler extends WebSocketMessageHandler{
        
        //Put all logic for handling a binary message here
        public void process(){
          
        }
        
    }
    
    
    

}
