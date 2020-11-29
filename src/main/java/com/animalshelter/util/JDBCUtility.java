package com.animalshelter.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.postgresql.Driver;

import com.animalshelter.controller.UserServlet;

public class JDBCUtility {

	public static Connection getConnection() {

		/*
		 * Using local PostgreSQL database
		 */

		// Hard coded the database username and password.
		// In the future need to move these to environment variables

		// AWS RDS database settings
		String url = "jdbc:postgresql://animal-shelter.cgc2dgdtlyfw.us-west-1.rds.amazonaws.com:5432/animalshelter1";
		String username = "postgres";
		String password = "dbAMC111!";
		
		// Local database settings
//		String url = "jdbc:postgresql://localhost:5432/postgres";
//		String username = "postgres";
//		String password = "amc111!";
		Driver postgresqlDriver = new Driver();
		Connection connection = null;

		try {
			DriverManager.registerDriver(postgresqlDriver);
			connection = DriverManager.getConnection(url, username, password);

		} catch (SQLException e) {
			Logger logger = Logger.getLogger(UserServlet.class);
			logger.debug("Error registering database driver: " + e.getMessage());
		} finally {
			try {
				DriverManager.deregisterDriver(postgresqlDriver);
			} catch (SQLException e) {
				Logger logger = Logger.getLogger(UserServlet.class);
				logger.debug("Error UN-registering database driver: " + e.getMessage());
			}
		}

		return connection;
	}
}
