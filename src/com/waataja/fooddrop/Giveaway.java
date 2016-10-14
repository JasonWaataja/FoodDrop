package com.waataja.fooddrop;

import java.util.Date;
import java.util.List;


public class Giveaway {
	
	private FoodDonator donator;
	
	public enum GiveawayType {
		ANY, FOODBANK, PEOPLE
	}

	public Giveaway(FoodDonator donator, Date start, Date end, GiveawayType type, String availability) {
		super();
		this.donator = donator;
		this.start = start;
		this.end = end;
		this.type = type;
		this.availability = availability;
	}
	
	public FoodDonator getDonator() {
		return donator;
	}

	public void setDonator(FoodDonator donator) {
		this.donator = donator;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public GiveawayType getType() {
		return type;
	}

	public void setType(GiveawayType type) {
		this.type = type;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	private Date start;
	private Date end;
	
	private GiveawayType type;
	
	private String availability;
}
