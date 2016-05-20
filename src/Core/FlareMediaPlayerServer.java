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
 * Main Server initialization Team #2 Starting point to run the server.
 * @author      Jimmy He
 * Date         04/20/2016
 * Modified     05/12/2016
 * Package      Core
 * File         FlareMediaPlayerServer.java
 * Description  The FlareMediaPlayerServer is the listener for client
 *              connections. Upon receiving successful connections, a
 *              new WebSocket and session ID will be made and given to a
 *              new instance of FlareClient. This FlareClient will then 
 *              handle any further communication of the connected client.
 *              The FlareMediaPlayerServer will add the FlareClient to a
 *              local thread pool and continue listening for new client
 *              connections.
 *              
 * See:         FlareClient class, FlareMediaServerAuthentificator class,
 *              FlareMediaPlayerServerAdminInterface JSP unit.
 *
 * Usage:       This server is started by the FlareMediaPlayerServerAdminInterface.
 *              But it requires the proper login credentials. However this can be
 *              started by simply running it as the main method is left for testing.
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

    /**
     * <p>Constructor of FlareMediaPlayerServer but is to be used
     * by a local static instance for Singleton. This initializes
     * server configurations and a thread pool for remote clients.</p>
     */
    public FlareMediaPlayerServer(){
        

        configure();
        threadPool = Executors.newCachedThreadPool();

    }


    /**
     * Main run loop. Listen for connections here
     * <p>Runs in an endless loop to listen for new incoming client connections.
     * Upon successful connection, it will create a representative FlareClient
     * and pass in a WebSocket along with a session ID. The FlareClient is then 
     * added to a thread pool and will handle any future communication 
     * with its respective client on its own.</p>
     */
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

    
    /**
     * <p>Activate the configurations for the server.</p>
     */
    public void configure(){

        //Load configuration stuff here
    }


    /**
     * Adds new client to threads
     *
     * <p>Add the FlareClient to the local thread pool in this server.</p>
     * @param client FlareClient to be added to the local thread pool.
     */
    public void addToActiveThreads(FlareClient client) {

        clientThreads.put(client.getId(), client);

    }


    
    /**
     * <p>Get the authorization status of this server.</p>
     * @return boolean - true for authorized use, false for denied access.
     */
    public boolean getAuthStatus ()
    {

        return this.auth;
    }


    /**
     * Gets singleton instance
     *
     * <p>Retrieve the static instance of this FlareMediaPlayerServer.</p>
     * @return FlareMediaPlayerServer static instance.
     */
    public FlareMediaPlayerServer getInstance() {

        if (mediaServer == null) {

            mediaServer = new FlareMediaPlayerServer();
        }

        return mediaServer;
    }




    
    /**
     * <p>Check if an instance of the server is already running.</p>
     * @param port the port used for testing..
     * @return boolean - true if the server is running, false for is not.
     */
    public boolean isServerRunning (int port)
    {

        boolean isRunning = true;
        try {
            serverSocket = new WebSocketServer(port);
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            isRunning = false;
        }
        return isRunning;
    }

    
    /**
     * <p>Stop this instance of the server.</p>
     * @param port integer of the port number that is being used for connections.
     * @throws IOException if server can't stop
     */
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

    
    /**
     * <p>Initialize and start the server. This will require an administrator 
     * account name and password.</p>
     * 
     * @param admin string of the admin user name.
     * @param password string of the admin password.
     */
    public void runFlareMediaPlayerServer(String admin, String password) {

        try {

            if (FlareMediaServerAuthentificator.
                    serverAuthentification(admin,
                            password)) {
                System.out.println("Initializing Server");
                mediaServer = new FlareMediaPlayerServer();
                mediaServer.run();
            } else {
                this.running = false;
                this.auth = false;
                System.out.println("Server failed to run because of bad password");
            }

        } catch (Exception ex) {
            this.running = false;
            System.out.println(ex.getMessage());
        }
    }

    
    /**
     * <p>Main method here is left for testing purposes only. During deployment
     * the server is activated through the FlareMediaPlayerServerAdminInterface unit.</p>
     * @param args command line args
     */
    public static void main(String[] args) {

        try {
            System.out.println("Initializing Server");

            mediaServer = new FlareMediaPlayerServer();
            mediaServer.run();

        } catch (Exception ex) {

        }
    }

}
