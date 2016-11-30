package com.synapse.gofer.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;


public class HttpPostConnector {

	String url;
	ArrayList nameValuePairs; 
	
	public HttpPostConnector(String strUrl,ArrayList<NameValuePair> nameValueParams)
	{
		// TODO Auto-generated constructor stub
		url=strUrl;
		nameValuePairs=nameValueParams;
	}
	
	public String getResponseData()
	{
		
		String result=null;				  		
		InputStream stream = null;
		try
		{
			HttpClient httpclient=new DefaultHttpClient();
			HttpPost httppost=new HttpPost(url); 
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
			// Execute HTTP Post Request  
			HttpResponse response = httpclient.execute(httppost);  
			HttpEntity entity=response.getEntity();		
			stream=entity.getContent();			
			result=convertStreamToString(stream);
						
		}catch (Exception e) {
			// TODO: handle exception
			Log.e("HttpException",">>>"+e);
		}		
				
		return result;
	}
	
	public String convertStreamToString(InputStream is) 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try
		{
			while ((line = reader.readLine()) != null) 
			{
				sb.append(line + "\n");
			}
		} catch (Exception e) {
			Log.e("HttpReaderException",">>>"+e.getMessage());
		} 
		
		return sb.toString();
	}

	
}
