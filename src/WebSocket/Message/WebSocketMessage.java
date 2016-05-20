package WebSocket.Message;

import WebSocket.WebSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *  Abstract class for interfacing with websocket. Used as a wrapper for received data
 * @author Brian Parra
 */
public abstract class  WebSocketMessage {
    

        public byte opCode;
        
        /**
         * Sets the type of data received
         * @param code the websocket op code, use text or binary
         */
        public void setOpcode(byte code){
            opCode = code;
        }
        
        /**
         * Returns the type of data being received
         * @return current op code
         */
        public byte getOpcode(){
            
            return opCode;
            
        }
        

    
}



