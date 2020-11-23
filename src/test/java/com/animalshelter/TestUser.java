package com.animalshelter;



import com.animalshelter.model.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestUser {
	
	
				
	
	@Test
	public void test1() {
		User testUser1 = new User();
		testUser1.setFirstName("Andrew");
		testUser1.setLastName("Capp");
		Assert.assertEquals("Able to create user and set first name", "Andrew", testUser1.getFirstName());
	}

}
