package Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.baidu.aip.ocr.AipOcr;
import com.thoughtworks.xstream.XStream;

import Message.Articles;
import Message.BaseMessage;
import Message.ImageMessage;
import Message.MusicMessage;
import Message.NewsMessage;
import Message.TextMessage;
import Message.VideoMessage;
import Message.VoiceMessage;
import entity.AccessToken;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import robot.Robot;



public class WeiXinService {
	//阿凡达数据的聊天机器人接口地址
    private static final String TOKEN="ljzs";//token和微信平台上设置的token一致
    private static final String APPKEY="ff59e5f46e4445d2bd8c6b5338125c35";//申请问答机器人的APPKEY；阿凡达数据
    private static final String url="http://api.avatardata.cn/Tuling/Ask";//阿凡达数据的接口地址
   
    //APPID  APPSECRET 写上自己的id以及密码  微信公众号
	private static final String GET_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String APPID="wxd585d1eb4ecd5c26";
	private static final String APPSECRET="ffcd5732ae66a5d9d959f804e745d833";
	
	//自动获取token,将token存储起来
	private static AccessToken at;
	
	//提取图中文字的测试类，设置APPID/AK/SK,使用了百度的AI接口
    public static final String APP_ID = "20354849";
    public static final String API_KEY = "KsgB5GbiT5f8NRxCSYGyA0Zn";
    public static final String SECRET_KEY = "ztib9oYiLIFBMaYiZBebj52T8uD7UXZt";
	
	private static void getToken() {//这个方法获取的Token并不能直接提供给其他方法使用
	    String url=GET_TOKEN_URL.replace("APPID",APPID).replace("APPSECRET", APPSECRET);
	    String token2 = Robot.getToken(url);
	    JSONObject jsonObject = JSONObject.fromObject(token2);
	    String token = jsonObject.getString("access_token");//获取token，将其封装在AccessToken类中
	    String expiresIn = jsonObject.getString("expires_in");
	    at = new AccessToken(token, expiresIn);//创建token对象并存储
	}
	
	//使外部的可以获取token的方法
	public static String getAccessToken() {
		if(at==null||at.expried()) {
			getToken();
		}
			return at.getAccessToken();
	}
	
	/**
	 * 验证签名
	 * @param timestamp
	 * @param nonce
	 * @param signature
	 * @return
	 */
	public static boolean check(String timestamp, String nonce, String signature) {
    
      //1）将token、timestamp、nonce三个参数进行字典序排序 
	  String[] str=new String[] {TOKEN,timestamp,nonce};
	  Arrays.sort(str);//排序
      //2）将三个参数字符串拼接成一个字符串进行sha1加密 
	  String str1=str[0]+str[1]+str[2];//将三个参数字符串拼接成一个字符串
	  String password=Secret(str1);//password就是加密之后的结果
	  //System.out.println(password);
	  //System.out.println(signature);
      //3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	  return password.equalsIgnoreCase(signature);
	}
	private static String Secret(String src) {
		//获取一个加密对象
		try {
			//获取一个加密对象
			MessageDigest md=MessageDigest.getInstance("sha1");//sha1加密
			//加密
			byte[] digest=md.digest(src.getBytes());
			char[] chars= {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};//十六进制数，来表示数字的高四位第四位
			StringBuilder sb=new StringBuilder();//用于接收结果的字符串
			//处理加密结果digest
			for(byte b:digest) {
				sb.append(chars[(b>>4)&15]);//将数分为高四位，低四位，整个向右右移4位，高四位变为第四位，与15相与00001111
				sb.append(chars[b&15]);//低四位直接与15相与
			}
			//System.out.println(sb.toString());
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//解析xml数据包
	public static Map<String, String> parseRequest(InputStream ls) {
		Map<String,String> map=new HashMap<>();//通过hashmap来建立KV对
        SAXReader reader=new SAXReader();
        try {
            Document document=reader.read(ls);
            //读取输入流获取文档对象
        	Element root=document.getRootElement();
        	//根据文档对象获取根节点
        	List<Element> elements=root.elements();
        	//获取根节点的所有子节点，放入一个集合中，对象就是element
        		
        	//遍历这些节点，单个的节点就为e
        	for(Element e:elements) {
        			map.put(e.getName(), e.getStringValue());
        		}
        } catch(DocumentException e){
        	e.printStackTrace();
        }
		return map;
	}
	
	//用于处理所有的事件和消息的回复，返回的是xml数据包
	public static String getResponse(Map<String,String> requestMap) {
		BaseMessage msg=null;
		String msgType = requestMap.get("MsgType");
		switch (msgType) {//分类处理消息的类型
		//此方法专门处理文本消息
		case "text":
            msg=dealTextMessage(requestMap);
			break;
		case "image"://图片消息
            msg=dealImageMessage(requestMap);
			break;
		case "voice"://语言消息 

			break;
		case "video"://视频消息

			break;
		case "news"://图文消息

			break;
		case "shortvideo"://短视频消息

			break;
		case "location"://位置信息

			break;
		case "link"://链接信息

			break;
		case "event"://处理自定义菜单的点击消息
            msg=dealeventMessage(requestMap);
			break;

		default:
			break;
		}
		if(msg!=null) {
		      //把文本消息msg处理成XML数据包
		      return msgtoXML(msg);
		}else {
			  return null;
		}
	}

	//把文本消息msg处理成XML数据包
	private static String msgtoXML(BaseMessage msg) {
		// 设置 需要处理的@XStreamAlias("")注解 的类
		XStream stream = new XStream();
		stream.processAnnotations(TextMessage.class);
		stream.processAnnotations(ImageMessage.class);
		stream.processAnnotations(MusicMessage.class);
		stream.processAnnotations(NewsMessage.class);
		stream.processAnnotations(VideoMessage.class);
		stream.processAnnotations(VoiceMessage.class);
		String xml = stream.toXML(msg);
        return xml;
	}
	
	//处理文本消息
	private static BaseMessage dealTextMessage(Map<String, String> requestMap) {
		//用户发来的请求
		String userMessage=requestMap.get("Content");
		
		//处理图文消息
		if(userMessage.equals("图文")) {//如果微信用户输入"图文"这两个字才会回复
			//处理图文消息
			List<Articles> articles=new ArrayList<>();
			articles.add(new Articles("图文消息的标题","图文消息的详细介绍","http://mmbiz.qpic.cn/sz_mmbiz_jpg/udYXuNKZOggUKKZ4ibZ131JrQPw4QJMuCUWEsDcNyHzaTqN72xatBOZoD8VVib8fPnke6fKzEEl0TF6qEdqtPBWA/0", "http://www.baidu.com"));
			//接收图片消息会接收到图片的prcurl，目前可以这样取得一个图片	  最后的url就是点击图文消息跳转至哪里
			NewsMessage nm=new NewsMessage(requestMap,articles);
			return nm;
		}
		if(userMessage.equals("购买")) {//用户发送购买，跳转至外部链接，获取网页授权来获取未关注的用户信息
			String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd585d1eb4ecd5c26&redirect_uri=http://nicelee.free.idcfengye.com/weixin/GetUserInf&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
			TextMessage tm=new TextMessage(requestMap, "点击<a href=\""+url+"\">这里</a>购买");
			return tm;
			
		}
		//调用方法返回聊天的内容
		String responseMessage=answer(userMessage);
		TextMessage tm=new TextMessage(requestMap,responseMessage);	
		return tm;
				
		/*
		TextMessage tm=new TextMessage(requestMap,"我吐了");
		return tm;//对文本消息直接回复的消息
		*/
	}
	
	//智能问答机器人接收用户消息的方法       发送的消息就为UserMessage
  	private static String answer(String userMessage) {
  		String result =null;
        String url ="http://api.avatardata.cn/Tuling/Ask";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("key",APPKEY);//问答机器人申请到的APPKEY
        params.put("info",userMessage);//要发送给机器人的内容，不超过30个字
        params.put("dtype","");//返回的数据类型:json或者XML，默认为json
        params.put("loc","");//地点
        params.put("lon","");//经度  东经116.234632小数点后保留六位，需要写为116234632
        params.put("lat","");//纬度   北纬40.234632小数点后保留六位，需要写为40234632
        params.put("userid","");//1~32位，此userId针对每一个用户，用于上下文关联
        try {
            result =Robot.net(url, params, "GET");
            System.out.println(result);
            //解析JSON
            JSONObject jsonObject = JSONObject.fromObject(result);
            //取出error_code看有没有出错，出错就返回空值
            int code = jsonObject.getInt("error_code");
            if(code!=0) {
            		return null;
            }
            //否则取出机器人返回的消息的内容
            String responseMessage = jsonObject.getJSONObject("result").getString("text");
            return responseMessage;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	//此方法专用于处理事件推送问题
	private static BaseMessage dealeventMessage(Map<String, String> requestMap) {
        String event = requestMap.get("Event");
		switch (event) {
		case "CLICK":
			return dealClickMessage(requestMap);
        case "VIEW":
			return dealViewMessage(requestMap);

		default:
			break;
		}
        return null;
	}
	
    //处理点击菜单跳转链接时的事件推送
	private static BaseMessage dealViewMessage(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		return null;
	}
	
    //处理自定义菜单事件
	private static BaseMessage dealClickMessage(Map<String, String> requestMap) {
        String key = requestMap.get("EventKey");//事件KEY值，与自定义菜单接口中KEY值对应
        switch (key) {
		case "1"://点击一级菜单，对应自定义菜单时的KEY值
			//处理点击了第一个一级菜单
			return new TextMessage(requestMap, "一级菜单就绪");
		case "3-2"://点击菜单，对应自定义菜单时的KEY值
			//处理点击了第三个一级菜单的第二个子菜单
			return new TextMessage(requestMap, "二级菜单已就绪");
		default:
			break;
		}
		return null;
	}

	//处理图片消息，对图片进行识别
	private static BaseMessage dealImageMessage(Map<String, String> requestMap) {
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
        String path=requestMap.get("PicUrl");              //String path = "D:\\1.png";这就只能返回这一张固定图片上的文字，无论发来的是什么图片
        //org.json.JSONObject res = client.basicGeneral(path, new HashMap<String, String>());这种方法只能取本地的图片，取不了网络上发来的图片
        //输出格式得转换为json然后返回给微信
        org.json.JSONObject res = client.generalUrl(path, new HashMap<String,String>());//这种方法可以取用网络上直接发过来的图片
        String json=res.toString();
        //转换为JSONobject
        JSONObject imjson = JSONObject.fromObject(json);
        JSONArray jsonArray = imjson.getJSONArray("words_result");
        Iterator<JSONObject> it = jsonArray.iterator();//对json结果进行遍历
        StringBuilder sb=new StringBuilder();
        while(it.hasNext()) {
        	JSONObject next=it.next();
        	sb.append(next.getString("words"));
        }
		return new TextMessage(requestMap, sb.toString());
	}
	
	//上传临时素材
	public static String upload(String path,String type) {//path,type是要上传的文件的路径和类型
		File file=new File(path);
		//文件的地址
		String url="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
		url=url.replace("ACCESS_TOKEN", getAccessToken()).replace("TYPE", type);
        try {
			URL urlObject=new URL(url);
			HttpsURLConnection oc = (HttpsURLConnection) urlObject.openConnection();//开连接，因为使用了https，所以强制转换为https安全连接
			//设置连接的信息
			oc.setDoInput(true);
			oc.setDoOutput(true);//打开输入输出
			oc.setUseCaches(false);
			//设置请求头信息
			oc.setRequestProperty("Connection", "Keep_Alive");
			oc.setRequestProperty("Charset", "utf8");
			//设置数据的边界
			String boundary="-----"+System.currentTimeMillis();//设置一个随机的边界
			oc.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			OutputStream out = oc.getOutputStream();//获取输出流
			InputStream in=new FileInputStream(file);//获取文件的输入流
			//准备数据
			//第一部分:准备头部信息
			StringBuilder sb=new StringBuilder();
			sb.append("--");
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition:form-data;name=\"media\";filename=\""+file.getName()+"\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");//内容类型
			out.write(sb.toString().getBytes());
			//第二部分:准备文件内容
			byte[] b=new byte[1024];
			int len;
			while ((len=in.read(b))!=-1) {
				out.write(b,0,len);
			}
			//第三部分:准备尾部信息
			String foot="\r\n--"+boundary+"--\r\n";
			out.write(foot.getBytes());
			out.flush();
			out.close();
			//读取数据
			InputStream ls2=oc.getInputStream();
			StringBuilder response = new StringBuilder();
			while ((len=ls2.read(b))!=-1) {
				response.append(new String(b,0,len));
			}
			return response.toString();
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
	}
	
	//此方法用于生成临时二维码
	public static String getQrCodeTicket() {
		String at=getAccessToken();
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+at;
		//生成临时字符串类型二维码
		String data="{\"expire_seconds\": 604800, \r\n" + 
				"\"action_name\": \"QR_STR_SCENE\", \r\n" + 
				"\"action_info\": \r\n" + 
				"         {\"scene\": \r\n" + 
				"                {\"scene_str\": \"你一定行\"}}}";
		String result=Robot.post(url, data);
		String ticket = JSONObject.fromObject(result).getString("ticket");
		return ticket;
	}
	
	//获取已关注的用户信息
	public static String getUserInf(String openId) {
		String url="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		url=url.replace("ACCESS_TOKEN", getAccessToken()).replace("OPENID", openId);
		String result=Robot.get(url);
		return result;
	}
	
	
	
	
	
	
	
	
}
