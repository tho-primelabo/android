package com.example.adapter;

import java.util.ArrayList;

import com.example.demolist.R;
import com.example.entity.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserAdapter extends BaseAdapter {

	ArrayList<User> listData;
	LayoutInflater inflater;
	ImageView imgAvatar;
	
	public UserAdapter(Context context, ArrayList<User> listData) {
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listData = listData;
	}
	
	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public User getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return 0;
	}
	public ArrayList<User> getListData() {
        return listData;
    }
 
    public void setListData(ArrayList<User> listData) {
        this.listData = listData;
    }
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.item_listview, parent, false);
		}
		
		User item = listData.get(position);
		TextView txtName, txtEmail;
		imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
		imgAvatar.setImageResource(item.getAvatar());
		txtName = (TextView) view.findViewById(R.id.txtName);
		txtEmail = (TextView) view.findViewById(R.id.txtEmail);
		txtName.setText(item.getName());
		txtEmail.setText(item.getEmail());
		return view;
	}
	
}
