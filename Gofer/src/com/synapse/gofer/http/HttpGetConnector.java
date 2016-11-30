package com.synapse.gofer.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.synapse.gofer.util.AppCrashLog;


public class HttpGetConnector {

	String url;
		
	public HttpGetConnector(String strUrl)
	{
		url=strUrl.replaceAll(" ","%20");		
	}
	
	public String getResponseData()
	{
		
		String result=null;				  		
		InputStream stream = null;
		try
		{
			HttpClient client = new DefaultHttpClient();       
            HttpGet request = new HttpGet(url);        
            HttpResponse response = client.execute(request);
			HttpEntity entity=response.getEntity();		
			stream=entity.getContent();			
			result=convertStreamToString(stream);
						
		}catch (Exception e) {
			Log.e("HttpException",">>>"+e);
			AppCrashLog.appendLog("HttpGetConnectorException>>>"+e);
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
				sb.append(line);
			}
		}catch (Exception e) {
			Log.e("HttpReaderException",">>>"+e.getMessage());
		} 		
		return sb.toString();
	}

	
}
