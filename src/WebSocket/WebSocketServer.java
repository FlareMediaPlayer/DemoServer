/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebSocket;


import java.io.IOException;
import java.net.ServerSocket;


/**
 *
 * @author mac
 */
public class WebSocketServer extends ServerSocket {

    
    public WebSocketServer(int port) throws IOException {
        super(port);
    }

    public WebSocket accept() throws IOException {
        
        WebSocket webSocket = WebSocket.create();
        
        implAccept(webSocket);
        
        //Perform the websocket handshake response
        webSocket.handshake();
        
      
        webSocket.initialize();   
       
        
        return webSocket;
    }
    

}
