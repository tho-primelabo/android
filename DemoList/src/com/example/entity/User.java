package com.example.entity;

import java.io.Serializable;

public class User implements Serializable {

	private int id;
	private int avatar;
	private  String name;
	private String email;
	public User (int id, int avatar, String name, String email) {
		this.id = id;
		this.avatar = avatar;
		this.email = email;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAvatar() {
        return avatar;
    }
 
    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
	
}
