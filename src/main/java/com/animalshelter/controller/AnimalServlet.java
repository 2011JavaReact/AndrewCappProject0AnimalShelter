package com.animalshelter.controller;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.animalshelter.exception.AnimalException;
import com.animalshelter.exception.DuplicateUsernameException;
import com.animalshelter.exception.RoleNotFoundException;
import com.animalshelter.exception.UserNotCreatedException;
import com.animalshelter.exception.UserNotDeletedException;
import com.animalshelter.exception.UserNotFoundException;
import com.animalshelter.exception.UserNotUpdatedException;
import com.animalshelter.model.Animal;
import com.animalshelter.model.User;
import com.animalshelter.service.AnimalsService;
import com.animalshelter.service.UsersService;
import com.animalshelter.template.UserTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class AnimalServlet
 */
// Using web.xml file for routing

public class AnimalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AnimalServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	ObjectMapper objectMapper = new ObjectMapper();

	// Get an Animal by ID (use uri /animals to search by species, breed or sex)

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getPathInfo().length() > 1) {
			try {
				Animal returnedAnimal = new AnimalsService()
						.getAnimalById(Integer.parseInt(request.getPathInfo().split("/")[1]));
				response.getWriter().append(objectMapper.writeValueAsString(returnedAnimal));
				response.setContentType("application/json");
				response.setStatus(200);

			} catch (AnimalException e) {
				response.setStatus(404);
				Logger logger = Logger.getLogger(AnimalServlet.class);
				logger.info(e.toString() + " URI: " + request.getPathInfo());
			}

		} else {
			Logger logger = Logger.getLogger(AnimalServlet.class);
			logger.debug("Path info is missing animal ID: " + request.getPathInfo());
			response.setStatus(400);
		}

	}

	// Create an Animal and Persist to Database

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

		Animal animalToInsert = objectMapper.readValue(jsonRequestBody, Animal.class);

		try {
			Animal insertedAnimal = new AnimalsService().createNewAnimal(animalToInsert);
			response.getWriter().append(objectMapper.writeValueAsString(insertedAnimal));
			response.setContentType("application/json");
			response.setStatus(201);

		} catch (AnimalException e) {
			Logger logger = Logger.getLogger(AnimalServlet.class);
			logger.debug("Error creating animal. " + e.getMessage());
			response.setStatus(400);
		} 
	}
//
//	// Update User - does not allow changing username
//	
//	protected void doPut(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		User returnedUser;
//
//		if (request.getPathInfo().length() > 1) {
//			try {
//				returnedUser = getUserById(request, response);
//				
//				BufferedReader requestBody = request.getReader();
//
//				StringBuilder requestBodyString = new StringBuilder();
//				String requestBodyLine;
//				String jsonRequestBody;
//
//				while ((requestBodyLine = requestBody.readLine()) != null) {
//					requestBodyString.append(requestBodyLine);
//				}
//
//				jsonRequestBody = requestBodyString.toString();
//				System.out.println(jsonRequestBody);
//
//				UserTemplate updateUserObject = objectMapper.readValue(jsonRequestBody, UserTemplate.class);
//				
//
//				try {
//					User updatedUser = new UsersService().updateUser(updateUserObject, returnedUser.getUserId());
//					response.getWriter().append(objectMapper.writeValueAsString(updatedUser));
//					response.setContentType("application/json");
//					response.setStatus(201);
//
//				} catch (RoleNotFoundException e) {
//					Logger logger = Logger.getLogger(UserServlet.class);
//					logger.debug("Error creating role - need to complete message");
//					response.setStatus(400);
//
//				} catch (UserNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (UserNotUpdatedException e) {
//					Logger logger = Logger.getLogger(UserServlet.class);
//					logger.debug(e.getMessage());
//					response.setStatus(400);
//				}
//			} catch (IOException e) {
//				response.setStatus(400);
//				Logger logger = Logger.getLogger(UserServlet.class);
//				logger.debug(e.toString() + " QueryString: " + request.getPathInfo());
//
//			} catch (UserNotFoundException e) {
//				response.setStatus(404);
//				Logger logger = Logger.getLogger(UserServlet.class);
//				logger.info(e.toString() + " URI: " + request.getPathInfo());
//			}
//
//			
//		} else {
//			Logger logger = Logger.getLogger(UserServlet.class);
//			logger.debug("Path info is missing user ID: " + request.getPathInfo());
//			response.setStatus(400);
//		}
//	}
//
//	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		
//		if (request.getPathInfo().length() > 1) {
//			try {
//				new UsersService("userid", request.getPathInfo().split("/")[1]).deleteUser();
//				
//				response.setStatus(200);
//
//			} catch (UserNotDeletedException e) {
//				response.setStatus(404);
//				Logger logger = Logger.getLogger(UserServlet.class);
//				logger.info(e.toString() + " URI: " + request.getPathInfo());
//			}
//
//		} else {
//			Logger logger = Logger.getLogger(UserServlet.class);
//			logger.debug("Path info is missing user ID: " + request.getPathInfo());
//			response.setStatus(400);
//		}
//
//	}
//	
//	

}
