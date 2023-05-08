package edu.ycp.cs320.project.controller;

import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.IDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
	public String useItem(Item item, Item selected,int userID, int objectiveID) {
		String message = "Nothing Happened";
		if(item.getName().equals("Empty Potion Bottle")) {
			message = db.useEmptyPotion(item, selected, model.getUser());
		}else if(item.getName().equals("Matches")) {
			message = db.useMatches(item, selected, model.getUser());
		}else if(item.getName().equals("Bag of Meow Mix")) {
			message = useMeowMix(selected.getName(), userID, objectiveID);
		}else if(item.getName().equals("Full Potion Bottle")) {
			message = useFullPotionBottle(selected.getName(), userID, objectiveID);
		}else if(selected.getName().equals("Empty Cauldron")){
			message = usePotionIngredient(item.getName(), userID, objectiveID);
		}else if(item.getName().equals("Hammer")) {
			message = useHammer(selected.getName(), userID);
		}else if(item.getName().equals("Lit Candle") && selected.getName().equals("Fire Alarm")) {
			message = "Weird. The fire alarm doesn't go off";
		}else if(item.getName().equals("Hammer")) {
			message = useHammer(selected.getName(), userID);
		}else if(item.getName().equals("Puzzle Piece 1") || item.getName().equals("Puzzle Piece 2") ||
				item.getName().equals("Puzzle Piece 3") || item.getName().equals("Puzzle Piece 4") && selected.getName().equals("Puzzle Board")) {
			message = usePuzzlePiece(item.getName(), userID, objectiveID);
		}else if(item.getName().equals("Key")) {
			message = useKey(selected.getName(), userID);
		}
		return message;
	}
	
	public String usePuzzlePiece(String itemName, int userID, int objectiveID) {
		String message = "You do not have all of the pieces yet. It's best to come back later";
		Item itemToAdd = db.findItemByNameAndIDInInv(itemName, userID);
		db.removeItemFromInventory(itemToAdd, userID);
		int taskID = db.getTaskIDByNameAndObjectiveID("Puzzle", objectiveID);
		if(db.addItemToTask(itemToAdd, taskID)) {
			message = "Piece was added to the board";
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
	
	public String useFullPotionBottle(String selectedName, int userID, int objectiveID) {
		String message = "Nothing happened";
		if(selectedName.equals("Messy")) {
			int taskID = db.getTaskIDByNameAndObjectiveID("Cat", objectiveID);
			db.addItemToTask(db.findItemByNameAndIDInInv("Full Potion Bottle", userID), taskID);
			db.removeItemFromInventory(db.findItemByNameAndIDInInv("Full Potion Bottle", userID), userID);
			message = "You gave Messy the potion. Messy can now talk <br> Messy: What are you doing here?";
		}
		return message;
	}
	
	public String useMeowMix(String selectedName, int userID, int objectiveID) {
		String message = "Nothing happened";
		if(selectedName.equals("Messy")) {
			//find the item in the inventory
			Item itemToAdd = db.findItemByNameAndIDInInv("Bag of Meow Mix", userID);
			//get the task id for the cat task
			int taskID = db.getTaskIDByNameAndObjectiveID("Cat", objectiveID);
			//put in usedItems
			db.addItemToTask(itemToAdd, taskID);
			//remove the item from inv
			db.removeItemFromInventory(itemToAdd, userID);
			message = "You gave Messy the Meow Mix. He seems to enjoy it!";
		}
		return message;
	}
	
	
	public String usePotionIngredient(String itemName, int userID, int objectiveID) {
		String message = "Item could not be added";
		Item itemToAdd = db.findItemByNameAndIDInInv(itemName, userID);
		db.removeItemFromInventory(itemToAdd, userID);
		int taskID = db.getTaskIDByNameAndObjectiveID("PotionMachine", objectiveID);
		if(db.addItemToTask(itemToAdd, taskID)) {
			message = "Item was placed in cauldron";
		}
		return message;
	}
	
	public String useHammer(String selectedName, int userID) {
		System.out.println("Using hammer");
		if(selectedName.equals("Fire Alarm")) {
			System.out.println("Using hammer...");
			int roomID = db.findRoomIDByUserID(userID);
			Item itemToRemove = db.findItemByNameAndIDInRoom("Fire Alarm", roomID);
			db.removeItemFromRoom(itemToRemove, roomID);
			itemToRemove = db.findItemByNameAndIDInInv("Hammer", userID);
			db.removeItemFromInventory(itemToRemove, userID);
			db.addItemToInventory(new Item("Key", true, 0,0,0), userID);
			return "The Fire alarm broke and you found a key";
		}
		return "Nothing happened";
	}
	
	public String useKey(String selectedName, int userID) {
		if(selectedName.equals("Window")) {
			int roomID = db.findRoomIDByUserID(userID);
			Item itemToRemove = db.findItemByNameAndIDInRoom("Window", roomID);
			db.removeItemFromRoom(itemToRemove, roomID);
			itemToRemove = db.findItemByNameAndIDInInv("Key", userID);
			db.removeItemFromInventory(itemToRemove, userID);

			return "You used the key to escape";
		}
		return "Nothing happened";
	}

	public String getSelectedMessage(String itemName, int userID, int objectiveID) {
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
			List<Item> usedItems = db.getUsedItemsByTaskId(db.getTaskIDByNameAndObjectiveID("Cat", objectiveID));
			int taskID = db.getTaskIDByNameAndObjectiveID("Cat", objectiveID);
			//TODO: figure out why this is not firing
			taskID = db.getTaskIDByNameAndObjectiveID("Bookshelf", objectiveID);
			if(taskID != -1) {
				return "Messy: I think the first comic had the X-Men. The seconds two are the same series, and the last one is Spiderman";
			}
			if(taskID == -1) {
				return "Messy: The password? I think it was the last four digits of some phone number";
			}
			
			Boolean isFed = false;
			Boolean canTalk = false;
			for(Item item : usedItems) {
				if(item.getName().equals("Bag of Meow Mix")) {
					isFed = true;
				}
				if(item.getName().equals("Full Potion Bottle")) {
					canTalk = true;
				}
			}
			
			if(!isFed && !canTalk) {
				//if messy cannot talk yet and he has not been fed, this is the output
				message = "You found Zeller's cat, Messy. Messy stares at you";
			}else if(isFed && !canTalk) {
				//if messy cannot talk and has been fed, this is the output
				message = "You found Zeller's cat, Messy. Messy nudges your hand";
			}else if(!isFed && canTalk) {
				//if messy can talk but has not been fed he will give bad advice
				message = "Messy: The password? The password is 1234.";
			}else {
				//if messy can talk and has been fed he will give the player a hint
				message = "Messy: The password? I think it was the last four digits of some phone number";
			}
		}else if(itemName.equals("Comic Stand")) {
			message = "You found a Comic Stand that displays Zeller's favorite comics";
		}else if(itemName.equals("Amazing Spiderman 300 Comic") || itemName.equals("Avengers 1 Comic")
				|| itemName.equals("Avengers 4 Comic") | itemName.equals("Fantastic Four 48 Comic") ||
				itemName.equals("Fantastic Four 9 Comic") || itemName.equals("Giant Size Xmen 1 Comic") ||
				itemName.equals("Superman 18 Comic") || itemName.equals("X-Men 1 Comic") ||
				itemName.equals("X-Men 94 Comic")) {
			updateSelectedComics(itemName, userID, objectiveID);
		}
		return message;
	}
	
	public Boolean updateSelectedComics(String itemName, int userID, int objectiveID) {
		//get the task id
		int taskID = db.getTaskIDByNameAndObjectiveID("Bookshelf", objectiveID);
		//check if the task is started
		if(taskID!=-1) {
			db.addItemToTask(db.findItemByNameAndIDInRoom(itemName, db.findRoomIDByUserID(userID)), taskID);
			return true;
		}else {
			return false;
		}
		
	}
	
	public Objective getCurrentObjective(List<Objective> objectives) {
		for(Objective i : objectives) {
			System.out.println("Objective: " + "is Started: " + i.getIsStarted() + " isComplete: " + i.getIsComplete());
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
					System.out.println("Starting next objective");
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
	
	public Boolean verifyPasscode(String passcode, User user) {
		// return false if passcode contains anything other than numbers
		Pattern pattern = Pattern.compile("^[0-9]+$");
		if(!pattern.matcher(passcode).matches()) {
			return false;
		}else if(pattern.matcher(passcode).matches() && passcode.equals("6651")) {
			Item lockedComicCase = db.findItemByNameAndIDInRoom("Locked Comic Stand", user.getRoom().getRoomID());
			Item ComicCase = new Item("Comic Stand", false, lockedComicCase.getXPosition(),lockedComicCase.getYPosition(),
					user.getRoom().getUserPosition());
			
			db.addItemToRoom(ComicCase, user.getRoom().getRoomID());
			db.swapItemInRoom(lockedComicCase, ComicCase, user);
		
			db.addItemToRoom(new Item("X-Men 1 Comic", false, 18, 698, 1), user.getRoom().getRoomID());
			db.addItemToRoom(new Item("Avengers 1 Comic", false, 6, 782, 1), user.getRoom().getRoomID());
			db.addItemToRoom(new Item("Avengers 4 Comic", false, 7, 867, 1), user.getRoom().getRoomID());
			db.addItemToRoom(new Item("Superman 18 Comic", false, 90, 698, 1), user.getRoom().getRoomID());
			db.addItemToRoom(new Item("Fantastic Four 48 Comic", false, 63, 783, 1), user.getRoom().getRoomID());
			db.addItemToRoom(new Item("Fantastic Four 9 Comic", false, 70, 866, 1), user.getRoom().getRoomID());
			db.addItemToRoom(new Item("X-Men 94 Comic", false, 128, 702, 1), user.getRoom().getRoomID());
			db.addItemToRoom(new Item("Amazing Spiderman 300 Comic", false, 131, 869, 1), user.getRoom().getRoomID());
			db.addItemToRoom(new Item("Giant Size X-Men 1 Comic", false, 121, 788, 1), user.getRoom().getRoomID());
			
			return true;
		}else {
			return false;
		}
			
	}

	public List<Task> getTasksFromObjectiveID(int objectiveID) {
		return db.getTasksByObjID(objectiveID);
	}
	public List<Objective> getObjectivesFromUserID(int userID) {
		int roomID = db.findRoomIDByUserID(userID);
		return db.getObjectivesByRoomID(roomID);
	}
	
	public void updateTime(int userID, int time) {
		db.updateTime(userID, time);
	}
}