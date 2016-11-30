package com.synapse.gofer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.contact.ContactActivity;
import com.synapse.gofer.adapter.ProfileJobAdapter;
import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.JobsModel;
import com.synapse.gofer.model.ProfileJobData;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.model.UserDetail;
import com.synapse.gofer.parser.ProfileJobParser;
import com.synapse.gofer.parser.UserDataPostParser;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.ImageProcessing;

public class MapProfile extends FragmentActivity implements OnClickListener,
		OnItemClickListener {

	private final String TAG = this.getClass().getName();

	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	public static final int SUCCESS_1 = 3;

	private Bitmap bitmap;
	List<ProfileJobData> arrayOfList;
	private ProfileJobAdapter mProfileJobAdapter;
	private Button btnPublic, btnJob, btnProvider, btnContact, btnRequest;
	private FragmentManager fragmentManager;
	private TextView btnSetting, title, btnedit, textView5, user_name,
			username, txtaddress, txtemail, txttotaljobcount,
			provided_services;
	private ScrollView scrollLayout;
	private ImageView karma1, karma2, karma3, level1, level2, level3, star1,
			star2, star3, star4, star5, star6, txtshield;
	private ImageButton image;
	private URL image_url, url;
	private Bitmap bitImg = null;
	private ProgressBar pbar;
	private ListView job_listView;
	private RatingBar ratingbar, ratingbar1;
	String[] values;
	private String isProvierJob = "";
	ArrayList list = null;

	ArrayList<ProfileJobData> providedJobs = new ArrayList<ProfileJobData>();
	ArrayList<ProfileJobData> requestedJobs = new ArrayList<ProfileJobData>();

	private TextView temp_karma_count, temp_level_testing;

	/*
	 * Created by Ksolves007
	 */
	private LinearLayout listItemView;
	private static String visibleList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map_profile);

		initViews();

		/*
		 * This code will give the description of the job Name, Category,Rating
		 * and Feedback After clicking on the list of service requested and
		 * service provided jobs.
		 */

		job_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				job_listView.setVisibility(View.GONE);
				listItemView.setVisibility(View.VISIBLE);

				ProfileJobData listItem = null;
				if (isProvierJob.equals("completed_provider_job"))
					listItem = providedJobs.get(position);
				else
					listItem = requestedJobs.get(position);

				((TextView) findViewById(R.id.job_name1)).setText(listItem
						.getJobName());
				((TextView) findViewById(R.id.job_category)).setText(listItem
						.getJobCategory());
				((TextView) findViewById(R.id.job_feddback)).setText(listItem
						.getJobFeedback());

				Log.d("JobRating", listItem.getJobRating());
				/*
				 * Code to get the Job Rating Used class ProfileJobData Changing
				 * the star images dynamically on the basis of Job Rating
				 */
				float a = 0.0f;
				if (!(listItem.getJobRating().equals(""))
						&& !(listItem.getJobRating().equals(" "))
						&& (listItem.getJobRating() != null)) {

					a = Float.valueOf(listItem.getJobRating());

				} else {
					a = 0.0f;
				}

				int b = Math.round(a);
				int c = (int) b;
				Log.d("a", String.valueOf(a));
				Log.d("c", String.valueOf(c));
				if (c == 5)

				{
					((ImageView) findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star2_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star3_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star4_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star5_r))
							.setImageResource(R.drawable.yellowstar);

				} else if (c == 4) {
					((ImageView) findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star2_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star3_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star4_r))
							.setImageResource(R.drawable.yellowstar);
				} else if (c == 3) {
					((ImageView) findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star2_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star3_r))
							.setImageResource(R.drawable.yellowstar);

				} else if (c == 2) {
					((ImageView) findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star2_r))
							.setImageResource(R.drawable.yellowstar);

				}

				else if (c == 1) {
					((ImageView) findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);

				}

			}
		});

		job_listView.setOnTouchListener(new OnTouchListener() {
			// Setting on Touch Listener for handling the touch inside
			// ScrollView
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Disallow the touch request for parent scroll on touch of
				// child view
				v.getParent().requestDisallowInterceptTouchEvent(true);

				return false;
			}

		});

		// getAccountDetails();

	}

	/*
	 * Initializing the views
	 */
	private void initViews() {
		temp_karma_count = (TextView) findViewById(R.id.temp_karma_count);
		temp_level_testing = (TextView) findViewById(R.id.temp_level_testing);

		scrollLayout = (ScrollView) findViewById(R.id.scrollLayout);
		mProfileJobAdapter = new ProfileJobAdapter(null, getLayoutInflater());

		provided_services = (TextView) findViewById(R.id.provided_services);
		provided_services.setVisibility(View.VISIBLE);
		btnSetting = (TextView) findViewById(R.id.btnBack);
		txttotaljobcount = (TextView) findViewById(R.id.txttotaljobcount);
		title = (TextView) findViewById(R.id.title);
		// btnedit=(TextView)findViewById(R.id.btnedit);
		user_name = (TextView) findViewById(R.id.user_name);
		textView5 = (TextView) findViewById(R.id.textView5);
		username = (TextView) findViewById(R.id.username);
		txtaddress = (TextView) findViewById(R.id.txtaddress);
		txtshield = (ImageView) findViewById(R.id.txtshield);
		txtemail = (TextView) findViewById(R.id.txtemail);
		btnProvider = (Button) findViewById(R.id.btnProvider);
		// ratingbar=(RatingBar)findViewById(R.id.ratingBar1);
		btnContact = (Button) findViewById(R.id.btnContact);
		btnRequest = (Button) findViewById(R.id.btnRequest);
		karma1 = (ImageView) findViewById(R.id.karma1);
		karma2 = (ImageView) findViewById(R.id.karma2);
		karma3 = (ImageView) findViewById(R.id.karma3);
		level1 = (ImageView) findViewById(R.id.level1);
		level2 = (ImageView) findViewById(R.id.level2);
		level3 = (ImageView) findViewById(R.id.level3);
		image = (ImageButton) findViewById(R.id.imgView);
		pbar = (ProgressBar) findViewById(R.id.progressBar1);
		job_listView = (ListView) findViewById(R.id.job_listView);
		job_listView.setAdapter(mProfileJobAdapter);
		listItemView = (LinearLayout) findViewById(R.id.list_item_view);
		btnProvider.setOnClickListener(this);
		btnContact.setOnClickListener(this);
		btnRequest.setOnClickListener(this);
		// btnedit.setOnClickListener(this);
		btnSetting.setOnClickListener(this);
		// ratingbar1=(RatingBar)findViewById(R.id.job_rating);
		star1 = (ImageView) findViewById(R.id.star1);
		star2 = (ImageView) findViewById(R.id.star2);
		star3 = (ImageView) findViewById(R.id.star3);
		star4 = (ImageView) findViewById(R.id.star4);
		star5 = (ImageView) findViewById(R.id.star5);

		// Bitmap bitImg = Media.getBitmap(this.getContentResolver(), new
		// Uri("http://www.4khdwallpapers.com/wp-content/uploads/2014/04/wallpapers-of-sunny-leone-in-hd-9.jpg"));

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		scrollLayout.setVisibility(View.GONE);

		if (Constants.isNetAvailable(MapProfile.this)) {
			ServerCommunication download = new ServerCommunication();
			download.execute(new String[] { "" });
		} else {
			Constants.NoInternetError(MapProfile.this);
		}

	}

	/*
	 * Deciding the view on the basis of clicking of view
	 */

	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		if (v == btnProvider) {

			isProvierJob = "completed_provider_job";

			/*
			 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			 * android.R.layout.simple_list_item_1, android.R.id.text1, list);
			 * 
			 * job_listView.setAdapter(adapter);
			 */
			btnRequest.setBackgroundResource(R.drawable.tab_left);
			btnProvider.setBackgroundResource(R.drawable.tab_right_s);
			job_listView.setVisibility(View.VISIBLE);
			btnContact.setVisibility(View.VISIBLE);

			if (providedJobs.isEmpty()) {
				if (Constants.isNetAvailable(MapProfile.this)) {
					getJobList11 download1 = new getJobList11();
					download1.execute(new String[] { "" });
				} else {
					Constants.NoInternetError(MapProfile.this);
				}

			} else {
				mProfileJobAdapter.refereshAdapter(providedJobs);
			}

		} else if (v == btnRequest) {
			isProvierJob = "completed_customer_job";

			/*
			 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			 * android.R.layout.simple_list_item_1, android.R.id.text1, list);
			 * 
			 * job_listView.setAdapter(adapter);
			 */
			job_listView.setVisibility(View.VISIBLE);
			btnRequest.setBackgroundResource(R.drawable.tab_left_s);
			btnProvider.setBackgroundResource(R.drawable.tab_right);
			btnContact.setVisibility(View.VISIBLE);

			if (requestedJobs.isEmpty()) {
				if (Constants.isNetAvailable(MapProfile.this)) {
					getJobList11 download1 = new getJobList11();
					download1.execute(new String[] { "" });
				} else {
					Constants.NoInternetError(MapProfile.this);
				}

			} else {
				mProfileJobAdapter.refereshAdapter(requestedJobs);
			}
		} else if (v == btnContact) {
			// openCC();
		} else if (v == btnSetting) {
			finish();
			// startActivity(new
			// Intent(ProfileActivity.this,SettingsActivity.class));
		}
	}

	private void openCC() {
		if (Constants.jobsModelList != null
				&& Constants.jobsModelList.size() > 0) {
			JobsModel jobs = (JobsModel) Constants.jobsModelList.get(0);
			JobData data[] = jobs.getJobData();
			if (jobs.getJobData().length == 1) {
				Intent intent = new Intent(MapProfile.this,
						ContactActivity.class);
				intent.putExtra("UserName", data[0].getUser().getUsername());
				intent.putExtra("UserID", data[0].getUser().getId());
				intent.putExtra("profileimage", data[0].getProfileImgUrl());
				startActivity(intent);
			} else {
				// openUserList();
			}
		}

	}

	/**
	 * Getting Jobs details
	 * 
	 * @param
	 * @return JSON array of Jobs
	 * @author ASHISH Currently not used in new UI
	 * */

	public void getJobList() {
		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				Log.d("profile", Constants.uid);
				while (Constants.uid == null) {

				}
				nameValuePairs.add(new BasicNameValuePair("user_id",
						Constants.uid));
				nameValuePairs.add(new BasicNameValuePair("user_type", ""
						+ Constants.userType));
				HttpPostConnector conn = new HttpPostConnector(
						Constants.HTTP_HOST + "getPaymentInfo", nameValuePairs);
				String accountdetails = conn.getResponseData();
				try {
					JSONObject jsonObj = new JSONObject(accountdetails);

					// String Paypal_id = jsonObj.getString("Paypal_id");
					if (!jsonObj.getString("cardInfo").equals("null"))
						Constants.cardInfo = jsonObj.getString("cardInfo");
					if (!jsonObj.getString("BankInfo").equals("null"))
						Constants.BankInfo = jsonObj.getString("BankInfo");
					Constants.uemail = jsonObj.getString("email");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thr.start();

	}

	/**
	 * Getting cards details
	 * 
	 * @param user
	 *            ID of login user
	 * @return card detail and bank detail
	 * @author ASHISH Currently not used in new UI
	 * */

	public void getAccountDetails() {
		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("user_id",
						Constants.uid));
				HttpPostConnector conn = new HttpPostConnector(
						Constants.HTTP_HOST + "getPaymentInfo", nameValuePairs);
				String accountdetails = conn.getResponseData();
				try {
					JSONObject jsonObj = new JSONObject(accountdetails);

					// String Paypal_id = jsonObj.getString("Paypal_id");
					if (!jsonObj.getString("cardInfo").equals("null"))
						Constants.cardInfo = jsonObj.getString("cardInfo");
					if (!jsonObj.getString("BankInfo").equals("null"))
						Constants.BankInfo = jsonObj.getString("BankInfo");
					Constants.uemail = jsonObj.getString("email");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thr.start();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	private void showAlertDialog(String s) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();

	}

	/*
	 * To download data from server. Code to display the data of user profile It
	 * will get all the data from the server and then display it into the user
	 * profile UserDataParser class is used to get the data
	 */
	private class ServerCommunication extends
			AsyncTask<String, String, UserDetail> {
		private final ProgressDialog progressBar = new ProgressDialog(
				MapProfile.this);
		String ID = getIntent().getExtras().getString("id");

		public ServerCommunication() {
			// TODO Auto-generated constructor stub
			progressBar.setCancelable(false);
		}

		@Override
		protected UserDetail doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("id", ID));

			UserDataPostParser parser = new UserDataPostParser(
					Constants.HTTP_HOST + "viewprofile");
			Log.d("profile1", Constants.uid);

			UserDetail data = parser.getParseData(nameValuePairs);
			if (data != null) {
				bitmap = ImageProcessing.downloadImage(data.getImage_big());

			}

			return data;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setMessage("Please wait while loading...");
			// Log.d("profile",Constants.uid);
			progressBar.show();
		}

		@Override
		protected void onPostExecute(UserDetail data) {
			super.onPostExecute(data);
			progressBar.dismiss();
			scrollLayout.setVisibility(View.VISIBLE);

			Message msg = handler.obtainMessage();
			msg.arg1 = SUCCESS;
			msg.obj = data;
			handler.sendMessage(msg);
		}
	}

	/*
	 * Handler is used for handling the responses. Its a thread.
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == SUCCESS) {
				if (msg.obj instanceof ResultData) {
					ResultData data = (ResultData) msg.obj;
					if (data.getAuthenticated() != null
							&& data.getAuthenticated().equalsIgnoreCase(
									"success")) {
						showAlertDialog(data.getMessage());
					} else {
						showAlertDialog(data.getMessage());
					}
				}
				if (msg.obj instanceof UserDetail) {
					UserDetail data = (UserDetail) msg.obj;
					if (data != null) {
						showProfileDetail(data);
					}
				}
			} else if (msg.arg1 == SUCCESS_1) {

				ArrayList<ProfileJobData> dataList = (ArrayList<ProfileJobData>) msg.obj;
				Log.d("dataList1 ", "dataList1 " + dataList);
				if (isProvierJob.equals("completed_provider_job"))
					providedJobs = dataList;
				else
					requestedJobs = dataList;

				mProfileJobAdapter.refereshAdapter(dataList);

			} else if (msg.arg1 == FAILURE) {
				showAlertDialog("Please try again.");
			}
		}
	};

	/*
	 * Function used for setting the data of user profile It will display the
	 * data
	 */
	private void showProfileDetail(UserDetail detail) {

		// To update the security shield

		if (detail.getSecurity().equals("blue"))
			txtshield.setImageResource(R.drawable.shieldx);
		else if (detail.getSecurity().equals("silver"))
			txtshield.setImageResource(R.drawable.shield_silver);
		else if (detail.getSecurity().equals("gold"))
			txtshield.setImageResource(R.drawable.shield_gold);

		username.setText(detail.getUsername() + " | ");
		user_name.setText(detail.getFirst_name() + " " + detail.getLast_name());
		temp_karma_count.setText(detail.getKarmaCount() + " Points");
		temp_level_testing.setText(detail.getSJCount() + " Points");

		txtemail.setText(detail.getEmail());
		btnProvider.setText(detail.getSJCount() + " Services Provided");
		btnRequest.setText(detail.getCJCount() + " Services Requested");

		int total_count, temp1, temp2;
		if (detail.getSJCount() != "")
			temp1 = Integer.valueOf(detail.getSJCount());
		else
			temp1 = 0;

		if (detail.getCJCount() != "")
			temp2 = Integer.valueOf(detail.getCJCount());
		else
			temp2 = 0;

		total_count = (temp1) + (temp2);

		// int total_count =
		// Integer.valueOf(detail.getSJCount())+Integer.valueOf(detail.getCJCount());

		txttotaljobcount.setText("(" + total_count + ")");

		Log.d("Rating", detail.getAvg_rating());
		/*
		 * Getting the ratings of the user also changing the images of star
		 * dynamically
		 */
		float a = 0.0f;
		if (!(detail.getAvg_rating().equals(""))
				&& !(detail.getAvg_rating().equals(" "))
				&& (detail.getAvg_rating() != null)) {

			a = Float.valueOf(detail.getAvg_rating());

		}

		int b = Math.round(a);
		int c = (int) b;
		Log.d("a", String.valueOf(a));
		Log.d("c", String.valueOf(c));
		if (c == 5)

		{
			star1.setImageResource(R.drawable.yellowstar);
			star2.setImageResource(R.drawable.yellowstar);
			star3.setImageResource(R.drawable.yellowstar);
			star4.setImageResource(R.drawable.yellowstar);
			star5.setImageResource(R.drawable.yellowstar);

		} else if (c == 4) {
			star1.setImageResource(R.drawable.yellowstar);
			star2.setImageResource(R.drawable.yellowstar);
			star3.setImageResource(R.drawable.yellowstar);
			star4.setImageResource(R.drawable.yellowstar);
		} else if (c == 3) {
			star1.setImageResource(R.drawable.yellowstar);
			star2.setImageResource(R.drawable.yellowstar);
			star3.setImageResource(R.drawable.yellowstar);

		} else if (c == 2) {
			star1.setImageResource(R.drawable.yellowstar);
			star2.setImageResource(R.drawable.yellowstar);
		}

		else if (c == 1) {
			star1.setImageResource(R.drawable.yellowstar);

		}

		txtaddress.setText(" " + detail.getAddress3() + ", "
				+ detail.getAddress4());

		if (bitmap != null) {

			image.setImageBitmap(Constants.getRoundedCornerImage(bitmap,
					image.getWidth(), image.getHeight()));

		} else {
			Bitmap image_bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.com_facebook_profile_picture_blank_square);
			image.setImageBitmap(Constants.getRoundedCornerImage(image_bitmap,
					image.getWidth(), image.getHeight()));

		}
		// To display the preferred services of a user
		String mstring = detail.getcouriercategory() + ',' + ' '
				+ detail.gethomecategory();
		provided_services.setText(mstring);
	}

	/*
	 * get Job List from server. ProfileJobParser is used for it. All the
	 * service requested and service provide jobs will display using this code.
	 */
	private class getJobList11 extends
			AsyncTask<String, String, ArrayList<JobsModel>> {
		private final ProgressDialog progressBar = new ProgressDialog(
				MapProfile.this);
		String ID = getIntent().getExtras().getString("user_id");

		public getJobList11() {
			// TODO Auto-generated constructor stub
			progressBar.setCancelable(false);
		}

		@Override
		protected ArrayList<JobsModel> doInBackground(String... strParams) {
			ProfileJobParser parser;

			Log.d("button", isProvierJob);

			if (isProvierJob.equals("completed_provider_job")) {

				parser = new ProfileJobParser(Constants.HTTP_HOST
						+ "getProviderCompletedJob");
			} else {
				parser = new ProfileJobParser(Constants.HTTP_HOST
						+ "getCustomerCompletedJob");
			}
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("user_id", ID));
			Log.d("button1", isProvierJob);
			ArrayList<JobsModel> dataList = parser.getParseData(nameValuePairs,
					isProvierJob);

			return dataList;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setMessage("Please wait while loading...");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(ArrayList<JobsModel> dataList) {
			super.onPostExecute(dataList);
			progressBar.dismiss();
			Message msg = handler.obtainMessage();
			msg.arg1 = SUCCESS_1;
			msg.obj = dataList;
			handler.sendMessage(msg);
		}
	}

	/*
	 * Function to close the view of job description It will be called when we
	 * will click on the cross image
	 */
	public void closeView(View v) {
		// visibleList = null;
		listItemView.setVisibility(View.GONE);
		job_listView.setVisibility(View.VISIBLE);
	}

}
