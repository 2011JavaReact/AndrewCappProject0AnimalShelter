package com.animalshelter;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.animalshelter.dao.DatabaseUserDAO;
import com.animalshelter.exception.UserNotFoundException;
import com.animalshelter.model.User;
import com.animalshelter.util.JDBCUtility;

public class StartAnimalShelter {

	public static void main(String[] args) throws UserNotFoundException {
		System.out.println("Welcome to Animal Shelter Web API");
		
		User user1 = new User();
		user1.setFirstName("Andrew");
		user1.setLastName("Capp");
		System.out.println(user1.toString());
		
		try (Connection connection = JDBCUtility.getConnection()) {
			System.out.println("Connection to DB made!");
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		
		System.out.println(new DatabaseUserDAO().getAllUsers());
		// Logger is currently saving logs to Animal Shelter project when run as a Java app
		// Logger is saving logs to /Applications/SpringToolSuite4.app/Contents/MacOS when server is running
		Logger logger = Logger.getLogger(StartAnimalShelter.class);
		logger.debug("Animal ShelterStarted " + StartAnimalShelter.class);
		
		System.out.println(System.getenv("HOME"));
		System.out.println(System.getProperty("user.dir"));
		
	}

}
