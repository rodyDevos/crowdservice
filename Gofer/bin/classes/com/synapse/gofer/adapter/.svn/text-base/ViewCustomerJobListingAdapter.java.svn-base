package com.synapse.gofer.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synapse.gofer.R;
import com.synapse.gofer.model.BiddingDetail;
import com.synapse.gofer.model.JobData;

public class ViewCustomerJobListingAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	// private JobData[] mJobsList = null;
	private ArrayList<JobData> mJobsList = null;
	private SimpleDateFormat serverFormate = null;
	private SimpleDateFormat deviceFormate = null;
	private Date currenDate = null;

	public ViewCustomerJobListingAdapter(ArrayList<JobData> list,
			LayoutInflater inflater) {
		mInflater = inflater;
		mJobsList = list;
		serverFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		deviceFormate = new SimpleDateFormat("yyyy-MM-dd");
		currenDate = Calendar.getInstance().getTime();
	}

	public ArrayList<JobData> getDataSource() {
		return mJobsList;
	}

	public void refereshAdapter(ArrayList<JobData> list) {
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
		JobData data = mJobsList.get(position);

		if (convertView == null) {
			holder = new Holder();

			RelativeLayout ll = (RelativeLayout) mInflater.inflate(
					R.layout.row_job_listing, null);

			holder.jobName = (TextView) ll.findViewById(R.id.row_job_desc);
			holder.postedDate = (TextView) ll.findViewById(R.id.row_job_date);
			holder.container = ll;

			holder.container.setTag(holder);

			convertView = holder.container;
		} else
			holder = (Holder) convertView.getTag();

		holder.jobName.setText(data.getJob().getName());

		BiddingDetail bid = data.getBidDetail();
		if (bid.getLinkStatus() != null) {
			if (bid.getLinkStatus().contains("A")) {
				holder.container.setBackgroundResource(R.drawable.red);
				holder.jobName.setTextColor(Color.WHITE);
				holder.postedDate.setTextColor(Color.WHITE);
				holder.postedDate.setText("Active");
			} else if (bid.getLinkStatus().contains("C")) {
				holder.container.setBackgroundResource(R.drawable.lightblue);
				holder.jobName.setTextColor(Color.BLACK);
				holder.postedDate.setTextColor(Color.BLACK);
				holder.postedDate.setText("Completed");

			} else if (bid.getLinkStatus().contains("V")) {
				holder.container.setBackgroundResource(R.drawable.grey);
				holder.jobName.setTextColor(Color.WHITE);
				holder.postedDate.setTextColor(Color.WHITE);
				holder.postedDate.setText("Archive");

			} else {
				holder.container.setBackgroundResource(R.drawable.blue);
				holder.postedDate.setText(bid.getCount() + " User has Bid");
				holder.jobName.setTextColor(Color.WHITE);
				holder.postedDate.setTextColor(Color.WHITE);
			}
		} else {
			holder.container.setBackgroundResource(R.drawable.blue);
			holder.postedDate.setText(bid.getCount() + " User has Bid");
			holder.jobName.setTextColor(Color.WHITE);
			holder.postedDate.setTextColor(Color.WHITE);
		}

		return convertView;
	}

	private class Holder {
		public RelativeLayout container;
		public TextView jobName, postedDate;
		public ImageView nextArrow;
	}

}
