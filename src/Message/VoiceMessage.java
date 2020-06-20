package Message;

import java.util.Map;

public class VoiceMessage extends BaseMessage{

	public VoiceMessage(Map<String, String> requestMap,String mediaId) {
		super(requestMap);
        setMsgType("voice");
        this.voice=voice;
	}
	
	private Voice voice;

	public Voice getVoice() {
		return voice;
	}

	public void setVoice(Voice voice) {
		this.voice = voice;
	}


	
	
	

}
