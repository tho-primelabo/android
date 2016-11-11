package com.example.demolist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.entity.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddpersonActivity extends Activity {
	EditText  edtAddName, edtAddEmail;
    Button btnAdd;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_person);
        //getSupportActionBar().hide();
        initview();
    }
 
    private void initview() {
    	 Log.i("testTag","before start adapter");
        edtAddName = (EditText ) findViewById(R.id.edtAddname);
        edtAddEmail = (EditText ) findViewById(R.id.edtaddEmail);
        
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	if( edtAddName.getText().toString().length() == 0 ) {
            		edtAddName.setError( "Name is required!" );
            	}
            	final String email = edtAddEmail.getText().toString();
				if (!isValidEmail(email)) {
					edtAddEmail.setError("Invalid Email");
					return;
				}

				
                User user = new User(0,R.drawable.abc_ic_go, edtAddName.getText().toString(),
                        edtAddEmail.getText().toString());
                Intent intent = new Intent(AddpersonActivity.this, MainActivity.class);
                intent.putExtra("ADD", user);//("ADD", person);
                setResult(100, intent);
                finish();
            	 //Toast.makeText(AddpersonActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
            	 //Toast.makeText(AddpersonActivity.this, "You Clicked at ", Toast.LENGTH_SHORT).show();
            }
        });
    }
	 // validating email id
	 	private boolean isValidEmail(String email) {
	 		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	 				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	 		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	 		Matcher matcher = pattern.matcher(email);
	 		return matcher.matches();
	 	}
	
	 	// validating password with retype password
	 	/*private boolean isValidPassword(String pass) {
	 		if (pass != null && pass.length() > 6) {
	 			return true;
	 		}
	 		return false;
	 	}*/
}