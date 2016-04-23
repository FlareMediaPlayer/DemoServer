/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Video;

import Core.FlareClient;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 *
 * @author mac
 */
public class VideoManager {

    private FlareClient flareClient;
    private File videoFile;
    private int id;
    
    //Temporary stuff
    byte[] imgBytes; 
    
    
    public VideoManager(FlareClient flareClient){

        this.flareClient = flareClient;
        
    }
    
    public void loadVideo(String path) throws IOException{
        
        this.videoFile = new File(path);
        //this.imgBytes = Files.readAllBytes(this.videoFile.toPath());
        
    }
    
    //Temporary for testing
    public byte[] getVideoByteArray(){
        
        return imgBytes;
        
    }
    
    public int getId(){
        
        return id;
    }
    
}
