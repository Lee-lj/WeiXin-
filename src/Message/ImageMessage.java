package Message;

import java.util.Map;

public class ImageMessage extends BaseMessage{
	
	public ImageMessage(Map<String, String> requestMap,String MediaId) {
		super(requestMap);
		setMsgType("image");
		this.image=image;
	}

	private Image image;

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	

	
}
