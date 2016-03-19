package Core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author csc 668/868 Team #2 Starting point to run the server.
 */
public class FlareMediaPlayerServer {

    private ServerSocket serverSocket;
    private boolean running = true;

    // Singleton Instance
    private static FlareMediaPlayerServer mediaServer;

    private void run() {
        try {
            // Open a connection using the given port to accept incoming connections
            serverSocket = new ServerSocket(6666);
            System.out.println("Running server on port 6666");

            // Loop indefinitely to establish multiple connections
            while (running) {
                try {
                    // Accept the incoming connection from client
                    Socket clientSocket = serverSocket.accept();
                    System.out.println(clientSocket.getInetAddress().getHostAddress() + " is connecting");

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
