/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebSocket.Message;

/**
 *
 * @author mac
 */
public class WebSocketBinaryMessage extends WebSocketMessage{
    
    private byte[] data;
    
    public byte[] getData(){
        return data;
    }
    
    public void setData(byte[] data){
        
        this.data = data;
        
    }
}
