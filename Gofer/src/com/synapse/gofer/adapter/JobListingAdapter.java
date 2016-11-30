package com.synapse.gofer.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synapse.gofer.R;
import com.synapse.gofer.model.JobData;

public class JobListingAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	private JobData[] mJobsList = null;
	private SimpleDateFormat serverFormate = null;
	private SimpleDateFormat deviceFormate = null;
	private Date currenDate = null;
	private String[] checkBid = null;

	public JobListingAdapter(JobData[] list, LayoutInflater inflater,
			String[] bids) {
		mInflater = inflater;
		mJobsList = list;
		serverFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		deviceFormate = new SimpleDateFormat("yyyy-MM-dd");
		currenDate = Calendar.getInstance().getTime();
		checkBid = bids;
	}

	public JobData[] getDataSource() {
		return mJobsList;
	}

	public void refereshAdapter(JobData[] list, String[] bids) {
		mJobsList = list;
		currenDate = Calendar.getInstance().getTime();
		notifyDataSetChanged();
		checkBid = bids;
	}

	@Override
	public int getCount() {
		if (mJobsList == null)
			return 0;
		else
			return mJobsList.length;
	}

	@Override
	public Object getItem(int arg0) {

		return mJobsList[arg0];
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		Holder holder = null;
		JobData data = mJobsList[position];

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

		try {
			Date serverDate = serverFormate.parse(data.getJob().getEndDate());
			long oneDay = 86400000;

			long diff = serverDate.getTime() - currenDate.getTime();

			diff = diff / oneDay;

			if (checkBid[position].equalsIgnoreCase("null")) {
				holder.jobName.setTextColor(Color.WHITE);
				holder.postedDate.setTextColor(Color.WHITE);

				if (diff < 1) {
					holder.container.setBackgroundResource(R.drawable.red);
				} else if (diff >= 1 && diff < 2) {
					holder.jobName.setTextColor(Color.BLACK);
					holder.postedDate.setTextColor(Color.BLACK);
					holder.container.setBackgroundResource(R.drawable.yellow);
				} else
					holder.container.setBackgroundResource(R.drawable.green);

				holder.postedDate.setText(deviceFormate.format(serverDate));
			} else {
				holder.container.setBackgroundResource(R.drawable.lightblue);
				holder.jobName.setTextColor(Color.BLACK);
				holder.postedDate.setTextColor(Color.BLACK);
				holder.postedDate.setText("Bid Placed");
			}

		} catch (Exception e) {
		}

		return convertView;
	}

	private class Holder {
		public RelativeLayout container;
		public TextView jobName, postedDate;
	}

}
