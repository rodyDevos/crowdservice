package com.synapse.gofer.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synapse.gofer.AlertDialogFragment;
import com.synapse.gofer.ProfileActivity;
import com.synapse.gofer.R;
import com.synapse.gofer.model.ComissionedJob;

public class CommissionedJobListingAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	private ArrayList<ComissionedJob> mJobsList = null;
	private SimpleDateFormat serverFormate = null;
	private SimpleDateFormat deviceFormate = null;
	private Date currenDate = null;
	private Fragment parentFragment;
	private FragmentActivity activity;
	private String mUserID = "";

	public CommissionedJobListingAdapter(ArrayList<ComissionedJob> list,
			LayoutInflater inflater, Fragment act, FragmentActivity acti,
			String userid) {
		mInflater = inflater;
		mJobsList = list;
		serverFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		deviceFormate = new SimpleDateFormat("yyyy-MM-dd");
		currenDate = Calendar.getInstance().getTime();
		parentFragment = act;
		activity = acti;
		mUserID = userid;
	}

	public ArrayList<ComissionedJob> getDataSource() {
		return mJobsList;
	}

	public void refereshAdapter(ArrayList<ComissionedJob> list) {
		mJobsList = list;
		currenDate = Calendar.getInstance().getTime();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mJobsList == null)
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
		ComissionedJob data = mJobsList.get(position);

		if (convertView == null) {
			holder = new Holder();

			RelativeLayout ll = (RelativeLayout) mInflater.inflate(
					R.layout.row_job_listing, null);

			holder.jobName = (TextView) ll.findViewById(R.id.row_job_desc);
			holder.postedDate = (TextView) ll.findViewById(R.id.row_job_date);
			holder.container = ll;
			holder.container.setOnClickListener(mclickListener);
			holder.container.setBackgroundResource(R.drawable.blue);

			convertView = holder.container;
			convertView.setTag(holder);

		} else
			holder = (Holder) convertView.getTag();

		holder.jobName.setText(data.getName());
		holder.postedDate.setText("Date");
		holder.container.setTag(data.getId());

		/*
		 * try{ Date serverDate =
		 * serverFormate.parse(data.getJob().getEndDate()); long oneDay =
		 * 86400000;
		 * 
		 * long diff = serverDate.getTime() - currenDate.getTime();
		 * 
		 * diff = diff/oneDay; if(data.getJob().getStatus() == null) { if(diff
		 * <=1) holder.container.setBackgroundResource(R.drawable.red); else
		 * if(diff>1 && diff <=3)
		 * holder.container.setBackgroundResource(R.drawable.green); else
		 * holder.container.setBackgroundResource(R.drawable.blue); } else
		 * holder.container.setBackgroundResource(R.drawable.lightblue);
		 * 
		 * holder.postedDate.setText(deviceFormate.format(serverDate)); }catch
		 * (Exception e) { // TODO: handle exception }
		 */

		return convertView;
	}

	private class Holder {
		public RelativeLayout container;
		public TextView jobName, postedDate;
		public ImageView nextArrow;
	}

	private OnClickListener mclickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			FragmentTransaction ft = parentFragment.getFragmentManager()
					.beginTransaction();
			Fragment fragmentpublic = new AlertDialogFragment();
			Bundle bundle = new Bundle();
			bundle.putString("user_id", mUserID);
			bundle.putString("job_id", (String) v.getTag());
			fragmentpublic.setArguments(bundle);
			if (activity instanceof ProfileActivity)
				ft.replace(R.id.mainContainer, fragmentpublic,
						"AlertDialogFragment");
			else
				ft.replace(R.id.job_customer_detail_container, fragmentpublic,
						"AlertDialogFragment");
			ft.addToBackStack("AlertDialogFragment");
			ft.commit();

		}
	};

}
