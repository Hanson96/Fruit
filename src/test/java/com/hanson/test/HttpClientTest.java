package com.hanson.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Test;

public class HttpClientTest {

	@Test
	public void get(){
		/*String str = HttpRequest.sendPost("http://www.tuling123.com/openapi/api", "key=c20997c62eab451e989b0efbbfb4c583&info="+"你好啊么么哒诶");
        System.out.println(str);JSONObject o = new JSONObject().parseObject(str);
        if(o.get("code").toString().equals("100000")){
        	System.out.println(o.get("text"));
        }*/
        CloseableHttpClient httpClient = HttpClients.createDefault(); 
        HttpGet get = new HttpGet("http://www.tuling123.com/openapi/api?key=c20997c62eab451e989b0efbbfb4c583&info="+"你好啊么么哒诶");
        try {
			CloseableHttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			System.out.println(EntityUtils.toString(entity));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void post(){
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://www.tuling123.com/openapi/api");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("key", "c20997c62eab451e989b0efbbfb4c583"));
		nvps.add(new BasicNameValuePair("info", "你好啊么么哒诶"));
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps));
			CloseableHttpResponse response = httpClient.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode==200){
				HttpEntity entity = response.getEntity();
				String content = EntityUtils.toString(entity);
				System.out.println(content);
				JSONObject json = new JSONObject(content);
				if(json.getInt("code")==100000){
					System.out.println("text:"+json.getString("text"));
				}
			}else{
				System.out.println("请求失败");
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
