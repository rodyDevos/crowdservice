package com.synapse.gofer.jobandcourier;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.synapse.gofer.R;
import com.synapse.gofer.adapter.CommissionedJobListingAdapter;

public class CourierDetailFragment extends Fragment implements OnClickListener {


	private ListView jobListView = null;
	private RelativeLayout listViewHeader = null;
	private ScrollView scrollView = null;
	private Button btnJobListing = null;

	private TextView txtDone = null;
	private CommissionedJobListingAdapter mJobListingAdapter = null;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_courier_detail,container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUi();
	}

	private void initUi()
	{

		listViewHeader = (RelativeLayout)getView().findViewById(R.id.customerdetail_listview_header);
		jobListView = (ListView)getView().findViewById(R.id.customerdetail_listview);
		scrollView = (ScrollView)getView().findViewById(R.id.customerdetail_scroll);
		//btnJobListing = (Button)getView().findViewById(R.id.customerdetail_lastjob_commissioned);
		//btnJobListing.setOnClickListener(this);

		txtDone = (TextView)getView().findViewById(R.id.customerdetail_listview_header_done);
		txtDone.setOnClickListener(this);
		
		mJobListingAdapter = new CommissionedJobListingAdapter(null,getActivity().getLayoutInflater(),this,getActivity(),"");
		jobListView.setAdapter(mJobListingAdapter);
		
	}

	@Override
	public void onClick(View v) {
		if(v == btnJobListing)
		{
			listViewHeader.setVisibility(View.VISIBLE);
			jobListView.setVisibility(View.VISIBLE);
			scrollView.setVisibility(View.GONE);
		}
		else if(v == txtDone)
		{
			listViewHeader.setVisibility(View.GONE);
			jobListView.setVisibility(View.GONE);
			scrollView.setVisibility(View.VISIBLE);
		}
	}
}
