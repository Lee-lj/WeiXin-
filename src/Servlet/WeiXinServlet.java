package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Service.WeiXinService;


@WebServlet("/wx")
public class WeiXinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
		
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String signature = request.getParameter("signature");// 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
		String timestamp = request.getParameter("timestamp");// 时间戳 
		String nonce = request.getParameter("nonce");// 随机数 
		String echostr = request.getParameter("echostr"); //随机字符串
		//校验请求，看请求是否来源于微信服务器
		if(WeiXinService.check(timestamp,nonce,signature)) {
			//System.out.println("接入成功");
			PrintWriter out=response.getWriter();//原样返回echostr参数
			out.print(echostr);
			out.flush();
			out.close();
		}else {
			System.out.println("接入失败");
		}
		
		PrintWriter out = response.getWriter();out.print(echostr);
		//System.out.println("get");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*微信服务器推过来xml文件包 的消息，打印这个xml包，看其中的数据
		ServletInputStream ls=request.getInputStream();
		byte[] b=new byte[1024];
		int len;
		StringBuilder sb=new StringBuilder();
		while((len=ls.read(b))!=-1) {
			sb.append(new String(b,0,len));
		}
		System.out.println(sb.toString());
		
		request.setCharacterEncoding("utf8");//设置请求的编码格式
		response.setCharacterEncoding("utf8");//设置回答的编码格式
		*/
		/*
		 * 接收消息和事件推送
		 */
		 /*Map<String,String> requestMap=WeiXinService.parseRequest(request.getInputStream());//使用WeiXinService的parseRequest方法，希望放入一个请求request就能接收到一个map
	    System.out.println(requestMap);
	             //准备自动回复的数据包   第一种，复杂的方式
	    String responseXML="<xml><ToUserName><![CDATA["+requestMap.get("FromUserName")+"]]></ToUserName><FromUserName><![CDATA["+requestMap.get("ToUserName")+"]]></FromUserName><CreateTime>"+System.currentTimeMillis()/1000+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[why-so-serious?]]></Content></xml>";//ToUserName就是要将数据发给谁，直接从request域中取，FromUserName，就是从哪获取数据,System.currentTimeMillis()就是获取时间，本身为毫秒数所以除以1000
	    System.out.println(responseXML);
	    PrintWriter out=response.getWriter();//PrintWriter是java中很常见的一个类，该类可用来创建一个文件并向文本文件写入数据。可以理解为java中的文件输出，java中的文件输入则是java.io.File。
	    out.print(responseXML);
        out.flush();//flush是把流里的缓冲数据输出
        out.close();//close是把这个流关闭了，关闭之前会把缓冲的数据都输出。
	     */
	    	
	     //面向对象的方式
		request.setCharacterEncoding("utf8");//设置请求的编码格式
		response.setCharacterEncoding("utf8");//设置回答的编码格式
		
		Map<String,String> requestMap=WeiXinService.parseRequest(request.getInputStream());//使用WeiXinService的parseRequest方法，希望放入一个请求request就能接收到一个map
	    System.out.println(requestMap);
	    String responseXML=WeiXinService.getResponse(requestMap);
	    System.out.println(responseXML);
	    PrintWriter out = response.getWriter();
	    out.print(responseXML);
	    out.flush();
	    out.close();
	  
	}

}
