package com.animalshelter;

import com.animalshelter.dao.DatabaseUserDAO;
import com.animalshelter.exception.RoleNotFoundException;
import com.animalshelter.exception.UserException;
import com.animalshelter.model.Role;
import com.animalshelter.model.User;
import com.animalshelter.service.UsersService;
import com.animalshelter.template.UserTemplate;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestUser {
	private User testUser1, testUser2, testUser3;
	private UserTemplate userTemplate1;

	private DatabaseUserDAO userDAOMock;
	private ArrayList<User> testUsers = new ArrayList<User>();

	private UsersService usersService;

	@Before
	public void setUp() throws Exception {

		userDAOMock = mock(DatabaseUserDAO.class);
		usersService = new UsersService();
		usersService.setUserDAO(userDAOMock);

		Role role1 = new Role(0, "user");
		Role role2 = new Role(1, "admin");

		testUser1 = new User(1, "Amber", "Burns", "amber1", role1);
		testUser2 = new User(2, "Andrew", "Capp", "andrew", role2);
		testUser3 = new User(3, "Jessica", "Z", "jessica", role1);
		userTemplate1 = new UserTemplate(testUser3.getFirstName(), testUser3.getLastName(), testUser3.getUsername(), "1111", testUser3.getRole().getRoleName());
		

		testUsers.add(testUser1);
		testUsers.add(testUser2);
		testUsers.add(testUser3);

	}

	@Test
	public void test1() {
		User testUser1 = new User();
		testUser1.setFirstName("Andrew");
		testUser1.setLastName("Capp");
		Assert.assertEquals("Able to create user and set first name", "Andrew", testUser1.getFirstName());
	}

	@Test
	public void testGetUserById() throws IOException, UserException {

		usersService.setRequestKey("userid");
		usersService.setRequestValue("1");

		when(userDAOMock.findUserById(anyString(), anyInt())).thenReturn(testUser1);

		Assert.assertEquals(usersService.findUser(), testUser1);

	}

	@Test
	public void testGetUserByIdResultKeyException() throws IOException, UserException {
		usersService.setRequestKey("user_id");
		usersService.setRequestValue("1");
		Assert.assertThrows(IOException.class, () -> {
			usersService.findUser();
		});
	}

	@Test
	public void testGetUserByUsername() throws IOException, UserException {

		usersService.setRequestKey("username");
		usersService.setRequestValue("andrew");

		when(userDAOMock.findUserByUsername(anyString(), anyString())).thenReturn(testUser2);

		Assert.assertEquals(usersService.findUser().getUsername(), testUser2.getUsername());
	}

	@Test
	public void testGetAllUsers() throws UserException {

		when(userDAOMock.getAllUsers()).thenReturn(testUsers);

		Assert.assertEquals(usersService.getAllUsers().size(), testUsers.size());
	}

	@Test
	public void testCreateNewUser() throws UserException, RoleNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
		
		when(userDAOMock.createUser(anyInt(), anyString(), anyString(), anyString(), any(byte[].class), anyString())).thenReturn(testUser3);
		
		Assert.assertEquals(usersService.createNewUser(userTemplate1), testUser3);
		
	}

	@Test
	public void testUpdateUser() throws UserException, RoleNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
		when(userDAOMock.updateUser(anyInt(), anyInt(), anyString(), anyString(), any(byte[].class), anyString())).thenReturn(testUser3);
		
		Assert.assertEquals(usersService.updateUser(userTemplate1, 1), testUser3);
	}
}
