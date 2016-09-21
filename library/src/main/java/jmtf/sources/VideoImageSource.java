/**
 * Java Motion Tracking Framework
 */
package jmtf.sources;

import android.media.MediaMetadataRetriever;

import java.util.LinkedList;

import jmtf.AbstractImageSource;
import jmtf.JMTFImage;

/**
 * <p>WARNING - NOT TESTED!</p>
 *
 * @author Luca Rossetto, edited by Alessandro Contenti
 */
public class VideoImageSource extends AbstractImageSource {
	
	private LinkedList<JMTFImage> queue = new LinkedList<>();
	private int index = 0;
	
	/**
	 * <p>Reads frames from file</p>
	 *
	 * <p>WARNING - NOT TESTED!</p>
	 *
	 * @param filename
	 */
	public VideoImageSource(String filename){
		this(filename, 0);
	}

	/**
	 * <p>Reads frames from file</p>
	 *
	 * <p>WARNING - NOT TESTED!</p>
	 *
	 * @param filename
	 * @param startFrame removing the first Startframe - 1 frames
	 */
	public VideoImageSource(String filename, int startFrame) {
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		retriever.setDataSource(filename);
		index += startFrame;
		fillQueue();
		while(this.index < startFrame && hasMoreImages()){
			queue.offer(new JMTFImage(retriever.getFrameAtTime(index * 10, MediaMetadataRetriever.OPTION_CLOSEST)));
			getNextImage();
		}
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#hasMoreImages()
	 */
	@Override
	public boolean hasMoreImages() {
		fillQueue();
		return !this.queue.isEmpty();
	}

	/* (non-Javadoc)
	 * @see jmtf.AbstractImageSource#getNextImage()
	 */
	@Override
	public JMTFImage getNextImage() {
		fillQueue();
		this.img = this.queue.poll();
		this.img.setFrameNumber(this.index++);
		notifyListeners();
		return this.img;
	}
	
	/*
	 * Queue is used in case multiple images are contained within one packet of video
	 */
	private void fillQueue(){
			while(queue.isEmpty()){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {}
			}
	}
}
