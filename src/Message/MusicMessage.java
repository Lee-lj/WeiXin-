package Message;

import java.util.Map;

public class MusicMessage extends BaseMessage{

	public MusicMessage(Map<String, String> requestMap) {
		super(requestMap);
		setMsgType("music");
		this.music=music;
	}

	private Music music;

	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}
	

}
