package com.synapse.gofer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Shared_Preferences {

	SharedPreferences sharedpreferences;
	String MyPREFERENCES = "gofer_app";
	Editor editor;
	String isShowTraffic = "traffic";

	public Shared_Preferences(Context _context) {
		sharedpreferences = _context.getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
	}

	public String getIsShowTraffic() {
		String _isShowTraffic = sharedpreferences
				.getString(isShowTraffic, null);
		return _isShowTraffic;
	}

	public void setIsShowTraffic(String _isShowTraffic) {
		editor.putString(this.isShowTraffic, _isShowTraffic);
		editor.commit();
	}
}
