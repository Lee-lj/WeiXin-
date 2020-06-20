package entity;

public class AccessToken {
	
	private String accessToken;//保存Token
	private long expireTime;//计算过期时间
	
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	public AccessToken(String accessToken, String expireIn) {
		super();
		this.accessToken = accessToken;
		expireTime=System.currentTimeMillis()+Integer.parseInt(expireIn)*1000;//从获取到token开始计算token失效的时间，因为获取到的时间为秒，系统时间为毫秒，所以乘以一千
	}

	public boolean expried() {//判断token是否过期,当前系统时间如果大于exprieTime就过期了，小的话就没过期
		return System.currentTimeMillis()>expireTime;
		
	}
	
}
