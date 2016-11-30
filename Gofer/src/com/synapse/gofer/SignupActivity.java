package com.synapse.gofer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.synapse.gofer.control.CircleImageView;
import com.synapse.gofer.model.ResultData;
import com.synapse.gofer.util.Constants;
import com.synapse.gofer.util.Utils;

public class SignupActivity extends Activity implements OnClickListener,
		LocationListener {

	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	private EditText edtUname, edtFname, edtLname, edtEmail, edtPwd;
	/*
			edtTrueAddr1, edtTrueAddr2, edtTrueAddr3, edtTrueAddr4,
			edtTrueAddr5, edtapproxAdr1, edtapproxAdr2, edtapproxAdr3,
			edtapproxAdr4, edtapproxAdr5;
			*/
	private Button btnSubmit;
	private ImageButton btnFBSignup;
	
	private CircleImageView imgPhoto;
	private final int REQUEST_CAMERA = 1001;
	private final int REQUEST_GALLARY = 1002;
	private Bitmap bitmap;
	private String PATH;
	private LocationManager mlocManager = null;
	private EditText edtConfirmPwd = null;

	CallbackManager callbackManager;
	
	// private EditText cardNumber = null, cardCvv = null;
	// private TextView cardexpiry = null;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		
		FacebookSdk.sdkInitialize(this.getApplicationContext());
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signup_register);

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
	                "com.facebook.samples.loginhowto", 
	                 PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
			  	    MessageDigest md = MessageDigest.getInstance("SHA");
			  	    md.update(signature.toByteArray());
			  	    	Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			  	  }
		} catch (NameNotFoundException e) {
	     	
		} catch (NoSuchAlgorithmException e) {
		     	
		}
	
		initViews();

		mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				1000, 0, this);
	}

	public void setupUI(View view) {

	    //Set up touch listener for non-text box views to hide keyboard.
	    if(!(view instanceof EditText)) {

	        view.setOnTouchListener(new OnTouchListener() {

	            public boolean onTouch(View v, MotionEvent event) {
	                hideSoftKeyboard(SignupActivity.this);
	                return false;
	            }

	        });
	    }

	    //If a layout container, iterate over children and seed recursion.
	    if (view instanceof ViewGroup) {

	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

	            View innerView = ((ViewGroup) view).getChildAt(i);

	            setupUI(innerView);
	        }
	    }
	}
	
	/*
	 * Called To initialize to user interface.
	 */
	private void initViews() {
		imgPhoto = (CircleImageView) findViewById(R.id.imgPhoto);
		Bitmap testcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.com_facebook_profile_picture_blank_square);
		imgPhoto.setImageBitmap(Constants.getRoundedCornerImage(testcon, 0, 0));

		edtUname = (EditText) findViewById(R.id.userName);
		edtFname = (EditText) findViewById(R.id.userFName);
		edtLname = (EditText) findViewById(R.id.userLName);
		edtEmail = (EditText) findViewById(R.id.userEmail);
		edtPwd = (EditText) findViewById(R.id.userPassword);

		edtConfirmPwd = (EditText) findViewById(R.id.userConfirmPassword);

		/*
		edtTrueAddr1 = (EditText) findViewById(R.id.userTrueAddress1);
		edtTrueAddr2 = (EditText) findViewById(R.id.userTrueAddress2);
		edtTrueAddr3 = (EditText) findViewById(R.id.userTrueAddress3);
		edtTrueAddr4 = (EditText) findViewById(R.id.userTrueAddress4);
		edtTrueAddr5 = (EditText) findViewById(R.id.userTrueAddress5);

		edtapproxAdr1 = (EditText) findViewById(R.id.userapprox_address1);
		edtapproxAdr2 = (EditText) findViewById(R.id.userapprox_address2);
		edtapproxAdr3 = (EditText) findViewById(R.id.userapprox_address3);
		edtapproxAdr4 = (EditText) findViewById(R.id.userapprox_address4);
		edtapproxAdr5 = (EditText) findViewById(R.id.userapprox_address5);
		*/
		
		// cardNumber = (EditText)findViewById(R.id.reg_card_number);
		// cardCvv = (EditText)findViewById(R.id.reg_card_cvv);
		// cardexpiry = (TextView)findViewById(R.id.reg_card_expiry);
		// cardexpiry.setOnClickListener(this);

		// edtAdr2=(EditText)findViewById(R.id.userAddress2);
		// edtAdr3=(EditText)findViewById(R.id.userAddress3);
		// edtQuote=(EditText)findViewById(R.id.userFavoriteQuote);

		btnSubmit = (Button) findViewById(R.id.btnRegister);
		btnSubmit.setOnClickListener(this);
		imgPhoto.setOnClickListener(this);

		btnFBSignup = (ImageButton) findViewById(R.id.btnFBSignup);
		btnFBSignup.setOnClickListener(this);
		
		TextView back = (TextView) findViewById(R.id.btnBack);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String fName = bundle.getString("fName");
			String lName = bundle.getString("lName");
			String userName = bundle.getString("userName");
			String email = bundle.getString("email");

			edtUname.setText(userName);
			edtFname.setText(fName);
			edtLname.setText(lName);
			edtEmail.setText(email);

			if (SinupOptionActivity.image != null) {
				imgPhoto.setImageBitmap(SinupOptionActivity.image);
			}
		}

	}

	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		hideSoftKeyboard(this);
		if (v == imgPhoto) {
			selectImage();
		} else if (v == btnSubmit) {
			if (validate(SignupActivity.this)) {
				
				doRegister(edtUname.getText().toString(), edtFname.getText()
						.toString(), edtLname.getText().toString(), edtEmail
						.getText().toString(), edtPwd.getText().toString(),
						"", "", "", "",
						"", "", "",
						"", "", "");
								
			}
		} else if (v == btnFBSignup) {
			onFBLogin();
		}
		/*
		 * else if(v == cardexpiry) { Calendar calendar =
		 * Calendar.getInstance(); int yy = calendar.get(Calendar.YEAR); int mm
		 * = calendar.get(Calendar.MONTH); int dd =
		 * calendar.get(Calendar.DAY_OF_MONTH); new DatePickerDialog(this, new
		 * DatePickerDialog.OnDateSetListener() {
		 * 
		 * @Override public void onDateSet(DatePicker view, int year, int
		 * monthOfYear, int dayOfMonth) { String str = monthOfYear + "/" + year;
		 * cardexpiry.setText(str); } }, yy, mm, dd).show(); }
		 */
	}

	boolean validate(Context cont) {
		if ((edtUname.getText().toString() == null)
				|| (edtUname.getText().toString().equals(""))) {
			showAlertDialog("Please enter user name.");
			return false;
		} else if (edtUname.getText().toString().length() < 3) {
			showAlertDialog("User name should be of minimum 3 characters!");
			return false;
		} else if (!edtUname.getText().toString().matches("[a-zA-Z0-9]*")) {
			showAlertDialog("User name can not contain any special character.");
			return false;
		} else if (edtUname.getText().toString().length() > 8) {
			showAlertDialog("User name should be of maximum 8 characters!");
			return false;
		} else if (!edtFname.getText().toString().matches("[a-zA-Z0-9 ]*")) {
			showAlertDialog("First name can not contain any special character.");
			return false;
		} else if (edtFname.getText().toString().equals(".")) {
			showAlertDialog("First name can not contain any special character.");
			return false;
		} else if (edtFname.getText().toString().length() > 20) {
			showAlertDialog("First name should be of maximum 20 characters!");
			return false;
		} else if (!edtLname.getText().toString().matches("[a-zA-Z0-9 ]*")) {
			showAlertDialog("Last name can not contain any special character.");
			return false;
		} else if (edtLname.getText().toString().equals(".")) {
			showAlertDialog("Last name can not contain any special character.");
			return false;
		} else if (edtLname.getText().toString().length() > 20) {
			showAlertDialog("Last name should be of maximum 20 characters!");
			return false;
		} else if ((edtFname.getText().toString() == null)
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
		} else if ((edtPwd.getText().toString() == null)
				|| (edtPwd.getText().toString().equals(""))) {
			showAlertDialog("Please enter password.");
			return false;
		} else if ((edtConfirmPwd.getText().toString() == null)
				|| (edtConfirmPwd.getText().toString().equals(""))) {
			showAlertDialog("Please enter comfirm password.");
			return false;
		}/* else if ((edtTrueAddr1.getText().toString() == null)
				|| (edtTrueAddr1.getText().toString().equals(""))) {
			showAlertDialog("Please enter true address.");
			return false;
		} */
		else if (edtPwd.getText().toString() != null
				&& edtConfirmPwd.getText().toString() != null) {
			if (!edtPwd.getText().toString()
					.equalsIgnoreCase(edtConfirmPwd.getText().toString())) {
				showAlertDialog("Password and confirm password does not match!");
				return false;
			}
		} else if (edtPwd.getText().toString() == null
				|| edtPwd.getText().toString().length() < 6) {
			showAlertDialog("Password should be of minimum 6 characters!");
			return false;
		}

		/*
		 * else if(cardNumber.getText().toString() == null ||
		 * (cardNumber.getText().toString().equals(""))) {
		 * showAlertDialog("Please enter card number."); return false; } else
		 * if(cardCvv.getText().toString() == null ||
		 * (cardCvv.getText().toString().equals(""))) {
		 * showAlertDialog("Please enter card cvv number."); return false; }
		 * else if(cardexpiry.getText().toString() == null ||
		 * (cardexpiry.getText().toString().equals(""))) {
		 * showAlertDialog("Please enter card expiry date."); return false; }
		 */

		// else if( (edtAdr2.getText().toString() == null ) ||
		// (edtAdr2.getText().toString().equals("")) )
		// {
		// showAlertDialog("Please enter address 2.");
		// return false;
		// }
		// else if( (edtAdr3.getText().toString() == null ) ||
		// (edtAdr3.getText().toString().equals("")) )
		// {
		// showAlertDialog("Please enter address 3.");
		// return false;
		// }
		// else if( (edtQuote.getText().toString() == null ) ||
		// (edtQuote.getText().toString().equals("")) )
		// {
		// showAlertDialog("Please enter quote.");
		// return false;
		// }

		return true;
	}

	private void onFBLogin(){
		callbackManager = CallbackManager.Factory.create();

	    // Set permissions 
	    LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_friends", "user_birthday" ));

	    LoginManager.getInstance().registerCallback(callbackManager,
	            new FacebookCallback<LoginResult>() {
	                @Override
	                public void onSuccess(LoginResult loginResult) {

	                    System.out.println("Success");
	                    GraphRequest request = GraphRequest.newMeRequest(
	                            loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
	                                @Override
	                                public void onCompleted(JSONObject json, GraphResponse response) {
	                                    if (response.getError() != null) {
	                                        // handle error
	                                        System.out.println("ERROR");
	                                    } else {
	                                        System.out.println("Success");
	                                        try {

	                                            String jsonresult = String.valueOf(json);
	                                            System.out.println("JSON Result"+jsonresult);

	                                            String str_email = json.getString("email");
	                                            String str_id = json.getString("id");
	                                            String str_password = str_id + "_P@ssword";
	                                            String str_firstname = json.getString("first_name");
	                                            String str_lastname = json.getString("last_name");
	                                            String str_username = str_firstname.toLowerCase() + str_lastname.toLowerCase() + str_id;
	                                            
	                                            Log.d("Facebook Login Info", "username:" + str_username + " pass:" + str_password);
	                                            doRegister(str_username, str_firstname, str_lastname, str_email, str_password,
	                            						"", "", "", "",
	                            						"", "", "",
	                            						"", "", "");
	                                            
	                                        } catch (JSONException e) {
	                                            e.printStackTrace();
	                                        }
	                                    }
	                                }

	                            });
	                    Bundle parameters = new Bundle();
	                    parameters.putString("fields", "id,first_name,last_name,picture,email");
	                    request.setParameters(parameters);
	                    request.executeAsync();

	                }

	                @Override
	                public void onCancel() {
	                    Log.d("FB Login","On cancel");
	                }

	                @Override
	                public void onError(FacebookException error) { 
	                    Log.d("FB Login",error.toString());
	                }
	    });
	}
	private void showAlertDialog(String s) {
		Context context = getApplicationContext();
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

	private void doRegister(final String edtUname, final String edtFname,
			final String edtLname, final String edtEmail, final String edtPwd,
			final String edtTrueAddr1, final String edtTrueAddr2,
			final String edtTrueAddr3, final String edtTrueAddr4,
			final String edtTrueAddr5, final String edtappAdr1,
			final String edtappAdr2, final String edtappAdr3,
			final String edtappAdr4, final String edtappAdr5) {
		final ProgressDialog progressBar = new ProgressDialog(
				SignupActivity.this);
		progressBar.setMessage("Please wait while registering...");
		progressBar.setCancelable(false);
		progressBar.show();

		Thread thread = new Thread(new Runnable() {
			// @Override
			public void run() {
				Location loc = mlocManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				Geocoder geocoder = new Geocoder(SignupActivity.this);
				try {
					if(loc != null){
						Constants.lat = String.valueOf(loc.getLatitude());
						Constants.lon = String.valueOf(loc.getLongitude());
						Constants.locationAdd = Utils.getfullAddres((geocoder
								.getFromLocation(loc.getLatitude(),
										loc.getLongitude(), 1)).get(0));
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				try {
					reqEntity.addPart("username", new StringBody(edtUname));
					reqEntity.addPart("first_name", new StringBody(edtFname));
					reqEntity.addPart("last_name", new StringBody(edtLname));
					reqEntity.addPart("email", new StringBody(edtEmail));
					reqEntity.addPart("password", new StringBody(edtPwd));
					reqEntity.addPart("billing_add1", new StringBody(
							edtTrueAddr1));
					reqEntity.addPart("address2", new StringBody(edtTrueAddr2));
					reqEntity.addPart("address3", new StringBody(edtTrueAddr3));
					reqEntity.addPart("address4", new StringBody(edtTrueAddr4));
					reqEntity.addPart("address5", new StringBody(edtTrueAddr5));
					// reqEntity.addPart("address2", new StringBody(edtAdr1));
					// reqEntity.addPart("address3", new StringBody(edtAdr2));
					reqEntity.addPart("aprox_address", new StringBody(
							edtappAdr1));
					reqEntity.addPart("aprox_address2", new StringBody(
							edtappAdr2));
					reqEntity.addPart("aprox_address3", new StringBody(
							edtappAdr3));
					reqEntity.addPart("aprox_address4", new StringBody(
							edtappAdr4));
					reqEntity.addPart("aprox_address5", new StringBody(
							edtappAdr5));

					/*
					 * reqEntity.addPart("cardNumber", new
					 * StringBody(cardNumber.getText().toString()));
					 * reqEntity.addPart("cardCvvNumber", new
					 * StringBody(cardCvv.getText().toString())); String str =
					 * cardexpiry.getText().toString(); String month = "";
					 * String year = ""; if(str.length() > 0) { month =
					 * str.split("/")[0]; year = str.split("/")[1]; }
					 * reqEntity.addPart("dcExpMonth", new StringBody(month));
					 * reqEntity.addPart("dcExpYear", new StringBody(year));
					 */

					reqEntity.addPart("device_id", new StringBody(
							Constants.gcmRegistrationId));
					reqEntity
							.addPart("latitude", new StringBody(Constants.lat));
					reqEntity.addPart("longitude",
							new StringBody(Constants.lon));
					reqEntity.addPart("is_login", new StringBody("1"));
					reqEntity.addPart("device_type", new StringBody("2"));
					reqEntity.addPart("location", new StringBody(
							Constants.locationAdd));

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (bitmap != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG, 75, bos);
					byte[] data = bos.toByteArray();
					String imageName = "image" + edtUname;
					ByteArrayBody bab = new ByteArrayBody(data, imageName
							+ ".jpeg");
					reqEntity.addPart("image", bab);
				}
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Constants.HTTP_HOST
						+ "registration");
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
					Log.d("Signup Result", strResponse);
					JSONObject jsonresponse = new JSONObject(strResponse);
					resultData = new ResultData();
					resultData.setAuthenticated(jsonresponse
							.getString("status"));
					try {
						resultData.setMessage(jsonresponse.getString("message"));
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					try {
						if(jsonresponse.has("id"))
							resultData.setUserid(jsonresponse.getString("id"));
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
					LoginManager.getInstance().logOut();
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
						// showAlertDialog(data.getMessage());
						Constants.uid = data.getUserid();
						Constants.userType = data.getUserType();
						Constants.approxAddress = data.getApproxAddress();
						Constants.username = edtUname.getText().toString();
						// saveLogin(edtUsername.getText().toString(),
						// edtPassword.getText().toString());
						startActivity(new Intent(SignupActivity.this,
								MapActivity.class));
						finish();
					} else {
						showAlertDialog(data.getMessage());
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
					
					setPhoto();

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == REQUEST_GALLARY) {
				Uri selectedImageUri = data.getData();
				String path = getPath(selectedImageUri, SignupActivity.this);
				BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inSampleSize=1;
				bitmap = BitmapFactory.decodeFile(path, options);
				
				setPhoto();
			}
			/*
			 * if (bm != bitmap) { bitmap.recycle(); bitmap = bm; } System.gc();
			 */
			
			
			else{
				callbackManager.onActivityResult(requestCode, resultCode, data);
			}
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
			imgPhoto.setMaxHeight(maxHeight);
			imgPhoto.setImageBitmap(Constants.getRoundedCornerImage(resized,
					maxWidth, maxHeight));
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		mlocManager.removeUpdates(this);
		super.onDestroy();
	}
}
