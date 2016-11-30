package com.synapse.gofer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.gofer.adapter.ServicesAdapter;
import com.synapse.gofer.control.CircleImageView;
import com.synapse.gofer.http.HttpPostConnector;
import com.synapse.gofer.model.Category;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.model.ServicesData;
import com.synapse.gofer.model.UserDetail;
import com.synapse.gofer.parser.UserDataPostParser;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.ImageProcessing;

public class EditUserProfile extends Activity implements OnClickListener {
	public static final String TAG = "EditUserProfile";
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	public static final int DATA_UPDATE = 3;
	public static final int DATA_FAIL = 4;
	private final int REQUEST_CAMERA = 1001;
	private final int REQUEST_GALLARY = 1002;
	List<ServicesData> arrayOfList;
	private ServicesAdapter mServicesData;
	private boolean isHomeCategory;
	private boolean isToggleClicked;

	private ProgressDialog progressBar1;
	private CircleImageView image;
	private URL image_url;
	private Bitmap bitImg = null, bitmap;
	private ScrollView scrollLayout;
	private String email, about;
	private ListView serviceList;
	private Switch categrory_switch;
	RadioButton btnCourier, btnHome;
	private Button btnSaveChanges, btnChangePass, btnCreditCard,
			btnBankDetails;
	private TextView txtEditProfile, txtProfile, txtBankDetail, txtshield;
	private EditText edtFirstName, edtLastName, edtUserName, edtAbout,
			edtAddress1, edtAddress2, edtCity, edtState, edtZip,
			edtAppAddress1, edtAppAddress2, edtAppCity, edtAppState, edtAppZip,
			edtChangePass, edtCreditCard, edtBankDetails;

	ListView courierList, homeList;
	ArrayAdapter<String> courierAdapter, homeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_user_profile);
		initViews();
	}

	private void initViews() {
		String[] courierCategories = new String[Constants.courierCategories
				.size()];
		for (int i = 0; i < Constants.courierCategories.size(); i++) {
			courierCategories[i] = Constants.courierCategories.get(i);
		}

		// String[] courierCategories = (String[])
		// Constants.courierCategories.toArray()
		courierList = (ListView) findViewById(R.id.courier_list);
		courierAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice,
				courierCategories);
		courierList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		courierList.setAdapter(courierAdapter);
		Log.d("Courier List", "" + courierAdapter);
		Log.d("Courier List1", courierAdapter.getPosition("PHARMACY PICKUP")
				+ "");

		courierList.setOnTouchListener(new OnTouchListener() {
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

		String[] homeCategories = new String[Constants.homeCategories.size()];
		for (int i = 0; i < Constants.homeCategories.size(); i++) {
			homeCategories[i] = Constants.homeCategories.get(i);
		}

		homeList = (ListView) findViewById(R.id.home_list);
		homeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice,
				homeCategories);
		homeList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		homeList.setAdapter(homeAdapter);

		homeList.setOnTouchListener(new OnTouchListener() {
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

		/*
		 * serviceList=(ListView)findViewById(R.id.serviceList); mServicesData =
		 * new ServicesAdapter(null, getLayoutInflater());
		 * serviceList.setAdapter(mServicesData);
		 */

		categrory_switch = (Switch) findViewById(R.id.categrory_switch);
		txtBankDetail = (TextView) findViewById(R.id.txtBankDetail);
		edtAddress1 = (EditText) findViewById(R.id.edtAddress1);
		edtAddress2 = (EditText) findViewById(R.id.edtAddress2);
		edtCity = (EditText) findViewById(R.id.edtCity);
		edtState = (EditText) findViewById(R.id.edtState);
		edtZip = (EditText) findViewById(R.id.edtZip);
		edtAppAddress1 = (EditText) findViewById(R.id.edtAppAddress1);
		edtAppAddress2 = (EditText) findViewById(R.id.edtAppAddress2);
		edtAppCity = (EditText) findViewById(R.id.edtAppCity);
		edtAppState = (EditText) findViewById(R.id.edtAppState);
		edtAppZip = (EditText) findViewById(R.id.edtAppZip);
		edtChangePass = (EditText) findViewById(R.id.edtChangePass);
		// edtCreditCard=(EditText)findViewById(R.id.edtCreditCard); //STRIPE
		edtBankDetails = (EditText) findViewById(R.id.edtBankDetails);

		btnCourier = (RadioButton) findViewById(R.id.btnCourier);
		btnHome = (RadioButton) findViewById(R.id.btnHome);

		btnCourier.setOnClickListener(this);
		btnHome.setOnClickListener(this);

		btnCourier.setSelected(true);

		image = (CircleImageView) findViewById(R.id.imgView);
		scrollLayout = (ScrollView) findViewById(R.id.scrollLayout);
		scrollLayout.setVisibility(View.INVISIBLE);
		txtProfile = (TextView) findViewById(R.id.txtProfile);
		txtEditProfile = (TextView) findViewById(R.id.txtEditProfile);

		btnChangePass = (Button) findViewById(R.id.btnChangePass);
		// btnCreditCard=(Button)findViewById(R.id.btnCreditCard); //STRIPE
		btnBankDetails = (Button) findViewById(R.id.btnBankDetails);

		btnSaveChanges = (Button) findViewById(R.id.btnSaveChanges);
		txtshield = (TextView) findViewById(R.id.txtshield);
		edtAbout = (EditText) findViewById(R.id.edtAbout);
		edtFirstName = (EditText) findViewById(R.id.edtFirstName);
		edtLastName = (EditText) findViewById(R.id.edtLastName);
		edtUserName = (EditText) findViewById(R.id.edtUserName);
		txtProfile.setOnClickListener(this);
		txtshield.setOnClickListener(this);
		txtEditProfile.setOnClickListener(this);
		btnSaveChanges.setOnClickListener(this);
		btnChangePass.setOnClickListener(this);
		// btnCreditCard.setOnClickListener(this); //STRIPE
		btnBankDetails.setOnClickListener(this);
		categrory_switch.setOnClickListener(this);

		// Code used for list view

		if (Constants.isNetAvailable(EditUserProfile.this)) {
			ServerCommunication download = new ServerCommunication();
			download.execute(new String[] { "" });
		} else {
			Constants.NoInternetError(EditUserProfile.this);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSaveChanges) {

			// Getting all selected courier & home list values
			SparseBooleanArray checkedCourierPosition = courierList
					.getCheckedItemPositions();
			ArrayList<String> checkedCourierCategories = new ArrayList<String>();
			Log.d("Size", checkedCourierPosition.size() + "");
			for (int i = 0; i < checkedCourierPosition.size(); i++) {
				// Item position in adapter
				int position = checkedCourierPosition.keyAt(i);

				// Add sport if it is checked i.e.) == TRUE!
				if (checkedCourierPosition.valueAt(i)) {
					checkedCourierCategories.add(courierAdapter
							.getItem(position));
				}
			}

			String courierStr = "";
			for (int i = 0; i < checkedCourierCategories.size(); i++) {
				if (courierStr == "")
					courierStr = checkedCourierCategories.get(i);
				else
					courierStr += ", " + checkedCourierCategories.get(i);

			}

			Log.d("Courier List", courierStr);
			SparseBooleanArray checkedHomePosition = homeList
					.getCheckedItemPositions();
			ArrayList<String> checkedHomeCategories = new ArrayList<String>();
			for (int i = 0; i < checkedHomePosition.size(); i++) {
				// Item position in adapter
				int position = checkedHomePosition.keyAt(i);
				// Add sport if it is checked i.e.) == TRUE!
				if (checkedHomePosition.valueAt(i))
					checkedHomeCategories.add(homeAdapter.getItem(position));
			}

			String homeStr = "";
			for (int i = 0; i < checkedHomeCategories.size(); i++) {
				if (homeStr == "")
					homeStr = checkedHomeCategories.get(i);
				else
					homeStr += ", " + checkedHomeCategories.get(i);
			}

			Log.d("Home List", homeStr);
			if (checkedCourierCategories.size() + checkedHomeCategories.size() > 4) {
				// showAlertDialog("Please select atmost four categoeries in Courier and Home sections.");
				AlertDialog alertDialog = new AlertDialog.Builder(this)
						.create();
				alertDialog
						.setMessage("Please select maximum four categoeries in Courier and Home sections.");
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				alertDialog.show();
			} else {
				if (validate()) {
					doUpdate(edtUserName.getText().toString(), edtFirstName
							.getText().toString(), edtLastName.getText()
							.toString(), edtAbout.getText().toString(), email,
							edtAddress1.getText().toString(), edtAddress2
									.getText().toString(), edtCity.getText()
									.toString(), edtState.getText().toString(),
							edtZip.getText().toString(), edtAppAddress1
									.getText().toString(), edtAppAddress2
									.getText().toString(), edtAppCity.getText()
									.toString(), edtAppState.getText()
									.toString(),
							edtAppZip.getText().toString(), courierStr, homeStr);
				}
			}

		} else if (v == txtProfile) {
			onBackPressed();
			// startActivity(new
			// Intent(EditUserProfile.this,ProfileActivity.class));
		} else if (v == txtEditProfile) {
			selectImage();
		} else if (v == txtshield) {
			// selectImage();
			String to = "gofer.backgroundcheck@Crowdserviceinc.com";
			String subject = "Background Check Request";
			Intent email = new Intent(Intent.ACTION_SEND);
			email.setType("text/html");
			email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
			email.putExtra(Intent.EXTRA_SUBJECT, subject);
			email.setType("message/rfc822");
			startActivity(Intent.createChooser(email,
					"Choose an Email client :"));

		} else if (v == btnChangePass) {
			if (!(edtChangePass.getText().toString() == null)
					&& !(edtChangePass.getText().toString().length() < 6)
					&& !(edtChangePass.getText().toString().equals(""))) {

				if (Constants.isNetAvailable(EditUserProfile.this)) {
					changePassword download = new changePassword();
					download.execute(new String[] { "" });
				} else {
					Constants.NoInternetError(EditUserProfile.this);
				}

			} else if (edtChangePass.getText().toString().equals("")) {
				showAlertDialog("Password field can not be blank");
			} else if (edtChangePass.getText().toString().length() < 6) {
				showAlertDialog("Password must contain atleast 6 digit");
			}
		} else if (v == btnCreditCard) {
			Intent intent = new Intent(EditUserProfile.this,
					StripeRegistrationActivity.class);
			startActivity(intent);

			// selectImage();
		} else if (v == btnBankDetails) {
			// accountRegisterAlert(); // STRIPE

			// PAYPAL
			progressBar1 = new ProgressDialog(EditUserProfile.this);
			progressBar1.setMessage("Please wait while loading...");
			progressBar1.setCancelable(false);
			progressBar1.show();
			Intent intent = new Intent(EditUserProfile.this,
					PayPalDetailsActivity.class);
			startActivity(intent);
			progressBar1.cancel();

			// selectImage();
		}

		else if (v == categrory_switch) {
			if (categrory_switch.isChecked()) {
				// Toast.makeText(getApplicationContext(), "Home", 1).show();
				// Modified by Ksolves07

				homeList.setVisibility(View.VISIBLE);
				courierList.setVisibility(View.GONE);
				/*
				 * isHomeCategory = true; ServicesList2 download=new
				 * ServicesList2(); download.execute(new String[]{""});
				 */

			} else {
				// Toast.makeText(getApplicationContext(), "Courier", 1).show();
				// Modified by Ksolves07
				courierList.setVisibility(View.VISIBLE);
				homeList.setVisibility(View.GONE);
				/*
				 * isHomeCategory = false; ServicesList2 download=new
				 * ServicesList2(); download.execute(new String[]{""});
				 */

			}

		}

		if (v == btnCourier) {
			courierList.setVisibility(View.VISIBLE);
			homeList.setVisibility(View.GONE);
		}

		if (v == btnHome) {
			homeList.setVisibility(View.VISIBLE);
			courierList.setVisibility(View.GONE);

		}
	}

	/*
	 * To download data from server.
	 */
	private class ServerCommunication extends
			AsyncTask<String, String, UserDetail> {
		private final ProgressDialog progressBar = new ProgressDialog(
				EditUserProfile.this);

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
			Log.d("Edit Profile Response", data.toString());
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

			/* Getting courier services list */
			// ServicesList download=new ServicesList();
			// download.execute(new String[]{""});

			handler.sendMessage(msg);
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == SUCCESS) {
				Log.d("msg.obj", "ashish " + msg.obj);
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
						try {
							showProfileDetail(data);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if (msg.obj instanceof ArrayList<?>) {

					try {
						ArrayList<Category> dataList = (ArrayList<Category>) msg.obj;
						if (dataList == null) {
							showAlertDialog("No Job found.");
							return;
						}

						mServicesData.refereshAdapter(dataList);
					} catch (Exception e) {

					}

				}
			} else if (msg.arg1 == FAILURE) {
				showAlertDialog("Please try again.");
			} else if (msg.arg1 == DATA_UPDATE) {

				if (msg.obj instanceof ResultData) {
					ResultData data = (ResultData) msg.obj;
					if (data.getAuthenticated() != null
							&& data.getAuthenticated().equalsIgnoreCase(
									"success")) {
						showAlertDialog("Data Updated successfully.");

					} else {
						showAlertDialog("Please try again.");
					}
				}

			} else if (msg.arg1 == DATA_FAIL) {
				showAlertDialog("Please try again.");
			}

		}
	};

	private void showAlertDialog(String s) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();

	}

	/*
	 * On activity result to pick an image from the user gallery
	 */

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bm = null;
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				File f = new File(Environment.getExternalStorageDirectory()
						.toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("temp.jpg")) {
						f = temp;
						break;
					}
				}
				try {
					/*
					 * BitmapFactory.Options btmapOptions = new
					 * BitmapFactory.Options(); bm =
					 * BitmapFactory.decodeFile(f.getAbsolutePath
					 * (),btmapOptions);
					 */
					String path = f.getAbsolutePath();
					BitmapFactory.Options options = new BitmapFactory.Options();
					// options.inSampleSize=1;
					bitmap = BitmapFactory.decodeFile(path, options);

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == REQUEST_GALLARY) {
				Uri selectedImageUri = data.getData();
				String path = getPath(selectedImageUri, EditUserProfile.this);
				BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inSampleSize=1;
				bitmap = BitmapFactory.decodeFile(path, options);
			}
			/*
			 * if (bm != bitmap) { bitmap.recycle(); bitmap = bm; } System.gc();
			 */
			setPhoto();
		}
	}

	private void setPhoto() {
		if (bitmap != null) {
			/*
			 * Drawable d = getResources().getDrawable(R.drawable.profile); int
			 * maxHeight = d.getIntrinsicHeight(); int maxWidth =
			 * d.getIntrinsicWidth(); Bitmap resized =
			 * Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight, true);
			 * image.setMaxHeight(maxHeight);
			 */
			image.setImageBitmap(Constants.getRoundedCornerImage(bitmap,
					image.getWidth(), image.getHeight()));
		}
	}

	private String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		int columnIndex = cursor.getColumnIndex(projection[0]);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);

		return path;
	}

	private void showProfileDetail(UserDetail detail) throws JSONException {

		// To update the security shield
		if (detail.getSecurity().equals("blue"))
			// txtshield.setImageResource(R.drawable.shieldx);
			txtshield.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.shieldx, 0, 0);
		else if (detail.getSecurity().equals("silver"))
			// txtshield.setImageResource(R.drawable.shield_silver);
			txtshield.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.shield_silver, 0, 0);
		else if (detail.getSecurity().equals("gold"))
			// txtshield.setImageResource(R.drawable.shield_gold);
			txtshield.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.shield_gold, 0, 0);

		Log.d("string", detail.getcouriercategory());
		Constants.uemail = detail.getEmail();
		edtUserName.setText(detail.getUsername());
		edtFirstName.setText(detail.getFirst_name());
		edtLastName.setText(detail.getLast_name());
		edtAbout.setText(detail.getAbout());
		edtAddress1.setText(detail.getAddress1());
		edtAddress2.setText(detail.getAddress2());
		edtCity.setText(detail.getAddress3());
		edtState.setText(detail.getAddress4());
		edtZip.setText(detail.getAddress5());
		edtAppAddress1.setText(detail.getAprox_address1());
		edtAppAddress2.setText(detail.getAprox_address2());
		edtAppCity.setText(detail.getAprox_address3());
		edtAppState.setText(detail.getAprox_address4());
		edtAppZip.setText(detail.getAprox_address5());

		Log.d("Credit Card", detail.getCreditCard());
		if (!(detail.getCreditCard().equals("null"))) {

			// edtCreditCard.setText(detail.getCreditCard()); //STRIPE
		}

		if (!(detail.getPaypal() == null) && !(detail.getPaypal().equals(""))) {
			txtBankDetail.setText("PayPal Details");
			Log.d("Credit Card", detail.getPaypal());
			if (!(detail.getPaypal().equals("null"))) {
				edtBankDetails.setText(detail.getPaypal());
			}
		} else {
			txtBankDetail.setText("Bank Details");
			if (!(detail.getBankDetail().equals(null))) {
				edtBankDetails.setText(detail.getBankDetail());
			}
		}

		email = detail.getEmail();
		if (bitmap != null) {
			image.setImageBitmap(Constants.getRoundedCornerImage(bitmap,
					image.getWidth(), image.getHeight()));
		} else {
			Bitmap image_bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.com_facebook_profile_picture_blank_square);
			image.setImageBitmap(Constants.getRoundedCornerImage(image_bitmap,
					image.getWidth(), image.getHeight()));
		}

		String mstring = detail.getcouriercategory();
		if (mstring != null) {
			String strArray[] = mstring.split(",");

			for (int k = 0; k < strArray.length; k++) {
				Log.d("String", strArray[k]);
			}

			for (Integer j = 0; j < strArray.length; j++) {
				for (Integer i = 0; i < courierAdapter.getCount(); i++) {
					if ((strArray[j].trim()).equals(courierAdapter.getItem(i))) {
						courierList.setItemChecked(i, true);
					}
				}
			}

		}

		String mstring1 = detail.gethomecategory();
		if (mstring1 != null) {
			String strArray[] = mstring1.split(",");

			for (int k = 0; k < strArray.length; k++) {
				Log.d("String", strArray[k]);
			}

			for (Integer j = 0; j < strArray.length; j++) {
				for (Integer i = 0; i < homeAdapter.getCount(); i++) {
					if ((strArray[j].trim()).equals(homeAdapter.getItem(i))) {
						homeList.setItemChecked(i, true);
					}
				}
			}

		}
	}

	// Log.d("YYY",""+courierAdapter.getPosition("HARDWARE-LIGHT"));
	/*
	 * Log.d("1",courierAdapter.getPosition("DOCUMENTS-DELIVER")+"");
	 * Log.d("2",courierAdapter.getPosition("DOCUMENTS-FETCH")+"");
	 * Log.d("3",courierAdapter.getPosition("DRY CLEANING-DELIVER")+"");
	 * Log.d("4",courierAdapter.getPosition("DRY CLEANING-FETCH")+"");
	 * Log.d("5",courierAdapter.getPosition("GIFT-DELIVER,FROM ME")+"");
	 * Log.d("6",courierAdapter.getPosition("GIFT PURCHASE AND DELIVER")+"");
	 * Log.d("7",courierAdapter.getPosition("GROCERY-FARMER'S MARKET")+"");
	 * Log.d("8",courierAdapter.getPosition("GROCERY-FETCH")+"");
	 * Log.d("9",courierAdapter.getPosition("HARDWARE-LIGHT")+"");
	 * Log.d("10",courierAdapter.getPosition("HOLD MY PLACE IN LINE")+"");
	 * Log.d(
	 * "11",courierAdapter.getPosition("MERCHANDIZE-GENERAL,BUY AND DELIVER"
	 * )+"");
	 * Log.d("12",courierAdapter.getPosition("MERCHANDIZE-GENERAL,PAID FOR"
	 * )+""); Log.d("13",courierAdapter.getPosition("MOVING -LOADING HELP")+"");
	 * Log.d("14",courierAdapter.getPosition("PHARMACY PICKUP")+"");
	 * Log.d("15",courierAdapter.getPosition("STORE PICKUP-PAID FOR")+"");
	 * Log.d("16",courierAdapter.getPosition("WALK MY DOG")+"");
	 * Log.d("1home",homeAdapter.getPosition("Appliance Repair")+"");
	 * Log.d("2home",homeAdapter.getPosition("Carpet-Clean")+"");
	 * Log.d("3home",homeAdapter.getPosition("Window-Clean")+"");
	 * Log.d("4home",homeAdapter.getPosition("Window-Repair/Install")+"");
	 */

	/*
	 * String mstring=detail.getcouriercategory();
	 * 
	 * Log.d("string full",mstring);
	 * 
	 * if(mstring!=null){ String strArray[] = mstring.split(",");
	 * 
	 * //JSONArray temp = new JSONArray(mstring); //Log.d("string1",temp+"");
	 * //String[] strArr = new String[temp.length()];
	 * 
	 * //
	 * Log.d("Courier Provider",courierAdapter.getPosition(temp.getString(0))+
	 * "");
	 * 
	 * 
	 * int position;
	 * 
	 * 
	 * 
	 * 
	 * 
	 * Log.d("Length",strArray.length+"") ; //Log.d("s0",strArray[0]);
	 * //Log.d("s1",strArray[1]); //Log.d("s0",strArray[1]);
	 * //Log.d("s0",strArray[2]); //Log.d("s0",strArray[3]);
	 * //Log.d("s0",strArray[4]); //Log.d("s0",strArray[5]); for (int i = 0; i <
	 * strArray.length; i++) {
	 * 
	 * position = courierAdapter.getPosition(strArray[i]);
	 * 
	 * Log.d("Position1",position+""); //courierList.setItemChecked(-1,true);
	 * //courierList.setItemChecked(0,true);
	 * //courierList.setItemChecked(1,true);
	 * //courierList.setItemChecked(2,true);
	 * //courierList.setItemChecked(3,true); //
	 * courierList.setItemChecked(9,true); if(position>-1) {
	 * courierList.setItemChecked(position,true);
	 * 
	 * } else {
	 * 
	 * } } } else {
	 * 
	 * }
	 */

	/*
	 * String mstring1=detail.gethomecategory(); if(mstring1!=null){ String
	 * strArray[] = mstring1.split(",");
	 * 
	 * //JSONArray temp = new JSONArray(mstring); //Log.d("string1",temp+"");
	 * //String[] strArr = new String[temp.length()];
	 * 
	 * //
	 * Log.d("Courier Provider",courierAdapter.getPosition(temp.getString(0))+
	 * "");
	 * 
	 * 
	 * int position; for (int i = 0; i < strArray.length; i++) {
	 * 
	 * position = homeAdapter.getPosition(strArray[i]);
	 * 
	 * Log.d("Position2",position+"");
	 * 
	 * /*homeList.setItemChecked(0,true); homeList.setItemChecked(1,true);
	 * homeList.setItemChecked(2,true); homeList.setItemChecked(3,true);
	 * homeList.setItemChecked(9,true);
	 */
	/*
	 * if(position>-1){ homeList.setItemChecked(position,true); } else { }
	 * 
	 * 
	 * } } else {
	 * 
	 * }
	 * 
	 * Log.d("Courier Category",detail.getcouriercategory());
	 * Log.d("Home Category",detail.gethomecategory());
	 * 
	 * }
	 */

	private void showServicesDetail(Category detail) {

		// showServicesDetail
	}

	/*
	 * private void doUpdate(final String edtUname,final String edtFname,final
	 * String edtLname,final String edtabout,final String email) {
	 * 
	 * final ProgressDialog progressBar = new
	 * ProgressDialog(EditUserProfile.this);
	 * progressBar.setMessage("Please wait while loading...");
	 * progressBar.setCancelable(false); progressBar.show(); Thread thr = new
	 * Thread(new Runnable() {
	 * 
	 * @Override public void run() { ArrayList<NameValuePair> nameValuePairs =
	 * new ArrayList<NameValuePair>(); nameValuePairs.add(new
	 * BasicNameValuePair("id",Constants.uid)); nameValuePairs.add(new
	 * BasicNameValuePair("username",edtUname)); nameValuePairs.add(new
	 * BasicNameValuePair("first_name",edtFname)); nameValuePairs.add(new
	 * BasicNameValuePair("last_name",edtLname)); nameValuePairs.add(new
	 * BasicNameValuePair("email",email));
	 * 
	 * 
	 * DataPostParser parser = new
	 * DataPostParser(Constants.HTTP_HOST+"profiledit_image"); ResultData
	 * postdata=parser.getParseData(nameValuePairs); progressBar.dismiss();
	 * 
	 * Message msg = handler.obtainMessage(); if (postdata != null) { msg.obj =
	 * postdata; msg.arg1 = DATA_UPDATE; } else { msg.arg1 = DATA_FAIL; }
	 * handler.sendMessage(msg); } }); thr.start(); }
	 */
	private void doUpdate(final String edtUname, final String edtFname,
			final String edtLname, final String edtabout, final String email,
			final String billing_add1, final String address2,
			final String address3, final String address4,
			final String address5, final String aprox_address,
			final String aprox_address2, final String aprox_address3,
			final String aprox_address4, final String aprox_address5,
			final String courier_categories, final String home_categories) {
		final ProgressDialog progressBar = new ProgressDialog(
				EditUserProfile.this);
		progressBar.setMessage("Please wait while loading...");
		progressBar.setCancelable(false);
		progressBar.show();

		Thread thread = new Thread(new Runnable() {
			// @Override
			public void run() {

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {
					Log.d("edtabout", "edtabout  " + edtabout);
					reqEntity.addPart("id", new StringBody(Constants.uid));
					reqEntity.addPart("first_name", new StringBody(edtFname));
					reqEntity.addPart("last_name", new StringBody(edtLname));
					reqEntity.addPart("about", new StringBody(edtabout));
					reqEntity.addPart("billing_add1", new StringBody(
							billing_add1));
					reqEntity.addPart("address2", new StringBody(address2));
					reqEntity.addPart("address3", new StringBody(address3));
					reqEntity.addPart("address4", new StringBody(address4));
					reqEntity.addPart("address5", new StringBody(address5));
					reqEntity.addPart("aprox_address", new StringBody(
							aprox_address));
					reqEntity.addPart("aprox_address2", new StringBody(
							aprox_address2));
					reqEntity.addPart("aprox_address3", new StringBody(
							aprox_address3));
					reqEntity.addPart("aprox_address4", new StringBody(
							aprox_address4));
					reqEntity.addPart("aprox_address5", new StringBody(
							aprox_address5));
					reqEntity.addPart("courier_cat", new StringBody(
							courier_categories));
					reqEntity.addPart("home_cat", new StringBody(
							home_categories));

					if (bitmap != null) {
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						bitmap.compress(CompressFormat.JPEG, 75, bos);
						byte[] data = bos.toByteArray();
						String imageName = "image" + edtUname;
						ByteArrayBody bab = new ByteArrayBody(data, imageName
								+ ".jpeg");
						reqEntity.addPart("image", bab);
					}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "profiledit_image");
				httppost.setEntity(reqEntity);
				// Execute HTTP Post Request
				HttpResponse response = null;
				try {
					response = httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HttpEntity entity = response.getEntity();
				ResultData resultData = null;
				try {
					InputStream stream = entity.getContent();
					String strResponse = convertStreamToString(stream);
					JSONObject jsonresponse = new JSONObject(strResponse);

					Log.d("Edit Response", jsonresponse + "");
					resultData = new ResultData();
					resultData.setAuthenticated(jsonresponse
							.getString("status"));

					resultData.setMessage(jsonresponse.getString("message"));
					if (jsonresponse.has("aprox_address"))
						resultData.setApproxAddress(jsonresponse
								.getString("aprox_address")
								+ ", "
								+ jsonresponse.getString("aprox_address2")
								+ ", "
								+ jsonresponse.getString("aprox_address3")
								+ ", "
								+ jsonresponse.getString("aprox_address4")
								+ ", "
								+ jsonresponse.getString("aprox_address5"));
					if (jsonresponse.has("email"))
						resultData.setUserEmail(jsonresponse.getString("email"));
					if (jsonresponse.has("amount"))
						resultData.setAmount(jsonresponse.getString("amount"));
					if (jsonresponse.has("reason"))
						resultData.setReason(jsonresponse.getString("reason"));
					if (jsonresponse.has("billing_add1"))
						resultData.setTrueAddress(jsonresponse
								.getString("billing_add1")
								+ ", "
								+ jsonresponse.getString("address2")
								+ ", "
								+ jsonresponse.getString("address3")
								+ ", "
								+ jsonresponse.getString("address4")
								+ ", "
								+ jsonresponse.getString("address5"));
					try {
						resultData.setMessage(jsonresponse.getString("message"));
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				progressBar.dismiss();

				Message msg = handler.obtainMessage();
				if (resultData != null) {

					Log.d("Result Profie", resultData.toString());
					msg.obj = resultData;
					msg.arg1 = DATA_UPDATE;
				} else {
					msg.arg1 = DATA_FAIL;
				}
				handler.sendMessage(msg);
			}
		});
		thread.start();
	}

	private boolean validate() {

		/*
		 * if( (edtFirstName.getText().toString() == null ) ||
		 * (edtFirstName.getText().toString().equals("")) ) {
		 * showAlertDialog("Please enter first name."); return false; } else
		 * if((edtLastName.getText().toString() == null ) ||
		 * (edtLastName.getText().toString().equals("")) ) {
		 * showAlertDialog("Please enter last name."); return false; } else
		 * if((edtAbout.getText().toString() == null ) ||
		 * (edtAbout.getText().toString().equals("")) ){
		 * showAlertDialog("Please fill blank field."); return false; }
		 */

		return true;
	}

	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception e) {
			Log.e(TAG + " HttpReaderException", ">>>" + e.getMessage());
		}
		return sb.toString();
	}

	/**
	 * Start the activity to pick an image from the user gallery
	 */

	private void selectImage() {
		final CharSequence[] items = { "Camera", "Choose from gallery",
				"Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Pick photo from");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Camera")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from gallery")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							REQUEST_GALLARY);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	/*
	 * public void changePassword(){ Thread thr = new Thread(new Runnable() { //
	 * 
	 * @Override public void run() { ArrayList<NameValuePair> nameValuePairs =
	 * new ArrayList<NameValuePair>(); nameValuePairs.add(new
	 * BasicNameValuePair("user_id",Constants.uid)); nameValuePairs.add(new
	 * BasicNameValuePair("password",edtChangePass.getText().toString()));
	 * HttpPostConnector conn = new
	 * HttpPostConnector(Constants.HTTP_HOST+"editPassword", nameValuePairs);
	 * String response = conn.getResponseData(); try { JSONObject jsonObj = new
	 * JSONObject(response);
	 * 
	 * Message msg = handler.obtainMessage();
	 * 
	 * 
	 * //String Paypal_id = jsonObj.getString("Paypal_id");
	 * if(!jsonObj.getString("status").equals("success")){ // msg.arg1=SUCCESS;
	 * // msg.obj=jsonObj.getString("message"); // //Constants.cardInfo =
	 * jsonObj.getString("cardInfo");
	 * showAlertDialog(jsonObj.getString("message")); }else
	 * if(!jsonObj.getString("status").equals("fail")){ // msg.arg1=SUCCESS; //
	 * msg.obj=jsonObj.getString("message");
	 * showAlertDialog(jsonObj.getString("message")); }
	 * 
	 * handler.sendMessage(msg); //Constants.BankInfo =
	 * jsonObj.getString("BankInfo"); //Constants.uemail =
	 * jsonObj.getString("email") ;
	 * 
	 * 
	 * 
	 * } catch (JSONException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } }); thr.start();
	 * 
	 * }
	 */

	private class changePassword extends AsyncTask<String, String, String> {
		private final ProgressDialog progressBar = new ProgressDialog(
				EditUserProfile.this);

		public changePassword() {
			// TODO Auto-generated constructor stub
			progressBar.setCancelable(false);
		}

		@Override
		protected String doInBackground(String... strParams) {
			String message = null;
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", Constants.uid));

			nameValuePairs.add(new BasicNameValuePair("password", edtChangePass
					.getText().toString()));
			HttpPostConnector conn = new HttpPostConnector(Constants.HTTP_HOST
					+ "changepassword", nameValuePairs);
			String response = conn.getResponseData();
			try {
				JSONObject jsonObj = new JSONObject(response);
				if (!jsonObj.getString("status").equals("success")) {
					message = jsonObj.getString("message");
				} else if (!jsonObj.getString("status").equals("fail")) {
					message = jsonObj.getString("message");
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return message;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setMessage("Please wait while loading...");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(String data) {
			super.onPostExecute(data);
			progressBar.dismiss();
			showAlertDialog(data);
		}
	}

	/*
	 * Adding this method for registering paypal or bank details in server if
	 * not exist
	 */

	private void accountRegisterAlert() {
		final AlertDialog.Builder viewDialog = new AlertDialog.Builder(
				EditUserProfile.this);

		LayoutInflater li = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// li.setContentView(R.layout.messagewith_ok_cancle);
		View dialogView = li.inflate(R.layout.popup_paypal_detials, null);
		viewDialog.setView(dialogView);
		final Dialog dd = viewDialog.create();
		dd.show();
		progressBar1 = new ProgressDialog(EditUserProfile.this);
		progressBar1.setMessage("Please wait while loading...");
		progressBar1.setCancelable(false);
		// viewDialog.

		Button btnPaypal = (Button) dialogView.findViewById(R.id.btnPaypal);

		Button btnBnk = (Button) dialogView.findViewById(R.id.btnBnk);
		btnBnk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dd.dismiss();
				progressBar1.show();
				Intent intent = new Intent(EditUserProfile.this,
						BankDetailsActivity.class);
				startActivity(intent);
				progressBar1.cancel();
			}
		});

		btnPaypal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dd.dismiss();
				progressBar1.show();
				Intent intent = new Intent(EditUserProfile.this,
						PayPalDetailsActivity.class);
				startActivity(intent);
				progressBar1.cancel();

			}
		});

	}

	/*
	 * To download data from server.
	 */

	/*
	 * private class ServicesList extends AsyncTask<String, String,
	 * ArrayList<Category>> { public ServicesList() { // TODO Auto-generated
	 * constructor stub }
	 * 
	 * @Override protected ArrayList<Category> doInBackground(String...
	 * strParams){
	 * 
	 * ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	 * CategoryPostParser parser; //nameValuePairs.add(new
	 * BasicNameValuePair("id",Constants.uid));
	 * 
	 * 
	 * if(isHomeCategory) parser = new
	 * CategoryPostParser(Constants.HTTP_HOST+"viewHomecategories"); else parser
	 * = new CategoryPostParser(Constants.HTTP_HOST+"viewcategories");
	 * 
	 * ArrayList<Category> dataList = parser.getParseData(nameValuePairs);
	 * 
	 * return dataList; }
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute();
	 * 
	 * }
	 * 
	 * @Override protected void onPostExecute(ArrayList<Category> dataList) {
	 * super.onPostExecute(dataList);
	 * 
	 * Message msg = handler.obtainMessage(); msg.arg1=SUCCESS;
	 * msg.obj=dataList; handler.sendMessage(msg); } }
	 */
	/*
	 * To download data from server.
	 */
	/*
	 * private class ServicesList2 extends AsyncTask<String, String,
	 * ArrayList<Category>> { private final ProgressDialog progressBar = new
	 * ProgressDialog(EditUserProfile.this);
	 * 
	 * 
	 * public ServicesList2() { // TODO Auto-generated constructor stub
	 * progressBar.setCancelable(false); }
	 * 
	 * @Override protected ArrayList<Category> doInBackground(String...
	 * strParams){
	 * 
	 * ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	 * CategoryPostParser parser; //nameValuePairs.add(new
	 * BasicNameValuePair("id",Constants.uid));
	 * 
	 * 
	 * if(isHomeCategory) parser = new
	 * CategoryPostParser(Constants.HTTP_HOST+"viewHomecategories"); else parser
	 * = new CategoryPostParser(Constants.HTTP_HOST+"viewcategories");
	 * 
	 * ArrayList<Category> dataList = parser.getParseData(nameValuePairs);
	 * 
	 * return dataList; }
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute();
	 * progressBar.setMessage("Please wait while loading...");
	 * progressBar.show();
	 * 
	 * }
	 * 
	 * @Override protected void onPostExecute(ArrayList<Category> dataList) {
	 * super.onPostExecute(dataList); progressBar.dismiss();
	 * 
	 * Message msg = handler.obtainMessage(); msg.arg1=SUCCESS;
	 * msg.obj=dataList; handler.sendMessage(msg); } }
	 */

}
