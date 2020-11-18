package com.AnimalShelter;

public class StartAnimalShelter {

	public static void main(String[] args) {
		System.out.println("Welcome to Animal Shelter Web API");
		
		User user1 = new User();
		user1.setField("firstName", "Andrew");
		user1.setField("lastName", "Capp");
		System.out.println(user1.toString());
		
		
	}

}
