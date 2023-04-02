package edu.ycp.cs320.project.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.project.model.MainPage;
import edu.ycp.cs320.project.controller.MainPageController;

public class MainpageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println("Main Page Servlet: doGet");
		
		String user = (String) req.getSession().getAttribute("user");
		if (user == null) {
			System.out.println("   User: <" + user + "> not logged in or session timed out");
			
			// user is not logged in, or the session expired
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		
		MainPage model = new MainPage();
		MainPageController controller = new MainPageController(model);
		controller.PopulateModel(user);
		
		req.setAttribute("items", model.getRoom().getItems());

		
		req.getRequestDispatcher("/_view/main_page.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		System.out.println("Main Page Servlet: doPost");
		
		String user = (String) req.getSession().getAttribute("user");
		if (user == null) {
			System.out.println("   User: <" + user + "> not logged in or session timed out");
			
			// user is not logged in, or the session expired
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		
		if(req.getParameter("logout") != null) {
			System.out.println("Clicked");
			req.getSession().setAttribute("user", null);
			resp.sendRedirect(req.getContextPath() + "/login");
		}
		
		MainPage model = new MainPage();
		MainPageController controller = new MainPageController(model);
		controller.PopulateModel(user);
		
		req.setAttribute("items", model.getRoom().getItems());
		System.out.println(req.getParameter("Matches") != null);
		for(int i = 0; i <= model.getRoom().getItems().size() - 1; i++) {
			String itemName = model.getRoom().getItems().get(i).getName();
			if(req.getParameter(itemName) != null) {
				System.out.println("Pressed");
				resp.sendRedirect(req.getContextPath() + "/main_page");
			}
		}
		
	
	}
}
