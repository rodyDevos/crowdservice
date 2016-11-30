package com.synapse.gofer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.gofer.adapter.CommissionedJobListingAdapter;
import com.synapse.gofer.model.ProfileData;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.parser.ProfilePostParser;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.ImageProcessing;

public class ProfileJobsFragment extends Fragment implements OnClickListener {

	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;

	private final int REQUEST_GALLARY = 301;
	private final int REQUEST_CAMERA = 300;

	private Button btnCustomer, btnCourier, btnK, btnTrofy, btnLastJob,
			btnCamera, btnUpdateProfile;
	private TextView txtName, txtJobs, txtPoints, txtVerify, txtMyHomePage,
			lblLocation;
	private ImageView imgPhoto;
	private RatingBar ratingBar;
	private LinearLayout layoutPhotoContainer;
	private boolean photoPicked;
	private String imgPath;
	private EditText edtAproxlocation, edtFavQuote, edtLink;
	private int imageCount = 0;
	private ArrayList<Bitmap> photos = null;
	private Bitmap bitmap;
	private String userType = "2";

	private ListView jobListView = null;
	private RelativeLayout listViewHeader = null;
	private ScrollView scrollView = null;
	private Button btnJobListing = null;
	private TextView txtDone = null;
	private CommissionedJobListingAdapter mJobListingAdapter = null;
	protected boolean noImageAvail = false;

	public static ProfileJobsFragment self = null;

	public ProfileJobsFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		photos = new ArrayList<Bitmap>();
		initViews();
		self = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_jobs, container, false);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	/*
	 * Called To initialize to user interface.
	 */
	private void initViews() {
		btnCustomer = (Button) getView().findViewById(R.id.btnCustomer);
		btnCourier = (Button) getView().findViewById(R.id.btnCourier);
		btnK = (Button) getView().findViewById(R.id.btnK);
		btnTrofy = (Button) getView().findViewById(R.id.btnTrofy);
		btnLastJob = (Button) getView().findViewById(R.id.btnLastJob);
		btnCamera = (Button) getView().findViewById(R.id.btnCamera);
		btnUpdateProfile = (Button) getView().findViewById(
				R.id.btnUpdateProfile);
		edtAproxlocation = (EditText) getView().findViewById(
				R.id.edtAproxlocation);
		edtFavQuote = (EditText) getView().findViewById(R.id.edtFavQuote);

		btnCustomer.setOnClickListener(this);
		btnCourier.setOnClickListener(this);
		btnK.setOnClickListener(this);
		btnTrofy.setOnClickListener(this);
		btnLastJob.setOnClickListener(this);
		btnCamera.setOnClickListener(this);
		btnUpdateProfile.setOnClickListener(this);

		txtName = (TextView) getView().findViewById(R.id.txtName);
		txtJobs = (TextView) getView().findViewById(R.id.txtJobs);
		txtPoints = (TextView) getView().findViewById(R.id.txtPoints);
		txtVerify = (TextView) getView().findViewById(R.id.txtVerify);
		txtMyHomePage = (TextView) getView().findViewById(R.id.txtMyHomePage);
		lblLocation = (TextView) getView().findViewById(R.id.lblAproxlocation);
		edtLink = (EditText) getView().findViewById(R.id.edtLink);

		imgPhoto = (ImageView) getView().findViewById(R.id.imgPhoto);
		imgPhoto.setOnClickListener(this);
		ratingBar = (RatingBar) getView().findViewById(R.id.ratingBar);
		layoutPhotoContainer = (LinearLayout) getView().findViewById(
				R.id.layoutPhotoContainer);

		listViewHeader = (RelativeLayout) getView().findViewById(
				R.id.listview_header);
		jobListView = (ListView) getView().findViewById(
				R.id.customerdetail_listview);
		scrollView = (ScrollView) getView().findViewById(
				R.id.customerdetail_scroll);

		txtDone = (TextView) getView().findViewById(
				R.id.customerdetail_listview_header_done);
		txtDone.setOnClickListener(this);

		mJobListingAdapter = new CommissionedJobListingAdapter(null,
				getActivity().getLayoutInflater(), this, getActivity(),
				Constants.uid);
		jobListView.setAdapter(mJobListingAdapter);

		imageCount = layoutPhotoContainer.getChildCount();

		if (Constants.isNetAvailable(getActivity())) {
			ServerCommunication server = new ServerCommunication();
			server.execute();
		} else {
			Constants.NoInternetError(getActivity());
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == imgPhoto) {
			photoPicked = false;
			selectImage();
		} else if (v == btnCamera) {
			photoPicked = true;
			selectImage();
		} else if (v == btnUpdateProfile) {
			// if(velidate(getActivity())){

			doUpdate(edtFavQuote.getText().toString(), edtLink.getText()
					.toString(), edtAproxlocation.getText().toString(),
					"Please wait while loading...");
			// }
		} else if (v == btnCustomer) {
			userType = "2";
			lblLocation.setText("Approx Location");

			if (Constants.isNetAvailable(getActivity())) {
				ServerCommunication server = new ServerCommunication();
				server.execute();
			} else {
				Constants.NoInternetError(getActivity());
			}
			btnCustomer.setBackgroundResource(R.drawable.leftcorner);
			btnCourier.setBackgroundResource(R.drawable.rightcorner_unselected);
		} else if (v == btnCourier) {
			userType = "3";
			lblLocation.setText("Preferred Services");

			if (Constants.isNetAvailable(getActivity())) {
				ServerCommunication server = new ServerCommunication();
				server.execute();
			} else {
				Constants.NoInternetError(getActivity());
			}

			btnCustomer.setBackgroundResource(R.drawable.leftcorner_unselected);
			btnCourier.setBackgroundResource(R.drawable.rightcorner);
		} else if (v == btnLastJob) {
			listViewHeader.setVisibility(View.VISIBLE);
			jobListView.setVisibility(View.VISIBLE);
			scrollView.setVisibility(View.GONE);
		} else if (v == txtDone) {
			listViewHeader.setVisibility(View.GONE);
			jobListView.setVisibility(View.GONE);
			scrollView.setVisibility(View.VISIBLE);
		}
	}

	private void showAlertDialog(String s) {
		Context context = getActivity().getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();
	}

	private void selectImage() {
		final CharSequence[] items = { "Camera", "Choose from gallary",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Pick photo from");
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Camera")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "goferpic.jpeg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from gallary")) {
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bm = null;
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				File f = new File(Environment.getExternalStorageDirectory()
						.toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("goferpic.jpeg")) {
						f = temp;
						imgPath = f.getAbsolutePath();
						break;
					}
				}
				try {
					/*
					 * BitmapFactory.Options btmapOptions = new
					 * BitmapFactory.Options(); btmapOptions.inJustDecodeBounds
					 * = false; btmapOptions.inPreferredConfig =
					 * Config.ARGB_8888; btmapOptions.inDither = true;
					 */
					bm = checkImageRotation(imgPath);

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == REQUEST_GALLARY) {
				Uri selectedImageUri = data.getData();
				imgPath = getPath(selectedImageUri, getActivity());
				/*
				 * BitmapFactory.Options options = new BitmapFactory.Options();
				 * options.inSampleSize = 3; options.inJustDecodeBounds = false;
				 * options.inPreferredConfig = Config.ARGB_8888;
				 * options.inDither = true;
				 */
				bm = checkImageRotation(imgPath);
			}
			if (bm != null) {
				if (photoPicked) {
					setImage(bm);
				} else {
					Bitmap resized = Bitmap.createScaledBitmap(bm, 162, 182,
							true);
					imgPhoto.setImageBitmap(resized);

					bitmap = bm;
				}
			}
		}
	}

	private String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private void setImage(final Bitmap bm) {
		if (imageCount < 10) {

			if (noImageAvail) {
				layoutPhotoContainer.removeAllViews();
				noImageAvail = false;
				imageCount = 0;
			}

			Bitmap bitmap = bm;// getCompressImage(bm);
			System.gc();
			int w = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 90, getActivity()
							.getResources().getDisplayMetrics());
			int m = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 5, getActivity()
							.getResources().getDisplayMetrics());
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					w, w);
			ImageView image = new ImageView(getActivity());
			image.setAdjustViewBounds(true);
			image.setScaleType(ImageView.ScaleType.CENTER_CROP);
			layoutParams.setMargins(m, 0, 0, 0);
			image.setLayoutParams(layoutParams);
			image.setOnClickListener(imageClickListenr);
			// Bitmap resized = Bitmap.createScaledBitmap(bmp, 162, 182, true);
			image.setImageBitmap(bitmap);
			image.setTag(array[imageCount]);
			image.setId(imageCount);

			layoutPhotoContainer.addView(image);
			photos.add(imageCount, bm);
			imageCount++;
		} else {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					getActivity());
			alertDialog.setTitle("Do you want to replace image with last.");
			alertDialog.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							Bitmap bitmap = bm;// getCompressImage(bm);
							System.gc();
							int w = (int) TypedValue.applyDimension(
									TypedValue.COMPLEX_UNIT_DIP, 90,
									getActivity().getResources()
											.getDisplayMetrics());
							int m = (int) TypedValue.applyDimension(
									TypedValue.COMPLEX_UNIT_DIP, 5,
									getActivity().getResources()
											.getDisplayMetrics());
							LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
									w, w);
							ImageView image = new ImageView(getActivity());
							image.setAdjustViewBounds(true);
							image.setScaleType(ImageView.ScaleType.CENTER_CROP);
							layoutParams.setMargins(m, 0, 0, 0);
							image.setLayoutParams(layoutParams);
							image.setOnClickListener(imageClickListenr);
							// Bitmap resized = Bitmap.createScaledBitmap(bmp,
							// 162, 182, true);
							image.setImageBitmap(bitmap);
							image.setTag(array[9]);
							image.setId(9);

							layoutPhotoContainer.removeViewAt(9);
							layoutPhotoContainer.addView(image, 9);

							photos.remove(9);
							photos.add(9, bm);

						}
					});
			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to invoke NO event
							dialog.cancel();
						}
					});
			// Showing Alert Message
			alertDialog.show();
		}

	}

	private void doUpdate(final String edtQuote, final String edtLink,
			final String edtDesc, String message) {
		final ProgressDialog progressBar = new ProgressDialog(getActivity());
		progressBar.setMessage(message);
		progressBar.setCancelable(false);
		progressBar.show();

		Thread thread = new Thread(new Runnable() {
			// @Override
			public void run() {
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {
					reqEntity.addPart("user_id", new StringBody(Constants.uid));
					if (userType.equals("2"))
						reqEntity.addPart("approx_location", new StringBody(
								edtDesc));
					else
						reqEntity.addPart("preferred_services", new StringBody(
								edtDesc));
					// reqEntity.addPart("user_type", new StringBody(userType));
					// reqEntity.addPart("user_description",new
					// StringBody(edtDesc));
					reqEntity.addPart("home_link", new StringBody(edtLink));
					reqEntity.addPart("favorite_quote",
							new StringBody(edtQuote));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (bitmap != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG, 75, bos);
					byte[] data = bos.toByteArray();
					String imageName = "image" + Constants.uid;
					ByteArrayBody bab = new ByteArrayBody(data, imageName
							+ ".jpeg");
					reqEntity.addPart("image", bab);
				}
				if (photos != null && photos.size() > 0) {
					for (int i = 0; i < photos.size(); i++) {
						Bitmap bmp = (Bitmap) photos.get(i);
						if (bmp != null) {
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							((Bitmap) bmp).compress(CompressFormat.JPEG, 75,
									bos);
							byte[] data = bos.toByteArray();
							String imageName = "jobprofile" + (i + 1);
							ByteArrayBody bab = new ByteArrayBody(data,
									imageName + ".jpeg");
							reqEntity.addPart(imageName, bab);
						}
					}
				}
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "addJobprofile");
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
					resultData = new ResultData();
					resultData.setAuthenticated(jsonresponse
							.getString("status"));
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
					msg.obj = resultData;
					msg.arg1 = SUCCESS;
				} else {
					msg.arg1 = FAILURE;
				}
				handler.sendMessage(msg);
			}
		});
		thread.start();
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
			Log.e("HttpReaderException", ">>>" + e.getMessage());
		}
		return sb.toString();
	}

	/*
	 * To download data from server.
	 */
	private class ServerCommunication extends
			AsyncTask<String, String, ProfileData> {
		private ProgressDialog progressBar = null;

		public ServerCommunication() {
			// TODO Auto-generated constructor stub
			progressBar = new ProgressDialog(getActivity());
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait while loading...");
		}

		@Override
		protected ProfileData doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("user_id", Constants.uid));
			nameValuePairs.add(new BasicNameValuePair("user_type", userType));

			ProfilePostParser parser = new ProfilePostParser(
					Constants.HTTP_HOST + "viewjobprofile");
			ProfileData data = parser.getParseData(nameValuePairs);

			if (data != null) {
				bitmap = ImageProcessing.downloadImage(data.getImage_big());
			}
			if (photos != null && photos.size() > 0) {
				photos.clear();
			}
			if (data != null && data.getJobprofile1().startsWith("http")) {
				Bitmap bitmap1 = ImageProcessing.downloadImage(data
						.getJobprofile1());
				if (bitmap1 != null) {
					photos.add(bitmap1);
				}
			}
			if (data != null && data.getJobprofile2().startsWith("http")) {
				Bitmap bitmap2 = ImageProcessing.downloadImage(data
						.getJobprofile2());
				if (bitmap2 != null) {
					photos.add(bitmap2);
				}
			}
			if (data != null && data.getJobprofile3().startsWith("http")) {
				Bitmap bitmap3 = ImageProcessing.downloadImage(data
						.getJobprofile3());
				if (bitmap3 != null) {
					photos.add(bitmap3);
				}
			}
			if (data != null && data.getJobprofile4().startsWith("http")) {
				Bitmap bitmap4 = ImageProcessing.downloadImage(data
						.getJobprofile4());
				if (bitmap4 != null) {
					photos.add(bitmap4);
				}
			}
			if (data != null && data.getJobprofile5().startsWith("http")) {
				Bitmap bitmap5 = ImageProcessing.downloadImage(data
						.getJobprofile5());
				if (bitmap5 != null) {
					photos.add(bitmap5);
				}
			}
			if (data != null && data.getJobprofile6().startsWith("http")) {
				Bitmap bitmap6 = ImageProcessing.downloadImage(data
						.getJobprofile6());
				if (bitmap6 != null) {
					photos.add(bitmap6);
				}
			}
			if (data != null && data.getJobprofile7().startsWith("http")) {
				Bitmap bitmap7 = ImageProcessing.downloadImage(data
						.getJobprofile7());
				if (bitmap7 != null) {
					photos.add(bitmap7);
				}
			}
			if (data != null && data.getJobprofile8().startsWith("http")) {
				Bitmap bitmap8 = ImageProcessing.downloadImage(data
						.getJobprofile8());
				if (bitmap8 != null) {
					photos.add(bitmap8);
				}
			}

			if (data != null && data.getJobprofile9().startsWith("http")) {
				Bitmap bitmap9 = ImageProcessing.downloadImage(data
						.getJobprofile9());
				if (bitmap9 != null) {
					photos.add(bitmap9);
				}
			}

			if (data != null && data.getJobprofile10().startsWith("http")) {
				Bitmap bitmap10 = ImageProcessing.downloadImage(data
						.getJobprofile10());
				if (bitmap10 != null) {
					photos.add(bitmap10);
				}
			}

			return data;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.show();
		}

		@Override
		protected void onPostExecute(ProfileData data) {
			super.onPostExecute(data);
			progressBar.dismiss();

			Message msg = handler.obtainMessage();
			msg.arg1 = SUCCESS;
			msg.obj = data;
			handler.sendMessage(msg);
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
					// showAlertDialog(data.getMessage());
				}
				if (msg.obj instanceof ProfileData) {
					ProfileData data = (ProfileData) msg.obj;
					if (data != null) {
						showProfileDetail(data);
					}
				}
			} else if (msg.arg1 == FAILURE) {
				showAlertDialog("Please try again.");
			}
		}

		private void showProfileDetail(ProfileData data) {
			if (bitmap != null) {
				imgPhoto.setMaxWidth(162);
				imgPhoto.setMaxHeight(182);
				imgPhoto.setImageBitmap(bitmap);
			}
			if (data == null)
				return;
			if (data.getUserdetail() != null) {
				txtName.setText(data.getUserdetail().getUsername());
				txtPoints.setText(data.getUserdetail().getKpoint() + " Points");
				if (data.getUserdetail().getSecurity().equals("P")) {
					txtVerify.setText("Not_Verified");
				} else {
					txtVerify.setText("Verified");
				}
			}
			if (data.getBiddetail() != null)
				txtJobs.setText(data.getBiddetail().getCount() + " Jobs");
			if (data.getJobprofile() != null) {
				if (userType.equals("2"))
					edtAproxlocation.setText(data.getJobprofile()
							.getApprox_location());
				else
					edtAproxlocation.setText(data.getJobprofile()
							.getPreferred_services());
				edtFavQuote.setText(data.getJobprofile().getFavorite_quote());
				edtLink.setText(data.getJobprofile().getHome_link());
			}
			if (layoutPhotoContainer != null
					&& layoutPhotoContainer.getChildCount() > 0) {
				layoutPhotoContainer.removeAllViews();
			}
			layoutPhotoContainer.removeAllViews();
			if (photos != null && photos.size() > 0) {
				for (int i = 0; i < photos.size(); i++) {
					Bitmap bmp = (Bitmap) photos.get(i);
					if (bmp != null) {

						// getCompressImage(bmp);
						System.gc();
						int w = (int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 90, getActivity()
										.getResources().getDisplayMetrics());
						int m = (int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 5, getActivity()
										.getResources().getDisplayMetrics());
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
								w, w);
						ImageView image = new ImageView(getActivity());
						image.setAdjustViewBounds(true);
						image.setScaleType(ImageView.ScaleType.CENTER_CROP);
						layoutParams.setMargins(m, 0, 0, 0);
						image.setLayoutParams(layoutParams);
						image.setOnClickListener(imageClickListenr);
						// Bitmap resized = Bitmap.createScaledBitmap(bmp, 162,
						// 182, true);
						image.setImageBitmap(bmp);
						image.setTag(array[i]);
						image.setId(i);

						layoutPhotoContainer.addView(image);

					}
				}
			} else {
				int w = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 90, getActivity()
								.getResources().getDisplayMetrics());
				int m = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 5, getActivity()
								.getResources().getDisplayMetrics());
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						w, w);
				ImageView image = new ImageView(getActivity());
				layoutParams.setMargins(m, 0, 0, 0);
				image.setLayoutParams(layoutParams);
				image.setAdjustViewBounds(true);
				image.setScaleType(ImageView.ScaleType.CENTER_CROP);
				image.setImageResource(R.drawable.no_image);

				noImageAvail = true;

				layoutPhotoContainer.addView(image);
			}
			imageCount = layoutPhotoContainer.getChildCount();
			System.gc();
		}
	};

	private String[] array = { "First", "Second", "Third", "Fourth", "Fifth",
			"Sixth", "Seventh", "Eightth", "Nineth", "Tenth" };
	public static int clickedImageNumber;

	boolean velidate(Context cont) {
		if ((edtAproxlocation.getText().toString() == null)
				|| (edtAproxlocation.getText().toString().equals(""))) {
			showAlertDialog("Please enter location.");
			return false;
		}
		if ((edtFavQuote.getText().toString() == null)
				|| (edtFavQuote.getText().toString().equals(""))) {
			showAlertDialog("Please enter quote.");
			return false;
		} else if ((edtLink.getText().toString() == null)
				|| (edtLink.getText().toString().equals(""))) {
			showAlertDialog("Please enter link.");
			return false;
		}
		return true;
	}

	private OnClickListener imageClickListenr = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ImageView imageView = (ImageView) v;

			clickedImageNumber = v.getId();

			Drawable dr = imageView.getDrawable();
			Bitmap bit = ((BitmapDrawable) dr).getBitmap();
			ImageActionActivity.bitmap = bit;
			Intent intent = new Intent(getActivity(), ImageActionActivity.class);
			intent.putExtra("imgno", (String) imageView.getTag());

			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_bottom_to_top,
					0);

		}
	};

	public void deleteImage(int index) {
		if (layoutPhotoContainer.getChildCount() > index) {
			layoutPhotoContainer.removeViewAt(index);
			photos.remove(index);
			doUpdate(edtFavQuote.getText().toString(), edtLink.getText()
					.toString(), "",
					"Please wait until image deleted on server");
		}
	}

	/*
	 * private Bitmap getCompressImage(Bitmap bm) { File file = new
	 * File(Environment.getExternalStorageDirectory().toString() + "/123.png");
	 * //Bitmap bm = (Bitmap) data.getExtras().get("data"); bm =
	 * Bitmap.createScaledBitmap(bm, 150, 150, true); try {
	 * file.createNewFile(); FileOutputStream fileOutputStream = new
	 * FileOutputStream( file); BufferedOutputStream bos = new
	 * BufferedOutputStream( fileOutputStream); bm.compress(CompressFormat.JPEG,
	 * 5, bos); bos.flush(); bos.close(); BitmapFactory.Options options = new
	 * BitmapFactory.Options();
	 * 
	 * options.inJustDecodeBounds = false; options.inPreferredConfig =
	 * Config.ARGB_8888; options.inDither = true; return
	 * BitmapFactory.decodeFile(file.getAbsolutePath(),options);
	 * 
	 * } catch (OutOfMemoryError e) {
	 * 
	 * System.gc(); return null; } catch (Exception e) { e.printStackTrace();
	 * return null; } }
	 */

	private Bitmap checkImageRotation(String imagePath) {
		try {

			ExifInterface exif = null;
			try {
				exif = new ExifInterface(imagePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			int angle = 0;
			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				angle = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				angle = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				angle = 270;
			}
			Matrix mat = new Matrix();
			mat.postRotate(angle);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 3;
			options.inJustDecodeBounds = false;
			// options.inPreferredConfig = Config.ARGB_8888;
			// options.inDither = true;

			Bitmap bm = BitmapFactory.decodeFile(imagePath, options);

			bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), mat, true);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			return bitmap;

		} catch (OutOfMemoryError oue) {
			System.gc();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
