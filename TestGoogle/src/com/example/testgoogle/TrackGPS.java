package com.example.testgoogle;

import com.google.android.gms.location.LocationListener;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class TrackGPS extends Service implements LocationListener {

	private final Context mContext;

	boolean checkGPS = false;

	boolean checkNetwork = false;

	boolean canGetLocation = false;

	Location loc;
	double latitude;
	double longitude;

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
	protected LocationManager locationManager;

	public TrackGPS(Context mContext) {
		this.mContext = mContext;
		getLocation();
	}

	private Location getLocation() {

		try {
			locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!checkGPS && !checkNetwork) {
				Toast.makeText(mContext, "No Service Provider Available", Toast.LENGTH_SHORT).show();
			} else {
				this.canGetLocation = true;
				// First get location from Network Provider
				if (checkNetwork) {
					Toast.makeText(mContext, "Network", Toast.LENGTH_SHORT).show();

					try {
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);
						Log.d("XXX", "Network");
						if (locationManager != null) {
							loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

						}

						if (loc != null) {
							latitude = loc.getLatitude();
							longitude = loc.getLongitude();
							Toast.makeText(mContext, "latitude:" +latitude , Toast.LENGTH_SHORT).show();
						}
					} catch (SecurityException e) {

					}
				}
			}
			// if GPS Enabled get lat/long using GPS Services
			if (checkGPS) {
				Toast.makeText(mContext, "GPS", Toast.LENGTH_SHORT).show();
				if (loc == null) {
					try {
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (loc != null) {
								latitude = loc.getLatitude();
								longitude = loc.getLongitude();
							}
						}
					} catch (SecurityException e) {

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return loc;
	}

	public double getLongitude() {
		if (loc != null) {
			longitude = loc.getLongitude();
		}
		return longitude;
	}

	public double getLatitude() {
		if (loc != null) {
			latitude = loc.getLatitude();
		}
		return latitude;
	}

	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		alertDialog.setTitle("GPS Not Enabled");

		alertDialog.setMessage("Do you wants to turn On GPS");

		alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
			}
		});

		alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		alertDialog.show();
	}

	public void stopUsingGPS() {
		if (locationManager != null) {

			locationManager.removeUpdates((android.location.LocationListener) TrackGPS.this);
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
