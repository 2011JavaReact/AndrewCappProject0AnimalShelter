package com.animalshelter.model;

public class Role {
	
	private int roleId;
	private String roleName;
	
	public Role() {
		super();
	}
	
	public Role(int roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", roleName=" + roleName + "]";
	}
		
}
