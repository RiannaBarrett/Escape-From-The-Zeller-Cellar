package edu.ycp.cs320.project.sqldemos;

import java.util.List;
import java.util.Scanner;

import edu.ycp.cs320.project.controller.InitDatabase;
import edu.ycp.cs320.project.model.*;
import edu.ycp.cs320.project.persist.DatabaseProvider;
import edu.ycp.cs320.project.persist.IDatabase;

public class PotionQuery {
	public static void main(String[] args) throws Exception {
		Scanner keyboard = new Scanner(System.in);

		// Create the default IDatabase instance
		InitDatabase.init(keyboard);
		String username = "tester1";
		
		// get the DB instance and execute transaction
		IDatabase db = DatabaseProvider.getInstance();
		User user = db.findUserByName(username);
		Item cauldron = new Item("Empty Cauldron", false, 100, 100, 0);
		Item catHairs = new Item("Jar of Cat Hairs", false, 100, 100, 0);
		Item clover = new Item("Clover", false, 100, 100, 0);
		Item wishbone = new Item("Wishbone", false, 100, 100, 0);
		Item limeJuice = new Item("Carton of Lime Juice", false, 100, 100, 0);
		
		int userID = user.getUserID();
		int roomID = user.getRoom().getRoomID();
		//add the items to use to inventory
		db.addItemToInventory(catHairs, userID);
		db.addItemToInventory(clover, userID);
		db.addItemToInventory(wishbone, userID);
		db.addItemToInventory(limeJuice, userID);
		//add the empty cauldron to the room
		db.addItemToRoom(cauldron, roomID);
		//refresh user
		user = db.findUserByName(username);
		//get the items back from the refreshed users
		for(int i = 0; i<user.getRoom().getItems().size();i++) {
			if(user.getRoom().getItems().get(i).getName().equals("Empty Cauldron"));
			cauldron = user.getRoom().getItems().get(i);
		}
		
		cauldron = user.getRoom().getItems().get(user.getRoom().getItems().size()-1);
		catHairs = user.getInventory().get(user.getInventory().size() - 4);
		clover = user.getInventory().get(user.getInventory().size() - 3);
		wishbone = user.getInventory().get(user.getInventory().size() - 2);
		limeJuice = user.getInventory().get(user.getInventory().size() - 1);
		//print the result message
		String message = db.usePotionIngredient(catHairs, cauldron, user);
		System.out.println(message);
		//refresh the userS
		user = db.findUserByName(username);
		message = db.usePotionIngredient(clover, cauldron, user);
		//print result message
		user = db.findUserByName(username);
		message = db.usePotionIngredient(wishbone, cauldron, user);
		
		user = db.findUserByName(username);
		message = db.usePotionIngredient(limeJuice, cauldron, user);
		System.out.println(message);
		user = db.findUserByName(username);
		
		// check if anything was returned and output the list
		if (user == null) {
			System.out.println("No users found with name: <" + username + ">");
		}
		else {
			System.out.println("User found!");
			System.out.println("ID: \t \t \t" + user.getUserID());
			System.out.println("Username: \t \t" + user.getUsername());
			System.out.println("Password: \t \t" + user.getPassword());
			System.out.println("Inventory: ");
			for (Item item : user.getInventory()) {
				System.out.println("\t Item ID: \t \t" + item.getItemID());
				System.out.println("\t Item Name: \t \t" + item.getName());
				System.out.println("\t Can be picked up?: \t" + item.getCanBePickedUp());
				System.out.println("\t X Position: \t \t" + item.getXPosition());
				System.out.println("\t Y Position: \t \t" + item.getYPosition());
				System.out.println("\t Room Position: \t \t" + item.getRoomPosition());
				System.out.println("");
			}
			System.out.println("Room ID: \t \t" + user.getRoom().getRoomID());
			System.out.println("User Position: \t \t" + user.getRoom().getUserPosition());
			System.out.println("Room Inventory: ");
			for (Item item : user.getRoom().getItems()) {
				System.out.println("\t Item ID: \t \t" + item.getItemID());
				System.out.println("\t Item Name: \t \t" + item.getName());
				System.out.println("\t Can be picked up?: \t" + item.getCanBePickedUp());
				System.out.println("\t X Position: \t \t" + item.getXPosition());
				System.out.println("\t Y Position: \t \t" + item.getYPosition());
				System.out.println("\t Room Position: \t \t" + item.getRoomPosition());
				System.out.println("");
			}
		}
	}
}
