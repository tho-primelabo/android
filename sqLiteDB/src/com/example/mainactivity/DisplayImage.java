package com.example.mainactivity;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.adapter.ImageResizer;
import com.example.dao.DAOdb;
import com.example.entity.MyImage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayImage extends Activity{
	private MyImage image;
    private ImageView imageView;
    private TextView description, txtTitle;
    private EditText editDesc, editTitle;
    private String jstring;
    private Button btnDelete;
    //private RelativeLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_image_view);
               
        imageView = (ImageView) findViewById(R.id.display_image_view);
        editTitle = (EditText) findViewById(R.id.edit_view_title);
        editDesc = (EditText) findViewById(R.id.edit_view_description);
      
        description = (TextView) findViewById(R.id.text_view_description);
        txtTitle = (TextView) findViewById(R.id.text_view_title);
        txtTitle.setHintTextColor(getResources().getColor(R.color.white));
        description.setHintTextColor(getResources().getColor(R.color.white));
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            jstring = extras.getString("IMAGE");
        }
        //Log.i("listView ---------", jstring);
        image = getMyImage(jstring);
        if (image != null) {
        	//Log.i("image ---------", image.getDescription());
	        description.setText(image.getDescription());
	        txtTitle.setText(image.getTitle());
	        Display display = getWindowManager().getDefaultDisplay();
	        Point size = new Point();
	        if(display != null) {
	        	display.getSize(size);
	        }
	        else {
	        	Log.i("size ---------", "size null");
	        }
	        int width = size.x;
	        int height = size.y;
	        imageView.setImageBitmap(ImageResizer
	                .decodeSampledBitmapFromFile(image.getPath(), width, height));
        }
        else {
        	Log.i("image ---------", "imag null");
        }
    }

    private MyImage getMyImage(String image) {
        try {
            JSONObject job = new JSONObject(image);
            return (new MyImage(job.getString("title"),
                    job.getString("description"), job.getString("path"),
                    job.getLong("datetimeLong")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * go back to main activity
     *
     * @param v
     */
    public void btnBackOnClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * update the current item;
     *
     * @param v
     */
    public void btnEditOnClick(View v) {
    	description.setVisibility(View.INVISIBLE);
    	
    	txtTitle.setVisibility(View.INVISIBLE);
    	editTitle.setVisibility(View.VISIBLE);
    	editDesc.setVisibility(View.VISIBLE);
    	btnDelete = (Button) findViewById(R.id.btnDelete);
    	btnDelete.setText("Update");
    	
    	editDesc.setText(description.getText());
    	editTitle.setText(txtTitle.getText());
    	//editTitle.setHintTextColor(getResources().getColor(R.color.white));
    	//editDesc.setHintTextColor(getResources().getColor(R.color.white));    	
    	
    }
    
    public void btnSaveOnClick(View v) {
    	image.setDescription(editDesc.getText().toString().trim());
    	image.setTitle(editTitle.getText().toString().trim());
    	//Log.i("btnSaveOnClick desc ---------", editDesc.getText().toString().trim());
    	//Log.i("btnSaveOnClick title ---------", editTitle.getText().toString().trim());
    	DAOdb db = new DAOdb(DisplayImage.this);
    	db.updateImage(image);
    	startActivity(new Intent(this, MainActivity.class));
    	finish();
    }
    /**
     * delete the current item;
     *
     * @param v
     */
    public void btnDeleteOnClick(View v) {
    	
    	Button btn = (Button)v;
    	if (btn.getText().equals("Delete")) {
	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayImage.this);
	    	// android.R.style.Theme_DeviceDefault_Dialog_MinWidth);
	    	
	    	alertDialogBuilder.setMessage("Are you sour !");
	    	alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface arg0, int arg1) {
	            	DAOdb db = new DAOdb(DisplayImage.this);
	                db.deleteImage(image);
	                db.close();
	                startActivity(new Intent(DisplayImage.this, MainActivity.class));
	                finish();
	               
	            }
	        });
	    	alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	               
	            }
	        });
	
	        AlertDialog alertDialog = alertDialogBuilder.create();
	        // create dialog
	        alertDialog.show();
    	}
    	else if (btn.getText().equals("Update")) {
    		image.setDescription(editDesc.getText().toString());
    		image.setTitle(editTitle.getText().toString());
        	Log.i("edit desc ---------", editDesc.getText().toString() + ":" + editTitle.getText().toString());
        	DAOdb db = new DAOdb(DisplayImage.this);
        	db.updateImage(image);
        	db.close();
        	startActivity(new Intent(this, MainActivity.class));
        	finish();
    	}
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        // Save the user's current game state
        if (jstring != null) {
            outState.putString("jstring", jstring);
        }
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        if (savedInstanceState.containsKey("jstring")) {
            jstring = savedInstanceState.getString("jstring");
        }
    }

}
