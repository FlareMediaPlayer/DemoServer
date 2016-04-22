package SendFrameTask;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ListIterator;
import javax.websocket.EncodeException;
 
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import Video.Frame;
import Video.VideoParser;
 
/** 
 * @ServerEndpoint gives the relative name for the end point
 * This will be accessed via ws://localhost:8080/EchoChamber/echo
 * Where "localhost" is the address of the host,
 * "EchoChamber" is the name of the package
 * and "echo" is the address to access this class from the server
 */
@ServerEndpoint("/echo") 
public class FrameTransmission {
    /**
     * @return 
     * @throws java.io.IOException
     * @throws org.jcodec.api.JCodecException
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was 
     * successful.
     */
    public Frame getVideoFrame() throws IOException, JCodecException
    {
    	/*VideoParser vp = new VideoParser(new File("C:\\Users\\Tai\\Downloads\\FlareVideoParser\\FlareVideoParser\\bunny.mp4"));
    	ArrayList <Frame>  al = vp.getAllFrames();
        ListIterator<Frame> frames = al.listIterator();
       // while(frames.hasNext()){
        String s = "";
        Frame f = al.get(i);
        byte [] b = f.getData();
        ByteBuffer buf = ByteBuffer.wrap(b);
        //}
        return buf;*/
        String filename = "C:\\Users\\Tai\\Downloads\\FlareVideoParser\\FlareVideoParser\\bunny.mp4";
        Picture p = FrameGrab.getNativeFrame(new File(filename), 1);
        Frame frame = new Frame( p , 1);       
        //byte [] b = f.getData();
        return frame;
    }
    @OnOpen
    public void onOpen(Session session) throws JCodecException{
        System.out.println(session.getId() + " has opened a connection"); 
        try {
            session.getBasicRemote().sendText("Connection Established");
            
        } catch (IOException ex) {
        }
    }
 
    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     * @param message
     * @param session
     * @throws org.jcodec.api.JCodecException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws JCodecException, EncodeException{
        System.out.println("Message from " + session.getId() + ": " + message);
        try {
            session.getBasicRemote().sendObject(getVideoFrame().getWidth());
            session.getBasicRemote().sendObject(getVideoFrame().getHeight());
            session.getBasicRemote().sendObject(getVideoFrame().getData().toString());
           // websocket.sendBinaryData();
        } catch (IOException ex) {
        }
    }
 
    /**
     * The user closes the connection.
     * 
     * Note: you can't send messages to the client from this method
     * @param session
     */
    @OnClose
    public void onClose(Session session){
        System.out.println("Session " +session.getId()+" has ended");
    }
}