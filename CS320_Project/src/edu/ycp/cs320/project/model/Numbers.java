package edu.ycp.cs320.project.model;

public class Numbers {
	private double first, second, third, result;
	
	public Numbers(double first, double second, double third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	public void setFirst(double first) {
		this.first = first;
	}
	
	public double getFirst() {
		return first;
	}
	
	public void setSecond(double second) {
		this.second = second;
	}
	
	public double getSecond() {
		return second;
	}
	
	public void setThird(double third) {
		this.third = third;
	}
	
	public double getThird() {
		return third;
	}
	
	public double getResult() {
		return result;
	}
	
	public void setAddition() {
		result = first + second + third;
	}
	
	public void setMultiplication() {
		result = first * second;
	}
	
}
