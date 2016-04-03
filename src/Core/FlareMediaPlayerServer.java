package Core;

import WebSocket.WebSocket;
import WebSocket.WebSocketServer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author csc 668/868 Team #2 Starting point to run the server.
 */
public class FlareMediaPlayerServer {

    private WebSocketServer serverSocket;
    private boolean running = true;
    private final ExecutorService threadPool;
    private Map<String, FlareClient> clientThreads = new HashMap<String, FlareClient>(); // Session ID -> Client



    // Singleton Instance
    private static FlareMediaPlayerServer mediaServer;
    //BufferedImage
    /**
     * For now
     */

    
    public FlareMediaPlayerServer(){
        
        configure();
        threadPool = Executors.newCachedThreadPool();
        
    }
    
    private void run() {
        try {
            // Open a connection using the given port to accept incoming connections
            serverSocket = new WebSocketServer(6661);
            System.out.println("Running server on port 6661");

            // Loop indefinitely to establish multiple connections
            while (running) {
                
                try {
                    // Accept the incoming connection from client
                    
                    WebSocket clientSocket = serverSocket.accept();
                    
                    
                    
                    String sessionToken = "asdasd"; //Put the actual token the client sent 
                    
                    FlareClient client = new FlareClient(sessionToken, clientSocket);
                    
                    addToActiveThreads(client);
                    // Initiate the client
                    threadPool.submit(client);
                 
                
                    
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void configure(){
        //Load configuration stuff here
    }
    
    public void addToActiveThreads(FlareClient client) {
        
        clientThreads.put(client.getId(), client);
        
    }


    public static FlareMediaPlayerServer getInstance() {

        if (mediaServer == null) {

            mediaServer = new FlareMediaPlayerServer();
        }

        return mediaServer;
    }

    public static void main(String[] args) {

        try {
            System.out.println("Initializing Server");

            mediaServer = new FlareMediaPlayerServer();
            mediaServer.run();

        } catch (Exception ex) {

        }
    }

}
