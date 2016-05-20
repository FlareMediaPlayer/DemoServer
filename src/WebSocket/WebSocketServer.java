package WebSocket;


import java.io.IOException;
import java.net.ServerSocket;


/**
 *  Extension of Server Socket to accept incoming WebSocket type connections
 * @author Brian Parra
 */
public class WebSocketServer extends ServerSocket {

    /**
     * Constructor, just needs port number
     * @param port active port number
     * @throws IOException 
     */
    public WebSocketServer(int port) throws IOException {
        super(port);
    }

    /**
     * the accept method is required to complete the websocket handshake.
     * the handshake authentication.
     * @return the new websocket connection after authenticating
     * @throws IOException 
     */
    public WebSocket accept() throws IOException {
        
        WebSocket webSocket = WebSocket.create();
        
        implAccept(webSocket);
        
        //Perform the websocket handshake response
        webSocket.handshake();
        
      
        webSocket.initialize();   
       
        
        return webSocket;
    }
    

}
