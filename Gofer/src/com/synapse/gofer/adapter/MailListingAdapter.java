package com.synapse.gofer.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synapse.gofer.R;
import com.synapse.gofer.model.Mail;
import com.synapse.gofer.model.MailData;
import com.synapse.gofer.util.Constants;

public class MailListingAdapter extends BaseAdapter {

	private Activity mContext;
	private ArrayList<MailData> mMailList = null;

	public MailListingAdapter(Activity activity, ArrayList<MailData> list) {
		mContext = activity;
		mMailList = list;
	}

	public ArrayList<MailData> getDataSource() {
		return mMailList;
	}

	public void refereshAdapter(ArrayList<MailData> list) {
		mMailList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mMailList == null)
			return 0;
		else
			return mMailList.size();
	}

	@Override
	public Object getItem(int arg0) {

		return mMailList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {

		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		Holder holder = null;
		MailData data = mMailList.get(position);

		if (convertView == null) {
			holder = new Holder();

			RelativeLayout ll = (RelativeLayout) mContext.getLayoutInflater().inflate(
					R.layout.row_mail_listing, null);

			holder.ivMessageIcon = (ImageView) ll.findViewById(R.id.ivMessageIcon);
			holder.txtFromUserName = (TextView) ll.findViewById(R.id.txtFromUserName);
			holder.txtMessage = (TextView) ll.findViewById(R.id.txtMessage);
			holder.txtDate = (TextView) ll.findViewById(R.id.txtDate);
			holder.container = ll;

			holder.container.setTag(holder);

			convertView = holder.container;
		} else
			holder = (Holder) convertView.getTag();

		
		Mail mail = data.getMails().get(0);
		
		holder.txtFromUserName.setText(mail.getJobName());
		holder.txtMessage.setText(mail.getFromUserName() + ": " + mail.getMessage());
		
		if(DateUtils.isToday(mail.getDate().getTime()))
			holder.txtDate.setText("Today");
		else 
			holder.txtDate.setText(Constants.deviceFormate.format(mail.getDate()));
		
		if(mail.getStatus().equals("2")){
			holder.ivMessageIcon.setImageResource(R.drawable.marker_question);
		}else if(mail.getStatus().equals("3")){
			holder.container.setBackgroundColor(mContext.getResources().getColor(R.color.bg_color_red));
		}else if(mail.getJobCreator().equals(Constants.uid)){
			holder.ivMessageIcon.setImageResource(R.drawable.marker_blank_blue);
		}else{
			holder.ivMessageIcon.setImageResource(R.drawable.marker_blank_red);
		}

		return convertView;
	}

	private class Holder {
		public RelativeLayout container;
		public TextView txtFromUserName, txtMessage, txtDate;
		public ImageView ivMessageIcon;
	}

}
