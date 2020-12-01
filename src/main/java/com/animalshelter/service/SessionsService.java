package com.animalshelter.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.animalshelter.dao.DatabaseUserDAO;
import com.animalshelter.exception.UserException;
import com.animalshelter.model.User;

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

	public User validateUser() throws UserException, NoSuchAlgorithmException, InvalidKeySpecException {
		return new DatabaseUserDAO().validateUser(getUsername(), getPassword());
		
	}
	
	@Override
	public String toString() {
		return "SessionsService [username=" + username + ", password=" + password + "]";
	}

	
}
