package edu.ycp.cs320.project.controller;

import edu.ycp.cs320.project.model.Numbers;

public class NumbersController {
	
	private Numbers model;
	
	public void setModel(Numbers model) {
		this.model = model;
	}
	
	public void add() {
		model.setAddition();
	}
	
	public void multiply() {
		model.setMultiplication();
	}
}
