package com.synapse.gofer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.model.UserDetail;
import com.synapse.gofer.parser.DataPostParser;
import com.synapse.gofer.parser.UserDataPostParser;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.ImageProcessing;

public class ProfilePublicFragment extends Fragment implements OnClickListener {

	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	private RelativeLayout headerLayout;
	private EditText edtUname, edtFname, edtLname, edtEmail;
	// private EditText
	// edtTrueAddr1,edtTrueAddr2,edtTrueAddr3,edtTrueAddr4,edtTrueAddr5;
	private EditText edtApproxAddr1, edtApproxAddr2, edtApproxAddr3,
			edtApproxAddr4, edtApproxAddr5;
	private Button btnSubmit;

	private final int REQUEST_CAMERA = 1001;
	private final int REQUEST_GALLARY = 1002;
	private Bitmap bitmap;
	private String PATH;

	public ProfilePublicFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.activity_signup, container, false);
		return view;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	/*
	 * Called To initialize to user interface.
	 */
	private void initViews() {
		headerLayout = (RelativeLayout) getView().findViewById(
				R.id.headerLayout);

		edtUname = (EditText) getView().findViewById(R.id.userName);
		edtFname = (EditText) getView().findViewById(R.id.userFName);
		edtLname = (EditText) getView().findViewById(R.id.userLName);
		edtEmail = (EditText) getView().findViewById(R.id.userEmail);

		// edtTrueAddr1=(EditText)getView().findViewById(R.id.userTrueAddress1);
		// edtTrueAddr2=(EditText)getView().findViewById(R.id.userTrueAddress2);
		// edtTrueAddr3=(EditText)getView().findViewById(R.id.userTrueAddress3);
		// edtTrueAddr4=(EditText)getView().findViewById(R.id.userTrueAddress4);
		// edtTrueAddr5=(EditText)getView().findViewById(R.id.userTrueAddress5);

		edtApproxAddr1 = (EditText) getView().findViewById(R.id.approxAdr1);
		edtApproxAddr2 = (EditText) getView().findViewById(R.id.approxAdr2);
		edtApproxAddr3 = (EditText) getView().findViewById(R.id.approxAdr3);
		edtApproxAddr4 = (EditText) getView().findViewById(R.id.approxAdr4);
		edtApproxAddr5 = (EditText) getView().findViewById(R.id.approxAdr5);

		btnSubmit = (Button) getView().findViewById(R.id.btnRegister);
		btnSubmit.setOnClickListener(this);

		edtUname.setKeyListener(null);
		edtEmail.setKeyListener(null);
		headerLayout.setVisibility(View.INVISIBLE);

		if (Constants.isNetAvailable(getActivity())) {
			ServerCommunication download = new ServerCommunication();
			download.execute(new String[] { "" });
		} else {
			Constants.NoInternetError(getActivity());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == btnSubmit) {
			if (velidate(getActivity())) {
				// edtTrueAddr1.getText().toString(),edtTrueAddr2.getText().toString(),edtTrueAddr3.getText().toString(),edtTrueAddr4.getText().toString(),edtTrueAddr5.getText().toString()
				doUpdate(edtUname.getText().toString(), edtFname.getText()
						.toString(), edtLname.getText().toString(), edtEmail
						.getText().toString(), edtApproxAddr1.getText()
						.toString(), edtApproxAddr2.getText().toString(),
						edtApproxAddr3.getText().toString(), edtApproxAddr4
								.getText().toString(), edtApproxAddr5.getText()
								.toString());
			}
		}
	}

	boolean velidate(Context cont) {
		if ((edtUname.getText().toString() == null)
				|| (edtUname.getText().toString().equals(""))) {
			showAlertDialog("Please enter user name.");
			return false;
		}
		if ((edtFname.getText().toString() == null)
				|| (edtFname.getText().toString().equals(""))) {
			showAlertDialog("Please enter first name.");
			return false;
		} else if ((edtLname.getText().toString() == null)
				|| (edtLname.getText().toString().equals(""))) {
			showAlertDialog("Please enter last name.");
			return false;
		} else if ((edtEmail.getText().toString() == null)
				|| (edtEmail.getText().toString().equals(""))) {
			showAlertDialog("Please enter email address.");
			return false;
		} else if (!isValidEmail(edtEmail.getText().toString())) {
			showAlertDialog("Please enter valid email address.");
			return false;
		}
		// else if((edtTrueAddr1.getText().toString() == null ) ||
		// (edtTrueAddr1.getText().toString().equals("")) )
		// {
		// showAlertDialog("Please enter true address 1.");
		// return false;
		// }
		// else if((edtTrueAddr2.getText().toString() == null ) ||
		// (edtTrueAddr2.getText().toString().equals("")) )
		// {
		// showAlertDialog("Please enter true address 2.");
		// return false;
		// }
		// else if( (edtTrueAddr3.getText().toString() == null ) ||
		// (edtTrueAddr3.getText().toString().equals("")) )
		// {
		// showAlertDialog("Please enter true address 3.");
		// return false;
		// }
		// else if( (edtTrueAddr4.getText().toString() == null ) ||
		// (edtTrueAddr4.getText().toString().equals("")) )
		// {
		// showAlertDialog("Please enter true address 4.");
		// return false;
		// }
		// else if( (edtTrueAddr5.getText().toString() == null ) ||
		// (edtTrueAddr5.getText().toString().equals("")) )
		// {
		// showAlertDialog("Please enter true address 5.");
		// return false;
		// }

		return true;
	}

	private void showAlertDialog(String s) {
		Context context = getActivity().getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();
	}

	/*
	 * To validate.
	 */
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	/*
	 * To Edit profile
	 */

	/*
	 * To login.
	 */

	// final String edtTrueAddr1,final String edtTrueAddr2,final String
	// edtTrueAdr3,final String edtTrueAdr4,final String edtTrueAdr5,
	private void doUpdate(final String edtUname, final String edtFname,
			final String edtLname, final String edtEmail,

			final String edtAproxAdr1, final String edtAproxAdr2,
			final String edtAproxAdr3, final String edtAproxAdr4,
			final String edtAproxAdr5) {

		final ProgressDialog progressBar = new ProgressDialog(getActivity());
		progressBar.setMessage("Please wait while loading...");
		progressBar.setCancelable(false);
		progressBar.show();
		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("id", Constants.uid));
				nameValuePairs
						.add(new BasicNameValuePair("username", edtUname));
				nameValuePairs.add(new BasicNameValuePair("first_name",
						edtFname));
				nameValuePairs
						.add(new BasicNameValuePair("last_name", edtLname));
				nameValuePairs.add(new BasicNameValuePair("email", edtEmail));
				// nameValuePairs.add(new
				// BasicNameValuePair("billing_add1",edtTrueAddr1));
				// nameValuePairs.add(new
				// BasicNameValuePair("address2",edtTrueAddr2));
				// nameValuePairs.add(new
				// BasicNameValuePair("address3",edtTrueAdr3));
				// nameValuePairs.add(new
				// BasicNameValuePair("address4",edtTrueAdr4));
				// nameValuePairs.add(new
				// BasicNameValuePair("address5",edtTrueAdr5));

				nameValuePairs.add(new BasicNameValuePair("billing_add1", ""));
				nameValuePairs.add(new BasicNameValuePair("address2", ""));
				nameValuePairs.add(new BasicNameValuePair("address3", ""));
				nameValuePairs.add(new BasicNameValuePair("address4", ""));
				nameValuePairs.add(new BasicNameValuePair("address5", ""));

				nameValuePairs.add(new BasicNameValuePair("aprox_address",
						edtAproxAdr1));
				nameValuePairs.add(new BasicNameValuePair("aprox_address2",
						edtAproxAdr2));
				nameValuePairs.add(new BasicNameValuePair("aprox_address3",
						edtAproxAdr3));
				nameValuePairs.add(new BasicNameValuePair("aprox_address4",
						edtAproxAdr4));
				nameValuePairs.add(new BasicNameValuePair("aprox_address5",
						edtAproxAdr5));

				DataPostParser parser = new DataPostParser(Constants.HTTP_HOST
						+ "profiledit");
				ResultData postdata = parser.getParseData(nameValuePairs);
				progressBar.dismiss();

				Message msg = handler.obtainMessage();
				if (postdata != null) {
					msg.obj = postdata;
					msg.arg1 = SUCCESS;
				} else {
					msg.arg1 = FAILURE;
				}
				handler.sendMessage(msg);
			}
		});
		thr.start();
	}

	/*
	 * private void doUpdate(final String edtUname,final String edtFname,final
	 * String edtLname,final String edtEmail, final String edtPwd,final String
	 * edtTrueAddr,final String edtAdr1,final String edtAdr2,final String
	 * edtAdr3,final String edtQuote) { final ProgressDialog progressBar = new
	 * ProgressDialog(getActivity());
	 * progressBar.setMessage("Please wait while loading...");
	 * progressBar.show();
	 * 
	 * Thread thread =new Thread(new Runnable() { //@Override public void run()
	 * {
	 * 
	 * MultipartEntity reqEntity = new
	 * MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE); try {
	 * reqEntity.addPart("id",new StringBody(Constants.uid));
	 * reqEntity.addPart("username", new StringBody(edtUname));
	 * reqEntity.addPart("first_name",new StringBody(edtFname));
	 * reqEntity.addPart("last_name",new StringBody(edtLname));
	 * reqEntity.addPart("email",new StringBody(edtEmail));
	 * reqEntity.addPart("billing_add1", new StringBody(edtTrueAddr));
	 * reqEntity.addPart("address2", new StringBody(edtAdr1));
	 * reqEntity.addPart("address3", new StringBody(edtAdr2));
	 * reqEntity.addPart("aprox_address", new StringBody(edtAdr3));
	 * reqEntity.addPart("favorite_quote", new StringBody(edtQuote));
	 * 
	 * }catch (UnsupportedEncodingException e){ //TODO Auto-generated catch
	 * block e.printStackTrace(); } if (bitmap != null) { ByteArrayOutputStream
	 * bos = new ByteArrayOutputStream();
	 * bitmap.compress(CompressFormat.JPEG,75,bos); byte[] data =
	 * bos.toByteArray(); String imageName = "image"+edtUname; ByteArrayBody bab
	 * = new ByteArrayBody(data,imageName+".jpeg");
	 * reqEntity.addPart("image",bab); }
	 * 
	 * HttpClient httpclient=new DefaultHttpClient(); HttpPost httppost = new
	 * HttpPost(Constants.HTTP_HOST+"profiledit");
	 * httppost.setEntity(reqEntity);
	 * 
	 * // Execute HTTP Post Request HttpResponse response = null; try {
	 * response=httpclient.execute(httppost); }catch (ClientProtocolException e)
	 * { // TODO Auto-generated catch block e.printStackTrace(); }catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace();
	 * } HttpEntity entity = response.getEntity(); ResultData resultData=null;
	 * try { InputStream stream = entity.getContent(); String strResponse =
	 * convertStreamToString(stream); JSONObject jsonresponse = new
	 * JSONObject(strResponse); resultData=new ResultData();
	 * resultData.setAuthenticated(jsonresponse.getString("status")); try {
	 * resultData.setMessage(jsonresponse.getString("message"));
	 * }catch(Exception ex){ ex.printStackTrace(); }
	 * 
	 * }catch (Exception e) { //TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * progressBar.dismiss();
	 * 
	 * Message msg = handler.obtainMessage(); if(resultData!=null){
	 * msg.obj=resultData; msg.arg1=SUCCESS; }else{ msg.arg1=FAILURE; }
	 * handler.sendMessage(msg); } }); thread.start(); }
	 */

	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception e) {
			Log.e("HttpReaderException", ">>>" + e.getMessage());
		}
		return sb.toString();
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
						showProfileDetail(data);
					}
				}
			} else if (msg.arg1 == FAILURE) {
				showAlertDialog("Please try again.");
			}
		}
	};

	/**
	 * Start the activity to pick an image from the user gallery
	 */

	private void selectImage() {
		final CharSequence[] items = { "Camera", "Choose from gallery",
				"Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
				String path = getPath(selectedImageUri, getActivity());
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

	private void setPhoto() {
		if (bitmap != null) {
			Drawable d = getResources().getDrawable(R.drawable.profile);
			int maxHeight = d.getIntrinsicHeight();
			int maxWidth = d.getIntrinsicWidth();
			Bitmap resized = Bitmap.createScaledBitmap(bitmap, maxWidth,
					maxHeight, true);
			// imgPhoto.setMaxHeight(maxHeight);
			// imgPhoto.setImageBitmap(resized);
		}
	}

	/*
	 * To download data from server.
	 */
	private class ServerCommunication extends
			AsyncTask<String, String, UserDetail> {
		private final ProgressDialog progressBar = new ProgressDialog(
				getActivity());

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

			Message msg = handler.obtainMessage();
			msg.arg1 = SUCCESS;
			msg.obj = data;
			handler.sendMessage(msg);
		}
	}

	private void showProfileDetail(UserDetail detail) {
		edtUname.setText(detail.getUsername());
		edtFname.setText(detail.getFirst_name());
		edtLname.setText(detail.getLast_name());
		edtEmail.setText(detail.getEmail());
		// edtPwd.setText("");
		// edtTrueAddr1.setText(detail.getAddress1());
		// edtTrueAddr2.setText(detail.getAddress2());
		// edtTrueAddr3.setText(detail.getAddress3());
		// edtTrueAddr4.setText(detail.getAddress4());
		// edtTrueAddr5.setText(detail.getAddress5());

		edtApproxAddr1.setText(detail.getAprox_address1());
		edtApproxAddr2.setText(detail.getAprox_address2());
		edtApproxAddr3.setText(detail.getAprox_address3());
		edtApproxAddr4.setText(detail.getAprox_address4());
		edtApproxAddr5.setText(detail.getAprox_address5());

		if (bitmap != null) {
			Drawable d = getResources().getDrawable(R.drawable.profile);
			int maxHeight = d.getIntrinsicHeight();
			int maxWidth = d.getIntrinsicWidth();
			Bitmap resized = Bitmap.createScaledBitmap(bitmap, maxWidth,
					maxHeight, true);
			// imgPhoto.setMaxHeight(maxHeight);
			// imgPhoto.setImageBitmap(resized);
		}
	}

}
