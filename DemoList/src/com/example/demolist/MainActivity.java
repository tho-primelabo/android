package com.example.demolist;

import java.io.IOException;
import java.util.ArrayList;

import com.example.adapter.UserAdapter;
import com.example.controller.SQLiteDataController;
import com.example.controller.SQLiteUser;
import com.example.entity.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	UserAdapter adapter;
	ListView lvDemo;
	ArrayList<User> arrData;
	ImageView imgAddPerson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lvDemo = (ListView) findViewById(R.id.lvDemo);
		imgAddPerson = (ImageView) findViewById(R.id.imgAddPerson);
		//createData();
		this.createDB();
		this.getListUser();
		// arrData = new String [] {"thole", "QuynhPham", "AnHoang"};
		adapter = new UserAdapter(this, arrData);// new
													// ArrayAdapter<String>(this,
													// android.R.layout.simple_list_item_1,
													// arrData);
		lvDemo.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		imgAddPerson = (ImageView) findViewById(R.id.imgAddPerson);
		imgAddPerson.setOnClickListener(Add_Click);
		lvDemo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MainActivity.this, EditActivity.class);
				intent.putExtra("EDIT", adapter.getItem(position));
				startActivityForResult(intent, 20);
			}
		});

	}

	View.OnClickListener Add_Click = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent inte = new Intent(MainActivity.this, AddpersonActivity.class);
			startActivityForResult(inte, 10);
		}
	};

	private void createData() {
		arrData = new ArrayList<User>();
		for (int i = 0; i < 10; i++) {
			arrData.add(new User(i + 1, R.drawable.ic_launcher, "tho" + i, "abc@.com" + i));
		}
	}

	private void createDB() {
		// init database
		SQLiteDataController sql = new SQLiteDataController(this);
		try {
			sql.isCreatedDatabase();
		} catch (IOException e) {
			Log.e("Error", "open >>"+ e.toString());
			e.printStackTrace();
		}
	}

	private void getListUser() {
        SQLiteUser user = new SQLiteUser(getApplicationContext());
        arrData = new ArrayList<User>();
        arrData = user.getListUser();
 
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 10:
			if (data != null) {
				// get result from AddpersonActivity
				User person = (User) data.getSerializableExtra("ADD");
				if (resultCode == 100) {
					// set id= adapter + 1
					person.setId(adapter.getCount() + 1);
					adapter.getListData().add(0, person);
					adapter.notifyDataSetChanged();
					Toast.makeText(MainActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
				}
			} else
				Toast.makeText(MainActivity.this, "Error, try again !", Toast.LENGTH_SHORT).show();
			break;
		case 20:
			if (data != null) {
				// get data from EditActivity
				User select = (User) data.getSerializableExtra("EDIT");
				if (resultCode == 200) {
					for (User item : adapter.getListData()) {
						if (item.getId() == select.getId()) {
							item.setName(select.getName());
							item.setEmail(select.getEmail());

							break;
						}
					}
					adapter.notifyDataSetChanged();
					break;
				} else if (resultCode == 300) {
					int ID = data.getExtras().getInt("DEL");
					int position = -1;
					for (int i = 0; i < adapter.getCount(); i++) {
						if (adapter.getItem(i).getId() == ID) {
							position = i;
							break;
						}
					}
					if (position < 0) {
						Toast.makeText(MainActivity.this, "can't define position", Toast.LENGTH_SHORT).show();
					} else {
						adapter.getListData().remove(position);
						adapter.notifyDataSetChanged();
						Toast.makeText(MainActivity.this, "Delete successful!", Toast.LENGTH_SHORT).show();
					}
				}
			}

		}
	}

}
