package Video;

/**
 *
 * @author csc 668/868 Team #2
 * This class is just a prototype to start working. Some default parameters 
 * added here such as socket may be replaced later by any other functionality.
 */

import FrameMessage.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.Transform;

public class VideoParser 
{
	File videoFile;
	ArrayList <Frame> frames = new ArrayList <>();
	
	public VideoParser (File _videoFile)
    {
        this.videoFile = _videoFile; 
    }
	
	public ArrayList <Frame> getAllFrames () throws IOException, JCodecException
	{
		
		for (int i = 0; i<10; i++)
		{
			Picture pic = FrameGrab.getNativeFrame(this.videoFile, i);
		    frames.add(new Frame(pic,i));
		}
		return frames;
		
	}
	
}

