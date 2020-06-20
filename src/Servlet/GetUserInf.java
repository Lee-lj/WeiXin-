package Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import robot.Robot;

/**
 * Servlet implementation class GetUserInf
 */
@WebServlet("/GetUserInf")
public class GetUserInf extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public GetUserInf() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //��ȡcode
		String code=request.getParameter("code");
		//��ȡAccesstoken�ĵ�ַ
		String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		url=url.replace("APPID", "wxd585d1eb4ecd5c26").replace("SECRET", "ffcd5732ae66a5d9d959f804e745d833").replace("CODE", code);//APPID,SECRET�����Լ����ںŵ�id������
		String result = Robot.get(url);//ȡ��token����Ϣ��ע��token������Сʱ���ģ�Ҫˢ��
		String token=JSONObject.fromObject(result).getString("accsess_token");
		String openId=JSONObject.fromObject(result).getString("openid");
		//��ȡ�û���Ϣ
		url="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		url=url.replace("ACCESS_TOKEN", token).replace("OPENID", openId);
		String result1=Robot.get(url);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
