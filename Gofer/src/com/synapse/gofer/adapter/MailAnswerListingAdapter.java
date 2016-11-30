package com.synapse.gofer.adapter;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.gofer.R;
import com.synapse.gofer.model.Mail;
import com.synapse.gofer.parser.JobbidParser;
import com.synapse.gofer.util.Constants;

public class MailAnswerListingAdapter extends BaseAdapter {

	private Activity mContext;
	private ArrayList<Mail> mMailList = null;

	public MailAnswerListingAdapter(Activity activity, ArrayList<Mail> list) {
		mContext = activity;
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
	public View getView(final int position, View convertView, ViewGroup group) {
		//Holder holder = null;
		Mail mail = mMailList.get(position);

		//holder = new Holder();

		RelativeLayout ll = (RelativeLayout) mContext.getLayoutInflater().inflate(
				R.layout.row_mail_answer_listing, null);

		LinearLayout questionLayout = (LinearLayout) ll.findViewById(R.id.layout_question);
		LinearLayout answerLayout = (LinearLayout) ll.findViewById(R.id.layout_answer);
		
		TextView txtFromUserName = (TextView) ll.findViewById(R.id.txtFromUserName);
		TextView txtAnswer = (TextView) ll.findViewById(R.id.txtAnswer);
		TextView txtQuestion = (TextView) ll.findViewById(R.id.txtQuestion);
		Button btnReply = (Button) ll.findViewById(R.id.btnReply);
		
		txtFromUserName.setText(mail.getFromUserName());
		txtQuestion.setText(mail.getMessage());
		
		if(!Constants.uid.equals(mail.getJobCreator()) || Constants.uid.equals(mail.getFromUserId()))
			btnReply.setVisibility(View.GONE);
		
		if(mail.getAnswers().size() > 0){
			Mail answer = mail.getAnswers().get(0);
			txtAnswer.setText(answer.getMessage());
			
			btnReply.setBackgroundResource(R.drawable.mail_reply_inactive);
			btnReply.setEnabled(false);
		}else{
			answerLayout.setVisibility(View.GONE);
			btnReply.setEnabled(true);
			btnReply.setBackgroundResource(R.drawable.mail_reply_active);
		}
		
		btnReply.setTag(position);
		btnReply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				displayReplyDialog(position);
			}
		});
		
		convertView = ll;
		return convertView;
	}
	
	private void displayReplyDialog(final int position) {

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("CrowdService");
		builder.setMessage("Reply to the question");

		final EditText input = new EditText(mContext);
		builder.setView(input);

		builder.setPositiveButton("Reply",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Mail mail = mMailList.get(position);
						if (Constants.isNetAvailable(mContext)) {
							new ServerCommunicationSendMessage().execute(Constants.uid, mail.getFromUserId(),
									 mail.getJobId(), input.getText().toString(), mail.getQuestionId());
							
							Mail answer = new Mail();
							answer.setFromUserId(Constants.uid);
							answer.setToUserId(mail.getFromUserId());
							answer.setJobId(mail.getJobId());
							answer.setStatus(mail.getStatus());
							answer.setMessage(input.getText().toString());
							answer.setParentId(mail.getQuestionId());
							
							mail.getAnswers().add(answer);
							
							mMailList.set(position, mail);
							
							refereshAdapter(mMailList);
						} else {
							Constants.NoInternetError(mContext);
						}

						dialog.dismiss();
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();

	}
	
	private class ServerCommunicationSendMessage extends
			AsyncTask<String, String, String> {
		
		private final ProgressDialog progressBar = new ProgressDialog(
				mContext);
		
		@Override
		protected String doInBackground(String... strParams) {
		
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("from_id", strParams[0]));
			nameValuePairs.add(new BasicNameValuePair("to_id", strParams[1]));
			nameValuePairs.add(new BasicNameValuePair("job_id", strParams[2]));
			nameValuePairs.add(new BasicNameValuePair("message", strParams[3]));
			nameValuePairs.add(new BasicNameValuePair("parent_id", strParams[4]));
			
			JobbidParser parser = new JobbidParser(Constants.HTTP_HOST
					+ "messaging");
			String response = parser.getParseData(nameValuePairs);
			return response;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait...");
			progressBar.show();
		}
		
		@Override
		protected void onPostExecute(String resp) {
			super.onPostExecute(resp);
			if(progressBar != null && progressBar.isShowing())
				progressBar.dismiss();
		
			showAlertDialog("Replay Send");
		}
	}
	
	private void showAlertDialog(String s) {
		Context context = mContext.getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();
	}
}
