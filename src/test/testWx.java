package test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.baidu.aip.ocr.AipOcr;
import com.thoughtworks.xstream.XStream;

import Button.AbstractButton;
import Button.Button;
import Button.ClickButton;
import Button.PhotoOrAlbumButton;
import Button.SubButton;
import Button.ViewButton;
import Message.ImageMessage;
import Message.MusicMessage;
import Message.NewsMessage;
import Message.TextMessage;
import Message.VideoMessage;
import Message.VoiceMessage;
import Service.WeiXinService;
import net.sf.json.JSONObject;

public class testWx {
	
	//��ȡͼ�����ֵĲ����࣬����APPID/AK/SK
    public static final String APP_ID = "20354849";
    public static final String API_KEY = "KsgB5GbiT5f8NRxCSYGyA0Zn";
    public static final String SECRET_KEY = "ztib9oYiLIFBMaYiZBebj52T8uD7UXZt";
	
	@Test//��ȡͼ�����ֵĲ�����
	public void testImage() {
		// ��ʼ��һ��AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // ��ѡ�������������Ӳ���
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // ��ѡ�����ô����������ַ, http��socket��ѡһ�����߾�������
        //client.setHttpProxy("proxy_host", proxy_port);  // ����http����
        //client.setSocketProxy("proxy_host", proxy_port);  // ����socket����

        // ��ѡ������log4j��־�����ʽ���������ã���ʹ��Ĭ������
        // Ҳ����ֱ��ͨ��jvm�����������ô˻�������
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // ���ýӿ�
        String path = "D:\\1.png";
        org.json.JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        System.out.println(res.toString(2));
}
		
	

	@Test//�������һ����ʱ�ز�
	public void testUplood() {
		String file="D:\\1.png";
		String upload = WeiXinService.upload(file, "image");
		System.out.println(upload);
	} 
	
	
	@Test
	public void button() {
		//���еĲ˵�����
		Button but=new Button();
		//��һ��һ���˵�
		but.getButton().add(new ClickButton("һ�����","1"));
		//�ڶ���һ���˵�
		but.getButton().add(new ViewButton("һ����ת", "http://www.baidu.com"));
		//����������һ���˵�
		SubButton sb=new SubButton("���Ӳ˵�");
		//������Ϊ������һ���˵������������Ӳ˵�
		sb.getSub_button().add(new PhotoOrAlbumButton("��ͼ", "31"));
		sb.getSub_button().add(new ClickButton("���", "32"));
		sb.getSub_button().add(new ViewButton("����Ͱ�", "http://www.alibaba.com"));
		//���������һ���˵�
		but.getButton().add(sb);
		//תΪJson
		JSONObject fromObject = JSONObject.fromObject(but);
		System.out.println(fromObject.toString());
	}
	
	@Test//���Ի�ȡtoken
	public void testGetToken() {
		System.out.println(WeiXinService.getAccessToken());
		System.out.println(WeiXinService.getAccessToken());
	}
	
	@Test
	public void  messageTest() {
		
		Map<String,String> map=new HashMap<>();
		map.put("ToUserName","226");
		map.put("FromUserName","8080");
		map.put("MessageType","text");
		TextMessage tm=new TextMessage(map,"������");
		System.out.println(tm);
		
		
		//����	��Ҫ�����@XStreamAlias("")ע�� ����
		XStream stream=new XStream();
		stream.processAnnotations(TextMessage.class);
		stream.processAnnotations(ImageMessage.class);
		stream.processAnnotations(MusicMessage.class);
		stream.processAnnotations(NewsMessage.class);
		stream.processAnnotations(VideoMessage.class);
		stream.processAnnotations(VoiceMessage.class);
		String xml=stream.toXML(tm);
		System.out.println(xml);
	}
	
	@Test//���Ի�ȡ��ά��
	public void testCodeTicket() {
		String ticket = WeiXinService.getQrCodeTicket();
		System.out.print(ticket);
	}
	
	@Test//���Ի�ȡ�ѹ�ע�û���Ϣ
	public void testUserInf() {
		String user="oj3Wo1T_Bb97sCTFylTV4l15SDWk";//��΢��ƽ̨�ϵ��ѹ�ע�˵�ID
		String inf = WeiXinService.getUserInf(user);
		System.out.print(inf);
	}

}
