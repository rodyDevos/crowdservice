package com.synapse.gofer.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class ImageProcessing {

	public ImageProcessing() {
		// TODO Auto-generated constructor stub
	}
		
	public static Bitmap downloadImage(String URL)
    {         
		Bitmap bitmap=null;
        InputStream in = null;    
        
        try 
        {
          
          BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = 3;
			//options.inPreferredConfig = Config.ARGB_8888;
			//options.inDither = true;
            in = OpenHttpConnection(URL);          
            bitmap = BitmapFactory.decodeStream(in,null,options);         
            in.close();
        }catch (Exception e1) {      
            e1.printStackTrace();
        }
        return bitmap;               
    }
	
	private static InputStream OpenHttpConnection(String urlString)throws Exception
	{
        InputStream in = null;
        int response = -1;
                
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
                  
        if (!(conn instanceof HttpURLConnection))throw new IOException("Not an HTTP connection");
			try
			{
			    HttpURLConnection httpConn = (HttpURLConnection) conn;
			    httpConn.setAllowUserInteraction(false);
			    httpConn.setInstanceFollowRedirects(true);
			    httpConn.setRequestMethod("GET");
	            httpConn.connect();
	 
	            response = httpConn.getResponseCode();                
	            if (response == HttpURLConnection.HTTP_OK) {
	                in = httpConn.getInputStream();                                
	            }                    
	        }
	        catch (Exception ex)
	        {
	            throw new IOException("Error connecting");           
	        }
	      return in;    
	}
		
	public static void uploadImage(String userId,String strMethodname,Bitmap bitmap){
		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);		
		try {
			reqEntity.addPart("UserID", new StringBody(userId));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		if (bitmap != null) {			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 75, bos);
			byte[] data = bos.toByteArray();
			String imageName = "image"+userId;
			ByteArrayBody bab = new ByteArrayBody(data, imageName+".jpeg");
			reqEntity.addPart("Image", bab);			
		}
		HttpClient httpclient=new DefaultHttpClient();
		HttpPost httppost = new HttpPost(Constants.HTTP_HOST+strMethodname);		
		httppost.setEntity(reqEntity);
		
		// Execute HTTP Post Request
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpEntity entity = response.getEntity();
	
		try {
			InputStream stream = entity.getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		//String strResponse = getResponse(stream);
	}
	
	private Bitmap getShrinkBitmap(String file, int width, int height){
	    
	     BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
	     bmpFactoryOptions.inJustDecodeBounds = true;
	     Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
	          
	        int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
	        int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);
	          
	        if (heightRatio > 1 || widthRatio > 1)
	        {
	         if (heightRatio > widthRatio)
	         {
	          bmpFactoryOptions.inSampleSize = heightRatio;
	         } else {
	          bmpFactoryOptions.inSampleSize = widthRatio;
	         }
	        }
	          
	        bmpFactoryOptions.inJustDecodeBounds = false;
	        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
	     return bitmap;
    }
	
	
}
