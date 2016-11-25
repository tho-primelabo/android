package com.example.mainactivity;

import java.util.ArrayList;

import com.example.adapter.ImageAdapter;
import com.example.dao.DAOdb;
import com.example.entity.MyImage;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ArrayList<MyImage> images;
	private ImageAdapter imageAdapter;
	private ListView listView;
	private Uri mCapturedImageURI;
	private static final int RESULT_LOAD_IMAGE = 1;
	private static final int REQUEST_IMAGE_CAPTURE = 2;
	private DAOdb daOdb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Construct the data source
		images = new ArrayList<MyImage>();
		// Create the adapter to convert the array to views
		imageAdapter = new ImageAdapter(this, images);
		// Attach the adapter to a ListView
		listView = (ListView) findViewById(R.id.main_list_view);
		imageAdapter.notifyDataSetChanged();
		listView.setAdapter(imageAdapter);
		addItemClickListener(listView);
		initDB();
	}

	@Override
	protected void onResume() {

		super.onResume();
		imageAdapter.notifyDataSetChanged();
		listView.setAdapter(imageAdapter);
	}

	/**
	 * initialize database
	 */
	private void initDB() {
		daOdb = new DAOdb(this);
		Log.i("init db ---------", "init db");
		// add images from database to images ArrayList
		for (MyImage mi : daOdb.getImages()) {
			// images.add(mi);
			Log.d("init debug ---------", mi.getTitle() + ":" + mi.getDescription());
			imageAdapter.add(mi);
			imageAdapter.notifyDataSetChanged();
		}
	}

	public void btnAddOnClick(View view) {
		Log.i("init dialog ---------", "init dialog");
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.custom_dialog_box);
		dialog.setTitle("Alert Dialog View");
		Button btnExit = (Button) dialog.findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.findViewById(R.id.btnChoosePath).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activeGallery();
			}
		});
		dialog.findViewById(R.id.btnTakePhoto).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activeTakePhoto();
			}
		});

		// show dialog on screen
		dialog.show();
	}

	/**
	 * take a photo
	 */
	private void activeTakePhoto() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			String fileName = "temp.jpg";
			ContentValues values = new ContentValues();
			values.put(MediaStore.Images.Media.TITLE, fileName);
			mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	/**
	 * to gallery
	 */
	private void activeGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		startActivityForResult(intent, RESULT_LOAD_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_LOAD_IMAGE:
			if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				if (data.getData() != null) {
					Uri selectedImage = data.getData();

					Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String picturePath = cursor.getString(columnIndex);
					cursor.close();
					MyImage image = new MyImage();
					//image.setTitle("Title:");
					//image.setDescription("Desciption:");
					image.setDatetime(System.currentTimeMillis());
					image.setPath(picturePath);
					images.add(image);
					daOdb.addImage(image);
				} else {
					if (data.getClipData() != null) {
						ClipData mClipData = data.getClipData();
						ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
						for (int i = 0; i < mClipData.getItemCount(); i++) {

							ClipData.Item item = mClipData.getItemAt(i);
							Uri uri = item.getUri();
							mArrayUri.add(uri);
							// Get the cursor
							Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
							// Move to first row
							cursor.moveToFirst();

							int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
							String picturePath = cursor.getString(columnIndex);
							MyImage image = new MyImage();
							if(image.getTitle() == null ) {
								image.setTitle("");
							}
							if (image.getDescription() == null) {
								image.setDescription("");
							}
							image.setDatetime(System.currentTimeMillis());
							image.setPath(picturePath);
							images.add(image);
							daOdb.addImage(image);
							cursor.close();

						}
						Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
					}
				}
			}
		case REQUEST_IMAGE_CAPTURE:
			if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
				String[] projection = { MediaStore.Images.Media.DATA };
				Cursor cursor =
						/*
						 * managedQuery(mCapturedImageURI, projection, null,
						 * null, null);
						 */
						getContentResolver().query(mCapturedImageURI, projection, null, null, null);
				int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				String picturePath = cursor.getString(column_index_data);
				MyImage image = new MyImage();
				image.setTitle("Title:");
				image.setDescription("Take a photo and add it to list view");
				image.setDatetime(System.currentTimeMillis());
				image.setPath(picturePath);
				images.add(image);
				daOdb.addImage(image);
			}
		}
	}

	/**
	 * item clicked listener used to implement the react action when an item is
	 * clicked.
	 *
	 * @param listView
	 */
	private void addItemClickListener(final ListView listView) {
		Log.i("listView ---------", "item click");
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				MyImage image = (MyImage) listView.getItemAtPosition(position);
				Log.i("listView click ---------", image.getPath());
				Intent intent = new Intent(MainActivity.this, DisplayImage.class);
				intent.putExtra("IMAGE", (new Gson()).toJson(image));
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Save the user's current game state
		if (mCapturedImageURI != null) {
			outState.putString("mCapturedImageURI", mCapturedImageURI.toString());
		}
		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// Always call the superclass so it can restore the view hierarchy
		super.onRestoreInstanceState(savedInstanceState);

		// Restore state members from saved instance
		if (savedInstanceState.containsKey("mCapturedImageURI")) {
			mCapturedImageURI = Uri.parse(savedInstanceState.getString("mCapturedImageURI"));
		}
	}
}
