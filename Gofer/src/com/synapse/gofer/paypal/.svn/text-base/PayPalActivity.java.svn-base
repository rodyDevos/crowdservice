package com.synapse.gofer.paypal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PayPalActivity extends Activity {

	private static final String TAG = "PayPalActivity";

	private WebView mWebView;

	private ProgressDialog mProgressDialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		mWebView = new WebView(this);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new CustomWebViewClient());
		setContentView(mWebView);

		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		Log.d(TAG, url);

		mWebView.loadUrl(url);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mProgressDialog != null) {
			if (mProgressDialog.isShowing()) {
				mProgressDialog.cancel();
			}
			mProgressDialog = null;
		}
	}

	public void showProgressDialog() {
		mProgressDialog = ProgressDialog.show(PayPalActivity.this, "",
				"Loading...");
		mProgressDialog.show();
	}

	public void hideProgressDialog() {
		try {
			if (mProgressDialog.isShowing()) {
				mProgressDialog.cancel();
			}
			mProgressDialog = null;
		} catch (Exception e) {

		}
	}

	private class CustomWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.e("VIPI", "onPageStarted URL >> " + url);
			showProgressDialog();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			hideProgressDialog();
			if (url.contains("/paymentReturn")
					|| url.contains("/paymentCancel")) {
				finish();
			}
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			return false;
		}
	}
}
