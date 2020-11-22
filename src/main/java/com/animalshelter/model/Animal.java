package com.animalshelter.model;


public class Animal {

	private String animalName, species, breed, sex, color, temperament;
	private int animalId, animalAge, weight;
	
	public Animal() {
		super();
	}
	
	public Animal(int animalId, String animalName, String species, String breed, String sex, String color, int animalAge, int weight, String temperament) {
		this.animalId = animalId;
		this.animalName = animalName;
		this.species = species;
		this.breed = breed;
		this.sex = sex;
		this.color = color;
		this.animalAge = animalAge;
		this.weight = weight;
		this.temperament = temperament;
	
	}

	public String getAnimalName() {
		return animalName;
	}

	public void setAnimalName(String animalName) {
		this.animalName = animalName;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTemperament() {
		return temperament;
	}

	public void setTemperament(String temperament) {
		this.temperament = temperament;
	}

	public int getAnimalId() {
		return animalId;
	}

	public void setAnimalId(int animalId) {
		this.animalId = animalId;
	}

	public int getAnimalAge() {
		return animalAge;
	}

	public void setAnimalAge(int animalAge) {
		this.animalAge = animalAge;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "Animal [animalName=" + animalName + ", species=" + species + ", breed=" + breed + ", sex=" + sex
				+ ", color=" + color + ", temperament=" + temperament + ", animalId=" + animalId + ", animalAge="
				+ animalAge + ", weight=" + weight + "]";
	}
	
	
	
	
}
