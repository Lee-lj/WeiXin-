package Message;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

//作为父类，含有所有类型的消息都含有的属性，然后让不同类型的信息继承此父类
public class BaseMessage {
	 
	@XStreamAlias("ToUserName")
	private String toUserName;
	@XStreamAlias("FromUserName")
	private String fromUserName;
	@XStreamAlias("CreateTime")
	private String createTime;
	@XStreamAlias("MsgType")
	private String msgType;
	
	
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	public BaseMessage(Map<String,String> requestMap) {//每种类型的消息都需要这些消息，此构造方法从request域中直接取得信息
		this.toUserName=requestMap.get("FromUserName");
		this.fromUserName=requestMap.get("ToUserName");
		this.createTime=System.currentTimeMillis()/1000+"";
	}
	
	

}
