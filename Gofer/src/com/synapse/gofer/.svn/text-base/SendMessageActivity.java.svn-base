package com.synapse.gofer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.synapse.gofer.control.RoundedImageView;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.util.Constants;

public class SendMessageActivity extends Activity implements OnClickListener {

	TextView btnBack;
	private final int REQUEST_GALLARY = 301;
	private final int REQUEST_CAMERA = 300;
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	private RoundedImageView imgPhoto;
	private String imgPath;
	ImageView btnCamera;
	String userid, jobid;
	Bitmap bm = null;
	Button btnNext;
	EditText edtComments;
	TextView txt_customer;
	String from_user_id, to_user_id, job_id, job_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendmessagelayout);

		init();
	}

	private void init() {
		btnBack = (TextView) findViewById(R.id.btnBack);
		imgPhoto = (RoundedImageView) findViewById(R.id.imgPhoto);
		btnCamera = (ImageView) findViewById(R.id.btnCamera);
		btnNext = (Button) findViewById(R.id.btnNext);
		edtComments = (EditText) findViewById(R.id.edtComments);

		txt_customer = (TextView) findViewById(R.id.txt_customer);

		from_user_id = getIntent().getStringExtra("from_user_id");
		to_user_id = getIntent().getStringExtra("to_user_id");
		job_id = getIntent().getStringExtra("job_id");
		job_name = getIntent().getStringExtra("job_name");

		txt_customer.setText(job_name);
		imgPhoto.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnBack) {

		}
		if (v == imgPhoto) {
			selectImage();
		}

		if (v == btnNext) {
			doSubmit(edtComments.getText().toString());
		}
	}

	private void selectImage() {
		final CharSequence[] items = { "Camera", "Choose from gallary",
				"Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SendMessageActivity.this);
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
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
					bm = BitmapFactory.decodeFile(imgPath, btmapOptions);

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == REQUEST_GALLARY) {
				Uri selectedImageUri = data.getData();
				imgPath = getPath(selectedImageUri, SendMessageActivity.this);
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				bm = BitmapFactory.decodeFile(imgPath, btmapOptions);
			}
			if (bm != null) {
				Bitmap resized = Bitmap.createScaledBitmap(bm, 162, 182, true);
				imgPhoto.setImageBitmap(resized);
				Constants.imageComment = bm;
				btnCamera.setVisibility(View.GONE);
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

	private void doSubmit(final String message) {

		final ProgressDialog progressBar = new ProgressDialog(
				SendMessageActivity.this);
		progressBar.setMessage("Sending...");
		progressBar.show();

		Thread thr = new Thread(new Runnable() {
			// @Override
			public void run() {
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {
					Log.d("VIPII", "from_user_id" + from_user_id
							+ " to_user_id " + to_user_id + " job_id " + job_id
							+ " message " + message + " job_name " + job_name);

					reqEntity.addPart("from_user_id", new StringBody(
							from_user_id));
					reqEntity.addPart("to_user_id", new StringBody(to_user_id));
					reqEntity.addPart("job_id", new StringBody(job_id));
					reqEntity.addPart("message", new StringBody(message));
					reqEntity.addPart("source_message", new StringBody("Text"));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (bm != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bm.compress(CompressFormat.JPEG, 75, bos);
					byte[] data = bos.toByteArray();
					String imageName = "image" + job_id + from_user_id;
					Log.d("VIPIII", "imageName> " + imageName);
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
					Log.d("VIPIII", "RES> " + strResponse);
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
						showAlertDialog("Message sending failed.");
					} else {
						showAlertDialog(data.getMessage());
					}

					finish();
					overridePendingTransition(0, R.anim.slide_top_to_bottom);
				} else {
					String message = (String) msg.obj;
					if (message.equals("refund failed")) {
						showAlertDialog("Message sending failed.");
					} else {
						showAlertDialog(message);
					}
					finish();
				}
			} else if (msg.arg1 == FAILURE) {
				Toast.makeText(SendMessageActivity.this, "Please try again.",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private void showAlertDialog(String s) {
		Context context = SendMessageActivity.this.getApplicationContext();
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

}
