/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlareTask;



import Core.FlareClient;
import WebSocket.Message.WebSocketMessage;

/**
 *
 * @author mac
 */
public abstract class FlareTask {
    
    protected WebSocketMessage message;
    
    protected int dataLength;
    
    protected FlareClient flareClient;
    
    public void setMessage(WebSocketMessage message){
        
        this.message = message;
 
    }
    
    public void setFlareClient(FlareClient flareClient){
        
        this.flareClient = flareClient;
       
        
    }
    
    public abstract void process();
}
