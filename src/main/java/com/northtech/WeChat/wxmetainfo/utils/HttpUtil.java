package com.northtech.WeChat.wxmetainfo.utils;

import com.northtech.Common.utils.MyX509TrustManager;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;


public class HttpUtil {
	private final static int CONNECT_TIMEOUT = 5000; // in milliseconds
	private final static String DEFAULT_ENCODING = "UTF-8";

	public static String httpsGetRequest(String requestUrl,String outputSt){
		return httpsRequest(requestUrl,"GET",outputSt);
	}
	public static String httpsPostRequest(String requestUrl,String outputSt){
		return httpsRequest(requestUrl,"POST",outputSt);
	}
	public static String httpsRequest(String requestUrl,String requestMethod,String outputSt) {
		try {
			//创建SSLContext
			SSLContext sslContext = SSLContext.getInstance("SSL","SunJSSE");
			TrustManager[] tm = {new MyX509TrustManager()};
			//初始化
			sslContext.init(null, tm, new java.security.SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod(requestMethod);
			//设置当前实例使用的SSL
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();
			//往服务器端写内容
			if(null!=outputSt){
				OutputStream os = conn.getOutputStream();

				os.write(outputSt.getBytes(DEFAULT_ENCODING));
				os.close();
			}
			//读取客户端返回的内容
			InputStream is  = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is,DEFAULT_ENCODING);
			BufferedReader br = new BufferedReader(isr);

			StringBuffer buffer = new StringBuffer();
			String line = null;
			while((line = br.readLine())!=null){
				buffer.append(line);
			}
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String postData(String urlStr, String data) {
		return postData(urlStr, data, null);
	}

	public static String postData(String urlStr, String data, String contentType) {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(CONNECT_TIMEOUT);
			if (contentType != null)
				conn.setRequestProperty("content-type", contentType);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);
			if (data == null)
				data = "";
			writer.write(data);
			writer.flush();
			writer.close();

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\r\n");
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
}
