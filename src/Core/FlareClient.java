package Core;

import FlareProtocol.FlareOpCode;
import FlareTask.FlareTask;
import FlareProtocol.TaskTable;
import WebSocket.Message.WebSocketBinaryMessage;
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





/*
* @author: Jose Ortiz and Brian Parra andTai Nguyen
* date:    05/16/2016
*          Upon the connection, this class handles the information sent from client side.
*          It manipulates and initialize necessary data in order to keep up with all updates/actions from client.
*          Then such actions will interpreted as request message and 
*          server will respond to it through the websocket mechanism.
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
    private Queue<FlareTask> taskeQueue;

    //This is our table to look up handlers for each WebSocketMessage
    private static final Map<Byte, Class> messageTable = initializeTable();

    /**
     * Sets up table for websocket op code types
     *
     * @return table with processor classes
     */
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


    /**
     * Constructor
     *
     * @param sessionId used to id clients
     * @param clientSocket socket being used
     * @throws IOException if io cannot be established
     */

    public FlareClient(String sessionId, WebSocket clientSocket) throws IOException {
        this.sessionId = sessionId;
        this.clientSocket = clientSocket;

        inputStream = clientSocket.getInputStream();
        outputStream = clientSocket.getOutputStream();
        dataInputStream = new DataInputStream(inputStream);
        in = new BufferedReader(new InputStreamReader(inputStream));

    }

    /**
     * Get session ID
     *
     * @return sessionID
     */
    public String getId() {
        return sessionId;
    }

    /**
     * Sends binary data over websocket
     *
     * @param data byte array to send
     * @throws IOException if cannot write to output
     */
    public void sendBinaryData(byte[] data) throws IOException {
        //System.out.println("data length is  " +  data.length);
        clientSocket.sendBinaryData(data);

    }

    /**
     * Main run loop. Listen for messages here. Use opcode to initialize
     * appropriate processor classes
     */
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

    /**
    *  An abstract object to interpret message from client
    *  The implementation for the abstract method is required 
    *  for each task. (Ex: BinaryMessageHandler is a task of sending a video)
    */

    public abstract class WebSocketMessageHandler {

        protected WebSocketMessage message;

        /**
         * Process the message
         */
        public abstract void process();

        /**
         * Initializer to add the message needed
         *
         * @param _message message to process
         */
        public void initialize(WebSocketMessage _message) {
            message = _message;
        }
    }

    /**
     * Handles incoming text data
     */
    public class TextMessageHandler extends WebSocketMessageHandler {

        /**
         * Not needed for now since using binary only
         */
        public void process() {
            //Do Nothing, not using text 
        }

        //END TESTING
    }


    /**
     * Handles binary incoming messages
     */

    public class BinaryMessageHandler extends WebSocketMessageHandler {

        /**
         * Processes the binary message here
         */
        public void process() {

            //System.out.println();
            byte flareOpCode = ((WebSocketBinaryMessage) message).getData()[0];

            System.out.println("flare op code is + " + flareOpCode);

            try {
                FlareTask task = (FlareTask) TaskTable.taskTable.get(flareOpCode).newInstance();
                task.setMessage(message);
                task.setFlareClient(FlareClient.this);
                task.process();

            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(FlareClient.class.getName()).log(Level.SEVERE, null, ex);

                //Invalid task op code
            }

        }

    }

}
