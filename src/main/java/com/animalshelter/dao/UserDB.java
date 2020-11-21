package com.animalshelter.dao;

import java.util.ArrayList;

import com.animalshelter.model.User;

public class UserDB {

	ArrayList<User> users = new ArrayList<User>();
	
	public ArrayList CreateUserDB() {
		User user1 = new User(1, "Andrew", "Capp", "acapp");
		
		System.out.println(user1);
		
		User user2 = new User(2, "Jessica", "Zhang", "jessica");
		
		System.out.println(user2.toString());
		users.add(user1);
		users.add(user2);
		
		return users;
	}
	
}
