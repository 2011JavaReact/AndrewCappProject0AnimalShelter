package com.animalshelter.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

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

	public User createNewUser(UserTemplate createUserObject) throws UserException, RoleNotFoundException, UserException,
			NoSuchAlgorithmException, InvalidKeySpecException {

		// First get a role object based on the input. If not admin role name will
		// default to user.

		Role roleObject = new RolesService().findRoleByName(createUserObject.getRoleName());

		// Generate random salt for password
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);

		// Hash password using PBKDF2 + salt
		KeySpec spec = new PBEKeySpec(createUserObject.getPassword().toCharArray(), salt, 131072, 128);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hashedPassword = factory.generateSecret(spec).getEncoded();

		// Create user based on input and roleObject returned

		User createdUserObject = userDAO.createUser(roleObject.getRoleId(), createUserObject.getFirstName(),
				createUserObject.getLastName(), createUserObject.getUsername(), salt, hashedPassword);

		return createdUserObject;
	}

	public User updateUser(UserTemplate updateUserObject, int userId) throws RoleNotFoundException, UserException, NoSuchAlgorithmException, InvalidKeySpecException {

		// First get a role object based on the input. If not admin role name will
		// default to user.

		Role roleObject = new RolesService().findRoleByName(updateUserObject.getRoleName());

		// Generate random salt for new password
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);

		// Hash password using PBKDF2 + salt
		KeySpec spec = new PBEKeySpec(updateUserObject.getPassword().toCharArray(), salt, 131072, 128);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hashedPassword = factory.generateSecret(spec).getEncoded();

		// Update user based on input and roleObject returned
		// Note: username cannot be changed

		User updatedUserObject = userDAO.updateUser(userId, roleObject.getRoleId(), updateUserObject.getFirstName(),
				updateUserObject.getLastName(), salt, hashedPassword);

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
