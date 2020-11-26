package com.animalshelter.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.animalshelter.exception.UserException;
import com.animalshelter.model.User;
import com.animalshelter.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class UsersServlet
 */
@WebServlet("/users")
public class UsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UsersServlet() {
		super();
	}

	ObjectMapper objectMapper = new ObjectMapper();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameterNames().hasMoreElements()) {

			String requestKey = request.getParameterNames().nextElement();
			String requestValue = request.getParameter(requestKey);

			// Only allow one parameter at this time (username, lastname, or userid)
			try {
				User user = new UsersService(requestKey, requestValue).findUser();

				// objectMapper is using the getter methods in the User class to write JSON
				// values...
				response.getWriter().append(objectMapper.writeValueAsString(user));
				response.setContentType("application/json");
				response.setStatus(201);
			} catch (IOException e) {
				response.setStatus(400);

				Logger logger = Logger.getLogger(UsersServlet.class);
				logger.debug(e.toString() + " QueryString: " + request.getQueryString());

			} catch (UserException e) {
				response.setStatus(404);

				Logger logger = Logger.getLogger(UsersServlet.class);
				logger.debug(e.toString() + " QueryString: " + request.getQueryString());

			}

		} else {

			try {
				ArrayList<User> users = new UsersService().getAllUsers();
				response.getWriter().append(objectMapper.writeValueAsString(users));
				response.setContentType("application/json");
				response.setStatus(200);
			} catch (UserException e) {
				response.setStatus(400);

				Logger logger = Logger.getLogger(UsersServlet.class);
				logger.debug(e.toString() + " QueryString: " + request.getQueryString());
			}
		}

	}

}
