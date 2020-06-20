package entity;

public class AccessToken {
	
	private String accessToken;//����Token
	private long expireTime;//�������ʱ��
	
	
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
		expireTime=System.currentTimeMillis()+Integer.parseInt(expireIn)*1000;//�ӻ�ȡ��token��ʼ����tokenʧЧ��ʱ�䣬��Ϊ��ȡ����ʱ��Ϊ�룬ϵͳʱ��Ϊ���룬���Գ���һǧ
	}

	public boolean expried() {//�ж�token�Ƿ����,��ǰϵͳʱ���������exprieTime�͹����ˣ�С�Ļ���û����
		return System.currentTimeMillis()>expireTime;
		
	}
	
}
