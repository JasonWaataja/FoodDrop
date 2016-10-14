package com.waataja.fooddrop;

public class FoodReceiver {
	
	public enum ReceiverType {
		PERSON, FOODBANK
	}
	
	private ReceiverType type;
	private LatLong l;
	
	public FoodReceiver(ReceiverType type, double latitude, double longitude) {
		super();
		this.type = type;
		this.l = new LatLong(latitude, longitude);
	}
	
	public ReceiverType getType() {
		return type;
	}
	public void setType(ReceiverType type) {
		this.type = type;
	}
	public double getLatitude() {
		return l.getLat();
	}
	public void setLatitude(double latitude) {
		l.setLat(latitude);
	}
	public double getLongitude() {
		return l.getLon();
	}
	public void setLongitude(double longitude) {
		l.setLon(longitude);
	}
	public LatLong getLatLong() {
		return l;
	}
}
