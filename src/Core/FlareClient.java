/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import TaskManagement.FlareOpCode;
import TaskManagement.Task;
import TaskManagement.TaskTable;
import Video.VideoManager;
import WebSocket.WebSocket;
import WebSocket.Message.WebSocketMessage;
import WebSocket.Message.WebSocketTextMessage;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


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
    private Queue<Task> taskeQueue;
    
    //Keep a map of active video managers for each client (each client can have more than one video per page)
    private Map<Integer, VideoManager> videoManagers;
    
    
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
        
        /**
         * Make an array of images
         */

    }

    public String getId() {
        return sessionId;
    }

    public void sendBinaryData(byte[] data) throws IOException{
        System.out.println("data length is  " +  data.length);
        clientSocket.sendBinaryData(data);
        
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
    
    private void addVideoManager(){
        
        int videoManagerId = 0;//videoManagers.size();
            
        videoManagers.put(videoManagerId, new VideoManager(this));

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
            
            try {
                
                Task task = (Task) TaskTable.taskTable.get(FlareOpCode.OPEN_VIDEO).newInstance();
                task.setMessage(message);
                task.setFlareClient(FlareClient.this);
                task.process();
                
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(FlareClient.class.getName()).log(Level.SEVERE, null, ex);
                
                //Invalid task op code
            }
            
            //TEMPORARY TO TRY TO SEND IMAGE AND JSON
            JSONParser parser = new JSONParser();
            System.out.println(((WebSocketTextMessage) message).getText() + "\n");
            BufferedImage img = null;
            String test = "asd";
            
            try {
                
                Object obj = parser.parse( ((WebSocketTextMessage) message).getText() );
                JSONObject jsonObject = (JSONObject) obj;
                
                if(jsonObject.containsKey("opCode")){
                    System.out.println(jsonObject.get("opCode"));
                    //make sure that data is okay then add to process queue
                    
                }else{
                    System.out.println("message is suspisious, drop the client");
                }
                
                
            } catch (ParseException ex) {
                Logger.getLogger(FlareClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        /*
            try {
                    
                    String aMessage = "{\"videoData\"  : \"hellos\" }";
                    
                    //FlareClient.this.clientSocket.sendTextData(aMessage);
                    //img = ImageIO.read(new File("testVideo/frame000.jpg"));
                    //img = ImageIO.read(new File("Lab.png"));
                    
                    
                    //PUT AN ACTUAL IMAGE YOU HAVE ON YOUR COMPUTER HERE
                    byte[] imgBytes = Files.readAllBytes(new File("testBmp/SampleVideo_1280x720_2mb000.bmp").toPath());
                    //byte[] imgBytes = Files.readAllBytes(new File("Lab.png").toPath());
                    
                    //byte[] hello = aMessage.getBytes(StandardCharsets.US_ASCII);
                    //new byte[65535]
                    FlareClient.this.clientSocket.sendBinaryData(imgBytes);
            
                    
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            */
        }
        
        //END TESTING
    }
    
    public class BinaryMessageHandler extends WebSocketMessageHandler{
        
        //Put all logic for handling a binary message here
        public void process(){
          
        }
        
    }
    
    
    

}
