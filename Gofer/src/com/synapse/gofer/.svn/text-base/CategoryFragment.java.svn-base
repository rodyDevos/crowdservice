package com.synapse.gofer;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.synapse.gofer.adapter.SpinnerCustomAdapter;
import com.synapse.gofer.model.Category;
import com.synapse.gofer.parser.CategoryPostParser;
import com.synapse.gofer.util.Constants;

public class CategoryFragment extends Fragment implements OnClickListener {

	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	private Spinner spinnerCategory;
	private EditText edtJob, edtMerchandiseCost;
	private ImageView btnNext;
	private Button btnSendNewCategory;
	private SpinnerCustomAdapter spinnerAdapter;
	private ArrayList<Category> catList;
	private Boolean isHomeCategory = false, isAutoCategory = false,
			isCourierCategory = false;
	RadioButton btnCourier, btnHome, btnAuto;

	public CategoryFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initViews();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_categoryselection,
				container, false);
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
		spinnerCategory = (Spinner) getView().findViewById(R.id.spinner);
		edtJob = (EditText) getView().findViewById(R.id.edtJobname);
		edtMerchandiseCost = (EditText) getView().findViewById(
				R.id.edtMerchandiseCost);
		btnNext = (ImageView) getView().findViewById(R.id.btnNext);
		btnSendNewCategory = (Button) getView().findViewById(
				R.id.btnSendNewCategory);
		btnNext.setOnClickListener(this);
		btnSendNewCategory.setOnClickListener(this);

		btnCourier = (RadioButton) getView().findViewById(R.id.btnCourier);
		btnCourier.setOnClickListener(this);

		btnHome = (RadioButton) getView().findViewById(R.id.btnHome);
		btnHome.setOnClickListener(this);

		btnAuto = (RadioButton) getView().findViewById(R.id.btnAuto);
		btnAuto.setOnClickListener(this);

		btnCourier.setSelected(true);
		edtMerchandiseCost
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							if (edtMerchandiseCost.getText().toString() != null
									&& edtMerchandiseCost.getText().toString()
											.length() == 0) {
								edtMerchandiseCost.setText("$");
								edtMerchandiseCost.setSelection(1);
							}
						}

					}
				});

		isCourierCategory = true;
		if (Constants.isNetAvailable(getActivity())) {
			ServerCommunication download = new ServerCommunication();
			download.execute(new String[] { "" });
		} else {
			Constants.NoInternetError(getActivity());
		}

	}

	@Override
	public void onClick(View v) {

		if (v == btnNext) {
			if (catList == null || catList.size() == 0) {
				showAlertDialog("Please select category !");
			} else if (edtJob.getText().toString().length() == 0) {
				showAlertDialog("Please enter job !");
			} else {
				Constants.hasjob_address = true;
				if (edtJob.hasFocus())
					hideSoftInput(edtJob);
				if (edtMerchandiseCost.hasFocus())
					hideSoftInput(edtMerchandiseCost);

				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				Fragment fragment = new AddressSelectionFragment();
				int index = spinnerCategory.getSelectedItemPosition();
				String id = catList.get(index).getId();
				String category_name = catList.get(index).getName();
				Bundle bundle = new Bundle();
				bundle.putString("CategoryId", id);
				bundle.putString("category_name", category_name);
				bundle.putString("JobName", edtJob.getText().toString());
				bundle.putString("Price", edtMerchandiseCost.getText()
						.toString());
				fragment.setArguments(bundle);

				fragmentTransaction.replace(R.id.mainContainer, fragment,
						"Address");
				fragmentTransaction.addToBackStack("Category");
				fragmentTransaction.commit();
			}

		} else if (v == btnSendNewCategory) {
			String to = "Gofer.CategorySuggestions@Crowdserviceinc.com";
			String subject = "Suggest a new category";
			Intent email = new Intent(Intent.ACTION_SEND);
			email.setType("text/html");
			email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
			email.putExtra(Intent.EXTRA_SUBJECT, subject);
			email.setType("message/rfc822");
			startActivity(Intent.createChooser(email,
					"Choose an Email client :"));

		} else if (v == btnHome) {
			btnCourier.setSelected(false);
			btnHome.setSelected(true);
			btnAuto.setSelected(false);
			isHomeCategory = true;
			isCourierCategory = false;
			isAutoCategory = false;
			ServerCommunication download = new ServerCommunication();
			download.execute(new String[] { "" });
		} else if (v == btnCourier) {
			btnCourier.setSelected(true);
			btnHome.setSelected(false);
			btnAuto.setSelected(false);
			isHomeCategory = false;
			isCourierCategory = true;
			isAutoCategory = false;
			ServerCommunication download = new ServerCommunication();
			download.execute(new String[] { "" });
		} else if (v == btnAuto) {
			btnCourier.setSelected(false);
			btnHome.setSelected(false);
			btnAuto.setSelected(true);
			isHomeCategory = false;
			isCourierCategory = false;
			isAutoCategory = true;
			ServerCommunication download = new ServerCommunication();
			download.execute(new String[] { "" });
		}

	}

	private void showAlertDialog(String s) {
		Context context = getActivity().getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void hideSoftInput(View view) {
		InputMethodManager inputManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/*
	 * Handler.
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == SUCCESS) {
				if (msg.obj instanceof ArrayList) {
					catList = (ArrayList<Category>) msg.obj;
					if (catList != null) {
						spinnerAdapter = new SpinnerCustomAdapter(
								getActivity(), R.layout.row_spinner, catList);
						// spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
						spinnerCategory.setAdapter(spinnerAdapter);
					}
				}
			} else if (msg.arg1 == FAILURE) {
				showAlertDialog("Please try again.");
			}
		}
	};

	/*
	 * To download data from server.
	 */
	private class ServerCommunication extends
			AsyncTask<String, String, ArrayList<Category>> {
		private final ProgressDialog progressBar = new ProgressDialog(
				getActivity());

		public ServerCommunication() {
			// TODO Auto-generated constructor stub
			progressBar.setCancelable(false);
		}

		@Override
		protected ArrayList<Category> doInBackground(String... strParams) {

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			CategoryPostParser parser = null;
			// nameValuePairs.add(new BasicNameValuePair("id",Constants.uid));

			if (isHomeCategory)
				parser = new CategoryPostParser(Constants.HTTP_HOST
						+ "viewHomecategories");
			else if (isCourierCategory)
				parser = new CategoryPostParser(Constants.HTTP_HOST
						+ "viewcategories");
			else if (isAutoCategory)
				parser = new CategoryPostParser(Constants.HTTP_HOST
						+ "viewAutocategories");

			ArrayList<Category> dataList = parser.getParseData(nameValuePairs);

			return dataList;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setMessage("Please wait while loading...");
			progressBar.show();
		}

		@Override
		protected void onPostExecute(ArrayList<Category> dataList) {
			super.onPostExecute(dataList);
			progressBar.dismiss();

			Message msg = handler.obtainMessage();
			msg.arg1 = SUCCESS;
			msg.obj = dataList;
			handler.sendMessage(msg);
		}
	}

	public void onBack() {
		FragmentManager fm = getFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();
		}
	}

}
