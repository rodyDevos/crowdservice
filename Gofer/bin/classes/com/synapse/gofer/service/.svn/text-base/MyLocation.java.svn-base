package com.synapse.gofer.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public abstract class MyLocation {

	Timer timer1;
	static LocationManager lm;
	LocationResult locationResult;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	private Context ctx;

	public boolean getLocation(Context context, LocationResult result) {
		this.ctx = context;
		locationResult = result;
		if (MyLocation.lm == null)
			MyLocation.lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

		try {
			gps_enabled = MyLocation.lm
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = MyLocation.lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		if (!gps_enabled && !network_enabled) {
			createGpsDisabledAlert(ctx);
			return false;
		}

		if (gps_enabled)
			MyLocation.lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					0, 0, locationListenerGps);
		if (network_enabled)
			MyLocation.lm.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListenerNetwork);
		timer1 = new Timer();
		timer1.schedule(new GetLastLocation(), 0);
		return true;
	}

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			Log.e("GPS --", "Location Changed");
			locationResult.gotLocation(location);
		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			locationResult.gotLocation(location);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	class GetLastLocation extends TimerTask {
		@Override
		public void run() {

			Location net_loc = null, gps_loc = null;
			if (gps_enabled)
				gps_loc = MyLocation.lm
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (network_enabled)
				net_loc = MyLocation.lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			Log.e("GPS --", "" + net_loc);
			Log.e("GPS --", "" + gps_loc);

			if (gps_loc != null && net_loc != null) {
				if (gps_loc.getTime() > net_loc.getTime())
					locationResult.gotLocation(gps_loc);
				else
					locationResult.gotLocation(net_loc);
				return;
			}

			if (gps_loc != null) {
				locationResult.gotLocation(gps_loc);
				return;
			}
			if (net_loc != null) {
				locationResult.gotLocation(net_loc);
				return;
			}
			locationResult.gotLocation(null);
		}
	}

	public void closeListeners() {
		MyLocation.lm.removeUpdates(locationListenerGps);
		MyLocation.lm.removeUpdates(locationListenerNetwork);
		this.timer1.cancel();
	}

	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}

	public abstract void locationClick();

	private void createGpsDisabledAlert(final Context act) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setMessage("Goffer wants to access your location, Please enable location service");
		builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				act.startActivity(new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showGpsOptions(Context act) {
		Intent gpsOptionsIntent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		act.startActivity(gpsOptionsIntent);
	}
}
