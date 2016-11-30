package com.synapse.gofer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
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

import com.synapse.gofer.adapter.CommissionedJobListingAdapter;
import com.synapse.gofer.adapter.ProfileJobAdapter;
import com.synapse.gofer.control.CircleImageView;
import com.synapse.gofer.model.JobsModel;
import com.synapse.gofer.model.ProfileJobData;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.model.UserDetail;
import com.synapse.gofer.parser.ProfileJobParser;
import com.synapse.gofer.parser.UserDataPostParser;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.ImageProcessing;

public class CustomerDetailFragment extends Fragment implements
		OnClickListener, OnTouchListener {

	// public static final int SUCCESS = 300;
	// protected static final int FAILURE = 301;
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	public static final int SUCCESS_1 = 3;
	/*
	 * 
	 * New code for customer profile
	 */

	List<ProfileJobData> arrayOfList;
	private ProfileJobAdapter mProfileJobAdapter;
	// private TextView temp_karma_count, temp_level_testing;
	private Button btnPublic, btnJob, btnProvider, btnContact, btnRequest;
	private FragmentManager fragmentManager;
	private TextView btnSetting, title, btnedit, textView5, user_name,
			username, txtaddress, txtemail, txttotaljobcount,
			provided_services;
	private ScrollView scrollLayout;
	private ImageView karma1, karma2, karma3, level1, level2, level3, star1_c,
			star2_c, star3_c, star4_c, star5_c, star6_c, txtshield;
	private CircleImageView image;
	private URL image_url, url;
	private Bitmap bitImg = null;
	private ProgressBar pbar;
	private ListView job_listView;
	// private RatingBar ratingbar, ratingbar1;
	String[] values;
	private String isProvierJob = "";
	ArrayList list = null;

	ArrayList<ProfileJobData> providedJobs = new ArrayList<ProfileJobData>();
	ArrayList<ProfileJobData> requestedJobs = new ArrayList<ProfileJobData>();

	private LinearLayout listItemView;
	private RelativeLayout header;
	private static String visibleList;
	/*
	 * 
	 * old code
	 */
	private ListView jobListView = null;
	private RelativeLayout listViewHeader = null;
	// private RelativeLayout profile_other;
	private ScrollView scrollView = null;
	private Button btnJobListing = null;
	private LinearLayout photocontainer = null;
	private TextView txtDone = null;
	private CommissionedJobListingAdapter mJobListingAdapter = null;

	private ArrayList<Bitmap> photos = null;
	private String userType = "2";
	private String userId = "";
	boolean onlyJob;
	private Bitmap bitmap;

	private ImageView profileImage = null; // close;
	private ImageButton karmaPointLevel = null;
	private RatingBar mRatingBar = null;

	private TextView nickName = null, noOfSer = null, kPoints = null,
			verified = null;
	private TextView favQuote = null, homeLink = null, approLoc = null;
	ImageView img_close;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// return inflater.inflate(R.layout.fragment_customer_detail, container,
		// false);
		return inflater.inflate(R.layout.activity_profile, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUi(getArguments());

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

				((TextView) getView().findViewById(R.id.job_name1))
						.setText(listItem.getJobName());
				((TextView) getView().findViewById(R.id.job_category))
						.setText(listItem.getJobCategory());
				((TextView) getView().findViewById(R.id.job_feddback))
						.setText(listItem.getJobFeedback());

				// ratingbar1.setRating(Float.parseFloat(listItem.getJobRating()));
				Log.d("CustomerJobRating", listItem.getJobRating());

				float a = 0.0f;
				if (!(listItem.getJobRating().equals(""))
						&& !(listItem.getJobRating().equals(" "))
						&& (listItem.getJobRating() != null))

				{
					a = Float.valueOf(listItem.getJobRating());
				}

				else {
					a = 0.0f;
				}

				int b = Math.round(a);
				int c = (int) b;
				Log.d("a", String.valueOf(a));
				Log.d("c", String.valueOf(c));
				if (c == 5)

				{
					((ImageView) getView().findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star2_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star3_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star4_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star5_r))
							.setImageResource(R.drawable.yellowstar);

				} else if (c == 4) {
					((ImageView) getView().findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star2_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star3_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star4_r))
							.setImageResource(R.drawable.yellowstar);
				} else if (c == 3) {
					((ImageView) getView().findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star2_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star3_r))
							.setImageResource(R.drawable.yellowstar);

				} else if (c == 2) {
					((ImageView) getView().findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star2_r))
							.setImageResource(R.drawable.yellowstar);

				}

				else if (c == 1) {
					((ImageView) getView().findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);
				} else if (c != 0) {
					((ImageView) getView().findViewById(R.id.star1_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star2_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star3_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star4_r))
							.setImageResource(R.drawable.yellowstar);
					((ImageView) getView().findViewById(R.id.star5_r))
							.setImageResource(R.drawable.yellowstar);
				}
			}
		});

	}

	private void initUi(Bundle bundle) {
		mProfileJobAdapter = new ProfileJobAdapter(null,
				getLayoutInflater(bundle));
		scrollLayout = (ScrollView) getView().findViewById(R.id.scrollLayout);
		userId = bundle.getString("UserId");
		onlyJob = bundle.getBoolean("onlyJob");
		Log.e("VIP", "< C. Get ." + userId);
		userType = bundle.getString("UserType");

		photos = new ArrayList<Bitmap>();
		// listViewHeader =
		// (RelativeLayout)getView().findViewById(R.id.customerdetail_listview_header);
		job_listView = (ListView) getView().findViewById(R.id.job_listView);
		job_listView.setAdapter(mProfileJobAdapter);
		img_close = (ImageView) getView().findViewById(R.id.img_close);
		img_close.setOnClickListener(this);
		job_listView.setOnTouchListener(this);
		listItemView = (LinearLayout) getView().findViewById(
				R.id.list_item_view);
		karma1 = (ImageView) getView().findViewById(R.id.karma1);
		karma2 = (ImageView) getView().findViewById(R.id.karma2);
		karma3 = (ImageView) getView().findViewById(R.id.karma3);

		level1 = (ImageView) getView().findViewById(R.id.level1);
		level2 = (ImageView) getView().findViewById(R.id.level2);
		level3 = (ImageView) getView().findViewById(R.id.level3);

		image = (CircleImageView) getView().findViewById(R.id.imgView);
		// profile_other = (RelativeLayout) getView().findViewById(
		// R.id.profile_other);
		// temp_karma_count = (TextView) getView().findViewById(
		// R.id.temp_karma_count);
		user_name = (TextView) getView().findViewById(R.id.user_name);
		username = (TextView) getView().findViewById(R.id.username);
		// temp_level_testing = (TextView) getView().findViewById(
		// R.id.temp_level_testing);
		txtaddress = (TextView) getView().findViewById(R.id.txtaddress);
		txtshield = (ImageView) getView().findViewById(R.id.txtshield);
		// ratingbar1 = (RatingBar) getView().findViewById(R.id.job_rating);
		txtemail = (TextView) getView().findViewById(R.id.txtemail);
		btnProvider = (Button) getView().findViewById(R.id.btnProvider);
		btnRequest = (Button) getView().findViewById(R.id.btnRequest);
		star1_c = (ImageView) getView().findViewById(R.id.star1);
		star2_c = (ImageView) getView().findViewById(R.id.star2);
		star3_c = (ImageView) getView().findViewById(R.id.star3);
		star4_c = (ImageView) getView().findViewById(R.id.star4);
		star5_c = (ImageView) getView().findViewById(R.id.star5);
		btnProvider.setOnClickListener(this);
		btnRequest.setOnClickListener(this);
		txttotaljobcount = (TextView) getView().findViewById(
				R.id.txttotaljobcount);
		// mRatingBar = (RatingBar)getView().findViewById(R.id.ratingBar1_c);
		// close = (ImageView) getView().findViewById(R.id.close_view);
		header = (RelativeLayout) getView().findViewById(R.id.headerLayout);
		header.setVisibility(View.GONE);
		provided_services = (TextView) getView().findViewById(
				R.id.provided_services);
		provided_services.setVisibility(View.VISIBLE);
		// profile_other.setVisibility(View.INVISIBLE);

		if (Constants.isNetAvailable(getActivity())) {
			ServerCommunication server = new ServerCommunication();
			server.execute(new String[] { "" });
		} else {
			Constants.NoInternetError(getActivity());
		}
	}

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
			btnRequest.setSelected(true);
			btnProvider.setSelected(false);
			job_listView.setVisibility(View.VISIBLE);

			if (providedJobs.isEmpty()) {

				if (Constants.isNetAvailable(getActivity())) {
					getJobList11 download1 = new getJobList11();
					download1.execute(new String[] { "" });
				} else {
					Constants.NoInternetError(getActivity());
				}

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
			btnRequest.setSelected(false);
			btnProvider.setSelected(true);

			if (requestedJobs.isEmpty()) {
				getJobList11 download1 = new getJobList11();
				download1.execute(new String[] { "" });
			} else {
				mProfileJobAdapter.refereshAdapter(requestedJobs);
				setListViewHeightBasedOnChildren(job_listView);
			}
		}
		if (v == img_close) {
			listItemView.setVisibility(View.GONE);
			job_listView.setVisibility(View.VISIBLE);
		}
	}

	/*
	 * Register handler.
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
						showcustomerDetail(data);
					}
				}

			} else if (msg.arg1 == SUCCESS_1) {

				ArrayList<ProfileJobData> dataList = (ArrayList<ProfileJobData>) msg.obj;
				Log.d("dataList1 ", "dataList1 " + dataList);
				if (isProvierJob.equals("completed_provider_job")) {
					providedJobs = dataList;

				} else {

					requestedJobs = dataList;
				}

				if (dataList == null) {
					showAlertDialog("No Job found.");
					return;
				}

				mProfileJobAdapter.refereshAdapter(dataList);

			}

			else if (msg.arg1 == FAILURE) {
				showAlertDialog("Please try again.");
			}
			setListViewHeightBasedOnChildren(job_listView);
		}
	};

	private void showAlertDialog(String s) {
		Context context = getActivity().getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();
	}

	private void showcustomerDetail(UserDetail userDetail) {

		// To update the security shield
		if (userDetail.getSecurity().equals("blue"))
			txtshield.setImageResource(R.drawable.shieldx);
		else if (userDetail.getSecurity().equals("silver"))
			txtshield.setImageResource(R.drawable.shield_silver);
		else if (userDetail.getSecurity().equals("gold"))
			txtshield.setImageResource(R.drawable.shield_gold);

		username.setText(userDetail.getUsername() + " | ");
		user_name.setText(userDetail.getFirst_name() + " "
				+ userDetail.getLast_name());
		// temp_karma_count.setText(userDetail.getKarmaCount() + " Points");
		// temp_level_testing.setText(userDetail.getSJCount() + " Points");
		txtaddress.setText(" " + userDetail.getAddress3() + ", "
				+ userDetail.getAddress4());
		txtemail.setText(userDetail.getEmail());
		btnProvider.setText(userDetail.getCJCount() + " Services Provided");
		btnRequest.setText(userDetail.getSJCount() + " Services Requested");

		if (!userDetail.getKarmaCount().equals(""))
			showKarma(userDetail.getKarmaCount());

		if (!userDetail.getSJCount().equals(""))
			showLevel(userDetail.getSJCount());

		int total_count, temp1, temp2;
		if (userDetail.getSJCount() != "")
			temp1 = Integer.valueOf(userDetail.getSJCount());
		else
			temp1 = 0;

		if (userDetail.getCJCount() != "")
			temp2 = Integer.valueOf(userDetail.getCJCount());
		else
			temp2 = 0;

		total_count = Integer.valueOf(temp1) + Integer.valueOf(temp2);

		txttotaljobcount.setText("(" + total_count + ")");

		if (userDetail.getAvg_rating() != "") {
			float a = Float.parseFloat(userDetail.getAvg_rating());
			int b = Math.round(a);
			int c = (int) b;
			Log.d("a", String.valueOf(a));
			Log.d("c", String.valueOf(c));
			if (c == 0) {
				star1_c.setImageResource(R.drawable.star);
				star2_c.setImageResource(R.drawable.star);
				star3_c.setImageResource(R.drawable.star);
				star4_c.setImageResource(R.drawable.star);
				star5_c.setImageResource(R.drawable.star);
			} else if (c == 5) {
				star1_c.setImageResource(R.drawable.yellowstar);
				star2_c.setImageResource(R.drawable.yellowstar);
				star3_c.setImageResource(R.drawable.yellowstar);
				star4_c.setImageResource(R.drawable.yellowstar);
				star5_c.setImageResource(R.drawable.yellowstar);

			} else if (c == 4) {
				star1_c.setImageResource(R.drawable.yellowstar);
				star2_c.setImageResource(R.drawable.yellowstar);
				star3_c.setImageResource(R.drawable.yellowstar);
				star4_c.setImageResource(R.drawable.yellowstar);
			} else if (c == 3) {
				star1_c.setImageResource(R.drawable.yellowstar);
				star2_c.setImageResource(R.drawable.yellowstar);
				star3_c.setImageResource(R.drawable.yellowstar);

			} else if (c == 2) {
				star1_c.setImageResource(R.drawable.yellowstar);
				star2_c.setImageResource(R.drawable.yellowstar);
			}

			else if (c == 1) {
				star1_c.setImageResource(R.drawable.yellowstar);
			}

		}
		// mRatingBar.setRating(Float.valueOf(userDetail.getAvg_rating()));

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
		String mstring = getString(R.string.detailstr) + " "
				+ userDetail.getcouriercategory() + ',' + ' '
				+ userDetail.gethomecategory();
		provided_services.setText(mstring);

		if (userDetail.getcouriercategory().equals("")
				&& userDetail.gethomecategory().equals("")) {
			provided_services.setVisibility(View.GONE);
		}

	}

	private class getJobList11 extends
			AsyncTask<String, String, ArrayList<JobsModel>> {
		private final ProgressDialog progressBar = new ProgressDialog(
				getActivity());

		public getJobList11() {
			// TODO Auto-generated constructor stub
			progressBar.setCancelable(false);
		}

		@Override
		protected ArrayList<JobsModel> doInBackground(String... strParams) {
			ProfileJobParser parser;

			if (isProvierJob.equals("completed_provider_job"))
				parser = new ProfileJobParser(Constants.HTTP_HOST
						+ "getProviderCompletedJob");
			else
				parser = new ProfileJobParser(Constants.HTTP_HOST
						+ "getCustomerCompletedJob");

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_id", userId));
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

	public class ServerCommunication extends
			AsyncTask<String, String, UserDetail> {
		private final ProgressDialog progressBar = new ProgressDialog(
				getActivity());

		public ServerCommunication() {
			progressBar.setCancelable(false);
		}

		@Override
		protected UserDetail doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			Log.e("VIP", " Condition >> " + onlyJob);
			if (!onlyJob)
				nameValuePairs.add(new BasicNameValuePair("id", userId));
			else
				nameValuePairs.add(new BasicNameValuePair("user_id", userId));
			// nameValuePairs.add(new BasicNameValuePair("user_type",
			// userType));

			Log.e("VIP", "Profile > " + Constants.HTTP_HOST + "viewprofile"
					+ " ID " + userId);
			UserDataPostParser parser = new UserDataPostParser(
					Constants.HTTP_HOST + "viewprofile");
			UserDetail data = parser.getParseData(nameValuePairs);
			if (data != null) {
				bitmap = ImageProcessing.downloadImage(data.getImage_big());
			}
			Log.d("USER NAME", "" + data.getUsername());
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

	@SuppressLint("ClickableViewAccessibility")
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
		Log.e("VIP", "totalHeight Call > " + totalHeight);
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
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

	private void showLevel(String strLvlCount) {

		int point = Integer.parseInt(strLvlCount);
		Log.e("VIPI", "showLevel >> " + point);
		if (0 == point) {
			Log.e("VIPI", "showLevel >> 0");
			level1.setImageResource(R.drawable.chalice_d);
			level2.setImageResource(R.drawable.chalice_10_d);
			level3.setImageResource(R.drawable.chalice_20_d);

		} else if (1 <= point && point <= 9) {
			Log.e("VIPI", "showLevel >> 1");
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_10_d);
			level3.setImageResource(R.drawable.chalice_20_d);

		} else if (10 <= point && point <= 19) {
			Log.e("VIPI", "showLevel >> 2");
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_10);
			level3.setImageResource(R.drawable.chalice_20_d);

		} else if (20 <= point && point <= 29) {
			Log.e("VIPI", "showLevel >> 3");
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_20);
			level3.setImageResource(R.drawable.chalice_30_d);

		} else if (30 <= point && point <= 39) {
			Log.e("VIPI", "showLevel >> 4");
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_30);
			level3.setImageResource(R.drawable.chalice_40_d);

		} else if (40 <= point && point <= 49) {
			Log.e("VIPI", "showLevel >> 5");
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_40);
			level3.setImageResource(R.drawable.chalice_50_d);

		} else if (50 <= point && point <= 59) {
			Log.e("VIPI", "showLevel >> 6");
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_50);
			level3.setImageResource(R.drawable.chalice_60_d);

		} else if (60 <= point && point <= 69) {
			Log.e("VIPI", "showLevel >> 7");
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_60);
			level3.setImageResource(R.drawable.chalice_70_d);

		} else if (70 <= point && point <= 79) {
			Log.e("VIPI", "showLevel >> 8");
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_70);
			level3.setImageResource(R.drawable.chalice_80_d);

		} else if (80 <= point && point <= 89) {
			Log.e("VIPI", "showLevel >> 9");
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_80);
			level3.setImageResource(R.drawable.chalice_90_d);

		} else if (90 <= point && point <= 99) {
			Log.e("VIPI", "showLevel >> 10");
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_90);
			level3.setImageResource(R.drawable.chalice_silver_d);

		} else if (100 <= point && point <= 199) {
			Log.e("VIPI", "showLevel >> 11");
			level1.setImageResource(R.drawable.chalice);
			level2.setImageResource(R.drawable.chalice_silver);
			level3.setImageResource(R.drawable.chalice_gold_d);

		} else if (200 <= point && point <= 299) {
			Log.e("VIPI", "showLevel >> 12");
			level1.setImageResource(R.drawable.chalice_silver);
			level2.setImageResource(R.drawable.chalice_gold);
			level3.setImageResource(R.drawable.chalice_red_d);

		} else if (300 <= point) {
			Log.e("VIPI", "showLevel >> 13");
			level1.setImageResource(R.drawable.chalice_silver);
			level2.setImageResource(R.drawable.chalice_gold);
			level3.setImageResource(R.drawable.chalice_red);

		}

	}
}