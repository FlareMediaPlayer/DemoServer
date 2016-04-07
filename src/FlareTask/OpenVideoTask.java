/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlareTask;

import FlareMessage.OpenVideoMessage;
import WebSocket.Message.WebSocketBinaryMessage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mac
 */
public class OpenVideoTask extends FlareTask {

    String testFile = "file.mp4";
    String requestPath;

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

        byte requestPathLength = data[5];
        System.out.println("request path length is " + requestPathLength);

        //Now Read the path
        StringBuilder pathStringBuilder = new StringBuilder(requestPathLength);
        for (int c = 0; c < requestPathLength; c++) {
            pathStringBuilder.append(Character.toChars(data[c + 6]));

        }

        requestPath = new String(pathStringBuilder);
        System.out.println(requestPath);

        //Check if file exists
        if (fileAvailable()) {

            //If exists return meta data and start a video manager
            responseMessage.setVideoAvailability(true);

        } else {

            //If not message back saying that its not good
            responseMessage.setVideoAvailability(false);

        }

        
        
        try {

            flareClient.sendBinaryData(responseMessage.toBinary());

        } catch (IOException ex) {

            System.out.println(ex.toString());
        }
        
        

    }

    private boolean fileAvailable() {

        File fileToOpen = new File(requestPath);

        return fileToOpen.isFile();

    }

}
