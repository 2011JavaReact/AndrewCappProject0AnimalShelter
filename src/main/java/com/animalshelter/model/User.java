package com.animalshelter.model;

import java.util.ArrayList;

public class User {
	
	private int userId;
	private String firstName, lastName, username, password;
	private Role role;
	
	public User() {

	}
	
	public User(int userId, String firstName, String lastName, String username) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
	}
	
	public User(int userId, String firstName, String lastName, String username, Role role) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.role = role;
								
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", username="
				+ username + ", password=" + password + ", role=" + role + "]";
	}
		
}
