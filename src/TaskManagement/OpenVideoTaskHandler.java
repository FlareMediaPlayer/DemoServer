/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TaskManagement;


import FlareMessage.OpenVideoMessage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author mac
 */
public class OpenVideoTaskHandler extends Task{
    
    String testFile = "file.mp4";
    
    @Override
    public void process(){
        
       OpenVideoMessage responseMessage = new OpenVideoMessage();
        System.out.println("do stuff");
        //Process link to make sure valid
        
        //Check if file exists
        if(fileAvailable()){
            
            //If exists return meta data and start a video manager
            
        }else{
            
            //If not message back saying that its not good
            
            responseMessage.setVideoAvailability(false);
            
           try {
               System.out.println(responseMessage.toBinary().length);
               flareClient.sendBinaryData(responseMessage.toBinary());
               
           } catch (IOException ex) {
               
               System.out.println(ex.toString());
           }
           
            
        }
        
        
        
        
        
    }
    
    
    private boolean fileAvailable(){
        
        File fileToOpen = new File(testFile);
        
        return fileToOpen.isFile();
        
    }
    
}
