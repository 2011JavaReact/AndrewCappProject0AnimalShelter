package com.animalshelter.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.animalshelter.dao.DatabaseUserDAO;
import com.animalshelter.exception.DuplicateUsernameException;
import com.animalshelter.exception.RoleNotFoundException;
import com.animalshelter.exception.UserNotCreatedException;
import com.animalshelter.exception.UserNotDeletedException;
import com.animalshelter.exception.UserNotFoundException;
import com.animalshelter.exception.UserNotUpdatedException;
import com.animalshelter.model.Role;
import com.animalshelter.model.User;
import com.animalshelter.template.UserTemplate;

public class UsersService {

	private String requestKey, requestValue;

	public UsersService() {

	}

	public UsersService(String requestKey, String requestValue) {
		this.requestKey = requestKey;
		this.requestValue = requestValue;
	}

	public User findUser() throws IOException, UserNotFoundException {

		switch (this.requestKey) {
		case "userid":
			return new DatabaseUserDAO().findUserById("user_id", Integer.parseInt(requestValue));
		case "username":
			return new DatabaseUserDAO().findUserByUsername("username", requestValue);
		case "lastname":
			return new DatabaseUserDAO().findUserByLastName("last_name", requestValue);
		default:
			throw new IOException("Invalid Search Option");
		}
	}

	public ArrayList<User> getAllUsers() throws UserNotFoundException {
		ArrayList<User> users = new DatabaseUserDAO().getAllUsers();
		return users;
	}

	public User createNewUser(UserTemplate createUserObject) throws UserNotCreatedException, RoleNotFoundException, UserNotFoundException, DuplicateUsernameException {
		System.out.println(createUserObject);
				
		// First get a role object based on the input.  If not admin role name will default to user.
		
		Role roleObject = new RolesService().findRoleByName(createUserObject.getRoleName());
		System.out.println(roleObject);
		
		// Create user based on input and roleObject returned
		
		User createdUserObject = new DatabaseUserDAO().createUser(roleObject.getRoleId(),
				createUserObject.getFirstName(),
				createUserObject.getLastName(),
				createUserObject.getUsername(),
				createUserObject.getPassword());
		
		System.out.println(createdUserObject);
		return createdUserObject;
	}
	
	public User updateUser(UserTemplate updateUserObject, int userId) throws RoleNotFoundException, UserNotFoundException, UserNotUpdatedException {
		
		System.out.println(updateUserObject);
		
		// First get a role object based on the input.  If not admin role name will default to user.
		
		Role roleObject = new RolesService().findRoleByName(updateUserObject.getRoleName());
		System.out.println(roleObject);
		
		// Update user based on input and roleObject returned
		// Note: username cannot be changed
		
		User updatedUserObject = new DatabaseUserDAO().updateUser(userId,
				roleObject.getRoleId(),
				updateUserObject.getFirstName(),
				updateUserObject.getLastName(),
				updateUserObject.getPassword());
		
		System.out.println(updatedUserObject);
		return updatedUserObject;
		
	}
	
	public void deleteUser() throws UserNotDeletedException {
		new DatabaseUserDAO().deleteUser(Integer.parseInt(this.requestValue));
		
	}
}
