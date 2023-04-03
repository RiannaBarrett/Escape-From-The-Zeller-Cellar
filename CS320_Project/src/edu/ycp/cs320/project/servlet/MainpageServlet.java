package edu.ycp.cs320.project.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs320.project.model.Item;
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

		List<Item> inventory = model.getUser().getInventory();
		System.out.println(inventory.size());
		for(int i = 0; i < inventory.size(); i++) {
			req.setAttribute("inv"+(i+1), inventory.get(i).getName());
		}
		req.getRequestDispatcher("/_view/main_page.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		System.out.println("Main Page Servlet: doPost");
		
		String user = (String) req.getSession().getAttribute("user");
		System.out.println(user);
		if (user == null || user == "") {
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
		
		
		// Attempt to pass the model back and forth for testing?
		MainPage model = new MainPage();
		try {
			model = (MainPage)req.getAttribute("model");
		}
		catch(NumberFormatException e) {
			System.out.println(e);
		}
		MainPageController controller = new MainPageController(model);
		if(model == null) {
			model = new MainPage();
			controller.setModel(model);
			controller.PopulateModel(user);
		}

		List<Item> inventory = model.getUser().getInventory();
		System.out.println(inventory.size());
		for(int i = 0; i < inventory.size(); i++) {
			req.setAttribute("inv"+(i+1), inventory.get(i).getName());
		}
		

		req.setAttribute("model", model);
		req.setAttribute("items", model.getRoom().getItems());
		//if pickup button was pressed
		if(req.getParameter("pickUp")!=null) {
			String selectedItem = req.getParameter("selected");
			System.out.println(selectedItem + " Selected");
			if(selectedItem.equals("")|| selectedItem == null) {
				System.out.print("selected item is blank");
				req.setAttribute("textOutput", "No item selected");
			}else {
				if(controller.transferItemFromRoomToUser(selectedItem) == true) {
					System.out.println("item can be added");
					req.setAttribute("textOutput", "Item added to inventory");
					inventory = model.getUser().getInventory();
					System.out.println(inventory.size());
					for(int i = 0; i < inventory.size(); i++) {
						req.setAttribute("inv"+(i+1), inventory.get(i).getName());
					}
				}else {
					System.out.println("item cannot be added");
					req.setAttribute("textOutput", "Item cannot be added to inventory");
				}
				
				
			}
			req.getRequestDispatcher("/_view/main_page.jsp").forward(req, resp);
		}else {
			for(int i = 0; i <= model.getRoom().getItems().size() - 1; i++) {
				String itemName = model.getRoom().getItems().get(i).getName();
				if(req.getParameter(itemName) != null) {
					System.out.println("Pressed: " + itemName);
					req.setAttribute("textOutput", "You found " + itemName);
					req.setAttribute("selected", itemName);
					req.getRequestDispatcher("/_view/main_page.jsp").forward(req, resp);
					
				}
			}
		}
		
	
	}
}