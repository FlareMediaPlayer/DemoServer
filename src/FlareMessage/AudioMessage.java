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
 * Class that wraps audio data. Currently sends as single m4a
 *
 * @author Brian Parra
 */
public class AudioMessage extends FlareMessage {

    byte[] audioData;
    protected ByteArrayOutputStream byteStream;

    /**
     * Constructor
     */
    public AudioMessage() {

        byteStream = new ByteArrayOutputStream();
        flareOpCode = FlareOpCode.AUDIO;

    }

    /**
     * Sets the path of the audio file. Currently just reads entire m4a file
     *
     * @param path where the audio file is located
     * @throws IOException if cannot open audio file
     */
    public void setAudioPath(String path) throws IOException {

        audioData = Files.readAllBytes(Paths.get(path));

    }

    /**
     * Convert message contents to binary array
     *
     * @return binary array
     */
    @Override
    public byte[] toBinary() {
        dataLength = 4 + audioData.length; //4 bytes for index + total binary length
        messageLength = dataLength + HEADER_LENGTH;

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
