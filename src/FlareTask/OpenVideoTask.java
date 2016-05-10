/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlareTask;

import FlareMessage.AudioMessage;
import FlareMessage.FrameMessage;
import FlareMessage.OpenVideoMessage;
import WebSocket.Message.WebSocketBinaryMessage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 *
 * @author mac
 */
public class OpenVideoTask extends FlareTask {

    String testFile = "sample.mp4";
    String requestID;
    List<String> metaData;
    int frameCount;

    private byte[] data;

    @Override
    public void process() {
        data = ((WebSocketBinaryMessage) message).getData();

        OpenVideoMessage responseMessage = new OpenVideoMessage();
        //Process link to make sure valid

        dataLength = data[4];
        dataLength = dataLength | data[3] << 8;
        dataLength = dataLength | data[2] << 16;
        dataLength = dataLength | data[1] << 32;
        System.out.println("datalength is + " + dataLength);

        byte requestIDLength = data[5];
        System.out.println("request ID length is " + requestIDLength);

        //Now Read the path
        StringBuilder pathStringBuilder = new StringBuilder(requestIDLength);
        for (int c = 0; c < requestIDLength; c++) {
            pathStringBuilder.append(Character.toChars(data[c + 6]));

        }

        requestID = new String(pathStringBuilder);
        System.out.println(requestID);

        //Check if file exists
        if (videoAvailable()) {

            
            try {
                metaData = Files.readAllLines(Paths.get(requestID + "/meta.txt"), Charset.forName("UTF-8"));
            } catch (IOException ex) {
                Logger.getLogger(OpenVideoTask.class.getName()).log(Level.SEVERE, null, ex);
            }

            frameCount = Integer.parseInt(metaData.get(0));
            int width = Integer.parseInt(metaData.get(1));
            int height = Integer.parseInt(metaData.get(2));
            double fps = Double.parseDouble(metaData.get(3));
            double duration = Double.parseDouble(metaData.get(4));
            System.out.println(duration);
        
                

            //If exists return meta data and start a video manager
            responseMessage.setVideoAvailability(true);
            responseMessage.setWidth(width);
            responseMessage.setHeight(height);
            responseMessage.setFps(fps);
            responseMessage.setDuration(duration);
            responseMessage.setFrameCount(frameCount);
            
            
            

        } else {

            //If not message back saying that its not good
            responseMessage.setVideoAvailability(false);

        }

        
        
        try {

            flareClient.sendBinaryData(responseMessage.toBinary());
            System.out.println("So far");
            
        } catch (IOException ex) {

            System.out.println(ex.toString());
        }
        
        
            /**
             * TEST CODE ONLY, THIS SHOULD GO IN FLARE CLIENT
             */
        if (videoAvailable()) {

            //If exists return meta data and start a video manager
            

            // ArrayList<BufferedImage> frameList = new ArrayList<BufferedImage>();
            
            //BufferedImage img = null;
            //
            
          
            
            try {
                // initializes video parser with the file to be parsed
   

                FrameMessage frameMessage = new FrameMessage(); 
   
                
                
                //Frame currentFrame = null;
                BufferedImage img = null;
                //NOW GET AUDIO
                AudioMessage audioMessage = new AudioMessage();
                audioMessage.setAudioPath(requestID +"/audio.m4a");
                flareClient.sendBinaryData(audioMessage.toBinary());
                
                // parse one frame at the time, and send its data to the client
                for(int n = 0; n < frameCount; n++){
                    //currentFrame = videoParser.getNextFrame(n); // current frame
                    //img = currentFrame.getBufferedImage(); // current buff image
                    img = ImageIO.read(new File(requestID + "/frame" + n + ".jpg"));
                    frameMessage.setFrame(img);
                    frameMessage.setIndex(n);
                    // sent data
                    flareClient.sendBinaryData(frameMessage.toBinary());
                    
                    
                }
                
                

                
            } catch (IOException ex ) {
                Logger.getLogger(OpenVideoTask.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
            }
 
            

        }
        
        

    }

    private boolean videoAvailable() {

        File fileToOpen = new File(requestID);

        return fileToOpen.isDirectory();

    }

}
