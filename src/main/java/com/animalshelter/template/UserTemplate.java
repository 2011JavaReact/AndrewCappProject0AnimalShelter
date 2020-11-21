package com.animalshelter.template;

public class UserTemplate {

	private String firstName, lastName, username, password, roleName;

	public UserTemplate() {
		super();
	}

	public UserTemplate(String firstName, String lastName, String username, String password, String roleName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.roleName = roleName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRole(String roleName) {
		this.roleName = roleName;
	}
	
	@Override
	public String toString() {
		return "CreateUserTemplate [firstName=" + firstName + ", lastName=" + lastName + ", username=" + username+ ", password=" + password + ", roleName=" + roleName + "]";
	}
		
}
