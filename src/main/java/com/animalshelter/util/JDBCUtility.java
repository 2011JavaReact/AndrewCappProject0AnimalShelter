package com.animalshelter.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.Driver;

public class JDBCUtility {

	public static Connection getConnection() throws SQLException {

		/*
		 * Using local PostgreSQL database
		 */

		// Hard coded the database username and password.
		// In the future need to move these to environment variables

		String url = "jdbc:postgresql://localhost:5432/postgres";
		String username = "postgres";
		String password = "amc111!";

		Connection connection = null;

		DriverManager.registerDriver(new Driver());
		connection = DriverManager.getConnection(url, username, password);

		return connection;

	}
}
