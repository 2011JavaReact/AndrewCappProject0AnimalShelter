package com.animalshelter.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.animalshelter.dao.DatabaseRoleDAO;
import com.animalshelter.dao.DatabaseUserDAO;
import com.animalshelter.exception.RoleNotFoundException;
import com.animalshelter.exception.UserException;
import com.animalshelter.model.Role;
import com.animalshelter.model.User;
import com.animalshelter.template.UserTemplate;

public class UsersService {

	private String requestKey, requestValue;
	private DatabaseUserDAO userDAO;
	private DatabaseRoleDAO roleDAO;

	public UsersService() {
		this.userDAO = new DatabaseUserDAO();
		this.roleDAO = new DatabaseRoleDAO();
	}

	public UsersService(DatabaseUserDAO userDAO) {
		this.userDAO = new DatabaseUserDAO();
	}

	public UsersService(String requestKey, String requestValue) {
		this.userDAO = new DatabaseUserDAO();
		this.roleDAO = new DatabaseRoleDAO();
		this.requestKey = requestKey;
		this.requestValue = requestValue;
	}

	// Combined find user by userid, username, and lastname into one method below.
	// Note that 'search by argument' is hard coded due to a different format needed
	// for the database

	public User findUser() throws IOException, UserException {

		switch (this.requestKey) {
		case "userid":
			return userDAO.findUserById("user_id", Integer.parseInt(requestValue));
		case "username":
			return userDAO.findUserByUsername("username", requestValue);
		case "lastname":
			return userDAO.findUserByLastName("last_name", requestValue);
		default:
			throw new IOException("Invalid Search Option");
		}
	}

	public ArrayList<User> getAllUsers() throws UserException {
		ArrayList<User> users = userDAO.getAllUsers();
		return users;
	}

	public User createNewUser(UserTemplate createUserObject)
			throws UserException, RoleNotFoundException, UserException {

		// First get a role object based on the input. If not admin role name will
		// default to user.

		Role roleObject = new RolesService().findRoleByName(createUserObject.getRoleName());

		// Create user based on input and roleObject returned

		User createdUserObject = userDAO.createUser(roleObject.getRoleId(), createUserObject.getFirstName(),
				createUserObject.getLastName(), createUserObject.getUsername(), createUserObject.getPassword());

		return createdUserObject;
	}

	public User updateUser(UserTemplate updateUserObject, int userId)
			throws RoleNotFoundException, UserException {

		// First get a role object based on the input. If not admin role name will
		// default to user.

		Role roleObject = new RolesService().findRoleByName(updateUserObject.getRoleName());

		// Update user based on input and roleObject returned
		// Note: username cannot be changed

		User updatedUserObject = userDAO.updateUser(userId, roleObject.getRoleId(), updateUserObject.getFirstName(),
				updateUserObject.getLastName(), updateUserObject.getPassword());

		return updatedUserObject;

	}

	public void deleteUser() throws UserException {
		userDAO.deleteUser(Integer.parseInt(this.requestValue));

	}

	public DatabaseUserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(DatabaseUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public String getRequestKey() {
		return requestKey;
	}

	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
	}

	public String getRequestValue() {
		return requestValue;
	}

	public void setRequestValue(String requestValue) {
		this.requestValue = requestValue;
	}
}
