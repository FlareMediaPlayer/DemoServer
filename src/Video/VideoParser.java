package Video;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.FileChannelWrapper;
import org.jcodec.common.JCodecUtil;
import org.jcodec.common.JCodecUtil.Format;
import org.jcodec.common.NIOUtils;
import org.jcodec.common.SeekableByteChannel;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;

/**
 * Description:     VideoParser takes in a video file and can extract the frames
 *                  and metadata of the file.
 * 
 * Usage:           Call getAllFrames() or getAllFramesFromTime(time) to get the
 *                  frames from the video in a list. To simply test a frame,
 *                  call getFrameAt(index) where index based on the set of
 *                  all frames in the video; pass in 0 to get the first frame.
 *                  Call Frame.create(String, String, boolean) after getting the
 *                  array list to output pictures to working directory.
 *                  Be wary of OutOfMemory errors when processing large video files.
 * 
 *                  Recommend 5 second video.
 * 
 * @author Jimmy
 */
public class VideoParser implements Comparator<Frame>{

    
    private int totalFrames;    //0 to totalFrames-1
    private double totalDuration;
    private FrameGrab frameGrab;
    private ArrayList<Frame> frameList;
    
    
    public VideoParser(String file) throws FileNotFoundException, IOException, JCodecException{
            this(new File(file));
    }
    
    public VideoParser(File file) throws FileNotFoundException, IOException, JCodecException{
            FileChannelWrapper ch = NIOUtils.readableFileChannel(file);
            
            frameGrab = new FrameGrab(ch);
            
            initMetaData(file, ch);
            
    }
    
    
    private void initMetaData(File file, SeekableByteChannel ch) throws IOException{
        
            Format detectFormat = JCodecUtil.detectFormat(file);
            if(detectFormat == Format.MOV){
                
                    MP4Demuxer mdm = new MP4Demuxer(ch);
            
                    totalFrames = mdm.getVideoTrack().getMeta().getTotalFrames();
                    totalDuration = mdm.getVideoTrack().getMeta().getTotalDuration();
                    
            }
            
    }
    
    
    //getters for metadata
    public int getTotalFrames(){
            return totalFrames;
    }
    
    public double getTotalDuration(){
            return totalDuration;
    }
    
    public double getFramesPerSecond(){
            if(totalDuration <= 0) return 0;
            return totalFrames/totalDuration;
    }
    
    
    /**
     * <p>Retrieve a single Frame from video at the given index.</p>
     * 
     * <p><strong>
     * Use this method to get a single frame, but DO NOT iteratively call this 
     * method many times. Use getAllFrames() for a list of Frames instead.
     * </strong></p>
     * 
     * @throws java.io.IOException
     * @throws org.jcodec.api.JCodecException
     * @see getAllFrames()
     * @param index : position of which this Frame should be in in respect to all the Frames.
     * @return Frame at the given index
     */
    public Frame getFrameAt(int index) throws IOException, JCodecException{
        
            if(index >= totalFrames || index < 0) 
                throw new IllegalArgumentException("VideoParser.getFrameAt(index) was given an index out of bounds");
            
            if(frameList == null){
                
                    Picture p = frameGrab.seekToFramePrecise(index).getNativeFrame();
                    return new Frame(p,index);
                    
            }else{
                    
                    return frameList.get(index);
                    
            }
            
    }
    
    //kept private because it returns an unverified Frame.
    private Frame getFrameAt(double sec) throws IOException, JCodecException{
        
            if(sec >= totalDuration || sec < 0) 
                throw new IllegalArgumentException("VideoParser.getFrameAt(time) was given invalid time");
            
            Picture p = frameGrab.seekToSecondPrecise(sec).getNativeFrame();
            
            return new Frame(p,-1);
            
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
     * @param sec long : the time in seconds.
     * @return an ArrayList of Frames
     */
    public ArrayList<Frame> getAllFramesFromTime(int sec) throws Exception {
            return getListOfFrames((double)sec); 
    }
    
    public ArrayList<Frame> getAllFramesFromTime(double sec) throws Exception{
            return getListOfFrames(sec);
    }
    
    
    
    private ArrayList<Frame> getListOfFrames() throws IOException, JCodecException{
            
            if(frameList == null){
                
                    frameList = new ArrayList<Frame>();
                    frameGrab.seekToFrameSloppy(0);
                    
                    Picture p = null;
                    
                    for(int i = 0; i < totalFrames; i++){

                            p = frameGrab.getNativeFrame();
                            frameList.add(new Frame(p,i));
                            //System.out.println("added Frame: " + i);

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
            
            Frame verifyMe = getFrameAt(time);
            
            int timeOffset = (int) getFramesPerSecond() * (int) time;
                    
            //find out which real index should the frame 'verifyMe' be given
            for(int i = timeOffset; i < realFrameList.size(); i++){
                
                if( compare(realFrameList.get(i),verifyMe) == 1 ){
                    
                    returningList = new ArrayList<Frame>();
                    verifyMe = getFrameAt(i);
                    
                    returningList.add(verifyMe);
                    returningList.addAll(realFrameList.subList(i+1, realFrameList.size()));
                    
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
