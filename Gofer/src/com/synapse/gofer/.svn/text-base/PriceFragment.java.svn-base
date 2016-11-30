package com.synapse.gofer;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.synapse.gofer.adapter.SpinnerCustomAdapter;
import com.synapse.gofer.model.Category;
import com.synapse.gofer.util.Constants;

public class PriceFragment extends Fragment implements OnClickListener {

	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	private Spinner spinnerCategory;
	private Button btnNext, btnPrevious;;
	private SpinnerCustomAdapter spinnerAdapter;
	private ArrayList<Category> catList;

	public PriceFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_priceselection,
				container, false);
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
		spinnerCategory = (Spinner) getView().findViewById(R.id.spinnerprice);
		btnNext = (Button) getView().findViewById(R.id.btnNext);
		btnPrevious = (Button) getView().findViewById(R.id.btnPrevious);
		btnNext.setOnClickListener(this);
		btnPrevious.setOnClickListener(this);

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
				showAlertDialog("Please select price !");
			} else {
				int index = spinnerCategory.getSelectedItemPosition();
				String price = catList.get(index).getName();
				Bundle bundle = getArguments();
				bundle.putString("Price", price);
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				Fragment fragment = new AddressSelectionFragment();
				fragmentTransaction.replace(R.id.mainContainer, fragment,
						"Address");
				fragmentTransaction.addToBackStack("Price");
				fragment.setArguments(bundle);
				fragmentTransaction.commit();
			}
		} else if (v == btnPrevious) {
			onBack();
		}
	}

	private void showAlertDialog(String s) {
		Context context = getActivity().getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, s, duration);
		toast.show();
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
						spinnerAdapter
								.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
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
			progressBar.setCancelable(false);
		}

		@Override
		protected ArrayList<Category> doInBackground(String... strParams) {
			final ArrayList<Category> dataList = new ArrayList<Category>();
			int x = 0;
			for (int i = 0; i < 30; i++) {
				x += 10;
				dataList.add(new Category("" + i, "$" + x));
			}
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