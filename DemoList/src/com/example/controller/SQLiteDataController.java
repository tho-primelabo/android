package com.example.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteDataController extends SQLiteOpenHelper {

	private static String TAG = "DataBaseHelper"; 
	public String DB_PATH = "//data//data//%s//databases//";
	private static String DB_NAME = "mySqLiteDB.sqlite";
	public SQLiteDatabase database;
	private final Context mContext;

	public SQLiteDataController(Context con) {
		super(con, DB_NAME, null, 1);
		DB_PATH = String.format(DB_PATH, con.getPackageName());
		this.mContext = con;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	/**
	 * copy database from assets to the device if not existed
	 *
	 * @return true if not exist and create database success
	 * @throws IOException
	 */
	public boolean isCreatedDatabase() throws IOException {
		// Default  DB existed
		boolean result = true;
		// copy from Asses to Data
		if (!checkExistDataBase()) {
			
			this.getReadableDatabase();
			try {
				copyDataBase();
				 Log.e(TAG, "createDatabase database created");
				result = false;
			} catch (Exception e) {
				Log.e("Data Error", "open >>"+ e.toString());
				throw new Error("Error copying database");
			}
		}

		return result;
	}

	/**
	 * check whether database exist on the device?
	 *
	 * @return true if existed
	 */
	private boolean checkExistDataBase() {

		File dbFile = new File(DB_PATH + DB_NAME);
        Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
	}

	/**
	 * copy database from assets folder to the device
	 *
	 * @throws IOException
	 */
	private void copyDataBase() throws IOException {
		InputStream myInput = mContext.getAssets().open(DB_NAME);
		OutputStream myOutput = new FileOutputStream(DB_PATH + DB_NAME);
		byte[] buffer = new byte[1024];
		Log.v("buffer----------", buffer.toString());
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();
	}
	
	/**
     * delete database file
     *
     * @return
     */
    public boolean deleteDatabase() {
        File file = new File(DB_PATH + DB_NAME);
        if (file != null && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * open database
     *
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {
        database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
    }
    
    public int deleteData_From_Table(String tbName) {

        int result = 0;
        try {
            openDataBase();
            database.beginTransaction();
            result = database.delete(tbName, null, null);
            if (result >= 0) {
                database.setTransactionSuccessful();
            }
        } catch (Exception e) {
            database.endTransaction();
            close();
        } finally {
            database.endTransaction();
            close();
        }

        return result;
    }

}
