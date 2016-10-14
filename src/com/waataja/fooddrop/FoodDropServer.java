package com.waataja.fooddrop;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import com.waataja.fooddrop.FoodMessage.MessageType;

public class FoodDropServer {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
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
			servSock.setSoTimeout(10000);
		} catch (SocketException e1) {
			System.out.println("Unable to set socket properties.");
			e1.printStackTrace();
			System.exit(1);;
		}
		while (true) {
			try {
				Socket sock = servSock.accept();
				try {
					ObjectInputStream reader = new ObjectInputStream(sock.getInputStream());
					FoodMessage msg = (FoodMessage) reader.readObject();
					reader.close();
					if (msg.getType() == MessageType.REQUEST) {
						FoodReceiver receiver = (FoodReceiver) msg.getObject();
						//Use reciever to get a list of giveaways
						//FoodMessage giveList = new FoodMessage(MessageType.RETURN, );
						//ObjectOutputStream writer = new ObjectOutputStream(sock.getOutputStream());
						//writer.writeObject(giveList);
						//writer.flush();
						//writer.close();
					} else if (msg.getType() == MessageType.ADD) {
						Giveaway giveaway = (Giveaway) msg.getObject();
						//Add to database here
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
