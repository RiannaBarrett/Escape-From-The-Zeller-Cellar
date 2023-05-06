package edu.ycp.cs320.project.controller;

import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.IDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs320.project.model.*;

public class MainPageController {

	private MainPage model;
	private IDatabase db;
	
	public MainPageController() {
		model = new MainPage();
	}
	
	public MainPageController(MainPage model) {
		this.model = model;
	}
	
	public void setModel(MainPage model) {
		this.model = model;
	}
	
	public MainPage getModel() {
		return model;
	}
	
	public void PopulateModel(String username) {
		db = DatabaseProvider.getInstance();
		User user = db.findUserByName(username);
		if(user != null) {
			model.setUser(user);
		}
	}
	
	public int getUserIDByName(String username) {
		int userID = -1;
		userID = db.findUserIDByName(username);
		return userID;
	}
	
	public boolean transferItemFromRoomToUser(String itemName) {
		// Does the user have inventory space?
		// MOVE TO CONTROLLER
		User user = model.getUser();
		System.out.println("Inventory size:" + user.getInventory().size());
		System.out.println("Inventory limit: " + user.getInventoryLimit());
		if(user.getInventory().size() >= user.getInventoryLimit()) {
			return false;
		}
		
		Item itemToBeTransferred = findItemByName(itemName, user.getRoom().getItems());
	
		// Does the item exist in the room?
		if(itemToBeTransferred != null) {
			System.out.println(itemName + " canBePickedUp: " + itemToBeTransferred.getCanBePickedUp());
			// Is the user able to interact with the item?
			System.out.println("Current user position: " + user.getRoom().getUserPosition());
			System.out.println("Current item position: " + itemToBeTransferred.getRoomPosition());
			if(itemToBeTransferred.getRoomPosition() == user.getRoom().getUserPosition()) {
				// Can the item be picked up?
				Boolean canBePickedUp = itemToBeTransferred.getCanBePickedUp();
				System.out.println("Position is correct");
				if(itemToBeTransferred.getCanBePickedUp() == true) {
					System.out.println("Item can be picked up");
					return db.transferItemFromRoomToUser(user, itemToBeTransferred);
				}
			}
		}
		return false;
	}
	
	// 3 sides, 1 up. 
	// 0, 1, 2 for sides
	// 3 for up
	public boolean moveUserLeft() {
		// Can't look left if looking up
		int currentPos = model.getUser().getRoom().getUserPosition();
		if(currentPos == 3) {
			return false;
		}
		// Max left at 0, loop back to 2
		if(currentPos == 0) {
			return db.moveUser(model.getUser(), 2);
		}
		else {
			return db.moveUser(model.getUser(), currentPos - 1);
		}
	}
	
	public boolean moveUserRight() {
		// Can't look left if looking up
		int currentPos = model.getUser().getRoom().getUserPosition();
		if(model.getUser().getRoom().getUserPosition() == 3) {
			return false;
		}
		// Max right at 2, loop back to 0
		if(currentPos == 2) {
			return db.moveUser(model.getUser(), 0);
		}
		else {
			return db.moveUser(model.getUser(), currentPos + 1);
		}
	}
	
	public boolean moveUserUp() {
		// Can't look up twice
		if(model.getUser().getRoom().getUserPosition() == 3) {
			return false;
		}
		else {
			return db.moveUser(model.getUser(), 3);
		}
	}
	
	public boolean moveUserDown() {
		// Can only look down if looking up
		if(model.getUser().getRoom().getUserPosition() != 3) {
			return false;
		}
		else {
			return db.moveUser(model.getUser(), 0);
		}
	}
	
	
	
	// Helper functions
	public Item findItemByName(String itemName, List<Item> itemList) {
		for (Item item : itemList) {
			if(item.getName().equals(itemName)) {
				return item;
			}
		}
		return null;
	}
	
	//TODO: make it so the functions only need item name and not the entire item!!!
	public String useItem(Item item, Item selected,int userID) {
		String message = "Nothing Happened";
		if(item.getName().equals("Empty Potion Bottle")) {
			message = db.useEmptyPotion(item, selected, model.getUser());
		}else if(item.getName().equals("Matches")) {
			message = db.useMatches(item, selected, model.getUser());
		}else if(item.getName().equals("Bag of Meow Mix")) {
			message = useMeowMix(selected.getName(), userID);
		}else if(item.getName().equals("Full Potion Bottle")) {
			message = useFullPotionBottle(selected.getName(), userID);
		}else if(selected.getName().equals("Empty Cauldron")){
			message = usePotionIngredient(item.getName(), selected.getName(), userID);
		}else if(selected.getName().equals("Puzzle board")) {
			//TODO: call function to check if it is correct
		}else if(item.getName().equals("Hammer")) {
			//TODO: call function to use hammer. If used on fire alarm drop a key
		}else if(item.getName().equals("Lit Candle") && selected.getName().equals("Fire Alarm")) {
			message = "Weird. The fire alarm doesn't go off";
		}
		return message;
	}
	
	
	public List<Item> findItemsInPosition(int position, String username) {
		List<Item> result = new ArrayList<Item>();
		int roomID = db.findRoomIDByUsername(username);
		System.out.println("found room ID" + roomID);
		result = db.findItemsInPositionByID(roomID, position);
		return result;
		
	}
	
	public List<Item> findInventoryByName(String username) {
		List<Item> result = new ArrayList<Item>();
		int userID = db.findUserIDByName(username);
		result = db.findItemsInInventory(userID);
		return result;
		
	}
	
	public String useFullPotionBottle(String selectedName, int userID) {
		String message = "Nothing happened";
		if(selectedName.equals("Messy")) {
			message = "You gave Messy the potion. Messy can now talk <br> Messy: [says something]";
			//TODO: indicate that messy can now talk in the database
			db.removeItemFromInventory(db.findItemByNameAndIDInInv("Full Potion Bottle", userID), userID);
		}
		return message;
	}
	
	public String useMeowMix(String selectedName, int userID) {
		String message = "Nothing happened";
		if(selectedName.equals("Messy")) {
			message = "You gave Messy the Meow Mix. He seems to enjoy it!";
			//TODO: indicate that messy has been fed in database
		}
		return message;
	}
	
	
	public String usePotionIngredient(String itemName, String selectedName, int userID) {
		//message telling the user they successfully added an item
				String message = "You put the item in the cauldron";
				List<Item> ingredients = new ArrayList<Item>();
				//get the room and user ids
				
				
				int roomID = db.findRoomIDByUserID(userID);
				
				//items with position 4 are items that were used on the empty cauldron. get these items
				ingredients = db.findItemsInPositionByID(roomID, 4);
				
				//get the item that is going to be added from the inventory
				Item item = db.findItemByNameAndIDInInv(itemName, userID);
				//create a new version of the item that is in the cauldron position
				Item itemToAdd = item;
				itemToAdd.setRoomPosition(4);
				
				//See if the max number of ingredients were already added
				if(ingredients.size() < 4) {
					//add it to the room
					db.addItemToRoom(itemToAdd, roomID);
					//remove the item that is used from inventory
					db.removeItemFromInventory(item, userID);	
					//get back the items after adding the new item
					ingredients = db.findItemsInPositionByID(roomID, 4);
				}
				
				System.out.println("Number of ingredients after adding: " + ingredients.size());
				//check if correct number of ingredients were added
				if(ingredients.size() >= 4) {
					//check if the ingredients are correct and in the right order
					if(ingredients.get(0).getName().equals("Jar of Cat Hairs") &&
							ingredients.get(1).getName().equals("Clover") &&
							ingredients.get(2).getName().equals("Wishbone") &&
							ingredients.get(3).getName().equals("Carton of Lime Juice")) {
							//make a full cauldron item by changing the name 
						Item emptyCauldron = db.findItemByNameAndIDInRoom(selectedName, roomID);
						//Create full cauldron
						Item fullCauldron = emptyCauldron;
						fullCauldron.setName("Cauldron with Potion");
						//if the potion was made swap the empty cauldron with the full cauldron into the room
						db.addItemToRoom(fullCauldron, roomID);
						db.removeItemFromRoom(emptyCauldron, roomID);
						//message telling the user they were successful
						message = "You created a potion";
						Item firstItem = ingredients.get(0);
						Item secondItem = ingredients.get(1);
						Item thirdItem = ingredients.get(2);
						Item fourthItem = ingredients.get(3);
						//remove the items (they do not need to be used anymore)
						//NOTE: if we are switching back to the empty cauldron keep
						//the items here so that the user cannot attempt to make another and lose their items
						db.removeItemFromRoom(fourthItem, roomID);
						db.removeItemFromRoom(thirdItem, roomID);
						db.removeItemFromRoom(secondItem, roomID);
						db.removeItemFromRoom(firstItem, roomID);
						
						
						db.changeCanBePickedUp(roomID, "Empty Potion Bottle", true);
						
						
					}else {
						//if they are incorrect return the items to inventory and remove them from ingredient list
						Item firstItem = ingredients.get(0);
						Item secondItem = ingredients.get(1);
						Item thirdItem = ingredients.get(2);
						Item fourthItem = ingredients.get(3);
						
						//return the incorrect items so the user can try again
						db.addItemToInventory(firstItem, userID);
						db.addItemToInventory(secondItem, userID);
						db.addItemToInventory(thirdItem, userID);
						db.addItemToInventory(fourthItem, userID);
						
						//remove items from cauldron
						db.removeItemFromRoom(firstItem, roomID);
						db.removeItemFromRoom(secondItem, roomID);
						db.removeItemFromRoom(thirdItem, roomID);
						db.removeItemFromRoom(fourthItem, roomID);
						//message telling the user they were not successful
						message = "The ingredients added did not seem to do anything";
					}
				}
				
		return message;
	}

	public String getSelectedMessage(String itemName, int userID) {
		String message = "You found a " + itemName;
		if(itemName.equals("Untitled Book")) {
			System.out.println("untitled book text");
			message = "You found a book of spells. Most of the pages are blank or damaged. <br> " +
			"Page 1: A l_ _ ht sp_l_ pro_ec_ _ the i_g_ed_ _ nt_ <br> Page 2: Potion of S_ _ ed: fea_he_, l_me j_ _c_, c _ _ _ _ r, c_ove_, b_u_ fl_ _ e_     " +
					" <br> Page 3: Potio_  o_  T_l_ _n_:  H_ir o_ th_ an_ _ _l, l_ck_ c_ov_ _, w_shb_ _ _, li_ _ ju_ _ _ ";
			
		}else if(itemName.equals("Jar of Cat Hairs") || itemName.equals("Jar with Hibiscus") || 
				itemName.equals("Clover") || itemName.equals("Wishbone")|| itemName.equals("Lime Juice")
				|| itemName.equals("Unlit Candle") || itemName.equals("Lit Candle")||
				itemName.equals("Empty Potion Bottle") || itemName.equals("Fire Spinning Book")){
			Boolean canBePickedUp = db.getCanBePickedUp(userID, itemName);
			if(canBePickedUp == false) {
				message = "You found a " + itemName + ". It seems to be stuck to the shelf";
			}
		}else if(itemName.equals("Messy")) {
			//TODO:insert if statements to check if messy can talk and has been fed
			//if messy cannot talk yet and he has not been fed, this is the output
			message = "You found Zeller's cat, Messy. Messy stares at you";
			//if messy cannot talk and has been fed, this is the output
			message = "YOu found Zeller's cat, Messy. Messy nudges your hand";
			//if messy can talk but has not been fed he will give bad advice
			message = "Messy: [insert bad advice]";
			//if messy can talk and has been fed he will give the player a hint
			message = "Messy: [insert hint]";
			
		}else if(itemName.equals("Comic Stand")) {
			message = "You found a Comic Stand that displays Zeller's favorite comics";
		}
		return message;
	}
	
	public Objective getCurrentObjective(List<Objective> objectives) {
		for(Objective i : objectives) {
			if(i.getIsStarted() && !i.getIsComplete()) {
				return i;
			}
		}
		return null;
		
	}
	
	public void startNextObjective(List<Objective> objectives) {
		for(int i = 0; i < objectives.size();i++) {
			if(objectives.get(i).getIsStarted() && objectives.get(i).getIsComplete()) {
				if(i+1 < objectives.size()) {
					Objective nextObjective = objectives.get(i+1);
					db.changeObjectiveIsStarted(nextObjective.getObjectiveID(), true);
					for(Task task : nextObjective.getTasks()) {
						db.changeTaskIsStarted(task.getTaskID(), true);
					}
				}
			}
		}
	}
	
	public void markObjectiveAsComplete(int objectiveID) {
		//mark the objective as complete
		db.changeObjectiveIsComplete(objectiveID, true);
	}
	
	public List<Task> getTasksFromObjectiveID(int objectiveID) {
		return db.getTasksByObjID(objectiveID);
	}
	public List<Objective> getObjectivesFromUserID(int userID) {
		int roomID = db.findRoomIDByUserID(userID);
		return db.getObjectivesByRoomID(roomID);
	}
}