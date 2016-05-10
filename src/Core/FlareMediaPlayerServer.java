package Core;

import WebSocket.WebSocket;
import WebSocket.WebSocketServer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author csc 668/868 Team #2 Starting point to run the server.
 */
public class FlareMediaPlayerServer {

    private WebSocketServer serverSocket;
    private boolean running = false;
    private boolean auth = false;
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
            running = true;
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
                    running = false;
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
    
    //hashmap each client with an ID when the client is active
    public void addToActiveThreads(FlareClient client) {
        
        clientThreads.put(client.getId(), client);
        
    }
    
    public boolean getAuthStatus ()
    {
        return this.auth;
    }
    
    public FlareMediaPlayerServer getInstance() {

        if (mediaServer == null) {

            mediaServer = new FlareMediaPlayerServer();
        }

        return mediaServer;
    }
    
    public boolean isServerRunning (int port)
    {
        boolean isRunning = true;
        try { 
            serverSocket = new WebSocketServer(port);
            isRunning = false;
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return isRunning;
    }
    
    public void stopServer (int port) throws IOException
    {
        try { 
            serverSocket = new WebSocketServer(port);
            serverSocket.close();
            System.out.println("Server Stopped");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    public void runFlareMediaPlayerServer(String admin, String password) {

        try {
            
            if (FlareMediaServerAuthentificator.
                serverAuthentification(admin,
                                      password))
            {
                System.out.println("Initializing Server");
                mediaServer = new FlareMediaPlayerServer();
                mediaServer.run();
            }
            else
            {
                this.running = false;
                this.auth = false; 
                System.out.println("Server failed to run because of bad password");
            }

        } catch (Exception ex) {
            this.running = false;
            System.out.println(ex.getMessage());
        }
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
