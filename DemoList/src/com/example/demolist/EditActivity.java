package com.example.demolist;

import com.example.entity.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditActivity extends Activity {
    ImageView imgEditAvatar;
    EditText edtEditName, edtEditEamil;
    Button btnSaveEdit, btnExitEdit, btnDel;
    User person;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        //  ivitView and getData
        initView();
        getData();
    }
    // init dialog
    //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    
    // initView
    private void initView() {
        imgEditAvatar = (ImageView) findViewById(R.id.imgEditAvatar);
        edtEditName = (EditText) findViewById(R.id.edtEditName);
        edtEditEamil = (EditText) findViewById(R.id.edtEditEmail);
      
        btnExitEdit = (Button) findViewById(R.id.btnExitEdit);
        btnSaveEdit = (Button) findViewById(R.id.btnEdit);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnSaveEdit.setOnClickListener(SaveEdit_Click);
        btnExitEdit.setOnClickListener(Exit_Edit);
        btnDel.setOnClickListener(Del_Click);
    }
    // getData
    private void getData() {
        if (getIntent().getExtras() != null) {
            person = (User) getIntent().getSerializableExtra("EDIT");
            //int a = person.getId();
            imgEditAvatar.setImageResource(person.getAvatar());
            edtEditName.setText(person.getName());
            edtEditEamil.setText(person.getEmail());
            
        }
    }
 
    View.OnClickListener SaveEdit_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EditActivity.this, MainActivity.class);
            int b = person.getId();
            User person1 = new User(b, person.getAvatar(),
                    edtEditName.getText().toString(), edtEditEamil.getText().toString());
            intent.putExtra("EDIT", person1);
            setResult(200, intent);
            finish();
        }
    };
   
    View.OnClickListener Del_Click = new View.OnClickListener() {
    	
        @Override
        public void onClick(View v) {
        	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditActivity.this);
        	// android.R.style.Theme_DeviceDefault_Dialog_MinWidth);
        	
        	alertDialogBuilder.setMessage("Are you sour !");
        	alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                	Intent intent = new Intent(EditActivity.this, MainActivity.class);
                	 intent.putExtra("DEL", person.getId());
                     setResult(300, intent);
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
            
            //Intent intent = new Intent(EditActivity.this, MainActivity.class);
            /*alertDialogBuilder.setMessage("Are you sour !");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                   
                }
            });*/
            /*intent.putExtra("DEL", person.getId());
            setResult(300, intent);
            finish();*/
        }
    };
    View.OnClickListener Exit_Edit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    
}