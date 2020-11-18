package com.AnimalShelter;

import java.util.ArrayList;

public class User {
	
	static ArrayList<User> users = new ArrayList<User>();
	
	private int id;
	private String firstName, lastName, username, password, role, createdAt, updatedAt;
	
	public User() {
		this.id = 0;
	}
	
	public User(int id, String firstName, String lastName, String username, String password, String role, String createdAt, String updatedAt) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.role = role;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		
		if (id > 0) { users.add(this); };
		
	}
	
	public void setField (String field, String value) {
		switch (field) {
		case "firstName":
			this.firstName = value;
			break;
		case "lastName":
			this.lastName = value;
			break;
		case "username":
			this.username = value;
			break;
		case "password":
			this.password = value;
			break;
		case "role":
			this.role = value;
			break;
		default:
			System.out.println("No property found for: " + field);
		}
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	@Override
	
	public String toString() {
		return "UserId: " + this.id + " " + "Name: " + this.firstName + " " + this.lastName;
	}
	
}
