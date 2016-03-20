package Core;
import org.apache.commons.codec.binary.Base64;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author csc 668/868 Team #2 Starting point to run the server.
 */
public class FlareMediaPlayerServer {

    private ServerSocket serverSocket;
    private boolean running = true;
    
    private String webSocketKey;
    private String GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    private String socketResponseKey;

    // Singleton Instance
    private static FlareMediaPlayerServer mediaServer;

    private void run() {
        try {
            // Open a connection using the given port to accept incoming connections
            serverSocket = new ServerSocket(6661);
            System.out.println("Running server on port 6661");

            // Loop indefinitely to establish multiple connections
            while (running) {
                try {
                    // Accept the incoming connection from client
                    Socket clientSocket = serverSocket.accept();
                    System.out.println(clientSocket.getInetAddress().getHostAddress() + " is connecting");

                    /**
                     *
                     * Just testing some data
                     */
                    InputStream stream = clientSocket.getInputStream();

                    BufferedReader in = new BufferedReader(new InputStreamReader(stream));

                    try {
                        // read the first line to get the request method, URI and HTTP version
                        String line = in.readLine();

                        while (line != null && line.trim().length() > 0) {
                            
                                StringTokenizer st = new StringTokenizer(line);
                                String label = st.nextToken();

                                if(label.equals("Sec-WebSocket-Key:")){
                                    webSocketKey = st.nextToken().trim();
                                }
                               
                
                            line = in.readLine();
                        }

                        socketResponseKey = Base64.encodeBase64String(DigestUtils.sha1(webSocketKey + GUID));

                        

                    } catch (IOException e) {
                        System.out.println("Error reading body");

                    }
                    
                    //Now We Respond!
                    BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
                    PrintWriter writer = new PrintWriter(out, true);  // char output to the client
                    
                    writer.println("HTTP/1.1 101 Switching Protocols");
                    writer.println("Upgrade: websocket");
                    writer.println("Connection: Upgrade");
                    writer.println("Sec-WebSocket-Accept: " + socketResponseKey);

                    writer.println("");

                    /**
                     * End Testing
                     */
                    
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
