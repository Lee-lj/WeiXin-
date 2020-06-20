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
	
	//提取图中文字的测试类，设置APPID/AK/SK
    public static final String APP_ID = "20354849";
    public static final String API_KEY = "KsgB5GbiT5f8NRxCSYGyA0Zn";
    public static final String SECRET_KEY = "ztib9oYiLIFBMaYiZBebj52T8uD7UXZt";
	
	@Test//提取图中文字的测试类
	public void testImage() {
		// 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        String path = "D:\\1.png";
        org.json.JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        System.out.println(res.toString(2));
}
		
	

	@Test//测试添加一个临时素材
	public void testUplood() {
		String file="D:\\1.png";
		String upload = WeiXinService.upload(file, "image");
		System.out.println(upload);
	} 
	
	
	@Test
	public void button() {
		//所有的菜单对象
		Button but=new Button();
		//第一个一级菜单
		but.getButton().add(new ClickButton("一级点击","1"));
		//第二个一级菜单
		but.getButton().add(new ViewButton("一级跳转", "http://www.baidu.com"));
		//创建第三个一级菜单
		SubButton sb=new SubButton("有子菜单");
		//这三行为第三个一级菜单增加了三个子菜单
		sb.getSub_button().add(new PhotoOrAlbumButton("传图", "31"));
		sb.getSub_button().add(new ClickButton("点击", "32"));
		sb.getSub_button().add(new ViewButton("阿里巴巴", "http://www.alibaba.com"));
		//加入第三个一级菜单
		but.getButton().add(sb);
		//转为Json
		JSONObject fromObject = JSONObject.fromObject(but);
		System.out.println(fromObject.toString());
	}
	
	@Test//测试获取token
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
		TextMessage tm=new TextMessage(map,"求你了");
		System.out.println(tm);
		
		
		//设置	需要处理的@XStreamAlias("")注解 的类
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
	
	@Test//测试获取二维码
	public void testCodeTicket() {
		String ticket = WeiXinService.getQrCodeTicket();
		System.out.print(ticket);
	}
	
	@Test//测试获取已关注用户信息
	public void testUserInf() {
		String user="oj3Wo1T_Bb97sCTFylTV4l15SDWk";//在微信平台上的已关注人的ID
		String inf = WeiXinService.getUserInf(user);
		System.out.print(inf);
	}

}
