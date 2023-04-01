package edu.ycp.cs320.project.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.ycp.cs320.project.controller.LoginController;


public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("Login Servlet: doGet");
		
		req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		System.out.println("Login Servlet: doPost");
		
		//get username and password parameters

		String errorMessage = "";
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		boolean validLogin  = false;
		
		//Create new controller
		LoginController controller = new LoginController();
		
		//if submit button was clicked
	if(req.getParameter("login") != null) {
		
				validLogin = controller.validateLogin(username, password);
				if(!validLogin) {
			
					errorMessage = "Invalid username or Password";
					req.setAttribute("errorMessage", errorMessage);
					req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
					
				}else {
					req.getSession().setAttribute("user", username);

					//go to start game page
					resp.sendRedirect(req.getContextPath() + "/start_game");
				}
			
	
			
		}
		
	}
}
