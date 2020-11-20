package com.animalshelter.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.animalshelter.dao.DatabaseUserDAO;
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
		// TODO Auto-generated constructor stub
	}

	ObjectMapper objectMapper = new ObjectMapper();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getQueryString() != null) {
			String requestKey = request.getQueryString().split("=")[0];
			String requestValue = request.getQueryString().split("=")[1];
			System.out.println(request.getQueryString().split("=")[0]);

			User user = new UsersService(requestKey, requestValue).findUser();

			System.out.println("ObjectMapper writeValue: " + objectMapper.writeValueAsString(user).toString());
			response.getWriter().append(objectMapper.writeValueAsString(user));

		} else {
			ArrayList<User> users = new UsersService().getAllUsers();

			// objectMapper is using the getter methods in the User class to write JSON
			// values...
			response.getWriter().append(objectMapper.writeValueAsString(users));
		}

	}

}
