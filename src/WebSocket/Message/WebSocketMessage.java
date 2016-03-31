/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebSocket.Message;

import WebSocket.WebSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mac
 */
public abstract class  WebSocketMessage {
    

        public byte opCode;
        
        public void setOpcode(byte code){
            opCode = code;
        }
        
        public byte getOpcode(){
            
            return opCode;
            
        }
        

    
}



