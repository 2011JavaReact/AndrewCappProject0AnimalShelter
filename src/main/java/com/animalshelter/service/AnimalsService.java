package com.animalshelter.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.animalshelter.dao.DatabaseAnimalDAO;
import com.animalshelter.exception.AnimalException;
import com.animalshelter.model.Animal;

public class AnimalsService {

	private String requestKey, requestValue;

	public AnimalsService() {
		// TODO Auto-generated constructor stub
	}

	public AnimalsService(String requestKey, String requestValue) {
		this.requestKey = requestKey;
		this.requestValue = requestValue;
	}

	public ArrayList<Animal> getAllAnimals() throws AnimalException {

		return new DatabaseAnimalDAO().getAllAnimals();
	}

	public ArrayList<Animal> getAnimalsByRequestKey() throws AnimalException {

		return new DatabaseAnimalDAO().getAnimalsByRequestKey(this.requestKey, this.requestValue);
			
	}

	public Animal getAnimalById(int animalId) throws AnimalException {
		
		return new DatabaseAnimalDAO().getAnimalById(animalId);
	}
	
	public Animal createNewAnimal(Animal animalToInsert) throws AnimalException {
		
		return new DatabaseAnimalDAO().createAnimal(animalToInsert);
	
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
