package com.synapse.gofer.adapter;

import java.util.ArrayList;
import java.util.Collections;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synapse.gofer.R;
import com.synapse.gofer.model.Mail;

public class MailDetailListingAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<Mail> mMailList = null;

	public MailDetailListingAdapter(LayoutInflater inflater, ArrayList<Mail> list) {
		mInflater = inflater;
		mMailList = list;
		
		Collections.reverse(mMailList);
	}

	public ArrayList<Mail> getDataSource() {
		return mMailList;
	}

	public void refereshAdapter(ArrayList<Mail> list) {
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
		Mail mail = mMailList.get(position);

		if (convertView == null) {
			holder = new Holder();

			RelativeLayout ll = (RelativeLayout) mInflater.inflate(
					R.layout.row_mail_detail_listing, null);

			holder.ivMessageIcon = (ImageView) ll.findViewById(R.id.ivMessageIcon);
			holder.txtMessage = (TextView) ll.findViewById(R.id.txtMessage);
			
			holder.container = ll;
			holder.container.setTag(holder);
			convertView = holder.container;
			
		} else
			holder = (Holder) convertView.getTag();

		
		holder.txtMessage.setText(mail.getMessage());
		
		if(mail.getJobCreator().equals(mail.getFromUserId())){
			holder.ivMessageIcon.setImageResource(R.drawable.marker_blank_blue);
		}else{
			holder.ivMessageIcon.setImageResource(R.drawable.marker_blank_red);
		}

		return convertView;
	}

	private class Holder {
		public RelativeLayout container;
		public TextView txtMessage;
		public ImageView ivMessageIcon;
	}

}
