package robot;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import Button.Button;
import Button.ClickButton;
import Button.PhotoOrAlbumButton;
import Button.SubButton;
import Button.ViewButton;
import Service.WeiXinService;
import net.sf.json.JSONObject;

public class CreateMenu {
	
	public static void main(String[] args) {
		//所有的菜单对象
			Button but=new Button();
			//第一个一级菜单
			but.getButton().add(new ClickButton("一级点击","1"));
			//第二个一级菜单
			but.getButton().add(new ViewButton("一级跳转", "http://www.baidu.com"));
			//创建第三个一级菜单
				
			SubButton sb=new SubButton("有子菜单");
			//这三行为第三个一级菜单增加了三个子菜单
			sb.getSub_button().add(new PhotoOrAlbumButton("传图", "3-1"));
			sb.getSub_button().add(new ClickButton("点击", "3-2"));
			sb.getSub_button().add(new ViewButton("阿里巴巴", "http://www.alibaba.com"));
			//加入第三个一级菜单
			but.getButton().add(sb);
			//转为Json
			JSONObject fromObject = JSONObject.fromObject(but);
			//准备URL;
			String url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
			//获取Token
			url=url.replace("ACCESS_TOKEN", WeiXinService.getAccessToken());
			//发送请求
			String result=Robot.postData(url,fromObject.toString());
			System.out.println(result);
	}

}
