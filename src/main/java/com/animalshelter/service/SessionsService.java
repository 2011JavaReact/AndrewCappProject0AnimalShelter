package com.animalshelter.service;

import com.animalshelter.dao.DatabaseUserDAO;

public class SessionsService {

	private String username;
	private String password;
	
	public SessionsService() {
		// TODO Auto-generated constructor stub
	}
	
	public SessionsService(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean validateUser() {
		return new DatabaseUserDAO().validUsername(getUsername(), getPassword());
	}
	
	@Override
	public String toString() {
		return "SessionsService [username=" + username + ", password=" + password + "]";
	}

	
}
