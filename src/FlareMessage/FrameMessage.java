/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlareMessage;

import static FlareMessage.FlareMessage.HEADER_LENGTH;
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
 * This class adds the header to the frame data and adds the entire message to a
 * byte array.
 *
 * @author Sapan
 */
public class FrameMessage extends FlareMessage {

    //private Frame frame;
    private BufferedImage frame;
    protected ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    private int index;

    /**
     * Sets the appropriate OpCode for FrameMessage.
     */
    public FrameMessage() {
        flareOpCode = FlareOpCode.FRAME;
        //System.out.println("frame code is " + flareOpCode);
    }

    /**
     * Sets frame to bufferedImage.
     *
     * @param frame
     */
    public void setFrame(BufferedImage frame) {

        this.frame = frame;

    }

    /**
     * Sets index.
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * It adds the header (messageLength and OpCode) to the video data, sends
     * the entire message to a byte array and returns the byte array.
     *
     * @return
     */
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
        messageLength = dataLength + HEADER_LENGTH;

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
