package edu.ycp.cs320.project.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.project.controller.WinGameController;

public class GameOverServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("Game Over Servlet: doGet");
		
		req.getRequestDispatcher("/_view/game_over.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
	        throws ServletException, IOException {
	    System.out.println("Game Over Servlet: doPost");
	    WinGameController controller = new WinGameController();
		String user = (String) req.getSession().getAttribute("user");
	    if(req.getParameter("reset") != null) {
	    	controller.resetUser(user);
	    }
	    req.getRequestDispatcher("/_view/game_over.jsp").forward(req, resp);

	}

}
