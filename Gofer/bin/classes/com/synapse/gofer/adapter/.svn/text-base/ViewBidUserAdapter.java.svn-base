package com.synapse.gofer.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synapse.gofer.R;
import com.synapse.gofer.jobandcourier.JobAndCourierDetailBaseFragment;
import com.synapse.gofer.model.BiddingDetail;
import com.synapse.gofer.model.CourierModel;
import com.synapse.gofer.model.UserDetail;
import com.synapse.gofer.parser.JobAcceptParser;
import com.synapse.gofer.util.Constants;

public class ViewBidUserAdapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	private ArrayList<CourierModel> mJobsList = null;
	private SimpleDateFormat serverFormate = null;
	private SimpleDateFormat deviceFormate = null;
	private Date currenDate = null;
	private String jobid = "";
	private String courierId = "";
	private String payKey = "";
	private String merch = "";
	private FragmentManager manager = null;
	private FragmentActivity activity = null;

	private static final int SUCCESS = 101;

	private static final int APPLY_JOB = 102;

	private OnAcceptListener mAcceptListener;

	public ViewBidUserAdapter(ArrayList<CourierModel> list,
			LayoutInflater inflater, String id, FragmentManager mgr,
			FragmentActivity act) {
		mInflater = inflater;
		mJobsList = list;
		serverFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		deviceFormate = new SimpleDateFormat("yyyy-MM-dd");
		currenDate = Calendar.getInstance().getTime();
		jobid = id;
		manager = mgr;
		activity = act;
	}

	public void setMerch(String merch) {
		this.merch = merch;
	}

	public ArrayList<CourierModel> getDataSource() {
		return mJobsList;
	}

	public void refereshAdapter(ArrayList<CourierModel> list) {
		mJobsList = list;
		currenDate = Calendar.getInstance().getTime();
		notifyDataSetChanged();
	}

	public void setAcceptListener(OnAcceptListener listener) {
		mAcceptListener = listener;
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
		CourierModel data = mJobsList.get(position);

		if (convertView == null) {
			holder = new Holder();

			RelativeLayout ll = (RelativeLayout) mInflater.inflate(
					R.layout.row_viewbid_user, null);

			holder.bidderName = (TextView) ll.findViewById(R.id.bidder_name);
			holder.price = (TextView) ll.findViewById(R.id.bidder_price);
			holder.ratingBar = (RatingBar) ll
					.findViewById(R.id.bidder_rating_bar);
			holder.ratingBar.setOnTouchListener(touch);
			holder.ratingBar.setIsIndicator(false);
			holder.container = (RelativeLayout) ll
					.findViewById(R.id.row_container);
			holder.container.setOnClickListener(clickListener);
			holder.accept = (Button) ll.findViewById(R.id.bidder_accept);
			holder.accept.setOnClickListener(acceptBid);

			ll.setTag(holder);
			convertView = ll;

		} else
			holder = (Holder) convertView.getTag();

		holder.bidderName.setText(data.getUser().getUsername());

		float rate = 0.0f;

		try {
			rate = Float.parseFloat(data.getUser().getAvg_rating());
		} catch (Exception e) {
			rate = 0.0f;
		}

		holder.ratingBar.setRating(rate);
		BiddingDetail bid = data.getBid();

		/*
		 * if(bid.getLinkStatus() != null) {
		 * if(bid.getLinkStatus().contains("A"))
		 * convertView.setBackgroundResource(R.drawable.red); else
		 * convertView.setBackgroundResource(R.drawable.blue); } else
		 */

		convertView.setBackgroundResource(R.drawable.blue);
		holder.price.setText(bid.getAmount());
		holder.container.setTag(position);
		holder.accept.setTag(position);

		return convertView;
	}

	private class Holder {
		public TextView bidderName, price;
		public RatingBar ratingBar;
		public RelativeLayout container;
		public Button accept;
	}

	private OnTouchListener touch = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return true;
		}
	};

	private OnClickListener acceptBid = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			CourierModel data = mJobsList.get(position);
			courierId = data.getUser().getId();

			BiddingDetail bd = data.getBid();

			mAcceptListener.onClick(data.getUser(), bd);
		}
	};

	public void acceptJob(String payKey) {
		this.payKey = payKey;

		if (Constants.isNetAvailable(activity)) {
			ServerCommuniBidjob download = new ServerCommuniBidjob();
			download.execute(new String[] { "" });
		} else {
			Constants.NoInternetError(activity);
		}

	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			CourierModel data = mJobsList.get(position);
			FragmentTransaction transaction = manager.beginTransaction();

			Fragment fragmentpublic = new JobAndCourierDetailBaseFragment();
			Bundle bundle = new Bundle();
			bundle.putString("data", jobid);
			Log.e("VIPII", "Pass courierId ID> " + data.getUser().getId());
			bundle.putString("courierId", data.getUser().getId());
			bundle.putString("service", data.getBid().getAmount());
			bundle.putString("status", "");
			bundle.putString("merch", merch);
			bundle.putInt("bidCount", 0);
			bundle.putBoolean("onlyJobDetail", false);
			fragmentpublic.setArguments(bundle);
			transaction.replace(R.id.mainContainer, fragmentpublic,
					"JobAndCourierDetailBaseFragment");
			transaction.addToBackStack("JobAndCourierDetailBaseFragment");

			transaction.commit();
		}
	};

	private class ServerCommuniBidjob extends
			AsyncTask<String, String, HashMap<String, String>> {

		private final ProgressDialog progressBar = new ProgressDialog(activity);

		@Override
		protected HashMap<String, String> doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("job_id", jobid));
			nameValuePairs.add(new BasicNameValuePair("user_id", courierId));
			nameValuePairs.add(new BasicNameValuePair("pay_key", payKey));
			JobAcceptParser parser = new JobAcceptParser(Constants.HTTP_HOST
					+ "UpdateBid");
			HashMap<String, String> dataList = parser
					.getParseData(nameValuePairs);

			return dataList;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setMessage("Please wait while loading...");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(HashMap<String, String> dataList) {
			super.onPostExecute(dataList);
			progressBar.dismiss();

			if (dataList.get("status").toString().equals("success")) {
				Message msg = handler.obtainMessage();
				msg.arg1 = SUCCESS;
				msg.what = APPLY_JOB;
				msg.obj = dataList;
				handler.sendMessage(msg);
			}
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == APPLY_JOB && msg.arg1 == SUCCESS) {
				HashMap<String, String> data = (HashMap<String, String>) msg.obj;

				FragmentManager fm = manager;
				if (fm.getBackStackEntryCount() > 0) {
					fm.popBackStack();
				}

			}
		}
	};

	public interface OnAcceptListener {
		public void onClick(UserDetail user, BiddingDetail bd);
	}
}
