package com.animalshelter.service;

import java.util.ArrayList;
import com.animalshelter.dao.DatabaseAnimalDAO;
import com.animalshelter.exception.AnimalException;
import com.animalshelter.model.Animal;

public class AnimalsService {

	private String requestKey, requestValue;
	
	private DatabaseAnimalDAO animalDAO;
	
	public AnimalsService() {
		this.animalDAO = new DatabaseAnimalDAO();
	}

	public AnimalsService(DatabaseAnimalDAO animalDAO) {
		this.animalDAO = new DatabaseAnimalDAO();
	}
	
	public AnimalsService(String requestKey, String requestValue) {
		this.requestKey = requestKey;
		this.requestValue = requestValue;
		this.animalDAO = new DatabaseAnimalDAO();
	}
	
	public ArrayList<Animal> getAllAnimals() throws AnimalException {

		return animalDAO.getAllAnimals();
	}

	public ArrayList<Animal> getAnimalsByRequestKey() throws AnimalException {

		return animalDAO.getAnimalsByRequestKey(this.requestKey, this.requestValue);
			
	}

	public Animal getAnimalById(int animalId) throws AnimalException {
		
		return animalDAO.getAnimalById(animalId);
	}
	
	public Animal createNewAnimal(Animal animalToInsert) throws AnimalException {
		
		return animalDAO.createAnimal(animalToInsert);
	}
	
	public Animal updateAnimal(Animal animalToUpdate, int animalId) throws AnimalException {
		
		return animalDAO.updateAnimal(animalToUpdate, animalId);
	}
	
	public void deleteAnimal(int animalId) throws AnimalException {
		
		animalDAO.deleteAnimal(animalId);
	}
	
	public void setDataBaseAnimalDAO(DatabaseAnimalDAO animalDAO) {
		this.animalDAO = animalDAO;
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

	@Override
	public String toString() {
		return "AnimalsService [requestKey=" + requestKey + ", requestValue=" + requestValue + "]";
	}

}
