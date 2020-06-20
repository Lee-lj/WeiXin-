package manager;

import org.junit.Test;

import Service.WeiXinService;
import robot.Robot;

public class TemplateMessageManager {
	
	@Test
	public void set() {
		//发送post请求设置行业
		String as=WeiXinService.getAccessToken();
		String url="https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token="+as;
		String data="{\r\n" + 
				"    \"industry_id1\":\"1\",\r\n" + 
				"    \"industry_id2\":\"4\"\r\n" + 
				"}";
		String result=Robot.postManager(url, data);
		System.out.println(data);
	}
	
	//获取行业信息
	@Test
	public void get() {
		String as=WeiXinService.getAccessToken();
		String url="https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token="+as;//向这个地址带token发送get请求
		String result=Robot.get(url);
		System.out.print(result);
	}
	
	@Test
	//发送模板消息
	public void sendTemplateMessage() {
		String as=WeiXinService.getAccessToken();
		String url="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+as;//带token发送post请求
		//touser指定了向谁发送，面向对象时肯定为触发了某一条件的人的微信号
		String data="{\n" + 
				"           \"touser\":\"oj3Wo1T_Bb97sCTFylTV4l15SDWk\",\n" + 
				"           \"template_id\":\"K0AxZcIx9P3o-Z3SO1aRCKtSg6Vi2EgBK3vxql-N_rY\",         \n" + 
				"           \"data\":{\n" + 
				"                   \"first\": {\n" + 
				"                       \"value\":\"您已被成功录用！\",\n" + 
				"                       \"color\":\"#abcdef\"\n" + 
				"                   },\n" + 
				"                   \"companY\":{\n" + 
				"                       \"value\":\"阿里巴巴\",\n" + 
				"                       \"color\":\"#fff000\"\n" + 
				"                   },\n" + 
				"                   \"time\": {\n" + 
				"                       \"value\":\"2020/8/11\",\n" + 
				"                       \"color\":\"#1f1f1f\"\n" + 
				"                   },\n" + 
				"                   \"result\": {\n" + 
				"                       \"value\":\"30K\",\n" + 
				"                       \"color\":\"#173177\"\n" + 
				"                   },\n" + 
				"                   \"remark\":{\n" + 
				"                       \"value\":\"请及时致电！\",\n" + 
				"                       \"color\":\"#173177\"\n" + 
				"                   }\n" + 
				"           }\n" + 
				"       }";
		String result = Robot.post(url, data);
		System.out.print(result);
	}
}
