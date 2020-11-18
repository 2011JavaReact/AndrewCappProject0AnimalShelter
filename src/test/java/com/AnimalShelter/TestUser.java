package com.AnimalShelter;

import org.junit.Assert.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class TestUser {
	
	
				
	
	@Test
	public static void test1() {
		User testUser1 = new User();
		testUser1.setField("firstName", "Andrew");
		testUser1.setField("lastName", "Capp");
		assertEquals("Able to create user and set first name", "Andrew", testUser1.getFirstName());
	}

}
