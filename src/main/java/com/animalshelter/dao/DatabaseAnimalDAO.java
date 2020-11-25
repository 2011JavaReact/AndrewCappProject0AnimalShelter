package com.animalshelter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;
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

		try (Connection connection = JDBCUtility.getConnection()) {
			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);

			pstmt.setString(1, requestValue.toLowerCase());

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

	public Animal createAnimal(Animal animalToInsert) throws AnimalException {

		String sqlQuery = "INSERT INTO animals (animal_name, species, breed, sex, color, animal_age, weight, temperament) VALUES (?,?,?,?,?,?,?,?)";

		try (Connection connection = JDBCUtility.getConnection()) {

			connection.setAutoCommit(false);

			int result;

			PreparedStatement pstmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, animalToInsert.getAnimalName());
			pstmt.setString(2, animalToInsert.getSpecies());
			pstmt.setString(3, animalToInsert.getBreed());
			pstmt.setString(4, animalToInsert.getSex());
			pstmt.setString(5, animalToInsert.getColor());
			pstmt.setInt(6, animalToInsert.getAnimalAge());
			pstmt.setInt(7, animalToInsert.getWeight());
			pstmt.setString(8, animalToInsert.getTemperament());

			result = pstmt.executeUpdate();

			if (result != 1) {
				throw new AnimalException("Insert animal failed - no rows were affected.");
			}

			int animalId = 0;

			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				animalId = generatedKeys.getInt(1);
			} else {
				throw new AnimalException("Insert animal failed - no ID was generated.");
			}

			connection.commit();

			return getAnimalById(animalId);

		} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseAnimalDAO.class);
			logger.debug(e.getMessage());
		}

		throw new AnimalException("Error inserting animal.");

	}

	public Animal updateAnimal(Animal animalToUpdate, int animalId) throws AnimalException {

		String sqlQuery = "UPDATE animals SET animal_name = ?, species = ?, breed = ?, sex = ?, color = ?, animal_age = ?, weight = ?, temperament = ? WHERE animal_id = ?";

		try (Connection connection = JDBCUtility.getConnection()) {

			connection.setAutoCommit(false);

			int result;

			PreparedStatement pstmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, animalToUpdate.getAnimalName());
			pstmt.setString(2, animalToUpdate.getSpecies());
			pstmt.setString(3, animalToUpdate.getBreed());
			pstmt.setString(4, animalToUpdate.getSex());
			pstmt.setString(5, animalToUpdate.getColor());
			pstmt.setInt(6, animalToUpdate.getAnimalAge());
			pstmt.setInt(7, animalToUpdate.getWeight());
			pstmt.setString(8, animalToUpdate.getTemperament());
			pstmt.setInt(9, animalId);

			result = pstmt.executeUpdate();

			if (result != 1) {
				throw new AnimalException("Update animal failed - no rows were affected.");
			}

			connection.commit();

			return getAnimalById(animalId);

		} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseAnimalDAO.class);
			logger.debug(e.getMessage());
		}

		throw new AnimalException("Error updating animal.");

	}

	// Delete animal based on animal_id

	public void deleteAnimal(int animalId) throws AnimalException {

		String sqlQuery = "DELETE FROM animals WHERE animal_id = ?";

		try (Connection connection = JDBCUtility.getConnection()) {

			connection.setAutoCommit(false);

			int result;

			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);

			pstmt.setInt(1, animalId);

			result = pstmt.executeUpdate();

			if (result != 1) {
				throw new AnimalException("Delete animal failed - animal ID not found.");
			}
			connection.commit();

		} catch (SQLException e) {
			Logger logger = Logger.getLogger(DatabaseAnimalDAO.class);
			logger.debug(e.getMessage());
		}
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