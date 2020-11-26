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
import com.animalshelter.exception.RoleNotFoundException;
import com.animalshelter.exception.UserException;
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
				logger.debug(e.toString() + " URI: " + request.getPathInfo());
			}

		} else {
			Logger logger = Logger.getLogger(AnimalServlet.class);
			logger.info("Path info is missing animal ID: " + request.getPathInfo());
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

		Animal animalToInsert = objectMapper.readValue(jsonRequestBody, Animal.class);

		try {
			Animal insertedAnimal = new AnimalsService().createNewAnimal(animalToInsert);
			response.getWriter().append(objectMapper.writeValueAsString(insertedAnimal));
			response.setContentType("application/json");
			response.setStatus(201);
			Logger logger = Logger.getLogger(AnimalServlet.class);
			logger.info("Inserted Animal in table animals - ID: " + insertedAnimal.getAnimalId() + " Name: "
					+ insertedAnimal.getAnimalName());

		} catch (AnimalException e) {
			Logger logger = Logger.getLogger(AnimalServlet.class);
			logger.debug("Error creating animal. " + e.getMessage());
			response.setStatus(400);
		}
	}
	// Update Animal in database based on animalId

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getPathInfo().length() > 1) {

			int animalId = Integer.parseInt(request.getPathInfo().split("/")[1]);

			BufferedReader requestBody = request.getReader();

			StringBuilder requestBodyString = new StringBuilder();
			String requestBodyLine;
			String jsonRequestBody;

			while ((requestBodyLine = requestBody.readLine()) != null) {
				requestBodyString.append(requestBodyLine);
			}

			jsonRequestBody = requestBodyString.toString();

			Animal animalToUpdate = objectMapper.readValue(jsonRequestBody, Animal.class);

			try {
				Animal updatedAnimal = new AnimalsService().updateAnimal(animalToUpdate, animalId);
				response.getWriter().append(objectMapper.writeValueAsString(updatedAnimal));
				response.setContentType("application/json");
				response.setStatus(200);
				Logger logger = Logger.getLogger(AnimalServlet.class);
				logger.info("Updated Animal in table animals - ID: " + updatedAnimal.getAnimalId() + " Name: "
						+ updatedAnimal.getAnimalName());

			} catch (AnimalException e) {
				Logger logger = Logger.getLogger(AnimalServlet.class);
				logger.debug("Error updating animal. " + e.getMessage());
				response.setStatus(400);
			}

		} else {
			Logger logger = Logger.getLogger(AnimalServlet.class);
			logger.info("Path info is missing animal ID: " + request.getPathInfo());
			response.setStatus(400);
		}

	}

	// Delete animal from database based on animalId
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getPathInfo().length() > 1) {
			try {
				int animalId = Integer.parseInt(request.getPathInfo().split("/")[1]);

				new AnimalsService().deleteAnimal(animalId);

				response.setStatus(200);
				Logger logger = Logger.getLogger(AnimalServlet.class);
				logger.info("Deleted Animal in table animals - ID: " + animalId);

			} catch (AnimalException e) {
				response.setStatus(404);
				Logger logger = Logger.getLogger(AnimalServlet.class);
				logger.debug(e.toString() + " URI: " + request.getPathInfo());
			}

		} else {
			Logger logger = Logger.getLogger(AnimalServlet.class);
			logger.info("Path info is missing animal ID: " + request.getPathInfo());
			response.setStatus(400);
		}

	}
}
