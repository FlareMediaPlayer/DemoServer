package WebSocket.Message;

/**
 * Wrapper for binary data to be sent over websocket
 * @author Brian Parra
 */
public class WebSocketBinaryMessage extends WebSocketMessage{
    
    private byte[] data;
    
    /**
     * Returns the raw bye array
     * @return raw binary data
     */
    public byte[] getData(){
        return data;
    }
    
    /**
     * Sets the binary data with a byte array
     * @param data binary data to send
     */
    public void setData(byte[] data){
        
        this.data = data;
        
    }
}
