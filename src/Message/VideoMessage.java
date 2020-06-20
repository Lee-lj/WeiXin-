package Message;

import java.util.Map;

public class VideoMessage extends BaseMessage{

	public VideoMessage(Map<String, String> requestMap,String mediaId,String title,String description) {
		super(requestMap);
		setMsgType("video");
		this.video=video;
	}
	
    private Video video;

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}
    
    
	
}
