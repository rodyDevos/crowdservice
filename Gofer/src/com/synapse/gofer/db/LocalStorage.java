package com.synapse.gofer.db;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {

	public static final String LOGINDATA="GoferLoginDb";
	private SharedPreferences loginDB; 
	private Context context;
	
	public LocalStorage(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	
	public void saveLogin(final String user,final String password,boolean isCheked)
	{		
		loginDB=context.getSharedPreferences(LOGINDATA,0);
		SharedPreferences.Editor editor = loginDB.edit();
		if(isCheked){			
			editor.putString("Username", user);
			editor.putString("Password", password);
			editor.commit();
		}else{
			editor.putString("Username","");
			editor.putString("Password","");
			editor.commit();
		}		
	}
	
	
	
}
