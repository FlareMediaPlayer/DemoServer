/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlareMessage;

import FlareProtocol.FlareOpCode;
import Utility.ByteUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mac
 */
public class AudioMessage extends FlareMessage{
    byte[] audioData;
    protected ByteArrayOutputStream byteStream;
    
    public AudioMessage(){
        
         byteStream = new ByteArrayOutputStream(); 
         flareOpCode = FlareOpCode.AUDIO;
        
    }
        
    public void setAudioPath(String path) throws IOException{
        
        audioData = Files.readAllBytes(Paths.get(path));
        
    }
    
    @Override
    public byte[] toBinary() {
        dataLength = 4 + audioData.length; //4 bytes for index + total binary length
        messageLength =  dataLength + HEADER_LENGTH;
        
        try {
            
            byteStream.write(ByteUtils.intToByteArray(messageLength)); // total length   
            byteStream.write(flareOpCode);//op code
            byteStream.write(audioData);
            
        } catch (IOException ex) {
            Logger.getLogger(AudioMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
            
         
        
        return byteStream.toByteArray();
    }
    
}
