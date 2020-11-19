package com.animalshelter.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.animalshelter.dao.UserDB;
import com.animalshelter.model.User;
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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		ArrayList<User> users = new ArrayList<User>();
		UserDB userDB = new UserDB();
		ArrayList<User> users = userDB.CreateUserDB();
		
//		System.out.println("CreateUser index 0: " + userDB.CreateUserDB().get(0).toString());
		// objectMapper is using the getter methods in the User class to write JSON values...
		System.out.println("ObjectMapper writeValue: " + objectMapper.writeValueAsString(users.get(1)).toString());
		response.getWriter().append(objectMapper.writeValueAsString(users));
	}


}
