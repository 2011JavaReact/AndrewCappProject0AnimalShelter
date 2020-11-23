package com.animalshelter;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.animalshelter.dao.DatabaseAnimalDAO;
import com.animalshelter.exception.AnimalException;
import com.animalshelter.model.Animal;
import com.animalshelter.service.AnimalsService;

public class TestAnimal {

	private Animal testAnimal1;
	private Animal testAnimal2;
	private Animal testAnimal3;

//	@Mock
	private DatabaseAnimalDAO animalDAOMock;
	private ArrayList<Animal> testAnimals = new ArrayList<Animal>();

//	@InjectMocks
	private AnimalsService animalsService;

	@Before
	public void setUp() throws Exception {

//		MockitoAnnotations.initMocks(this);
		animalDAOMock = mock(DatabaseAnimalDAO.class);
		animalsService = new AnimalsService();
		animalsService.setDataBaseAnimalDAO(animalDAOMock);

		this.testAnimal1 = new Animal("Test Name", "dog", "Papillon", "male", "yellow", 1, 15, "happy");
		this.testAnimal2 = new Animal("Test Name", "cat", "Snowball", "male", "white", 5, 7, "mean");
		this.testAnimal3 = new Animal("Test Name", "dog", "Pomeranian", "female", "brown", 7, 12, "loving");
		
		testAnimals.add(testAnimal1);
		testAnimals.add(testAnimal2);
		testAnimals.add(testAnimal3);

	}

	@Test
	public void testGetAnimalById() throws AnimalException {

		when(animalDAOMock.getAnimalById(anyInt())).thenReturn(testAnimal1);

		Assert.assertEquals(animalsService.getAnimalById(1), testAnimal1);
	}

	@Test
	public void testGetAnimals() throws AnimalException {
		
		when(animalDAOMock.getAllAnimals()).thenReturn(testAnimals);

		Assert.assertEquals(animalsService.getAllAnimals().size(), testAnimals.size());
	}

	@Test
	public void testGetAnimalsByRequestKey() throws AnimalException {

		when(animalDAOMock.getAnimalsByRequestKey(anyString(), anyString())).thenReturn(testAnimals);

		Assert.assertEquals(animalsService.getAnimalsByRequestKey().size(), testAnimals.size());
	}
	
	@Test
	public void testCreateAnimal() throws AnimalException {
		
	}
	
	@Test
	public void testUpdateAnimal() throws AnimalException {
		
	}
	
	@Test
	public void testDeleteAnimal() throws AnimalException {
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
