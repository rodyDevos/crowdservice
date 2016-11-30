package com.synapse.gofer.adapter;

import java.util.ArrayList;
import com.synapse.gofer.model.Category;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SpinnerCustomAdapter extends ArrayAdapter<String> {

	//private Typeface regularFont = null;
	private ArrayList<Category> mData = null;

	public SpinnerCustomAdapter(Context context, int textViewResourceId,ArrayList<Category>  data) {
		super(context, textViewResourceId);		
		mData = data;
		//regularFont = Typeface.createFromAsset(context.getAssets(),"font/Quicksand-Regular.ttf");
	}
	
	@Override
	public int getCount() {
		if(mData == null)
			return 0;
		else
			return mData.size();
	}

	@Override
	public String getItem(int position) {
		
		return mData.get(position).getName();
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}
	
    	
	@Override
	public TextView getView(int position, View convertView, ViewGroup parent) {
		TextView v = (TextView) super.getView(position, convertView, parent);
		Category cat=mData.get(position);
		v.setText(cat.getName());
		//v.setTypeface(regularFont);		
		return v;
	}
	

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView v = (TextView) super.getView(position, convertView, parent);
		//v.setTypeface(regularFont);
		return v;
	}
}
