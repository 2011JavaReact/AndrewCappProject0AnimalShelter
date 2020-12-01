package com.animalshelter.dao;

import java.sql.Statement;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;

import com.animalshelter.StartAnimalShelter;
import com.animalshelter.exception.UserException;
import com.animalshelter.model.Role;
import com.animalshelter.model.User;
import com.animalshelter.template.UserTemplate;
import com.animalshelter.util.JDBCUtility;

public class DatabaseUserDAO {

	public ArrayList<User> getAllUsers() throws UserException {

		ArrayList<User> users = new ArrayList();
		String sqlQuery = "SELECT * FROM users u INNER JOIN roles r ON u.role_id = r.role_id";

		try (Connection connection = JDBCUtility.getConnection()) {
			ResultSet rs = connection.createStatement().executeQuery(sqlQuery);

			while (rs.next()) {
				int userId = rs.getInt(1);
				int roleId = rs.getInt(2);
				String firstName = rs.getString(3);
				String lastName = rs.getString(4);
				String username = rs.getString(5);
				String roleName = rs.getString(9);

				users.add(new User(userId, firstName, lastName, username, new Role(roleId, roleName)));

			}

			return users;

		} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseUserDAO.class);
			logger.debug(e.getMessage());
		}

		throw new UserException();

	}

	public User findUserById(String resultKey, int resultValue) throws UserException {

		String sqlQuery = "SELECT * FROM users u INNER JOIN roles r ON u.role_id = r.role_id WHERE " + resultKey
				+ " = ? LIMIT 1";

		try (Connection connection = JDBCUtility.getConnection()) {

			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
			pstmt.setInt(1, resultValue);

			return createUserFromResultSet(pstmt.executeQuery());

		} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseUserDAO.class);
			logger.debug(e.getMessage());
		}

		throw new UserException("User id not found.");
	}

	public User findUserByUsername(String resultKey, String resultValue) throws UserException {

		String sqlQuery = "SELECT * FROM users u INNER JOIN roles r ON u.role_id = r.role_id WHERE " + resultKey
				+ " = ? LIMIT 1";

		try (Connection connection = JDBCUtility.getConnection()) {

			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
			pstmt.setString(1, resultValue);

			return createUserFromResultSet(pstmt.executeQuery());

		} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseUserDAO.class);
			logger.debug(e.getMessage());
		}

		throw new UserException("Username not found.");
	}

	public User findUserByLastName(String resultKey, String resultValue) throws UserException {

		String sqlQuery = "SELECT * FROM users u INNER JOIN roles r ON u.role_id = r.role_id WHERE " + resultKey
				+ " = ? LIMIT 1";

		try (Connection connection = JDBCUtility.getConnection()) {

			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
			pstmt.setString(1, resultValue);

			return createUserFromResultSet(pstmt.executeQuery());

		} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseUserDAO.class);
			logger.debug(e.getMessage());
		}

		throw new UserException("Last name not found.");

	}

	public User createUser(int roleId, String firstName, String lastName, String username, byte[] salt,
			String hashedPassword) throws UserException {
		String sqlQuery = "INSERT INTO users (role_id, first_name, last_name, username, salt, password_hash) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection connection = JDBCUtility.getConnection()) {

			connection.setAutoCommit(false);

			int result;

			PreparedStatement pstmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, roleId);
			pstmt.setString(2, firstName);
			pstmt.setString(3, lastName);
			pstmt.setString(4, username);
			pstmt.setBytes(5, salt);
			pstmt.setString(6, hashedPassword);

			try {
				result = pstmt.executeUpdate();

				if (result != 1) {
					throw new UserException("Insert user failed - no rows were affected");
				}

				int userId = 0;

				ResultSet generatedKeys = pstmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					userId = generatedKeys.getInt(1);
				} else {
					throw new UserException("Insert user failed - no ID was generated");
				}

				connection.commit();

				return findUserById("user_id", userId);
			} catch (PSQLException e) {
				throw new UserException(e.getMessage());
			}
		} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseUserDAO.class);
			logger.debug(e.getMessage());
		}

		throw new UserException();
	}

	public User updateUser(int userId, int roleId, String firstName, String lastName, byte[] salt,
			String hashedPassword) throws UserException {
		String sqlQuery = "UPDATE users SET role_id = ?, first_name = ?, last_name = ?, salt = ?, password_hash = ? WHERE user_id = ?";

		try (Connection connection = JDBCUtility.getConnection()) {

			connection.setAutoCommit(false);

			int result;

			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
			pstmt.setInt(1, roleId);
			pstmt.setString(2, firstName);
			pstmt.setString(3, lastName);
			pstmt.setBytes(4, salt);
			pstmt.setString(5, hashedPassword);
			pstmt.setInt(6, userId);

			result = pstmt.executeUpdate();

			if (result != 1) {
				throw new UserException("Update user failed - no rows were affected");
			}
			connection.commit();

			return findUserById("user_id", userId);

		} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseUserDAO.class);
			logger.debug(e.getMessage());
		}

		throw new UserException();
	}

	public void deleteUser(int userId) throws UserException {
		String sqlQuery = "DELETE FROM users WHERE user_id = ?";

		try (Connection connection = JDBCUtility.getConnection()) {

			connection.setAutoCommit(false);

			int result;

			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);

			pstmt.setInt(1, userId);

			result = pstmt.executeUpdate();

			if (result != 1) {
				throw new UserException("Delete user failed - no rows were affected");
			}
			connection.commit();

		} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseUserDAO.class);
			logger.debug(e.getMessage());
		}

	}

	public User validateUser(String username, String password) throws UserException, NoSuchAlgorithmException, InvalidKeySpecException {
		String sqlQuery = "SELECT user_id, username, salt, password_hash FROM users WHERE username = ? LIMIT 1";
		String hashedPassword;
		byte[] salt;
		int userId;
		try (Connection connection = JDBCUtility.getConnection()) {

			try (PreparedStatement pstmt = connection.prepareStatement(sqlQuery)) {

				pstmt.setString(1, username);
				ResultSet resultSet = pstmt.executeQuery();

				resultSet.next();
				userId = resultSet.getInt("user_id");
				salt = resultSet.getBytes("salt");
				hashedPassword = resultSet.getString("password_hash");
				
				if (hashedPassword.equals(hashingMethod(password, salt))) {
					return findUserById("user_id", userId);
				} else {
					throw new UserException("Password does not match.");
				}
			} catch (UserException e) {
			throw new UserException("Username not found.");
		}

	} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseUserDAO.class);
			logger.debug(e.getMessage());
		}
	
	throw new UserException("User not found.");
	}

	public String hashingMethod(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		// Hash password using SHA + salt
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(salt);
		byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
		String passwordHashString = Hex.encodeHexString(hashedPassword);

		return passwordHashString;
	}

	User createUserFromResultSet(ResultSet rs) throws SQLException, UserException {

		if (rs.next()) {
			int userId = rs.getInt(1);
			int roleId = rs.getInt(2);
			String firstName = rs.getString(3);
			String lastName = rs.getString(4);
			String username = rs.getString(5);
			byte[] salt = rs.getBytes(6);
			String hashedPassword = rs.getString(7);

			String roleName = rs.getString(9);

			return new User(userId, firstName, lastName, username, salt, hashedPassword, new Role(roleId, roleName));
		} else {
			throw new UserException("User not found.");
		}
	}
}
