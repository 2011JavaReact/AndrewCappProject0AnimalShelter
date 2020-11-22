package com.animalshelter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;
import com.animalshelter.exception.AnimalException;
import com.animalshelter.model.Animal;
import com.animalshelter.util.JDBCUtility;

public class DatabaseAnimalDAO {

	public ArrayList<Animal> getAllAnimals() throws AnimalException {

		String sqlQuery = "SELECT * FROM animals";

		try (Connection connection = JDBCUtility.getConnection()) {
			ResultSet rs = connection.createStatement().executeQuery(sqlQuery);

			return createAnimalArrayList(rs);

		} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseAnimalDAO.class);
			logger.debug(e.getMessage());
		}

		throw new AnimalException("Unable to retreive animals from database.");

	}

	public ArrayList<Animal> getAnimalsByRequestKey(String requestKey, String requestValue) throws AnimalException {

		String sanitizedRequestKey;

		switch (requestKey) {
		case "species":
			sanitizedRequestKey = "species";
			break;
		case "breed":
			sanitizedRequestKey = "breed";
			break;
		case "sex":
			sanitizedRequestKey = "sex";
			break;
		default:
			throw new AnimalException("Error - only search for species, breed, and sex allowed.");
		}

		String sqlQuery = "SELECT * FROM animals WHERE LOWER(" + sanitizedRequestKey + ") = ?";

		System.out.println(sqlQuery);

		try (Connection connection = JDBCUtility.getConnection()) {
			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);

			pstmt.setString(1, requestValue.toLowerCase());

			System.out.println(pstmt);

			ResultSet rs = pstmt.executeQuery();

			ArrayList<Animal> animalsArrayList = createAnimalArrayList(rs);

			if (animalsArrayList.size() > 0) {
				return animalsArrayList;
			} else {
				throw new AnimalException("Query did not return any animals.");
			}

		} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseAnimalDAO.class);
			logger.debug(e.getMessage());
		}

		throw new AnimalException("Unable to retreive animals from database.");

	}

	public Animal getAnimalById(int animalId) throws AnimalException {
		String sqlQuery = "SELECT * FROM animals WHERE animal_id = ? LIMIT 1";

		try (Connection connection = JDBCUtility.getConnection()) {
			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);

			pstmt.setInt(1, animalId);

			ResultSet rs = pstmt.executeQuery();

			ArrayList<Animal> animalsArrayList = createAnimalArrayList(rs);

			if (animalsArrayList.size() == 1) {
				return animalsArrayList.get(0);
			} else {
				throw new AnimalException("Query did not return any animals.");
			}

		} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseAnimalDAO.class);
			logger.debug(e.getMessage());
		}

		throw new AnimalException("Unable to retreive animals from database.");

		
	}
	
	// Utility method to create ArrayList of Animal objects from the result set
	
	public ArrayList<Animal> createAnimalArrayList(ResultSet rs) throws SQLException {
		ArrayList<Animal> animals = new ArrayList<Animal>();

		while (rs.next()) {
			int animalId = rs.getInt(1);
			String animalName = rs.getString(2);
			String species = rs.getString(3);
			String breed = rs.getString(4);
			String sex = rs.getString(5);
			String color = rs.getString(6);
			int animalAge = rs.getInt(7);
			int weight = rs.getInt(8);
			String temperament = rs.getString(9);

			animals.add(new Animal(animalId, animalName, species, breed, sex, color, animalAge, weight, temperament));

		}

		return animals;
	}
}

//	
//	public User findUserById(String resultKey, int resultValue) throws UserNotFoundException {
//
//		String sqlQuery = "SELECT * FROM users u INNER JOIN roles r ON u.role_id = r.role_id WHERE " + resultKey
//				+ " = ? LIMIT 1";
//
//		try (Connection connection = JDBCUtility.getConnection()) {
//
//			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
//			pstmt.setInt(1, resultValue);
//			System.out.println(pstmt);
//
//			return createUserFromResultSet(pstmt.executeQuery());
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		throw new UserNotFoundException("User id not found.");
//	}
//
//	public User findUserByUsername(String resultKey, String resultValue) throws UserNotFoundException {
//
//		String sqlQuery = "SELECT * FROM users u INNER JOIN roles r ON u.role_id = r.role_id WHERE " + resultKey
//				+ " = ? LIMIT 1";
//
//		try (Connection connection = JDBCUtility.getConnection()) {
//
//			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
//			pstmt.setString(1, resultValue);
//			System.out.println(pstmt);
//
//			return createUserFromResultSet(pstmt.executeQuery());
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		throw new UserNotFoundException("Username not found.");
//	}
//
//	public User findUserByLastName(String resultKey, String resultValue) throws UserNotFoundException {
//
//		String sqlQuery = "SELECT * FROM users u INNER JOIN roles r ON u.role_id = r.role_id WHERE " + resultKey
//				+ " = ? LIMIT 1";
//
//		try (Connection connection = JDBCUtility.getConnection()) {
//
//			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
//			pstmt.setString(1, resultValue);
//			System.out.println(pstmt);
//
//			return createUserFromResultSet(pstmt.executeQuery());
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		throw new UserNotFoundException("Last name not found.");
//
//	}
//
//	public User createUser(int roleId, String firstName, String lastName, String username, String password)
//			throws UserNotFoundException, UserNotCreatedException, DuplicateUsernameException {
//		String sqlQuery = "INSERT INTO users (role_id, first_name, last_name, username, password_hash) "
//				+ "VALUES (?, ?, ?, ?, ?)";
//
//		try (Connection connection = JDBCUtility.getConnection()) {
//
//			connection.setAutoCommit(false);
//
//			int result;
//
//			PreparedStatement pstmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
//			pstmt.setInt(1, roleId);
//			pstmt.setString(2, firstName);
//			pstmt.setString(3, lastName);
//			pstmt.setString(4, username);
//			pstmt.setString(5, password);
//
//			System.out.println(pstmt);
//
//			try {
//				result = pstmt.executeUpdate();
//
//				if (result != 1) {
//					throw new UserNotCreatedException("Insert user failed - no rows were affected");
//				}
//
//				int userId = 0;
//
//				ResultSet generatedKeys = pstmt.getGeneratedKeys();
//				if (generatedKeys.next()) {
//					userId = generatedKeys.getInt(1);
//				} else {
//					throw new UserNotCreatedException("Insert user failed - no ID was generated");
//				}
//
//				connection.commit();
//
//				return findUserById("user_id", userId);
//			} catch (PSQLException e) {
//				// System.out.println("PSQLException code: " + e.getMessage().split(":")[1]);
//				throw new DuplicateUsernameException(e.getMessage());
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		throw new UserNotCreatedException();
//	}
//
//	public User updateUser(int userId, int roleId, String firstName, String lastName, String password)
//			throws UserNotFoundException, UserNotUpdatedException {
//		String sqlQuery = "UPDATE users SET role_id = ?, first_name = ?, last_name = ?, password_hash = ? WHERE user_id = ?";
//
//		try (Connection connection = JDBCUtility.getConnection()) {
//
//			connection.setAutoCommit(false);
//
//			int result;
//
//			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
//			pstmt.setInt(1, roleId);
//			pstmt.setString(2, firstName);
//			pstmt.setString(3, lastName);
//			pstmt.setString(4, password);
//			pstmt.setInt(5, userId);
//
//			System.out.println(pstmt);
//
//			result = pstmt.executeUpdate();
//
//			if (result != 1) {
//				throw new UserNotUpdatedException("Update user failed - no rows were affected");
//			}
//			connection.commit();
//
//			return findUserById("user_id", userId);
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		throw new UserNotUpdatedException();
//	}
//
//	public void deleteUser(int userId) throws UserNotDeletedException {
//		String sqlQuery = "DELETE FROM users WHERE user_id = ?";
//
//		try (Connection connection = JDBCUtility.getConnection()) {
//
//			connection.setAutoCommit(false);
//
//			int result;
//
//			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
//
//			pstmt.setInt(1, userId);
//
//			System.out.println(pstmt);
//
//			result = pstmt.executeUpdate();
//
//			if (result != 1) {
//				throw new UserNotDeletedException("Delete user failed - no rows were affected");
//			}
//			connection.commit();
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public Boolean validUsername(String username, String password) {
//		String sqlQuery = "SELECT * FROM users WHERE username = ? and password_hash = ? LIMIT 1";
//
//		try (Connection connection = JDBCUtility.getConnection()) {
//
//			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
//			pstmt.setString(1, username);
//			pstmt.setString(2, password);
//
//			System.out.println(pstmt);
//
//			ResultSet resultSet = pstmt.executeQuery();
//
//			if (resultSet.next()) {
//				return true;
//			} else {
//				return false;
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		// need to fix this line
//
//		System.out.println("validUsername why is it hitting this line?");
//		return false;
//
//	}
//
//	User createUserFromResultSet(ResultSet rs) throws SQLException, UserNotFoundException {
//
//		if (rs.next()) {
//			System.out.println("rs getString result: " + rs.getString(3));
//
//			int userId = rs.getInt(1);
//			int roleId = rs.getInt(2);
//			String firstName = rs.getString(3);
//			String lastName = rs.getString(4);
//			String username = rs.getString(5);
//			String roleName = rs.getString(8);
//
//			return new User(userId, firstName, lastName, username, new Role(roleId, roleName));
//		} else {
//			throw new UserNotFoundException("User not found.");
//		}
//	}
//}
