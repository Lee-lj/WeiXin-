package Message;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class TextMessage extends BaseMessage {
    @XStreamAlias("Content")   
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public TextMessage(Map<String,String> requestMap,String content){
		super(requestMap);//调取父类中的构造方法
		//此构造方法设置文本消息的类型为text
		this.setMsgType("text");
		this.content=content;
	}
 
	@Override
	public String toString() {
		return "TextMessage [content=" + content + ", getContent()=" + getContent() + ", getToUserName()="
				+ getToUserName() + ", getFromUserName()=" + getFromUserName() + ", getCreateTime()=" + getCreateTime()
 				+ ", toString()=" + super.toString() + "]";
	}
	
	
}
	


