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
import edu.ycp.cs320.project.model.Objective;
import edu.ycp.cs320.project.model.Task;
import edu.ycp.cs320.project.controller.LoginController;
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
		
		int userID = controller.getUserIDByName(user);
		
		//TODO: make a more specific db function to get position if time allows
		//based on current position, get the items from the room model
		List<Item> items = controller.findItemsInPosition(model.getUser().getRoom().getUserPosition(), user);
		//get items in room
		
		//add the items to the jsp
		req.setAttribute("items", items);

		//get the inventory and add the images of the items to the jsp
		List<Item> inventory = controller.findInventoryByName(user);
		req.setAttribute("inventory", inventory);
		
		//check what the current position is and set background image
		int position = model.getUser().getRoom().getUserPosition();
		
		
		
		//tells the jsp which image to use
		req.setAttribute("ViewNumber", position);
		
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
			System.out.println("Clicked logout");
			req.getSession().setAttribute("user", null);
			resp.sendRedirect
			(req.getContextPath() + "/login");
		}
		
		//create list for items in the room
		List<Item> items = new ArrayList<Item>();
		
		
		
		
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
		int userID = controller.getUserIDByName(user);
		//get items in room
		items = controller.findItemsInPosition(model.getUser().getRoom().getUserPosition(), user);

		List<Objective> objectives = controller.getObjectivesFromUserID(userID);
		//Get the objective the user is currently on
		Objective objective = controller.getCurrentObjective(objectives);
		
		//display inventory on jsp
		List<Item> inventory = controller.findInventoryByName(user);
		System.out.println("inventory size " + inventory.size());
		for(int i = 0; i < inventory.size(); i++) {
			req.setAttribute("inv"+(i+1), inventory.get(i).getName());
		}
		//determine position and set image in jsp
		req.setAttribute("items", items);
		int position = 0;
		
		
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
					System.out.println("Item can be added");
					req.setAttribute("textOutput", "Item added to inventory");
				}else {
					//notify the user that the item cannot be picked up
					System.out.println("Item cannot be added");
					req.setAttribute("textOutput", "Item cannot be added to inventory");
				}
				
			}
			
			//left arrow clicked
		}
		if(req.getParameter("left") != null){
			System.out.println("Left Pressed");
			controller.moveUserLeft();
		}
		
		//move right
		if(req.getParameter("right")!= null) {
			System.out.println("Right Pressed");
			controller.moveUserRight();
		}
		
		//move up
		if(req.getParameter("up")!=null){
			System.out.println("Up Pressed");
			controller.moveUserUp();
		}
		
		//move down
		if(req.getParameter("down")!=null){
			System.out.println("Down Pressed");
			controller.moveUserDown();
			
		}
		//check if items in room were selected
		for(int i = 0; i <= model.getUser().getRoom().getItems().size() - 1; i++) {
			String itemName = model.getUser().getRoom().getItems().get(i).getName();
			if(req.getParameter(itemName) != null) {
				System.out.println(itemName + " was selected");
				req.setAttribute("textOutput", "You found a " + itemName);
				req.setAttribute("selected", itemName);
				req.setAttribute("textOutput", controller.getSelectedMessage(itemName, userID, objective.getObjectiveID()));
					
			}
		}
		
		//check if items are being used
		for(int i = 0; i < inventory.size(); i++) {
			String invItem = inventory.get(i).getName();
			if(req.getParameter(invItem) != null) {
				System.out.println(invItem + " was pressed");
				if(req.getParameter("selected") != null && req.getParameter("selected").equals("") == false) {
					
					Item selected = controller.findItemByName(req.getParameter("selected"), items);
	
					System.out.println(selected.getName() + " is the selectd item");
					String message = "";
					message = controller.useItem(inventory.get(i), selected, userID, objective.getObjectiveID());
					req.setAttribute("textOutput", message);
					
				}
				
			}
		}
		
		String passcode = req.getParameter("comicBookCode");
		System.out.println("Code input by User "+ passcode);
		boolean validCode = false;
		if(req.getParameter("Enter") != null) {
			validCode = controller.verifyPasscode(passcode,controller.getModel().getUser());


			System.out.println(validCode);
			if(validCode) {
				System.out.println("Correct Passcode");
			}else {
				System.out.println("Incorrect Passcode");
			}
		}
		
		//refreshes the model before adding things to the screen
		controller.PopulateModel(user);	
		
		//check what the current position is and set background image
				position = model.getUser().getRoom().getUserPosition();
				
				
				//tells the jsp which image to use
				req.setAttribute("ViewNumber", position);
				

				
				String taskMessage = "";
				objectives = controller.getObjectivesFromUserID(userID);
				//Get the objective the user is currently on
				objective = controller.getCurrentObjective(objectives);
				//verifies is all the tasks are marked as complete
				controller.getTasksFromObjectiveID(objective.getObjectiveID());
				for(Task task : controller.getTasksFromObjectiveID(objective.getObjectiveID())) {
					if(!task.getIsComplete()) {
						taskMessage = task.validateComplete(userID);
						if(taskMessage != "") {
							req.setAttribute("textOutput", taskMessage);
						}
					}
				}
				//check if the current objective is complete by checking if all tasks are complete
				if(objective!=null) {
					Boolean objIsComplete = true;
					List<Task> tasksToCheck = controller.getTasksFromObjectiveID(objective.getObjectiveID());
					for(Task task : tasksToCheck) {
						if(task.getIsComplete() == false) {
							objIsComplete = false;
						}
					}
					//if all the tasks are complete, mark the objective as complete and start the next one
					if(objIsComplete) {
						System.out.println("An objective was completed");
						controller.markObjectiveAsComplete(objective.getObjectiveID());
						controller.startNextObjective(controller.getObjectivesFromUserID(userID));
					}
				}
				
				Boolean gameComplete = true;
				//check if all objectives complete
				for(Objective obj : controller.getObjectivesFromUserID(userID)) {
					if(!obj.getIsComplete()) {
						gameComplete = false;
					}
				}
				
				if(gameComplete) {
					System.out.println("All objectives compelted");
					//TODO: win condition achieved, do something
				}
				
				//get the inventory and add the images of the items to the jsp
				inventory = controller.findInventoryByName(user);
				req.setAttribute("inventory", inventory);
				
				
				//get the items in the user's current position
				items = controller.findItemsInPosition(position, user);
				req.setAttribute("items", items);
		req.getRequestDispatcher("/_view/main_page.jsp").forward(req, resp);
	}

}