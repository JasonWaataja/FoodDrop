package com.waataja.fooddrop;

public class FoodDonator {
	
	private String name;
	
	private String address;
	
	
	public FoodDonator(String name, String address, String description) {
		super();
		this.name = name;
		this.address = address;
		this.description = description;
	}

	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	
}
