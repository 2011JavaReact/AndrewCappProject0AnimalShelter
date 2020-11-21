package com.animalshelter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.animalshelter.exception.RoleNotFoundException;
import com.animalshelter.model.Role;
import com.animalshelter.util.JDBCUtility;

public class DatabaseRoleDAO {

	public DatabaseRoleDAO() {
		// TODO Auto-generated constructor stub
	}

	public Role findRoleById(int roleId) throws RoleNotFoundException {
		String sqlQuery = "SELECT * FROM roles WHERE role_id = ? LIMIT 1";
		
		try (Connection connection = JDBCUtility.getConnection()) {
			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
			pstmt.setInt(1, roleId);
			
			return createRoleFromResultSet(pstmt.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		throw new RoleNotFoundException("Role id not found.");
		
	}
	
	public Role findRoleByName(String roleName) throws RoleNotFoundException {
		String sqlQuery = "SELECT * FROM roles WHERE role_name = ? LIMIT 1";
		
		try (Connection connection = JDBCUtility.getConnection()) {
			PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
			pstmt.setString(1, roleName);
			
			return createRoleFromResultSet(pstmt.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		throw new RoleNotFoundException("Role name not found.");
		
	}
	
	
	Role createRoleFromResultSet(ResultSet rs) throws SQLException, RoleNotFoundException {

		if (rs.next()) {
			System.out.println("rs getString result: " + rs.getString(2));

			int roleId = rs.getInt(1);
			String roleName = rs.getString(2);

			return new Role(roleId, roleName);
		} else {
			throw new RoleNotFoundException("Role not found.");
		}
	}
}
