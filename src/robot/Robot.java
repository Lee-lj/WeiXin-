package robot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;


public class Robot {
		public static final String DEF_CHATSET = "UTF-8";
		public static final int DEF_CONN_TIMEOUT = 30000;
		public static final int DEF_READ_TIMEOUT = 30000;
		public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
		// 配置KEY
		public static final String APPKEY = "*************************";

		//用于创建自定义菜单，向指定的地址发送一个带着data数据的post请求
		public static String postData(String url,String data) {
			try {
				URL urlObj=new URL(url);
				URLConnection openConnection = urlObj.openConnection();
				openConnection.setDoOutput(true);//要发送数据，所以要设置为可发送数据状态
				OutputStream os = openConnection.getOutputStream();//获取输出流
				os.write(data.getBytes());//写出数据
				os.close();
				InputStream is = openConnection.getInputStream();//获取输入流
				byte[] b = new byte[1024];
				int len;
				StringBuilder sb = new StringBuilder();
				while ((len=is.read(b))!=-1) {
					sb.append(new String(b,0,len));
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		//用于对模板消息的处理，主要是向微信服务器发送post请求设置行业
		public static String postManager(String url,String data) {
			try {
				URL urlObj=new URL(url);
				URLConnection openConnection = urlObj.openConnection();
				openConnection.setDoOutput(true);//要发送数据，所以要设置为可发送数据状态
				OutputStream os = openConnection.getOutputStream();//获取输出流
				os.write(data.getBytes());//写出数据
				os.close();
				InputStream is = openConnection.getInputStream();//获取输入流
				byte[] b = new byte[1024];
				int len;
				StringBuilder sb = new StringBuilder();
				while ((len=is.read(b))!=-1) {
					sb.append(new String(b,0,len));
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		//向指定的地址发送get请求
	    public static String getToken(String url) {//输入url，获取微信的token
	    	try {
				URL urlObj=new URL(url);
				URLConnection openConnection = urlObj.openConnection();//开启链接
				InputStream inputStream = openConnection.getInputStream();
				byte[] b=new byte[1024];
				int len;
				StringBuilder sb=new StringBuilder();
				while ((len=inputStream.read(b))!=-1) {
					sb.append(new String(b,0,len));
				}
				return sb.toString();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
	    	return null;
	    }
		/**
		 * @param url 请求地址
		 * @param data
		 * @return by 缃楀彫鍕� Q缇�193557337
		 */
		public static String post(String url, String data) {
			try {
				URL urlObj = new URL(url);
				URLConnection connection = urlObj.openConnection();
				// 瑕佸彂閫佹暟鎹嚭鍘伙紝蹇呴』瑕佽缃负鍙彂閫佹暟鎹姸鎬�
				connection.setDoOutput(true);
				// 鑾峰彇杈撳嚭娴�
				OutputStream os = connection.getOutputStream();
				// 鍐欏嚭鏁版嵁
				os.write(data.getBytes());
				os.close();
				// 鑾峰彇杈撳叆娴�
				InputStream is = connection.getInputStream();
				byte[] b = new byte[1024];
				int len;
				StringBuilder sb = new StringBuilder();
				while ((len = is.read(b)) != -1) {
					sb.append(new String(b, 0, len));
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * 
		 * 
		 * @param url
		 * @return by 缃楀彫鍕� Q缇�193557337
		 */
		public static String get(String url) {
			try {
				URL urlObj = new URL(url);
				// 寮�杩炴帴
				URLConnection connection = urlObj.openConnection();
				InputStream is = connection.getInputStream();
				byte[] b = new byte[1024];
				int len;
				StringBuilder sb = new StringBuilder();
				while ((len = is.read(b)) != -1) {
					sb.append(new String(b, 0, len));
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 *
		 * @param strUrl
		 *            璇锋眰鍦板潃
		 * @param params
		 *            璇锋眰鍙傛暟
		 * @param method
		 *            璇锋眰鏂规硶
		 * @return 缃戠粶璇锋眰瀛楃涓�
		 * @throws Exception
		 */
		public static String net(String strUrl, Map params, String method) throws Exception {
			HttpURLConnection conn = null;
			BufferedReader reader = null;
			String rs = null;
			try {
				StringBuffer sb = new StringBuffer();
				if (method == null || method.equals("GET")) {
					strUrl = strUrl + "?" + urlencode(params);
				}
				URL url = new URL(strUrl);
				conn = (HttpURLConnection) url.openConnection();
				if (method == null || method.equals("GET")) {
					conn.setRequestMethod("GET");
				} else {
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
				}
				conn.setRequestProperty("User-agent", userAgent);
				conn.setUseCaches(false);
				conn.setConnectTimeout(DEF_CONN_TIMEOUT);
				conn.setReadTimeout(DEF_READ_TIMEOUT);
				conn.setInstanceFollowRedirects(false);
				conn.connect();
				if (params != null && method.equals("POST")) {
					try {
						DataOutputStream out = new DataOutputStream(conn.getOutputStream());
						out.writeBytes(urlencode(params));
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				InputStream is = conn.getInputStream();
				reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
				String strRead = null;
				while ((strRead = reader.readLine()) != null) {
					sb.append(strRead);
				}
				rs = sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					reader.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			}
			return rs;
		}

		//将map转为请求参数类型
		public static String urlencode(Map<String, Object> data) {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry i : data.entrySet()) {
				try {
					sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		}

}

