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

public class BreedServlet extends HttpServlet {

	

		private static final long serialVersionUID = 1L;

		/**
		 * @see HttpServlet#HttpServlet()
		 */
		public BreedServlet() {
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
							try {
								ArrayList<String> returnedBreeds = new AnimalsService().getBreeds();
					
					response.getWriter().append(objectMapper.writeValueAsString(returnedBreeds));
					response.setContentType("application/json");
					response.setStatus(200);
				} catch (Exception e) {
					response.setStatus(404);
					Logger logger = Logger.getLogger(BreedServlet.class);
					logger.debug(e.toString() + " URI: " + request.getPathInfo());
				}

			

		}
	
	
}
