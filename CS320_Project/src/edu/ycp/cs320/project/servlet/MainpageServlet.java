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
		
		
		
		//based on current position, get the items from the room model
		ArrayList<Item> items = new ArrayList<Item>();
		//get items in room
		for(int i = 0; i<model.getUser().getRoom().getItems().size();i++) {
			if(model.getUser().getRoom().getItems().get(i).getRoomPosition() == model.getUser().getRoom().getUserPosition()) {
				
				items.add(model.getUser().getRoom().getItems().get(i));
			}
		}
		//add the items to the jsp
		req.setAttribute("items", items);

		//get the inventory and add the images of the items to the jsp
		List<Item> inventory = model.getUser().getInventory();
		req.setAttribute("inventory", inventory);
		
		//check what the current position is and set background image
		//TODO: this code will be changed once our backgrounds are final
		int position = model.getUser().getRoom().getUserPosition();
		if(position == 0 ) {
			position=0;
		}
		if(position==1) {
			position=1;
		}
		if(position==2) {
			position=2;
		}
		if(position == 3 ) {
			position=3;
		}
		
		
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
		ArrayList<Item> items = new ArrayList<Item>();
		
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
		//get items in room
		for(int i = 0; i<model.getUser().getRoom().getItems().size();i++) {
			if(model.getUser().getRoom().getItems().get(i).getRoomPosition() == model.getUser().getRoom().getUserPosition()) {
				
				items.add(model.getUser().getRoom().getItems().get(i));
			}
		}

		//display inventory on jsp
		List<Item> inventory = model.getUser().getInventory();
		System.out.println("inventory size " + inventory.size());
		for(int i = 0; i < inventory.size(); i++) {
			req.setAttribute("inv"+(i+1), inventory.get(i).getName());
		}
		


		//determine position and set image in jsp
		req.setAttribute("model", model);
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
					System.out.println("item can be added");
					req.setAttribute("textOutput", "Item added to inventory");
				}else {
					//notify the user that the item cannot be picked up
					System.out.println("item cannot be added");
					req.setAttribute("textOutput", "Item cannot be added to inventory");
				}
				
				
			}
			
			//left arrow clicked
		}
		if(req.getParameter("left") != null){
			System.out.println("Left Pressed");
			controller.moveUserLeft();
			position = model.getUser().getRoom().getUserPosition();
			
			
		}
		
		//move right
		if(req.getParameter("right")!= null) {
			System.out.println("Right Pressed");
			controller.moveUserRight();
			position = model.getUser().getRoom().getUserPosition();
			
		}
		
		//move up
		if(req.getParameter("up")!=null){
			System.out.println("Up Pressed");
			controller.moveUserUp();
			position = model.getUser().getRoom().getUserPosition();	
			
		}
		
		//move down
		if(req.getParameter("down")!=null){
			System.out.println("Down Pressed");
			controller.moveUserDown();
			position = model.getUser().getRoom().getUserPosition();
			
		}
		
		//check if items in room were selected
		for(int i = 0; i <= model.getUser().getRoom().getItems().size() - 1; i++) {
			String itemName = model.getUser().getRoom().getItems().get(i).getName();
			if(req.getParameter(itemName) != null) {
				
				System.out.println(itemName + " was selected");
				req.setAttribute("textOutput", "You found a " + itemName);
				req.setAttribute("selected", itemName);
				if(itemName.equals("Untitled Book")) {
					System.out.println("untitled book text");
					req.setAttribute("textOutput", "You found a book of spells. Most of the pages are blank or damaged. " +
					"Page 1: Potion of S_ _ ed: fea_he_, l_me j_ _c_, c _ _ _ _ r, c_ove_, b_u_ fl_ _ e_     " +
							"Page 2: Potio_  o_  T_l_ _n_:  H_ir o_ th_ an_ _ _l, l_ck_ c_ov_ _, w_shb_ _ _, li_ _ ju_ _ _ ");
				}
					
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
					message = controller.useItem(inventory.get(i), selected);
					req.setAttribute("textOutput", message);
					
				}
				
			}
		}
		
		//refreshes the model before adding things to the screen
		controller.PopulateModel(user);	
		
		//check what the current position is and set background image
				//TODO: this code will be changed once our backgrounds are final
				position = model.getUser().getRoom().getUserPosition();
				if(position == 0 ) {
					position=0;
				}
				if(position==1) {
					position=1;
				}
				if(position==2) {
					position=2;
				}
				if(position == 3 ) {
					position=3;
				}
				
				//tells the jsp which image to use
				req.setAttribute("ViewNumber", position);
				
			System.out.println("Current position " + model.getUser().getRoom().getUserPosition());
				
		//get the inventory and add the images of the items to the jsp
				inventory = model.getUser().getInventory();
				req.setAttribute("inventory", inventory);
				
				List<Item> roomInv = model.getUser().getRoom().getItems();
				
				items = new ArrayList<Item>();
				//get items in room
				for(int i = 0; i<roomInv.size();i++) {
					if(roomInv.get(i).getRoomPosition() == model.getUser().getRoom().getUserPosition()) {
						
						items.add(model.getUser().getRoom().getItems().get(i));
					}
				}
				
		req.setAttribute("items", items);
		req.getRequestDispatcher("/_view/main_page.jsp").forward(req, resp);
	}
		
	

}