package com.animalshelter.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.animalshelter.exception.UserException;
import com.animalshelter.model.User;
import com.animalshelter.service.SessionsService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class UserSessionServlet
 */
@WebServlet(urlPatterns = { "/login", "/logout" })
public class SessionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SessionServlet() {
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
	 * 
	 *      Log in user functionality
	 */
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

		SessionsService userSession = objectMapper.readValue(jsonRequestBody, SessionsService.class);

		try {
			// Validate username and password for login

			User user = userSession.validateUser();

			// Create session and set the username and role attributes.
			// If the role attribute is 'Admin' then can delete users.
			// Otherwise will not be allowed.

			HttpSession session = request.getSession();
			session.setAttribute("username", user.getUsername());
			session.setAttribute("role", user.getRole().getRoleName());

			// Send response to browser that Login was successful.

			response.setContentType("text/html");
			PrintWriter pwriter = response.getWriter();
			pwriter.print("<h2>Welcome " + userSession.getUsername() + "!</h2>");
			pwriter.close();
			Logger logger = Logger.getLogger(SessionServlet.class);
			logger.info(userSession.getUsername() + " Logged in.");

		} catch (UserException e) {
			Logger logger = Logger.getLogger(SessionServlet.class);
			logger.debug("Bad username or password: " + userSession.getUsername());
			response.setStatus(401);

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			Logger logger = Logger.getLogger(UserServlet.class);
			logger.debug(e.getMessage());

		}

	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getSession().getAttribute("username") == null) {
			response.setStatus(400);
			response.getWriter().print("Error - not logged in yet");
		} else {
			Logger logger = Logger.getLogger(SessionServlet.class);
			logger.info(request.getSession().getAttribute("username") + " Logged out.");

			request.getSession().invalidate();
			response.getWriter().print("Successfully Logged Out!");
		}

	}

}
