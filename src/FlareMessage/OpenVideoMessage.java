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
    int height;
    int width;
    double fps;
    double duration;
    int frameCount;

    
    public OpenVideoMessage() {
        
        videoIsAvailable = false;
        flareOpCode = FlareOpCode.OPEN_VIDEO;
        
    }
    
    public void setVideoAvailability(boolean available){
        
        videoIsAvailable = available;
        
    }
    
    public void setHeight(int height){
        this.height = height;
    }
    
    public void setWidth(int width){
        this.width = width;
    }
    
    public void setFps(double fps){
        this.fps = fps;
    }
    
    public void setDuration(double duration){
        this.duration = duration;
    }
    
    public void setFrameCount(int frameCount){
        this.frameCount = frameCount;
    }

    @Override
    public byte[] toBinary() {
       byte[] data = null;
       
       
       if(!videoIsAvailable){
           
           
           
           dataLength = 1;
           messageLength =  dataLength + HEADER_LENGTH;
           data = new byte[messageLength];
           
           //Put Message Length
           FlareMessage.intToData(data, 0, messageLength);
           data[4] = flareOpCode;
           
           
           
           
           //Followed by (byte) 0
           data[5] = 0;
           //System.out.println(data[5]);
           
           
           
       }else{
           
           dataLength = 29;
           messageLength =  dataLength + HEADER_LENGTH;
           data = new byte[messageLength];
           
           
           FlareMessage.intToData(data, 0, messageLength);
           data[4] = flareOpCode;
           
           //Followed by (byte) 0
           data[5] = 1;
           
           //width
           FlareMessage.intToData(data, 6, width);
           
           //height
           FlareMessage.intToData(data, 10, height);
           
           //fps
           FlareMessage.doubleToData(data, 14, fps);
           
           //duration
           FlareMessage.doubleToData(data, 22, duration);
           
           
           FlareMessage.intToData(data, 30, frameCount);
           
       }
       
       
       
       return data;       
    }
    
}
