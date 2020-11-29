package com.animalshelter.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;

import com.animalshelter.exception.RoleNotFoundException;
import com.animalshelter.exception.UserException;
import com.animalshelter.model.User;
import com.animalshelter.service.UsersService;
import com.animalshelter.template.UserTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet(urlPatterns = { "/user", "/user/*" })
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	public UserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	ObjectMapper objectMapper = new ObjectMapper();

	// Get a User by user ID (use uri /users to search for user or return all users)

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User returnedUser;

		if (request.getPathInfo().length() > 1) {
			try {
				returnedUser = getUserById(request, response);
				response.getWriter().append(objectMapper.writeValueAsString(returnedUser));
				response.setContentType("application/json");
				response.setStatus(200);

			} catch (IOException e) {
				response.setStatus(400);

				Logger logger = Logger.getLogger(UserServlet.class);
				logger.debug(e.toString() + " QueryString: " + request.getPathInfo());

			} catch (UserException e) {
				response.setStatus(404);

				Logger logger = Logger.getLogger(UserServlet.class);
				logger.debug(e.toString() + " URI: " + request.getPathInfo());
			}

		} else {
			Logger logger = Logger.getLogger(UserServlet.class);
			logger.info("Path info is missing user ID: " + request.getPathInfo());
			response.setStatus(400);
		}

	}

	// Create a User and Persist to Database

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		BufferedReader requestBody = request.getReader();

		StringBuilder requestBodyString = new StringBuilder();
		String requestBodyLine;
		String jsonRequestBody;

		while ((requestBodyLine = requestBody.readLine()) != null) {
			requestBodyString.append(requestBodyLine);
		}

		jsonRequestBody = requestBodyString.toString();

		UserTemplate createUserObject = objectMapper.readValue(jsonRequestBody, UserTemplate.class);

		try {

			User insertedUser = new UsersService().createNewUser(createUserObject);

			response.getWriter().append(objectMapper.writeValueAsString(insertedUser));
			response.setContentType("application/json");
			response.setStatus(201);

			Logger logger = Logger.getLogger(UserServlet.class);
			logger.info("Inserted User in table users - ID: " + insertedUser.getUserId() + " Name: "
					+ insertedUser.getFirstName() + " " + insertedUser.getLastName());

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			Logger logger = Logger.getLogger(UserServlet.class);
			logger.debug(e.getMessage());
		} catch (UserException e) {
			Logger logger = Logger.getLogger(UserServlet.class);
			logger.debug(e.getMessage());
			response.setStatus(400);

		} catch (RoleNotFoundException e) {
			Logger logger = Logger.getLogger(UserServlet.class);
			logger.debug("Error creating role: " + e.getMessage());
			response.setStatus(400);

		}
	}

	// Update User - does not allow changing username

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User returnedUser;

		if (request.getPathInfo().length() > 1) {
			try {
				returnedUser = getUserById(request, response);

				BufferedReader requestBody = request.getReader();

				StringBuilder requestBodyString = new StringBuilder();
				String requestBodyLine;
				String jsonRequestBody;

				while ((requestBodyLine = requestBody.readLine()) != null) {
					requestBodyString.append(requestBodyLine);
				}

				jsonRequestBody = requestBodyString.toString();

				UserTemplate updateUserObject = objectMapper.readValue(jsonRequestBody, UserTemplate.class);

				try {
					User updatedUser = new UsersService().updateUser(updateUserObject, returnedUser.getUserId());
					response.getWriter().append(objectMapper.writeValueAsString(updatedUser));
					response.setContentType("application/json");
					response.setStatus(201);
					Logger logger = Logger.getLogger(UserServlet.class);
					logger.info("Updated User in table users - ID: " + updatedUser.getUserId() + " Name: "
							+ updatedUser.getFirstName() + " " + updatedUser.getLastName());

				} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
					Logger logger = Logger.getLogger(UserServlet.class);
					logger.debug(e.getMessage());

				} catch (RoleNotFoundException e) {
					Logger logger = Logger.getLogger(UserServlet.class);
					logger.debug("Error creating role: " + e.getMessage());
					response.setStatus(400);

				} catch (UserException e) {
					Logger logger = Logger.getLogger(UserServlet.class);
					logger.debug(e.getMessage());
				}
			} catch (IOException e) {
				response.setStatus(400);
				Logger logger = Logger.getLogger(UserServlet.class);
				logger.debug(e.toString() + " QueryString: " + request.getPathInfo());

			} catch (UserException e) {
				response.setStatus(404);
				Logger logger = Logger.getLogger(UserServlet.class);
				logger.debug(e.toString() + " URI: " + request.getPathInfo());
			}

		} else {
			Logger logger = Logger.getLogger(UserServlet.class);
			logger.info("Path info is missing user ID: " + request.getPathInfo());
			response.setStatus(400);
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getPathInfo().length() > 1) {
			try {
				new UsersService("userid", request.getPathInfo().split("/")[1]).deleteUser();
				response.setStatus(200);

				Logger logger = Logger.getLogger(UserServlet.class);
				logger.info("Deleted User in table users - ID: " + request.getPathInfo().split("/")[1]);

			} catch (UserException e) {
				response.setStatus(404);
				Logger logger = Logger.getLogger(UserServlet.class);
				logger.debug(e.toString() + " URI: " + request.getPathInfo());
			}

		} else {
			Logger logger = Logger.getLogger(UserServlet.class);
			logger.info("Path info is missing user ID: " + request.getPathInfo());
			response.setStatus(400);
		}

	}

	// Utility method to return an instance of a User based on a user ID

	public User getUserById(HttpServletRequest request, HttpServletResponse response)
			throws IOException, UserException {

		return new UsersService("userid", request.getPathInfo().split("/")[1]).findUser();
	}
}
