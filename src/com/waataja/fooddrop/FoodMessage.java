package com.waataja.fooddrop;

import java.io.Serializable;

public class FoodMessage implements Serializable {
	
	static final long serialVersionUID = 944837719;

	public enum MessageType {
		REQUEST, ADD, RETURN
	}
	
	private MessageType type;
	private Object obj;
	
	public FoodMessage(MessageType type, Object obj) {
		super();
		this.type = type;
		this.obj = obj;
	}
	
	public MessageType getType() {
		return type;
	}
	
	public Object getObject() {
		return obj;
	}
}
