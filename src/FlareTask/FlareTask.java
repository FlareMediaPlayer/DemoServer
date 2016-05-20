package FlareTask;



import Core.FlareClient;
import WebSocket.Message.WebSocketMessage;

/**
 * Abstract class for handlers for each op code
 * @author Brian Parra
 */
public abstract class FlareTask {
    
    protected WebSocketMessage message;
    
    protected int dataLength;
    
    protected FlareClient flareClient;
    
    /**
     * Sets the message to process
     * @param message process this message
     */
    public void setMessage(WebSocketMessage message){
        
        this.message = message;
 
    }
    
    /**
     * Reference to it's client 
     * @param flareClient the client thread
     */
    public void setFlareClient(FlareClient flareClient){
        
        this.flareClient = flareClient;
       
        
    }
    
    /**
     * Abstract method to process the flare message
     */
    public abstract void process();
}
