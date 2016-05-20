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
 *
 * @author csc 668/868
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
     * Constructor
     */
    public FlareMediaPlayerServer() {

        configure();
        threadPool = Executors.newCachedThreadPool();

    }

    /**
     * Main run loop. Listen for connections here
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
     * Left for reading configuration file later
     */
    public void configure() {
        //Load configuration stuff here
    }

    /**
     * Adds new client to threads
     *
     * @param client flareClient to add as thread
     */
    public void addToActiveThreads(FlareClient client) {

        clientThreads.put(client.getId(), client);

    }

    /**
     * Gets authorization status
     *
     * @return the authorization status
     */
    public boolean getAuthStatus() {
        return this.auth;
    }

    /**
     * Gets singleton instance
     *
     * @return global singleton Server
     */
    public FlareMediaPlayerServer getInstance() {

        if (mediaServer == null) {

            mediaServer = new FlareMediaPlayerServer();
        }

        return mediaServer;
    }

    /**
     * Checks if server is running on specific port
     *
     * @param port port to check
     * @return true if running
     */
    public boolean isServerRunning(int port) {
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

    /**
     * Stops server from running
     *
     * @param port to stop on
     * @throws IOException if cannotclose
     */
    public void stopServer(int port) throws IOException {
        try {
            serverSocket = new WebSocketServer(port);
            serverSocket.close();
            System.out.println("Server Stopped");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    /**
     * Starts the server running after authenticationg
     *
     * @param admin user name
     * @param password password
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
     * Main
     *
     * @param args commandline arguments
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
