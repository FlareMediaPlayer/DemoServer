/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlareMessage;

import FlareProtocol.FlareOpCode;
import Utility.ByteUtils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author mac
 */
public class FrameMessage  extends FlareMessage{
    
    //private Frame frame;
    private BufferedImage frame;
    protected ByteArrayOutputStream byteStream = new ByteArrayOutputStream(); 
    private int index;

    
    public FrameMessage(){
        flareOpCode = FlareOpCode.FRAME;
        //System.out.println("frame code is " + flareOpCode);
    }
    
    // sets frame to bufferedImage
    public void setFrame(BufferedImage frame){
        
        this.frame = frame;
        
    }
    

    
    public void setIndex(int index){
        this.index = index;
    }
    
    @Override
    public byte[] toBinary() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(frame, "jpg", baos);
        } catch (IOException ex) {
            Logger.getLogger(FrameMessage.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] frameBytes = baos.toByteArray();

        dataLength = 4 + frameBytes.length; //4 bytes for index + total binary length
        messageLength =  dataLength + HEADER_LENGTH;
         
           
        
        try {
            byteStream.reset(); 
            byteStream.write(ByteUtils.intToByteArray(messageLength)); // total length   
            byteStream.write(flareOpCode);//op code
            byteStream.write(ByteUtils.intToByteArray(index));//frame number
            byteStream.write(frameBytes);
            byteStream.flush();
            
            
   
            
         
        } catch (IOException ex) {
            Logger.getLogger(FrameMessage.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }

        
        
       return byteStream.toByteArray();
    }
    
}
