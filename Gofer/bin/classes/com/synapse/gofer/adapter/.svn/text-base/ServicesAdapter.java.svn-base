package com.synapse.gofer.adapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.synapse.gofer.R;
import com.synapse.gofer.model.Category;

import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.ProfileJobData;


import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ServicesAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	private ArrayList<Category> mJobsList = null;
	private SimpleDateFormat serverFormate = null;
	private SimpleDateFormat deviceFormate = null;
	private Date currenDate = null;
	private Category data;


	public ServicesAdapter(ArrayList<Category> list, LayoutInflater inflater)
	{
		mInflater = inflater;
		mJobsList = list;
		serverFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		deviceFormate = new SimpleDateFormat("yyyy-MM-dd");
		currenDate = Calendar.getInstance().getTime();

	}

	//public JobData[] getDataSource(){return mJobsList;}

	public void refereshAdapter(ArrayList<Category>  list)
	{
		mJobsList = list;
		// currenDate = Calendar.getInstance().getTime();
		notifyDataSetChanged();
		//checkBid = bids;
	}

	@Override
	public int getCount() {
		if(mJobsList == null)
			return 0;
		else
			return mJobsList.size();
	}

	@Override
	public Object getItem(int arg0) {

		return mJobsList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		Holder holder = null;

		data  = mJobsList.get(position);

		if(convertView == null)
		{
			holder = new Holder();

			RelativeLayout ll = (RelativeLayout) mInflater.inflate(R.layout.services_list_adapter, null);

			holder.jobName = (TextView)ll.findViewById(R.id.row_job_desc);
			
			holder.container = ll;

			holder.container.setTag(holder);

			convertView = holder.container;
		}
		else
			holder = (Holder) convertView.getTag();

		holder.jobName.setText(data.getName());

		

		return convertView;
	}


	OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

		
			// TODO Auto-generated method stub

		}
	};


	private class Holder 
	{
		public RelativeLayout container;
		public TextView jobName;
		public ImageView nextArrow;
	
	}

}
