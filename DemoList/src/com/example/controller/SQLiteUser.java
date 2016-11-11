package com.example.controller;

import java.util.ArrayList;

import com.example.demolist.R;
import com.example.entity.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class SQLiteUser extends SQLiteDataController {

	public SQLiteUser(Context con) {
		super(con);
	}

	public ArrayList<User> getListUser() {
		ArrayList<User> listUsers = new ArrayList<>();
		// open connection
		try {
			openDataBase();
			Log.e("Info-------------", "open >>"+ database.toString());
			Cursor cs = database.rawQuery("SELECT * FROM user", null);
			User user;
			while (cs.moveToNext()) {
				user = new User(cs.getInt(0), R.drawable.ic_launcher, cs.getString(1), cs.getString(2));
				listUsers.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return listUsers;
	}

	public boolean insertUser(User user) {
		boolean result = false;
		try {

			openDataBase();
			ContentValues values = new ContentValues();
			values.put("name", user.getName());
			values.put("email", user.getEmail());

			long rs = database.insert("user", null, values);
			if (rs > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	public boolean updateUser(User user) {
		boolean result = false;
		try {

			openDataBase();
			ContentValues values = new ContentValues();
			values.put("name", user.getName());
			values.put("email", user.getEmail());
			int rs = database.update("user", values, "id=" + user.getId(), null);
			if (rs > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}

	public boolean deleteUserc(int id) {
		boolean result = false;
		try {

			openDataBase();
			//
			int rs = database.delete("user", "id=" + id, null);
			if (rs > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}
}
