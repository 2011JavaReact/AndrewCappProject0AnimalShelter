package com.animalshelter.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.animalshelter.exception.RoleNotFoundException;
import com.animalshelter.exception.UserNotCreatedException;
import com.animalshelter.exception.UserNotFoundException;
import com.animalshelter.model.User;
import com.animalshelter.service.UsersService;
import com.animalshelter.template.CreateUserTemplate;
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getPathInfo().length() > 1) {

			try {
				User user = new UsersService("userid", request.getPathInfo().split("/")[1]).findUser();
				response.getWriter().append(objectMapper.writeValueAsString(user));
				response.setContentType("application/json");
				response.setStatus(201);
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				response.setStatus(400);

				Logger logger = Logger.getLogger(UserServlet.class);
				logger.debug(e.toString() + " QueryString: " + request.getPathInfo());

			} catch (UserNotFoundException e) {
				// TODO Auto-generated catch block
				response.setStatus(404);

				Logger logger = Logger.getLogger(UserServlet.class);
				logger.info(e.toString() + " URI: " + request.getPathInfo());
			}

		} else {
			Logger logger = Logger.getLogger(UserServlet.class);
			logger.debug("Path info is missing user ID: " + request.getPathInfo());
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
		System.out.println(jsonRequestBody);

		CreateUserTemplate createUserObject = objectMapper.readValue(jsonRequestBody, CreateUserTemplate.class);
		// Continue working on create user and add service role that does a get role or
		// default 'user' role

		try {
			User insertedUser = new UsersService().createNewUser(createUserObject);
		} catch (UserNotCreatedException e) {
			Logger logger = Logger.getLogger(UserServlet.class);
			logger.debug("Error creating user - need to complete message");
			response.setStatus(400);

		} catch (RoleNotFoundException e) {
			Logger logger = Logger.getLogger(UserServlet.class);
			logger.debug("Error creating role - need to complete message");
			response.setStatus(400);
			
		}
	}

}
