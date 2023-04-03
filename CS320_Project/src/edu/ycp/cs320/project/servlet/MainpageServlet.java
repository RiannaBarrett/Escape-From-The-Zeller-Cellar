package edu.ycp.cs320.project.servlet;

import java.io.IOException;
import java.util.ArrayList;
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
		
		//check if user is logged in
		String user = (String) req.getSession().getAttribute("user");
		if (user == null) {
			System.out.println("   User: <" + user + "> not logged in or session timed out");
			
			// user is not logged in, or the session expired
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		
		//populate model
		MainPage model = new MainPage();
		MainPageController controller = new MainPageController(model);
		controller.PopulateModel(user);
		//check what the current position is and set background image
		int position = model.getRoom().getUserPosition();
		if(position == 2 || position == 0 ) {
			position=1;
		}
		if(position == 3) {
			position = 2;
		}
		
		req.setAttribute("ViewNumber", position);
		
		//based on current position, get the items from the room model
		List<Item> items = new ArrayList<Item>();
		for(int i = 0; i<model.getRoom().getItems().size();i++) {
			if(model.getRoom().getItems().get(i).getRoomPosition() == model.getRoom().getUserPosition()) {
				
				items.add(model.getRoom().getItems().get(i));
			}
		}
		//add the items to the jsp
		req.setAttribute("items", items);

		//get the inventory and add the images of the items to the jsp
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
		
		//check if logged in
		String user = (String) req.getSession().getAttribute("user");
		System.out.println(user);
		if (user == null || user == "") {
			System.out.println("   User: <" + user + "> not logged in or session timed out");
			
			// user is not logged in, or the session expired
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		
		//see if logout button was pressed
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

		//display inventory on jsp
		List<Item> inventory = model.getUser().getInventory();
		System.out.println(inventory.size());
		for(int i = 0; i < inventory.size(); i++) {
			req.setAttribute("inv"+(i+1), inventory.get(i).getName());
		}
		
		//based on current position, get the items from the room model
				List<Item> items = new ArrayList<Item>();
				for(int i = 0; i<model.getRoom().getItems().size();i++) {
					if(model.getRoom().getItems().get(i).getRoomPosition() == model.getRoom().getUserPosition()) {
						items.add(model.getRoom().getItems().get(i));
					}
				}

		//determine position and set image in jsp
		req.setAttribute("model", model);
		req.setAttribute("items", items);
		int position = model.getRoom().getUserPosition();
		if(position == 2 || position == 0 ) {
			position=1;
		}
		if(position == 3) {
			position = 2;
		}
		
		req.setAttribute("ViewNumber", position);
		
		
		//if pickup button was pressed
		if(req.getParameter("pickUp")!=null) {
			String selectedItem = req.getParameter("selected");
			System.out.println(selectedItem + " Selected");
			//if no item was selected do nothing and update message
			if(selectedItem.equals("")|| selectedItem == null) {
				req.setAttribute("textOutput", "No item selected");
			}else {
				//if item was selected, pick it up and notify the user it was successful
				if(controller.transferItemFromRoomToUser(selectedItem) == true) {
					System.out.println("item can be added");
					req.setAttribute("textOutput", "Item added to inventory");
					inventory = model.getUser().getInventory();
					System.out.println(inventory.size());
					for(int i = 0; i < inventory.size(); i++) {
						req.setAttribute("inv"+(i+1), inventory.get(i).getName());
					}
				}else {
					//notify the user that the item cannot be picked up
					System.out.println("item cannot be added");
					req.setAttribute("textOutput", "Item cannot be added to inventory");
				}
				
				
			}
			req.getRequestDispatcher("/_view/main_page.jsp").forward(req, resp);
			//left arrow clicked
		}else if(req.getParameter("left") != null){
			System.out.println("Left Pressed");
			controller.moveUserLeft();
			position = model.getRoom().getUserPosition();
			if(position == 2 || position == 0 ) {
				position=1;
			}
			if(position == 3) {
				position = 2;
			}
			req.setAttribute("ViewNumber", position);
			items = new ArrayList<Item>();
			for(int i = 0; i<model.getRoom().getItems().size();i++) {
				if(model.getRoom().getItems().get(i).getRoomPosition() == model.getRoom().getUserPosition()) {
					
					items.add(model.getRoom().getItems().get(i));
				}
			}
			req.setAttribute("items", items);
			req.getRequestDispatcher("/_view/main_page.jsp").forward(req, resp);
			
		}else if(req.getParameter("right")!= null) {
			System.out.println("Right Pressed");
			controller.moveUserRight();
			position = model.getRoom().getUserPosition();
			if(position == 2 || position == 0 ) {
				position=1;
			}
			if(position == 3) {
				position = 2;
			}
			req.setAttribute("ViewNumber", position);
			items = new ArrayList<Item>();
			for(int i = 0; i<model.getRoom().getItems().size();i++) {
				if(model.getRoom().getItems().get(i).getRoomPosition() == model.getRoom().getUserPosition()) {
					
					items.add(model.getRoom().getItems().get(i));
				}
			}
			req.setAttribute("items", items);
			req.getRequestDispatcher("/_view/main_page.jsp").forward(req, resp);
		}else if(req.getParameter("up")!=null){
			System.out.println("Up Pressed");
			controller.moveUserUp();
			position = model.getRoom().getUserPosition();
			if(position == 2 || position == 0 ) {
				position=1;
			}
			if(position == 3) {
				position = 2;
			}
			req.setAttribute("ViewNumber", position);
			items = new ArrayList<Item>();
			for(int i = 0; i<model.getRoom().getItems().size();i++) {
				if(model.getRoom().getItems().get(i).getRoomPosition() == model.getRoom().getUserPosition()) {
					
					items.add(model.getRoom().getItems().get(i));
				}
			}
			req.setAttribute("items", items);
			req.getRequestDispatcher("/_view/main_page.jsp").forward(req, resp);
		}else if(req.getParameter("down")!=null){
			System.out.println("Down Pressed");
			controller.moveUserDown();
			position = model.getRoom().getUserPosition();
			if(position == 2 || position == 0 ) {
				position=1;
			}
			if(position == 3) {
				position = 2;
			}
			req.setAttribute("ViewNumber", position);
			items = new ArrayList<Item>();
			for(int i = 0; i<model.getRoom().getItems().size();i++) {
				if(model.getRoom().getItems().get(i).getRoomPosition() == model.getRoom().getUserPosition()) {
					
					items.add(model.getRoom().getItems().get(i));
				}
			}
			req.setAttribute("items", items);
			req.getRequestDispatcher("/_view/main_page.jsp").forward(req, resp);
		}else {
			for(int i = 0; i <= model.getRoom().getItems().size() - 1; i++) {
				String itemName = model.getRoom().getItems().get(i).getName();
				if(req.getParameter(itemName) != null) {
					req.setAttribute("textOutput", "You found " + itemName);
					req.setAttribute("selected", itemName);
					req.getRequestDispatcher("/_view/main_page.jsp").forward(req, resp);
					
				}
			}
		}
		
	
	}
}