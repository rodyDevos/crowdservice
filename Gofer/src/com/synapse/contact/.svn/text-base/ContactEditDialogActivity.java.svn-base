package com.synapse.contact;

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
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.gofer.R;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.parser.JobbidParser;
import com.synapse.gofer.util.Constants;

public class ContactEditDialogActivity extends Activity implements
		OnClickListener {

	private final int REQUEST_GALLARY = 301;
	private final int REQUEST_CAMERA = 300;
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	public static final int SENTMSG = 12;

	private LinearLayout container = null;
	private EditText editText = null, editText_amount = null;
	private TextView send = null, cancel = null, contact_photo = null,
			photoLine = null, contact_text = null;
	private Bitmap bitmap;
	private String imgPath;
	private String amount = "0";
	private String jobid;
	private String userid;
	private boolean isPhoto = false;
	private String source_message = "";

	String fromJobDetail = "";
	String fromId = "";
	String toId = "";
	String jobId = "";
	Boolean isAmount = false;
	String ws = "";
	String istextChange = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contatct_edittext_activity);

		container = (LinearLayout) findViewById(R.id.contact_edit_dailog);
		editText = (EditText) findViewById(R.id.contact_edittext);
		editText_amount = (EditText) findViewById(R.id.contact_edittext_amount);
		editText_amount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editText_amount.getText().toString() != null
						&& editText_amount.getText().toString().length() == 0) {
					editText_amount.setText("$");
					editText_amount.setSelection(1);

				}

			}

			/*
			 * @Override public void onFocusChange(View v, boolean hasFocus) {
			 * if(hasFocus) { if(editText_amount.getText().toString() != null &&
			 * editText_amount.getText().toString().length() == 0) {
			 * editText_amount.setText("$"); editText_amount.setSelection(1);
			 * 
			 * } }
			 * 
			 * }
			 */
		});

		send = (TextView) findViewById(R.id.contact_send);
		send.setOnClickListener(this);
		contact_text = (TextView) findViewById(R.id.contact_textview);

		cancel = (TextView) findViewById(R.id.contact_cancel);
		cancel.setOnClickListener(this);
		contact_photo = (TextView) findViewById(R.id.contact_photo);
		contact_photo.setOnClickListener(this);
		photoLine = (TextView) findViewById(R.id.contact_photo_line);

		// intent.putExtra("from_id",Constants.uid);
		// intent.putExtra("to_id", job.getUserId());
		// intent.putExtra("job_id",job.getId());
		// intent.putExtra("from", "jobdetails");
		// intent.putExtra("ws", "messaging");

		try {
			fromJobDetail = getIntent().getStringExtra("from");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			isAmount = getIntent().getBooleanExtra("isAmount", false);
			Log.d("isAmount", "isAmount" + isAmount);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (fromJobDetail != null
				&& fromJobDetail.equalsIgnoreCase("jobdetails")) {
			fromId = getIntent().getStringExtra("from_id");
			toId = getIntent().getStringExtra("to_id");
			jobId = getIntent().getStringExtra("job_id");
			ws = getIntent().getStringExtra("ws");

		}

		// @Ashish
		try {
			istextChange = getIntent().getStringExtra("textChange");
			// if (istextChange.equals("change")) {
			if (istextChange.equals("Ask the customer about this job"))
				send.setText("Ask");

			contact_text.setText(istextChange);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			source_message = getIntent().getStringExtra("source_message");

		} catch (Exception e) {
			e.printStackTrace();
		}
		// -Ashish-

		int gravity = getIntent().getIntExtra("dialogGravity", Gravity.CENTER);
		userid = getIntent().getStringExtra("UserID");
		jobid = getIntent().getStringExtra("JobId");
		isPhoto = getIntent().getBooleanExtra("isPhoto", false);
		if (!isPhoto) {
			contact_photo.setVisibility(View.GONE);
			photoLine.setVisibility(View.GONE);
		} else {
			contact_photo.setVisibility(View.VISIBLE);
			photoLine.setVisibility(View.VISIBLE);
		}
		if (isAmount) {
			editText_amount.setVisibility(View.VISIBLE);

		}

		container.setGravity(gravity);

	}

	@Override
	public void onClick(View v) {
		if (v == send) {
			// if(editText.getText().toString().length()>0){
			if (fromJobDetail != null
					&& fromJobDetail.equalsIgnoreCase("jobdetails")) {

				if (Constants.isNetAvailable(ContactEditDialogActivity.this)) {
					new ServerCommunicationSendMessage().execute();
				} else {
					Constants.NoInternetError(ContactEditDialogActivity.this);
				}

			} else {
				try {
					if (isAmount) {
						amount = editText_amount.getText().toString();
						if (amount.equals("$"))
							showAlertDialog("Please enter the amount");
						else
							doSubmit(editText.getText().toString(), amount);
					} else {
						doSubmit(editText.getText().toString(), amount);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// }
		} else if (v == cancel) {
			finish();
			overridePendingTransition(0, R.anim.slide_top_to_bottom);
		} else if (v == contact_photo) {
			selectImage();
		}
	}

	/*
	 * To Submit Record.
	 */
	private void doSubmit(final String message, final String amount) {

		final ProgressDialog progressBar = new ProgressDialog(
				ContactEditDialogActivity.this);
		progressBar.setMessage("Sending...");
		progressBar.show();

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {
					reqEntity.addPart("from_user_id", new StringBody(
							Constants.uid));
					reqEntity.addPart("to_user_id", new StringBody(userid));
					reqEntity.addPart("job_id", new StringBody(jobid));
					reqEntity.addPart("message", new StringBody(message));
					reqEntity.addPart("amount", new StringBody(amount));
					reqEntity.addPart("source_message", new StringBody(
							source_message));
					// Log.d("a","from_user_id"+Constants.uid+" to_user_id "+userid+" job_id "+jobid+" message "+message+" amount "+amount+" source_message "+source_message);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (bitmap != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG, 75, bos);
					byte[] data = bos.toByteArray();
					String imageName = "image" + jobid + userid;
					ByteArrayBody bab = new ByteArrayBody(data, imageName
							+ ".jpeg");
					reqEntity.addPart("image", bab);
				}
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "sendmessage");
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
					Log.d("strResponse", strResponse);
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
		thr.start();
	}

	/*
	 * Handler.
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == SUCCESS) {
				if (msg.obj instanceof ResultData) {
					ResultData data = (ResultData) msg.obj;
					if (data.getMessage().equals("refund failed")) {
						showAlertDialog("Return payment failed.");
					} else {
						showAlertDialog(data.getMessage());
					}

					finish();
					overridePendingTransition(0, R.anim.slide_top_to_bottom);
				} else {
					String message = (String) msg.obj;
					if (message.equals("refund failed")) {
						showAlertDialog("Return payment failed.");
					} else {
						showAlertDialog(message);
					}
					finish();
				}
			} else if (msg.arg1 == FAILURE) {
				Toast.makeText(ContactEditDialogActivity.this,
						"Please try again.", Toast.LENGTH_SHORT).show();
			}
		}
	};

	private void showAlertDialog(String s) {
		Context context = ContactEditDialogActivity.this
				.getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();
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

	private void selectImage() {
		final CharSequence[] items = { "Camera", "Choose from gallary",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(
				ContactEditDialogActivity.this);
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
				imgPath = getPath(selectedImageUri,
						ContactEditDialogActivity.this);
				/*
				 * BitmapFactory.Options options = new BitmapFactory.Options();
				 * options.inSampleSize = 3; options.inJustDecodeBounds = false;
				 * options.inPreferredConfig = Config.ARGB_8888;
				 * options.inDither = true;
				 */
				bm = checkImageRotation(imgPath);
			}
			if (bm != null) {
				bitmap = bm;
				doSubmit("", amount);
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

	private class ServerCommunicationSendMessage extends
			AsyncTask<String, String, String> {

		private final ProgressDialog progressBar = new ProgressDialog(
				ContactEditDialogActivity.this);

		@Override
		protected String doInBackground(String... strParams) {

			// WS : messaging
			// Post var : from_id, to_id, job_id, message

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("from_id", fromId));
			nameValuePairs.add(new BasicNameValuePair("to_id", toId));
			nameValuePairs.add(new BasicNameValuePair("job_id", jobId));
			nameValuePairs.add(new BasicNameValuePair("reply", "YES"));
			nameValuePairs.add(new BasicNameValuePair("message", editText
					.getText().toString()));
			JobbidParser parser = new JobbidParser(Constants.HTTP_HOST + ws);
			String response = parser.getParseData(nameValuePairs);
			return response;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setCancelable(false);
			progressBar.setMessage("Please wait while loading...");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(String resp) {
			super.onPostExecute(resp);
			progressBar.dismiss();

			Message msg = handler.obtainMessage();
			msg.arg1 = SUCCESS;
			msg.what = SENTMSG;
			msg.obj = resp;
			handler.sendMessage(msg);
		}
	}

}
