package com.waataja.fooddrop;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.waataja.fooddrop.FoodMessage.MessageType;
import com.waataja.fooddrop.FoodReceiver.ReceiverType;
import com.waataja.fooddrop.Giveaway.GiveawayType;

import us.noop.data.BigData;

public class FoodDropServer {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		BigData database = new BigData(new File("giveaways"));
		ServerSocket servSock = null;
		try {
			servSock = new ServerSocket(8729, 5);
		} catch (IOException e) {
			System.out.println("Unable to create socket");
			e.printStackTrace();
			System.exit(1);
		}
		try {
			servSock.setReuseAddress(true);
		} catch (SocketException e1) {
			System.out.println("Unable to set socket properties.");
			e1.printStackTrace();
			System.exit(1);;
		}
		while (true) {
			try {
				Socket sock = servSock.accept();
				sock.setSoTimeout(10000);
				try {
					ObjectInputStream reader = new ObjectInputStream(sock.getInputStream());
					FoodMessage msg = (FoodMessage) reader.readObject();
					reader.close();
					if (msg.getType() == MessageType.REQUEST) {
						FoodReceiver receiver = (FoodReceiver) msg.getObject();
						ArrayList<Giveaway> giveaways = new ArrayList<Giveaway>();
						if (receiver.getType() == ReceiverType.PERSON) {
							for (int i = database.getGiveaways().size() - 1; i >= 0; i--)
								if (database.getGiveaways().get(i).getType() == GiveawayType.ANY || database.getGiveaways().get(i).getType() == GiveawayType.PEOPLE)
									giveaways.add(database.getGiveaways().get(i));
						} else {
							for (int i = giveaways.size() - 1; i >= 0; i--)
								if (database.getGiveaways().get(i).getType() == GiveawayType.ANY || database.getGiveaways().get(i).getType() == GiveawayType.FOODBANK)
									giveaways.add(database.getGiveaways().get(i));
						}
						giveaways = LatLong.sortAll(receiver, giveaways);
						giveaways = (ArrayList<Giveaway>) giveaways.subList(0, Math.min(5, giveaways.size()));
						FoodMessage giveList = new FoodMessage(MessageType.RETURN, giveaways);
						ObjectOutputStream writer = new ObjectOutputStream(sock.getOutputStream());
						writer.writeObject(giveList);
						writer.flush();
						writer.close();
					} else if (msg.getType() == MessageType.ADD) {
						Giveaway giveaway = (Giveaway) msg.getObject();
						database.getGiveaways().add(giveaway);
						database.saveGiveaways();
					} else {
						System.out.println("Illegal message type");
					}
					sock.close();
				} catch (IOException e) {
					sock.close();
					System.out.println("Connection IO error");
					e.printStackTrace();
					continue;
				} catch (ClassNotFoundException e) {
					sock.close();
					System.out.println("Connection message error");
					e.printStackTrace();
				}
			} catch (IOException e) {
				System.out.println("Error accepting connection");
				e.printStackTrace();
				continue;
			}
		}
	}

}
