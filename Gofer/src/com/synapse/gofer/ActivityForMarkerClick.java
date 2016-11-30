package com.synapse.gofer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class ActivityForMarkerClick extends FragmentActivity implements OnClickListener {

	private FragmentManager fragmentManager;
	private TextView title = null;
	
	private  final int SUCCESS = 101;

	
	
	int requestForService = 120;
	
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_findgofer);

		initViews();
		
	}

	/*
	 * Called to initialize to user interface.
	 */
	private void initViews()
	{			
		TextView back = (TextView)findViewById(R.id.btnBack);
		back.setOnClickListener(this);
		title = (TextView)findViewById(R.id.title);
		
		int displayType = getIntent().getExtras().getInt("displayType");
		String ID = getIntent().getExtras().getString("id");
		
		fragmentManager=getSupportFragmentManager();
		FragmentTransaction transaction=fragmentManager.beginTransaction();
		JobAndCustomerDetailFromMarkerClick fragmentpublic = new JobAndCustomerDetailFromMarkerClick();
		Bundle bundle = new Bundle();
		bundle.putInt("displayType", displayType);
		bundle.putString("id", ID);
		fragmentpublic.setArguments(bundle);
		transaction.replace(R.id.mainContainer, fragmentpublic,"JobAndCustomerDetailFromMarkerClick");
		transaction.commit();
		
		if(displayType == MapActivity.JOB_MARKER)
		{
			title.setText("Job Details");
		}
		else if(displayType == MapActivity.ACTIVE_COURIER || displayType == MapActivity.COURIER_MARKER)
		{
			title.setText("Courier Details");
		}
		else
			title.setText("Customer Details");
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnBack)
		{
				finish();
		}
	}
	public void setTitle(String titleStr){title.setText(titleStr);}
	public interface BackListener{ public void backPressed();}
	//public void setBackListener(BackListener listener){ backListner = listener;}

	
//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			if(msg.arg1 == SUCCESS)
//			{
//				fragmentManager=getSupportFragmentManager();
//				FragmentTransaction transaction=fragmentManager.beginTransaction();
//				Fragment fragmentpublic = null;
//				if(requestForService == 1)
//				{
//					title.setText("Request Service");
//					fragmentpublic=new CategoryFragment();
//					transaction.replace(R.id.mainContainer, fragmentpublic,"Category");
//				}
//				else if(requestForService == 0)
//				{
//					
//					title.setText("Open For Bids");
//					fragmentpublic=new JobsListingFragment();
//					transaction.replace(R.id.mainContainer, fragmentpublic,"JobsListingFragment");
//				}
//				transaction.commit();
//			}
//			else
//				Toast.makeText(ActivityForMarkerClick.this, "Problem", Toast.LENGTH_LONG).show();
//		}
//	};
//	
//	
//	
//	private	class ServerCommunication extends AsyncTask<String, String, String> 
//	{
//
//		private final ProgressDialog progressBar = new ProgressDialog(ActivityForMarkerClick.this);
//
//		
//
//		@Override
//		protected String doInBackground(String... strParams){
//			
//			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//			nameValuePairs.add(new BasicNameValuePair("id",Constants.uid));	
//			String x = requestForService == 1 ? "2" : "3";
//			nameValuePairs.add(new BasicNameValuePair("is_customer", x));
//			JobbidParser parser = new JobbidParser(Constants.HTTP_HOST+"updateuser");
//			String dataList = parser.getParseData(nameValuePairs);
//
//			return dataList;
//		}
//
//		@Override
//		protected void onPreExecute() 
//		{
//			super.onPreExecute();
//			progressBar.setCancelable(false);
//			String str = requestForService == 1 ? "Making you customer" : "Making you as provider";
//			progressBar.setMessage(str);
//			progressBar.show();	
//		}
//
//		@Override
//		protected void onPostExecute(String dataList) 
//		{
//			super.onPostExecute(dataList);
//			progressBar.dismiss();	
//
//			Message msg = handler.obtainMessage();
//			if(dataList.equalsIgnoreCase("success"))
//			{
//				msg.arg1=SUCCESS;
//			}
//			else
//				msg.arg1 = 100;
//			msg.obj=dataList;	
//			handler.sendMessage(msg);
//		}
//	}
	
}
