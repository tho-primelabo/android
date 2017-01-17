package com.example.testgoogle;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

	private static GoogleMap mMap;
	int PLACE_PICKER_REQUEST = 1;
	ImageView imgbtnsearch;
	private static LatLng COUNTRY = null;//= new LatLng(-31.952854, 115.857342);
	/*private TrackGPS gps;
    double longitude;
    double latitude;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		imgbtnsearch = (ImageView) findViewById(R.id.imgbtnsearch);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		
		
		imgbtnsearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*gps = new TrackGPS(MainActivity.this);


                if(gps.canGetLocation()){


                    longitude = gps.getLongitude();
                    latitude = gps .getLatitude();

                    Toast.makeText(getApplicationContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude),Toast.LENGTH_SHORT).show();
                }
                else
                {

                    gps.showSettingsAlert();
                }*/
				try {
					PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
					// intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
					Intent intent = intentBuilder.build(MainActivity.this);
					startActivityForResult(intent, PLACE_PICKER_REQUEST);
				} catch (GooglePlayServicesRepairableException e) {
				} catch (GooglePlayServicesNotAvailableException e) {
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		Toast.makeText(this, "ready !", Toast.LENGTH_SHORT).show();
		mMap = googleMap;
		
		TrackGPS gps = new TrackGPS(this);
		COUNTRY = new LatLng(gps.getLatitude(), gps.getLongitude());
		
		mMap.addMarker(new MarkerOptions()
                .position(COUNTRY)
                .title("Perth"));
		 mMap.moveCamera(CameraUpdateFactory.newLatLng(COUNTRY));
		/*if (ContextCompat.checkSelfPermission(MainActivity.this,
				Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			mMap.setMyLocationEnabled(true);
		} else {
		}
		// Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);*/

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PLACE_PICKER_REQUEST) {
			if (resultCode == RESULT_OK) {
				Place place = PlacePicker.getPlace(data, this);
				Toast.makeText(this, place.getName(), Toast.LENGTH_LONG).show();
				/*if (mMap == null) {
					onMapReady(mMap);
				}*/
				if (mMap!= null && place != null) {
					mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 17));
				}
				else if (mMap == null) {
					Toast.makeText(this, "mMap is null", Toast.LENGTH_LONG).show();
				}
			} else if (resultCode == PlacePicker.RESULT_ERROR) {
				// Status status = PlacePicker.get(this, data);
				Place place = PlacePicker.getPlace(data, this);
				CharSequence name = place.getName();
				Toast.makeText(this, name.toString(), Toast.LENGTH_LONG).show();
				
			} else if (resultCode == RESULT_CANCELED) {
			}
		}
	}

}
