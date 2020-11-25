package com.animalshelter.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.animalshelter.service.SessionsService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class UserSessionServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		BufferedReader requestBody = request.getReader();

		StringBuilder requestBodyString = new StringBuilder();

		String requestBodyLine;
		String jsonRequestBody;

		while ((requestBodyLine = requestBody.readLine()) != null) {
			requestBodyString.append(requestBodyLine);
		}

		jsonRequestBody = requestBodyString.toString();

		SessionsService userSession = objectMapper.readValue(jsonRequestBody, SessionsService.class);

		if (userSession.validateUser()) {
			HttpSession session = request.getSession();
			session.setAttribute("username", userSession.getUsername());
			response.setContentType("text/html");
			PrintWriter pwriter = response.getWriter();
			pwriter.print("<h2>Welcome " + userSession.getUsername() + "!</h2>");
			pwriter.close();
			Logger logger = Logger.getLogger(LoginServlet.class);
			logger.info(userSession.getUsername() + " Logged in.");

		} else {

			Logger logger = Logger.getLogger(LoginServlet.class);
			logger.debug("Bad username or password: " + userSession.getUsername());
			response.setStatus(401);
		}

	}

}
