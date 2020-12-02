package com.animalshelter.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.animalshelter.dao.DatabaseAnimalDAO;
import com.animalshelter.exception.AnimalException;
import com.animalshelter.model.Animal;
import com.animalshelter.service.AnimalsService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class AnimalsServlet
 */
// Using web.xml for routing

public class AnimalsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AnimalsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// Return all animals or search animals by species, breed, or sex
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		if (request.getParameterNames().hasMoreElements()) {

			String requestKey = request.getParameterNames().nextElement();
			String requestValue = request.getParameter(requestKey);
			
			// Only allow one query string for searching animals - will ignore additional query strings
			try {
				ArrayList<Animal> animals = new AnimalsService(requestKey, requestValue).getAnimalsByRequestKey();

				response.getWriter().append(objectMapper.writeValueAsString(animals));
				response.setContentType("application/json");
				response.setStatus(200);

			} catch (IOException e) {
				response.setStatus(400);

				Logger logger = Logger.getLogger(UsersServlet.class);
				logger.debug(e.toString() + " QueryString: " + request.getQueryString());

			} catch (AnimalException e) {
				response.setStatus(404);

				Logger logger = Logger.getLogger(AnimalsServlet.class);
				logger.debug(e.toString() + " QueryString: " + request.getQueryString());

				// Use below items to view environment variables or current working directory
				// for log file
				// System.out.println(System.getenv("HOME"));
				// System.out.println(System.getProperty("user.dir"));
			}

		} else {
			try {
				ArrayList<Animal> animals = new AnimalsService().getAllAnimals();
				response.getWriter().append(objectMapper.writeValueAsString(animals));
				response.setContentType("application/json");
				response.setStatus(200);
			} catch (AnimalException e) {
				response.setStatus(400);

				Logger logger = Logger.getLogger(AnimalsServlet.class);
				logger.debug(e.getMessage());
			}
		}
	}
}
