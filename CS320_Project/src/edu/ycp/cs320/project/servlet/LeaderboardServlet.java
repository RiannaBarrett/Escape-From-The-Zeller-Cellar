package edu.ycp.cs320.project.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.project.controller.LeaderboardController;
import edu.ycp.cs320.project.controller.MainPageController;
import edu.ycp.cs320.project.controller.WinGameController;

public class LeaderboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("Leaderboard Game Servlet: doGet");
		
		LeaderboardController controller = new LeaderboardController();
		req.setAttribute("pairs", controller.getLeaderboard());
		req.getRequestDispatcher("/_view/leader_board.jsp").forward(req, resp);

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
	        throws ServletException, IOException {
	    System.out.println("Leaderboard Servlet: doPost");
	    LeaderboardController controller = new LeaderboardController();
		String user = (String) req.getSession().getAttribute("user");
		req.setAttribute("pairs", controller.getLeaderboard());
		req.getRequestDispatcher("/_view/leader_board.jsp").forward(req, resp);



	}

}
