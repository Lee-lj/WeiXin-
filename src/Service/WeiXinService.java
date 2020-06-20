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
	//���������ݵ���������˽ӿڵ�ַ
    private static final String TOKEN="ljzs";//token��΢��ƽ̨�����õ�tokenһ��
    private static final String APPKEY="ff59e5f46e4445d2bd8c6b5338125c35";//�����ʴ�����˵�APPKEY������������
    private static final String url="http://api.avatardata.cn/Tuling/Ask";//���������ݵĽӿڵ�ַ
   
    //APPID  APPSECRET д���Լ���id�Լ�����  ΢�Ź��ں�
	private static final String GET_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String APPID="wxd585d1eb4ecd5c26";
	private static final String APPSECRET="ffcd5732ae66a5d9d959f804e745d833";
	
	//�Զ���ȡtoken,��token�洢����
	private static AccessToken at;
	
	//��ȡͼ�����ֵĲ����࣬����APPID/AK/SK,ʹ���˰ٶȵ�AI�ӿ�
    public static final String APP_ID = "20354849";
    public static final String API_KEY = "KsgB5GbiT5f8NRxCSYGyA0Zn";
    public static final String SECRET_KEY = "ztib9oYiLIFBMaYiZBebj52T8uD7UXZt";
	
	private static void getToken() {//���������ȡ��Token������ֱ���ṩ����������ʹ��
	    String url=GET_TOKEN_URL.replace("APPID",APPID).replace("APPSECRET", APPSECRET);
	    String token2 = Robot.getToken(url);
	    JSONObject jsonObject = JSONObject.fromObject(token2);
	    String token = jsonObject.getString("access_token");//��ȡtoken�������װ��AccessToken����
	    String expiresIn = jsonObject.getString("expires_in");
	    at = new AccessToken(token, expiresIn);//����token���󲢴洢
	}
	
	//ʹ�ⲿ�Ŀ��Ի�ȡtoken�ķ���
	public static String getAccessToken() {
		if(at==null||at.expried()) {
			getToken();
		}
			return at.getAccessToken();
	}
	
	/**
	 * ��֤ǩ��
	 * @param timestamp
	 * @param nonce
	 * @param signature
	 * @return
	 */
	public static boolean check(String timestamp, String nonce, String signature) {
    
      //1����token��timestamp��nonce�������������ֵ������� 
	  String[] str=new String[] {TOKEN,timestamp,nonce};
	  Arrays.sort(str);//����
      //2�������������ַ���ƴ�ӳ�һ���ַ�������sha1���� 
	  String str1=str[0]+str[1]+str[2];//�����������ַ���ƴ�ӳ�һ���ַ���
	  String password=Secret(str1);//password���Ǽ���֮��Ľ��
	  //System.out.println(password);
	  //System.out.println(signature);
      //3�������߻�ü��ܺ���ַ�������signature�Աȣ���ʶ��������Դ��΢��
	  return password.equalsIgnoreCase(signature);
	}
	private static String Secret(String src) {
		//��ȡһ�����ܶ���
		try {
			//��ȡһ�����ܶ���
			MessageDigest md=MessageDigest.getInstance("sha1");//sha1����
			//����
			byte[] digest=md.digest(src.getBytes());
			char[] chars= {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};//ʮ��������������ʾ���ֵĸ���λ����λ
			StringBuilder sb=new StringBuilder();//���ڽ��ս�����ַ���
			//������ܽ��digest
			for(byte b:digest) {
				sb.append(chars[(b>>4)&15]);//������Ϊ����λ������λ��������������4λ������λ��Ϊ����λ����15����00001111
				sb.append(chars[b&15]);//����λֱ����15����
			}
			//System.out.println(sb.toString());
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//����xml���ݰ�
	public static Map<String, String> parseRequest(InputStream ls) {
		Map<String,String> map=new HashMap<>();//ͨ��hashmap������KV��
        SAXReader reader=new SAXReader();
        try {
            Document document=reader.read(ls);
            //��ȡ��������ȡ�ĵ�����
        	Element root=document.getRootElement();
        	//�����ĵ������ȡ���ڵ�
        	List<Element> elements=root.elements();
        	//��ȡ���ڵ�������ӽڵ㣬����һ�������У��������element
        		
        	//������Щ�ڵ㣬�����Ľڵ��Ϊe
        	for(Element e:elements) {
        			map.put(e.getName(), e.getStringValue());
        		}
        } catch(DocumentException e){
        	e.printStackTrace();
        }
		return map;
	}
	
	//���ڴ������е��¼�����Ϣ�Ļظ������ص���xml���ݰ�
	public static String getResponse(Map<String,String> requestMap) {
		BaseMessage msg=null;
		String msgType = requestMap.get("MsgType");
		switch (msgType) {//���ദ����Ϣ������
		//�˷���ר�Ŵ����ı���Ϣ
		case "text":
            msg=dealTextMessage(requestMap);
			break;
		case "image"://ͼƬ��Ϣ
            msg=dealImageMessage(requestMap);
			break;
		case "voice"://������Ϣ 

			break;
		case "video"://��Ƶ��Ϣ

			break;
		case "news"://ͼ����Ϣ

			break;
		case "shortvideo"://����Ƶ��Ϣ

			break;
		case "location"://λ����Ϣ

			break;
		case "link"://������Ϣ

			break;
		case "event"://�����Զ���˵��ĵ����Ϣ
            msg=dealeventMessage(requestMap);
			break;

		default:
			break;
		}
		if(msg!=null) {
		      //���ı���Ϣmsg�����XML���ݰ�
		      return msgtoXML(msg);
		}else {
			  return null;
		}
	}

	//���ı���Ϣmsg�����XML���ݰ�
	private static String msgtoXML(BaseMessage msg) {
		// ���� ��Ҫ�����@XStreamAlias("")ע�� ����
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
	
	//�����ı���Ϣ
	private static BaseMessage dealTextMessage(Map<String, String> requestMap) {
		//�û�����������
		String userMessage=requestMap.get("Content");
		
		//����ͼ����Ϣ
		if(userMessage.equals("ͼ��")) {//���΢���û�����"ͼ��"�������ֲŻ�ظ�
			//����ͼ����Ϣ
			List<Articles> articles=new ArrayList<>();
			articles.add(new Articles("ͼ����Ϣ�ı���","ͼ����Ϣ����ϸ����","http://mmbiz.qpic.cn/sz_mmbiz_jpg/udYXuNKZOggUKKZ4ibZ131JrQPw4QJMuCUWEsDcNyHzaTqN72xatBOZoD8VVib8fPnke6fKzEEl0TF6qEdqtPBWA/0", "http://www.baidu.com"));
			//����ͼƬ��Ϣ����յ�ͼƬ��prcurl��Ŀǰ��������ȡ��һ��ͼƬ	  ����url���ǵ��ͼ����Ϣ��ת������
			NewsMessage nm=new NewsMessage(requestMap,articles);
			return nm;
		}
		if(userMessage.equals("����")) {//�û����͹�����ת���ⲿ���ӣ���ȡ��ҳ��Ȩ����ȡδ��ע���û���Ϣ
			String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd585d1eb4ecd5c26&redirect_uri=http://nicelee.free.idcfengye.com/weixin/GetUserInf&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
			TextMessage tm=new TextMessage(requestMap, "���<a href=\""+url+"\">����</a>����");
			return tm;
			
		}
		//���÷����������������
		String responseMessage=answer(userMessage);
		TextMessage tm=new TextMessage(requestMap,responseMessage);	
		return tm;
				
		/*
		TextMessage tm=new TextMessage(requestMap,"������");
		return tm;//���ı���Ϣֱ�ӻظ�����Ϣ
		*/
	}
	
	//�����ʴ�����˽����û���Ϣ�ķ���       ���͵���Ϣ��ΪUserMessage
  	private static String answer(String userMessage) {
  		String result =null;
        String url ="http://api.avatardata.cn/Tuling/Ask";//����ӿڵ�ַ
        Map params = new HashMap();//�������
        params.put("key",APPKEY);//�ʴ���������뵽��APPKEY
        params.put("info",userMessage);//Ҫ���͸������˵����ݣ�������30����
        params.put("dtype","");//���ص���������:json����XML��Ĭ��Ϊjson
        params.put("loc","");//�ص�
        params.put("lon","");//����  ����116.234632С���������λ����ҪдΪ116234632
        params.put("lat","");//γ��   ��γ40.234632С���������λ����ҪдΪ40234632
        params.put("userid","");//1~32λ����userId���ÿһ���û������������Ĺ���
        try {
            result =Robot.net(url, params, "GET");
            System.out.println(result);
            //����JSON
            JSONObject jsonObject = JSONObject.fromObject(result);
            //ȡ��error_code����û�г�������ͷ��ؿ�ֵ
            int code = jsonObject.getInt("error_code");
            if(code!=0) {
            		return null;
            }
            //����ȡ�������˷��ص���Ϣ������
            String responseMessage = jsonObject.getJSONObject("result").getString("text");
            return responseMessage;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	//�˷���ר���ڴ����¼���������
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
	
    //�������˵���ת����ʱ���¼�����
	private static BaseMessage dealViewMessage(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		return null;
	}
	
    //�����Զ���˵��¼�
	private static BaseMessage dealClickMessage(Map<String, String> requestMap) {
        String key = requestMap.get("EventKey");//�¼�KEYֵ�����Զ���˵��ӿ���KEYֵ��Ӧ
        switch (key) {
		case "1"://���һ���˵�����Ӧ�Զ���˵�ʱ��KEYֵ
			//�������˵�һ��һ���˵�
			return new TextMessage(requestMap, "һ���˵�����");
		case "3-2"://����˵�����Ӧ�Զ���˵�ʱ��KEYֵ
			//�������˵�����һ���˵��ĵڶ����Ӳ˵�
			return new TextMessage(requestMap, "�����˵��Ѿ���");
		default:
			break;
		}
		return null;
	}

	//����ͼƬ��Ϣ����ͼƬ����ʶ��
	private static BaseMessage dealImageMessage(Map<String, String> requestMap) {
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
        String path=requestMap.get("PicUrl");              //String path = "D:\\1.png";���ֻ�ܷ�����һ�Ź̶�ͼƬ�ϵ����֣����۷�������ʲôͼƬ
        //org.json.JSONObject res = client.basicGeneral(path, new HashMap<String, String>());���ַ���ֻ��ȡ���ص�ͼƬ��ȡ���������Ϸ�����ͼƬ
        //�����ʽ��ת��ΪjsonȻ�󷵻ظ�΢��
        org.json.JSONObject res = client.generalUrl(path, new HashMap<String,String>());//���ַ�������ȡ��������ֱ�ӷ�������ͼƬ
        String json=res.toString();
        //ת��ΪJSONobject
        JSONObject imjson = JSONObject.fromObject(json);
        JSONArray jsonArray = imjson.getJSONArray("words_result");
        Iterator<JSONObject> it = jsonArray.iterator();//��json������б���
        StringBuilder sb=new StringBuilder();
        while(it.hasNext()) {
        	JSONObject next=it.next();
        	sb.append(next.getString("words"));
        }
		return new TextMessage(requestMap, sb.toString());
	}
	
	//�ϴ���ʱ�ز�
	public static String upload(String path,String type) {//path,type��Ҫ�ϴ����ļ���·��������
		File file=new File(path);
		//�ļ��ĵ�ַ
		String url="https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
		url=url.replace("ACCESS_TOKEN", getAccessToken()).replace("TYPE", type);
        try {
			URL urlObject=new URL(url);
			HttpsURLConnection oc = (HttpsURLConnection) urlObject.openConnection();//�����ӣ���Ϊʹ����https������ǿ��ת��Ϊhttps��ȫ����
			//�������ӵ���Ϣ
			oc.setDoInput(true);
			oc.setDoOutput(true);//���������
			oc.setUseCaches(false);
			//��������ͷ��Ϣ
			oc.setRequestProperty("Connection", "Keep_Alive");
			oc.setRequestProperty("Charset", "utf8");
			//�������ݵı߽�
			String boundary="-----"+System.currentTimeMillis();//����һ������ı߽�
			oc.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			OutputStream out = oc.getOutputStream();//��ȡ�����
			InputStream in=new FileInputStream(file);//��ȡ�ļ���������
			//׼������
			//��һ����:׼��ͷ����Ϣ
			StringBuilder sb=new StringBuilder();
			sb.append("--");
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition:form-data;name=\"media\";filename=\""+file.getName()+"\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");//��������
			out.write(sb.toString().getBytes());
			//�ڶ�����:׼���ļ�����
			byte[] b=new byte[1024];
			int len;
			while ((len=in.read(b))!=-1) {
				out.write(b,0,len);
			}
			//��������:׼��β����Ϣ
			String foot="\r\n--"+boundary+"--\r\n";
			out.write(foot.getBytes());
			out.flush();
			out.close();
			//��ȡ����
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
	
	//�˷�������������ʱ��ά��
	public static String getQrCodeTicket() {
		String at=getAccessToken();
		String url="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+at;
		//������ʱ�ַ������Ͷ�ά��
		String data="{\"expire_seconds\": 604800, \r\n" + 
				"\"action_name\": \"QR_STR_SCENE\", \r\n" + 
				"\"action_info\": \r\n" + 
				"         {\"scene\": \r\n" + 
				"                {\"scene_str\": \"��һ����\"}}}";
		String result=Robot.post(url, data);
		String ticket = JSONObject.fromObject(result).getString("ticket");
		return ticket;
	}
	
	//��ȡ�ѹ�ע���û���Ϣ
	public static String getUserInf(String openId) {
		String url="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
		url=url.replace("ACCESS_TOKEN", getAccessToken()).replace("OPENID", openId);
		String result=Robot.get(url);
		return result;
	}
	
	
	
	
	
	
	
	
}
