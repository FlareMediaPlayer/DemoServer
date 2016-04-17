package Video;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;

/**
 * VideoParser takes in a file and parses the video into Frames.
 * WARNING: EXTREMELY SLOW. If testing then use a 3 second video or less to get
 *          a result in a 'reasonable' time.
 * 
 * @author Jimmy
 */
public class VideoParser implements Comparator<Frame>{

    private File videoFile;
    private int totalFrames;    //0 to totalFrames-1
    private ArrayList<Frame> frameList;
    
    
    public VideoParser(String file) throws FileNotFoundException, IOException{
            this(new File(file));
    }
    
    public VideoParser(File file) throws FileNotFoundException, IOException{
            videoFile = file;
            
            //need total frames; FrameGrab does not return null if time/index is beyond the video.
            totalFrames = (new MP4Demuxer(NIOUtils.readableFileChannel(videoFile)))
                                    .getVideoTrack().getMeta().getTotalFrames();
              
    }
    

    /**
     * Retrieve all the frames in the video file.
     * 
     * @throws IOException
     * @throws org.jcodec.api.JCodecException
     * @see getListOfFrames()
     * @return an ArrayList of Frames
     */
    public ArrayList<Frame> getAllFrames() throws IOException, JCodecException{
            return getListOfFrames();
    }
    
    /**
     * Retrieve all the frames in the video file from given time to end of video.
     * 
     * @throws Exception
     * @see getListOfFrames(double)
     * @param sec long : the time.
     * @return an ArrayList of Frames
     */
    public ArrayList<Frame> getAllFramesFromTime(int sec) throws Exception {
            return getListOfFrames((double)sec); 
    }
    
    public ArrayList<Frame> getAllFramesFromTime(double sec) throws Exception{
            return getListOfFrames(sec);
    }
    

    
    //private helper methods
    private ArrayList<Frame> getListOfFrames() throws IOException, JCodecException{
            
            if(frameList == null){
                
                    frameList = new ArrayList<Frame>();

                    for(int i = 0; i < totalFrames; i++){
                        
                        Picture p = FrameGrab.getNativeFrame(videoFile, i);
                        Frame frame = new Frame(p,i);
                        frameList.add(i,frame);
                        //System.out.println("added: " + i);

                    }
                
            }

            return frameList;
    }
    
    /**
     * Retrieves list of frames starting with the frame at the specified time,
     * but only if the frame is valid. The specified frame is only valid if its
     * data matches another frame that from a list of known real frames.
     * 
     * @throws Exception 
     * @param time double
     * @return ArrayList of Frames or null
     */
    private ArrayList<Frame> getListOfFrames(double time) throws Exception{
        
            ArrayList<Frame> realFrameList = getListOfFrames();
            ArrayList<Frame> returningList = null;

            Picture p = FrameGrab.getNativeFrame(videoFile, time);
            Frame verifyMe = new Frame(p,-1);
            
            //find out which real index this should the frame 'verifyMe' be given
            for(int i = 0; i < realFrameList.size(); i++){
                
                if( compare(realFrameList.get(i),verifyMe) == 1 ){
                    
                    returningList = new ArrayList<Frame>();
                    verifyMe = new Frame(p,i);
                    
                    returningList.add(verifyMe);
                    realFrameList.addAll(i+1, returningList);
                    
                    break;
                    
                }
                
            }
            
            //if returningList is null then the given time produced an invalid frame.
            if(returningList == null)
                    throw new Exception("Invalid time/ Invalid Frame");
                            
            return returningList;
    }
    
    
    /**
     * Compare two Frames for equivalence by checking their image content.
     * 
     * @param f1 : Frame
     * @param f2 : Frame
     * @return int 1 for true, 0 for false
     */
    @Override
    public int compare(Frame f1, Frame f2) {
        
            boolean dataEquals = Arrays.equals(f1.getData(), f2.getData());

            if(dataEquals){
                return 1;
            }else{
                return 0;
            }
            
    }

}
