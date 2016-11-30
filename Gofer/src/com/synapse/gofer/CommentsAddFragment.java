package com.synapse.gofer;

import java.io.File;

import org.apache.http.entity.mime.content.ByteArrayBody;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.synapse.gofer.control.RoundedImageView;
import com.synapse.gofer.util.Constants;

public class CommentsAddFragment extends Fragment implements OnClickListener {

	private final int REQUEST_GALLARY = 301;
	private final int REQUEST_CAMERA = 300;
	private EditText edtComments;
	private Button btnNext, btnPrevious;
	private RoundedImageView imgPhoto;
	private ImageView btnCamera;
	private String imgPath;
	ByteArrayBody bab;
	TextView txt_no_img;

	public CommentsAddFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initViews();
		Setview();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_comment, container,
				false);

		return view;
	}

	private void Setview() {
		Bitmap bm = null;
		try {
			if (!Constants.TempImagePath.equals("")) {
				imgPath = Constants.TempImagePath;
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				bm = BitmapFactory.decodeFile(imgPath, btmapOptions);
			}
			if (bm != null) {
				Bitmap resized = Bitmap.createScaledBitmap(bm, 162, 182, true);
				imgPhoto.setImageBitmap(resized);
				Constants.imageComment = bm;
				txt_no_img.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/*
	 * Called to initialize to user interface.
	 */
	private void initViews() {
		edtComments = (EditText) getView().findViewById(R.id.edtComments);
		btnNext = (Button) getView().findViewById(R.id.btnNext);
		btnPrevious = (Button) getView().findViewById(R.id.btnPrevious);
		imgPhoto = (RoundedImageView) getView().findViewById(R.id.imgPhoto);
		btnCamera = (ImageView) getView().findViewById(R.id.btnCamera);
		txt_no_img = (TextView) getView().findViewById(R.id.txt_no_img);
		imgPhoto.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnPrevious.setOnClickListener(this);
		btnCamera.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnNext) {
			/*
			 * if(edtComments.getText().toString().length()==0){
			 * showAlertDialog("Please enter comment."); }else{
			 */
			Bundle bundle = getArguments();
			bundle.putString("Comment", edtComments.getText().toString());
			bundle.putString("imgpath", imgPath);
			Constants.TempImagePath = imgPath;
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			Fragment fragment = new ReviewFragment();
			fragmentTransaction.replace(R.id.mainContainer, fragment, "Review");
			fragmentTransaction.addToBackStack("Comments");
			fragment.setArguments(bundle);
			fragmentTransaction.commit();
			/* } */

		} else if (v == btnPrevious) {
			onBack();
		} else if (v == btnCamera) {
			selectImage();
		}
	}

	public void onBack() {
		FragmentManager fm = getFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
		}
	}

	/*
	 * private void showAlertDialog(String s) { Context context =
	 * getActivity().getApplicationContext(); int duration = Toast.LENGTH_SHORT;
	 * Toast toast = Toast.makeText(context, s, duration); toast.show(); }
	 */

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
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
					bm = BitmapFactory.decodeFile(imgPath, btmapOptions);

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == REQUEST_GALLARY) {
				Uri selectedImageUri = data.getData();
				imgPath = getPath(selectedImageUri, getActivity());
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				bm = BitmapFactory.decodeFile(imgPath, btmapOptions);
			}
			if (bm != null) {
				Bitmap resized = Bitmap.createScaledBitmap(bm, 162, 182, true);
				imgPhoto.setImageBitmap(resized);
				Constants.imageComment = bm;
				txt_no_img.setVisibility(View.GONE);
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

}
