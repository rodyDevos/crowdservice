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
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.contact.ContactActivity;
import com.synapse.gofer.adapter.ProfileJobAdapter;
import com.synapse.gofer.control.CircleImageView;
import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.Category;
import com.synapse.gofer.model.JobData;
import com.synapse.gofer.model.JobsModel;
import com.synapse.gofer.model.ProfileJobData;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.model.UserDetail;
import com.synapse.gofer.parser.CategoryPostParser;
import com.synapse.gofer.parser.ProfileJobParser;
import com.synapse.gofer.parser.UserDataPostParser;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.ImageProcessing;

public class ProfileActivity extends FragmentActivity implements
		OnClickListener, OnItemClickListener, OnTouchListener {

	private final String TAG = this.getClass().getName();

	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;

	public static final int SUCCESS_1 = 3;

	private Bitmap bitmap;
	List<ProfileJobData> arrayOfList;
	private ProfileJobAdapter mProfileJobAdapter;
	private Button btnPublic, btnJob, btnProvider, btnRequest;
	private FragmentManager fragmentManager;
	private TextView btnSetting, title, btnedit, textView5, user_name,
			username, txtaddress, txtemail, txttotaljobcount,
			provided_services;
	private ScrollView scrollLayout;
	private ImageView karma1, karma2, karma3, level1, level2, level3, star1,
			star2, star3, star4, star5, star6, txtshield;
	private CircleImageView image;
	private URL _url, url;
	private Bitmap bitImg = null;
	private ProgressBar pbar;
	private ListView job_listView;
	private RatingBar ratingbar;
	String[] values;
	private String isProvierJob = "", shieldType = "blue";
	ArrayList list = null;
	ImageView img_close;

	LinearLayout list_parent_view;

	ArrayList<ProfileJobData> providedJobs = new ArrayList<ProfileJobData>();
	ArrayList<ProfileJobData> requestedJobs = new ArrayList<ProfileJobData>();

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
		setContentView(R.layout.activity_profile);
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

				RelativeLayout _relative = (RelativeLayout) view;
				TextView _row_job_desc = (TextView) _relative.getChildAt(1);

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

				if (_row_job_desc.getTag().equals("5"))

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

				} else if (_row_job_desc.getTag().equals("4")) {
					((ImageView) findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star2_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star3_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star4_r))
							.setImageResource(R.drawable.yellowstar);

					((ImageView) findViewById(R.id.star5_r))
							.setImageResource(R.drawable.blackstar);
				} else if (_row_job_desc.getTag().equals("3")) {
					((ImageView) findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star2_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star3_r))
							.setImageResource(R.drawable.yellowstar);

					((ImageView) findViewById(R.id.star4_r))
							.setImageResource(R.drawable.blackstar);
					((ImageView) findViewById(R.id.star5_r))
							.setImageResource(R.drawable.blackstar);
				} else if (_row_job_desc.getTag().equals("2")) {
					((ImageView) findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) findViewById(R.id.star2_r))
							.setImageResource(R.drawable.yellowstar);

					((ImageView) findViewById(R.id.star3_r))
							.setImageResource(R.drawable.blackstar);
					((ImageView) findViewById(R.id.star4_r))
							.setImageResource(R.drawable.blackstar);
					((ImageView) findViewById(R.id.star5_r))
							.setImageResource(R.drawable.blackstar);
				} else if (_row_job_desc.getTag().equals("1")) {
					((ImageView) findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);

					((ImageView) findViewById(R.id.star2_r))
							.setImageResource(R.drawable.blackstar);
					((ImageView) findViewById(R.id.star3_r))
							.setImageResource(R.drawable.blackstar);
					((ImageView) findViewById(R.id.star4_r))
							.setImageResource(R.drawable.blackstar);
					((ImageView) findViewById(R.id.star5_r))
							.setImageResource(R.drawable.blackstar);
				} else {
					((ImageView) findViewById(R.id.star1_r))
							.setImageResource(R.drawable.blackstar);
					((ImageView) findViewById(R.id.star2_r))
							.setImageResource(R.drawable.blackstar);
					((ImageView) findViewById(R.id.star3_r))
							.setImageResource(R.drawable.blackstar);
					((ImageView) findViewById(R.id.star4_r))
							.setImageResource(R.drawable.blackstar);
					((ImageView) findViewById(R.id.star5_r))
							.setImageResource(R.drawable.blackstar);

				}
			}
		});

		// getAccountDetails();
	}

	/*
	 * Initializing the views
	 */
	private void initViews() {

		scrollLayout = (ScrollView) findViewById(R.id.scrollLayout);
		mProfileJobAdapter = new ProfileJobAdapter(null, getLayoutInflater());

		txtshield = (ImageView) findViewById(R.id.txtshield);
		provided_services = (TextView) findViewById(R.id.provided_services);
		provided_services.setVisibility(View.VISIBLE);
		btnSetting = (TextView) findViewById(R.id.btnBack);
		txttotaljobcount = (TextView) findViewById(R.id.txttotaljobcount);
		title = (TextView) findViewById(R.id.title);
		btnedit = (TextView) findViewById(R.id.btnedit);
		user_name = (TextView) findViewById(R.id.user_name);
		textView5 = (TextView) findViewById(R.id.textView5);
		username = (TextView) findViewById(R.id.username);
		txtaddress = (TextView) findViewById(R.id.txtaddress);
		txtemail = (TextView) findViewById(R.id.txtemail);
		btnProvider = (Button) findViewById(R.id.btnProvider);
		// ratingbar=(RatingBar)findViewById(R.id.ratingBar1);
		btnRequest = (Button) findViewById(R.id.btnRequest);
		karma1 = (ImageView) findViewById(R.id.karma1);
		karma2 = (ImageView) findViewById(R.id.karma2);
		karma3 = (ImageView) findViewById(R.id.karma3);
		level1 = (ImageView) findViewById(R.id.level1);
		level2 = (ImageView) findViewById(R.id.level2);
		level3 = (ImageView) findViewById(R.id.level3);
		image = (CircleImageView) findViewById(R.id.imgView);
		pbar = (ProgressBar) findViewById(R.id.progressBar1);
		job_listView = (ListView) findViewById(R.id.job_listView);
		job_listView.setOnTouchListener(this);
		img_close = (ImageView) findViewById(R.id.img_close);
		img_close.setOnClickListener(this);
		list_parent_view = (LinearLayout) findViewById(R.id.list_parent_view);

		job_listView.setAdapter(mProfileJobAdapter);

		listItemView = (LinearLayout) findViewById(R.id.list_item_view);
		btnProvider.setOnClickListener(this);
		btnRequest.setOnClickListener(this);
		btnedit.setOnClickListener(this);
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

		// Getting new Courier & Home Categories
		Constants.courierCategories.clear();
		Constants.homeCategories.clear();

		if (Constants.isNetAvailable(ProfileActivity.this)) {
			new GettingCategories().execute(new String[] { "Courier" });
			new GettingCategories().execute(new String[] { "Home" });

			ServerCommunication download = new ServerCommunication();
			download.execute(new String[] { "" });

		} else {
			Constants.NoInternetError(ProfileActivity.this);
		}
	}

	/*
	 * Called to initialize to user interface.
	 */

	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		if (v == btnRequest) {

			isProvierJob = "completed_provider_job";

			/*
			 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			 * android.R.layout.simple_list_item_1, android.R.id.text1, list);
			 * 
			 * job_listView.setAdapter(adapter);
			 */
			// btnRequest.setBackgroundResource(R.drawable.tab_right_s);
			// btnProvider.setBackgroundResource(R.drawable.tab_left);
			btnRequest.setSelected(true);
			btnProvider.setSelected(false);
			job_listView.setVisibility(View.VISIBLE);

			if (providedJobs.isEmpty()) {
				getJobList11 download1 = new getJobList11();
				download1.execute(new String[] { "" });
			} else {
				mProfileJobAdapter.refereshAdapter(providedJobs);
				setListViewHeightBasedOnChildren(job_listView);
			}

		} else if (v == btnProvider) {
			isProvierJob = "completed_customer_job";

			/*
			 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			 * android.R.layout.simple_list_item_1, android.R.id.text1, list);
			 * 
			 * job_listView.setAdapter(adapter);
			 */
			job_listView.setVisibility(View.VISIBLE);
			btnProvider.setSelected(true);
			btnRequest.setSelected(false);
			// btnRequest.setBackgroundResource(R.drawable.tab_right);
			// btnProvider.setBackgroundResource(R.drawable.tab_left_s);

			if (requestedJobs.isEmpty()) {
				getJobList11 download1 = new getJobList11();
				download1.execute(new String[] { "" });
			} else {
				mProfileJobAdapter.refereshAdapter(requestedJobs);
				setListViewHeightBasedOnChildren(job_listView);
			}
		} else if (v == btnedit) {
			// startActivity(new Intent(ProfileActivity.this,
			// EditUserProfile.class));

		} else if (v == btnSetting) {
			finish();
			// startActivity(new
			// Intent(ProfileActivity.this,SettingsActivity.class));
		}
		if (v == img_close) {
			listItemView.setVisibility(View.GONE);
			job_listView.setVisibility(View.VISIBLE);
		}
	}

	private void openCC() {
		if (Constants.jobsModelList != null
				&& Constants.jobsModelList.size() > 0) {
			JobsModel jobs = (JobsModel) Constants.jobsModelList.get(0);
			JobData data[] = jobs.getJobData();
			if (jobs.getJobData().length == 1) {
				Intent intent = new Intent(ProfileActivity.this,
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
	 * To download data from server.
	 */
	private class ServerCommunication extends
			AsyncTask<String, String, UserDetail> {
		private final ProgressDialog progressBar = new ProgressDialog(
				ProfileActivity.this);

		public ServerCommunication() {
			// TODO Auto-generated constructor stub
			progressBar.setCancelable(false);
		}

		@Override
		protected UserDetail doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", Constants.uid));

			UserDataPostParser parser = new UserDataPostParser(
					Constants.HTTP_HOST + "viewprofile");
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
				Log.e("help", "refereshAdapter Call");

			} else if (msg.arg1 == FAILURE) {
				showAlertDialog("Please try again.");

			}
			setListViewHeightBasedOnChildren(job_listView);
		}
	};

	private void showProfileDetail(UserDetail detail) {

		// To update the security shield
		/*
		 * shieldType=getIntent().getStringExtra("ShieldType");
		 * if(shieldType.equals("blue"))
		 * txtshield.setBackgroundResource(R.drawable.shieldx); else
		 * if(shieldType.equals("silver"))
		 * txtshield.setBackgroundResource(R.drawable.shield_silver); else
		 * if(shieldType.equals("gold"))
		 * txtshield.setBackgroundResource(R.drawable.shield_gold);
		 */

		if (detail.getSecurity().equals("blue"))
			txtshield.setImageResource(R.drawable.shieldx);
		else if (detail.getSecurity().equals("silver"))
			txtshield.setImageResource(R.drawable.shield_silver);
		else if (detail.getSecurity().equals("gold"))
			txtshield.setImageResource(R.drawable.shield_gold);

		Log.d("Security Response", detail.getSecurity());

		username.setText(detail.getUsername() + " | ");
		user_name.setText(detail.getFirst_name() + " " + detail.getLast_name());

		if (!detail.getSJCount().equals(""))
			showLevel(detail.getSJCount());

		if (!detail.getKarmaCount().equals(""))
			showKarma(detail.getKarmaCount());

		txtemail.setText(detail.getEmail());
		Log.e("help",
				"Check Val >> " + detail.getCJCount() + " >> "
						+ detail.getSJCount());
		btnProvider.setText(detail.getCJCount() + " Services Provided");
		btnRequest.setText(detail.getSJCount() + " Services Requested");
		try {
			int total_count = Integer.valueOf(detail.getSJCount())
					+ Integer.valueOf(detail.getCJCount());
			txttotaljobcount.setText("(" + total_count + ")");
		} catch (Exception e) {

		}

		float a = 0.0f;
		Log.d("VIPI", "getAvg >" + detail.getAvg_rating());
		if (!(detail.getAvg_rating().equals(""))
				&& !(detail.getAvg_rating().equals(" "))
				&& (detail.getAvg_rating() != null)) {
			a = Float.valueOf(detail.getAvg_rating());
		}

		int b = Math.round(a);
		int c = (int) b;
		Log.d("VIPI", "a >" + String.valueOf(a));
		Log.d("VIPI", "c >" + String.valueOf(c));
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

		} else if (c != 0) {
			star1.setImageResource(R.drawable.yellowstar);
			star2.setImageResource(R.drawable.yellowstar);
			star3.setImageResource(R.drawable.yellowstar);
			star4.setImageResource(R.drawable.yellowstar);
			star5.setImageResource(R.drawable.yellowstar);
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

		if (!detail.getcouriercategory().equals("")
				|| !detail.gethomecategory().equals("")) {

			String mstring = "My areas of expertise include ";

			if (detail.getcouriercategory().equals(""))
				mstring = mstring + detail.gethomecategory() + ".";
			else if (detail.gethomecategory().equals(""))
				mstring = mstring + detail.getcouriercategory() + ".";
			else
				mstring = mstring + detail.getcouriercategory() + " and "
						+ detail.gethomecategory();
			Log.d("Preferred services", mstring);
			provided_services.setText(mstring);
		}
	}

	private void showLevel(String strLvlCount) {

		int point = Integer.parseInt(strLvlCount);
		Log.e("VIPI", "Point >> " + point);
		if (0 == point) {
			Log.e("VIPI", "Point 1>> " + point);
			level1.setImageResource(R.drawable.chalice_d);
			level2.setImageResource(R.drawable.chalice_10_d);
			level3.setImageResource(R.drawable.chalice_20_d);

		} else if (1 <= point && point <= 9) {
			Log.e("VIPI", "Point 2>> " + point);
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_10_d);
			level3.setImageResource(R.drawable.chalice_20_d);

		} else if (10 <= point && point <= 19) {
			Log.e("VIPI", "Point 3>> " + point);
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_10);
			level3.setImageResource(R.drawable.chalice_20_d);

		} else if (20 <= point && point <= 29) {
			Log.e("VIPI", "Point 4>> " + point);
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_20);
			level3.setImageResource(R.drawable.chalice_30_d);

		} else if (30 <= point && point <= 39) {
			Log.e("VIPI", "Point 5>> " + point);
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_30);
			level3.setImageResource(R.drawable.chalice_40_d);

		} else if (40 <= point && point <= 49) {
			Log.e("VIPI", "Point 6>> " + point);
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_40);
			level3.setImageResource(R.drawable.chalice_50_d);

		} else if (50 <= point && point <= 59) {
			Log.e("VIPI", "Point 7>> " + point);
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_50);
			level3.setImageResource(R.drawable.chalice_60_d);

		} else if (60 <= point && point <= 69) {
			Log.e("VIPI", "Point 8>> " + point);
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_60);
			level3.setImageResource(R.drawable.chalice_70_d);

		} else if (70 <= point && point <= 79) {
			Log.e("VIPI", "Point 9>> " + point);
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_70);
			level3.setImageResource(R.drawable.chalice_80_d);

		} else if (80 <= point && point <= 89) {
			Log.e("VIPI", "Point 10>> " + point);
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_80);
			level3.setImageResource(R.drawable.chalice_90_d);

		} else if (90 <= point && point <= 99) {
			Log.e("VIPI", "Point 11>> " + point);
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_90);
			level3.setImageResource(R.drawable.chalice_silver_d);

		} else if (100 <= point && point <= 199) {
			Log.e("VIPI", "Point 12>> " + point);
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_silver);
			level3.setImageResource(R.drawable.chalice_gold_d);

		} else if (200 <= point && point <= 299) {
			Log.e("VIPI", "Point 13>> " + point);
			level1.setImageResource(R.drawable.chalice_silver);
			level2.setImageResource(R.drawable.chalice_gold);
			level3.setImageResource(R.drawable.chalice_red_d);

		} else if (300 <= point) {
			Log.e("VIPI", "Point 14>> " + point);
			level1.setImageResource(R.drawable.chalice_silver);
			level2.setImageResource(R.drawable.chalice_gold);
			level3.setImageResource(R.drawable.chalice_red);

		}

	}

	private void showKarma(String strKarCount) {

		int point = Integer.parseInt(strKarCount);

		if (0 == point) {
			karma1.setImageResource(R.drawable.crown_d);
			karma2.setImageResource(R.drawable.crown_10_d);
			karma3.setImageResource(R.drawable.crown_20_d);
		} else if (1 <= point && point <= 9) {

			karma1.setImageResource(R.drawable.crown);
			karma2.setImageResource(R.drawable.crown_10_d);
			karma3.setImageResource(R.drawable.crown_20_d);

		} else if (10 <= point && point <= 19) {

			karma1.setImageResource(R.drawable.crown);
			karma2.setImageResource(R.drawable.crown_10);
			karma3.setImageResource(R.drawable.crown_20_d);

		} else if (20 <= point && point <= 29) {

			karma1.setImageResource(R.drawable.crown);
			karma2.setImageResource(R.drawable.crown_20);
			karma3.setImageResource(R.drawable.crown_30_d);

		} else if (30 <= point && point <= 39) {

			karma1.setImageResource(R.drawable.crown);
			karma2.setImageResource(R.drawable.crown_30);
			karma3.setImageResource(R.drawable.crown_40_d);

		} else if (40 <= point && point <= 49) {

			karma1.setImageResource(R.drawable.crown);
			karma2.setImageResource(R.drawable.crown_40);
			karma3.setImageResource(R.drawable.crown_50_d);

		} else if (50 <= point && point <= 59) {

			karma1.setImageResource(R.drawable.crown);
			karma2.setImageResource(R.drawable.crown_50);
			karma3.setImageResource(R.drawable.crown_60_d);

		} else if (60 <= point && point <= 69) {

			karma1.setImageResource(R.drawable.crown);
			karma2.setImageResource(R.drawable.crown_60);
			karma3.setImageResource(R.drawable.crown_70_d);

		} else if (70 <= point && point <= 79) {

			karma1.setImageResource(R.drawable.crown);
			karma2.setImageResource(R.drawable.crown_70);
			karma3.setImageResource(R.drawable.crown_80_d);

		} else if (80 <= point && point <= 89) {

			karma1.setImageResource(R.drawable.crown);
			karma2.setImageResource(R.drawable.crown_80);
			karma3.setImageResource(R.drawable.crown_90_d);

		} else if (90 <= point && point <= 99) {

			karma1.setImageResource(R.drawable.crown);
			karma2.setImageResource(R.drawable.crown_90);
			karma3.setImageResource(R.drawable.crown_silver_d);

		} else if (100 <= point && point <= 199) {

			karma1.setImageResource(R.drawable.crown);
			karma2.setImageResource(R.drawable.crown_silver);
			karma3.setImageResource(R.drawable.crown_gold_d);

		} else if (200 <= point && point <= 299) {

			karma1.setImageResource(R.drawable.crown_silver);
			karma2.setImageResource(R.drawable.crown_gold);
			karma3.setImageResource(R.drawable.crown_red_d);

		} else if (300 <= point) {
			karma1.setImageResource(R.drawable.crown_silver);
			karma2.setImageResource(R.drawable.crown_gold);
			karma3.setImageResource(R.drawable.crown_red);

		}
	}

	/*
	 * get Job List from server. ProfileJobParser is used for it. All the
	 * service requested and service provide jobs will display using this code.
	 */
	private class getJobList11 extends
			AsyncTask<String, String, ArrayList<JobsModel>> {
		private final ProgressDialog progressBar = new ProgressDialog(
				ProfileActivity.this);

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
			nameValuePairs
					.add(new BasicNameValuePair("user_id", Constants.uid));
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

	/*
	 * This asynchronous task gets the Courier & Home categories values from
	 * server and sets the values in corresponding adapters courierAdapter, &
	 * homeAdapter
	 */
	private class GettingCategories extends AsyncTask<String, String, String[]> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String[] doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			CategoryPostParser parser = null;
			if (strParams[0].equals("Courier"))
				parser = new CategoryPostParser(Constants.HTTP_HOST
						+ "viewcategories");
			else if (strParams[0].equals("Home"))
				parser = new CategoryPostParser(Constants.HTTP_HOST
						+ "viewHomecategories");

			ArrayList<Category> obj = parser.getParseData(nameValuePairs);

			for (int i = 0; i < obj.size(); i++) {
				if (strParams[0].equals("Courier"))
					Constants.courierCategories.add(obj.get(i).getName());
				else if (strParams[0].equals("Home"))
					Constants.homeCategories.add(obj.get(i).getName());
			}

			return null;
		}

		@Override
		protected void onPostExecute(String[] homeArray) {
			super.onPostExecute(homeArray);
			// Toast.makeText(getApplicationContext(),
			// homeArray[0]+", "+homeArray[1], 1).show();

		}
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		Log.e("help", "totalHeight Call > " + totalHeight);
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (v == job_listView) {
			int action = event.getAction();
			switch (action) {
			// case MotionEvent.ACTION_DOWN:
			// // Disallow ScrollView to intercept touch events.
			// v.getParent().requestDisallowInterceptTouchEvent(true);
			// break;
			//
			// case MotionEvent.ACTION_UP:
			// // Allow ScrollView to intercept touch events.
			// v.getParent().requestDisallowInterceptTouchEvent(false);
			// break;
			}
			// // Handle ListView touch events.
			// v.onTouchEvent(event);
			return false;
		}

		return false;
	}

}