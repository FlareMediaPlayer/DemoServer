/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlareMessage;

import static FlareMessage.FlareMessage.HEADER_LENGTH;
import FlareProtocol.FlareOpCode;

/**
 * This class adds the header to the dataLength. The header contains the
 * messageLength and OpCode. It then adds the entire message to a byte array.
 *
 * @author Sapan
 */
public class OpenVideoMessage extends FlareMessage {

    boolean videoIsAvailable;
    int height;
    int width;
    double fps;
    double duration;
    int frameCount;

    /**
     * This method sets the video availability to default i.e. false and sets
     * the appropriate OpCode.
     */
    public OpenVideoMessage() {

        videoIsAvailable = false;
        flareOpCode = FlareOpCode.OPEN_VIDEO;

    }

    /**
     * Sets actual video availability.
     *
     * @param available
     */
    public void setVideoAvailability(boolean available) {

        videoIsAvailable = available;

    }

    /**
     * Sets video height.
     *
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Sets video width.
     *
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets video frames per second.
     *
     * @param fps
     */
    public void setFps(double fps) {
        this.fps = fps;
    }

    /**
     * Sets video duration.
     *
     * @param duration
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Sets the number of frames in the video.
     *
     * @param frameCount
     */
    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    /**
     * Checks if the video is available. If yes, it adds the header
     * (messageLength and OpCode) to the video data and sends the entire message
     * to a byte array and returns the byte array.
     *
     * @return
     */
    @Override
    public byte[] toBinary() {
        byte[] data = null;

        if (!videoIsAvailable) {

            dataLength = 1;
            messageLength = dataLength + HEADER_LENGTH;
            data = new byte[messageLength];

            //Put Message Length
            FlareMessage.intToData(data, 0, messageLength);
            data[4] = flareOpCode;

            //Followed by (byte) 0
            data[5] = 0;
            //System.out.println(data[5]);

        } else {

            dataLength = 29;
            messageLength = dataLength + HEADER_LENGTH;
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
