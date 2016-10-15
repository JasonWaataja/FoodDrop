package com.waataja.fooddrop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

import com.waataja.fooddrop.FoodMessage.MessageType;

public class ObjectSender {
	
	@SuppressWarnings("unchecked")
	public static List<Giveaway> getGiveaways(FoodReceiver reciever) {
		try {
			Socket sock = new Socket("www.psv3.org", 8729);
			try {
				ObjectOutputStream writer = new ObjectOutputStream(sock.getOutputStream());
				writer.writeObject(new FoodMessage(MessageType.REQUEST, reciever));
				writer.flush();
				//writer.close();
				ObjectInputStream reader = new ObjectInputStream(sock.getInputStream());
				FoodMessage msg = (FoodMessage) reader.readObject();
				List<Giveaway> giveaways = null;
				if (msg.getType() == MessageType.RETURN) {
					giveaways = (ArrayList<Giveaway>) msg.getObject();
				} else {
					System.out.println("Illegal message type");
				}
				//reader.close();
				sock.close();
				return giveaways;
			} catch (IOException e) {
				sock.close();
				System.out.println("Connection IO error");
				e.printStackTrace();
				return null;
			} catch (ClassNotFoundException e) {
				sock.close();
				System.out.println("Connection message error");
				e.printStackTrace();
				return null;
			}
		} catch (IOException e) {
			System.out.println("Error establishing connection");
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean addGiveaway(Giveaway giveaway) {
		try {
			Socket sock = new Socket("www.psv3.org", 8729);
			try {
				ObjectOutputStream writer = new ObjectOutputStream(sock.getOutputStream());
				writer.writeObject(new FoodMessage(MessageType.ADD, giveaway));
				writer.flush();
				writer.close();
				sock.close();
				return true;
			} catch (IOException e) {
				sock.close();
				System.out.println("Connection IO error");
				e.printStackTrace();
				return false;
			}
		} catch (IOException e) {
			System.out.println("Error establishing connection");
			e.printStackTrace();
			return false;
		}
	}

}
