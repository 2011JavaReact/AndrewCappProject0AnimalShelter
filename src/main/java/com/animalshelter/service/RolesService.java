package com.animalshelter.service;

import com.animalshelter.dao.DatabaseRoleDAO;
import com.animalshelter.exception.RoleNotFoundException;
import com.animalshelter.model.Role;

public class RolesService {
	private int roleId;
	private String roleName;
	
	public RolesService() {
		// TODO Auto-generated constructor stub
	}
	
	public RolesService(int roleId) {
		this.roleId = roleId;
	}
	
	public RolesService(String roleName) {
		this.roleName = roleName;
	}

	// findRoleById not currently implemented
	
	public Role findRoleById(int roleId) {
		return null;
	}
	
	public Role findRoleByName(String roleName) throws RoleNotFoundException {
		String sanitizedRoleName;
		
		if (roleName.toLowerCase().equals("admin")) { sanitizedRoleName = "Admin"; }
		else { sanitizedRoleName = "User"; }
		
		return new DatabaseRoleDAO().findRoleByName(sanitizedRoleName);
	}
	
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "RolesService [roleId=" + roleId + ", roleName=" + roleName + "]";
	}

}
