/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlareMessage;

import FlareProtocol.FlareOpCode;

/**
 *
 * @author mac
 */
public class OpenVideoMessage extends FlareMessage{
    
    boolean videoIsAvailable;

    
    public OpenVideoMessage() {
        
        videoIsAvailable = false;
        flareOpCode = FlareOpCode.OPEN_VIDEO;
        
    }
    
    public void setVideoAvailability(boolean available){
        
        videoIsAvailable = available;
        
    }

    @Override
    public byte[] toBinary() {
       byte[] data = null;
       
       
       if(!videoIsAvailable){
           
           
           
           dataLength = 1;
           messageLength =  dataLength + HEADER_LENGTH;
           data = new byte[messageLength];
           
           
           data[0] = flareOpCode;
           //Put Message Length
           FlareMessage.intToData(data, 1, dataLength);
           
           //Followed by (byte) 0
           data[5] = 0;
           //System.out.println(data[5]);
           
           
           
       }else{
           
           dataLength = 1;
           messageLength =  dataLength + HEADER_LENGTH;
           data = new byte[messageLength];
           
           
           data[0] = flareOpCode;
           //Put Message Length
           FlareMessage.intToData(data, 1, dataLength);
           
           //Followed by (byte) 0
           data[5] = 1;
           
       }
       
       
       
       return data;       
    }
    
}
