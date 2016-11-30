package com.synapse.gofer.service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.synapse.gofer.util.Constants;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GPSLoggerService extends Service {

    
    private LocationManager lm;
    private LocationListener locationListener;
   
    private static long minTimeMillis = 100;//2000
    private static long minDistanceMeters = 10;
    private static float minAccuracyMeters = 35;
    private int lastStatus = 0;
    private static boolean showingDebugToast = false;
    private static final String tag="GPSLoggerService";

    
    public GPSLoggerService() {
		// TODO Auto-generated constructor stub
	}
    
    /** Called when the activity is first created. */
    private void startLoggerService() {

        //---use the LocationManager class to obtain GPS locations---
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        minTimeMillis,
                        minDistanceMeters,
                        locationListener);          
    }
   
    private void shutdownLoggerService() {
            lm.removeUpdates(locationListener);
    }

    public class MyLocationListener implements LocationListener {
           
            public void onLocationChanged(Location loc) {
                if (loc != null) {
                    boolean pointIsRecorded = false;
                    try 
                    {
                        if (loc.hasAccuracy() && loc.getAccuracy() <= minAccuracyMeters) {
                                pointIsRecorded = true;
                                GregorianCalendar greg = new GregorianCalendar();
                                TimeZone tz = greg.getTimeZone();
                                int offset = tz.getOffset(System.currentTimeMillis());
                                greg.add(Calendar.SECOND, (offset/1000) * -1);
                               
                                Constants.lat=String.valueOf(loc.getLatitude());
                                Constants.lon=String.valueOf(loc.getLongitude());
                                
                                System.out.println("Location>>>>>>>>>>>"+Constants.lat+" "+Constants.lon);                                 
                        }
                    } catch (Exception e) {
                            Log.e(tag, e.toString());
                    } 
                        
                }
            }

            public void onProviderDisabled(String provider) {
                    if (showingDebugToast) Toast.makeText(getBaseContext(), "onProviderDisabled: " + provider,
                                    Toast.LENGTH_SHORT).show();

            }

            public void onProviderEnabled(String provider) {
                    if (showingDebugToast) Toast.makeText(getBaseContext(), "onProviderEnabled: " + provider,
                                    Toast.LENGTH_SHORT).show();

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                    String showStatus = null;
                    if (status == LocationProvider.AVAILABLE)
                            showStatus = "Available";
                    if (status == LocationProvider.TEMPORARILY_UNAVAILABLE)
                            showStatus = "Temporarily Unavailable";
                    if (status == LocationProvider.OUT_OF_SERVICE)
                            showStatus = "Out of Service";
                    if (status != lastStatus && showingDebugToast) {
                            Toast.makeText(getBaseContext(),
                                            "new status: " + showStatus,
                                            Toast.LENGTH_SHORT).show();
                    }
                    lastStatus = status;
            }

    }

    // Below is the service framework methods

    private NotificationManager mNM;

    @Override
    public void onCreate() {
            super.onCreate();
            mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            startLoggerService();
    }

    @Override
    public void onDestroy() {
            super.onDestroy();           
            shutdownLoggerService();         
    }

  

    // This is the object that receives interactions from clients. See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
            return mBinder;
    }

    public static void setMinTimeMillis(long _minTimeMillis) {
            minTimeMillis = _minTimeMillis;
    }

    public static long getMinTimeMillis() {
            return minTimeMillis;
    }

    public static void setMinDistanceMeters(long _minDistanceMeters) {
            minDistanceMeters = _minDistanceMeters;
    }

    public static long getMinDistanceMeters() {
            return minDistanceMeters;
    }

    public static float getMinAccuracyMeters() {
            return minAccuracyMeters;
    }
   
    public static void setMinAccuracyMeters(float minAccuracyMeters) {
            GPSLoggerService.minAccuracyMeters = minAccuracyMeters;
    }

    public static void setShowingDebugToast(boolean showingDebugToast) {
            GPSLoggerService.showingDebugToast = showingDebugToast;
    }

    public static boolean isShowingDebugToast() {
            return showingDebugToast;
    }

    /**
     * Class for clients to access. Because we know this service always runs in
     * the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        GPSLoggerService getService() {
                return GPSLoggerService.this;
        }
    }

}

