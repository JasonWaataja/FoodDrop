/*
 * Copyright (c) 2016, Jason Waataja
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *    
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *    
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
				System.out.println("Established connection");
				try {
					ObjectInputStream reader = new ObjectInputStream(sock.getInputStream());
					FoodMessage msg = (FoodMessage) reader.readObject();
					//reader.close();
					if (msg.getType() == MessageType.REQUEST) {
						FoodReceiver receiver = (FoodReceiver) msg.getObject();
						ArrayList<Giveaway> giveaways = new ArrayList<Giveaway>();
						if (receiver.getType() == ReceiverType.PERSON) {
							for (int i = database.getGiveaways().size() - 1; i >= 0; i--)
								if (database.getGiveaways().get(i).getType() == GiveawayType.ANY || 
										database.getGiveaways().get(i).getType() == GiveawayType.PEOPLE)
									giveaways.add(database.getGiveaways().get(i));
						} else {
							for (int i = giveaways.size() - 1; i >= 0; i--)
								if (database.getGiveaways().get(i).getType() == GiveawayType.ANY || 
										database.getGiveaways().get(i).getType() == GiveawayType.FOODBANK)
									giveaways.add(database.getGiveaways().get(i));
						}
						giveaways = LatLong.sortAll(receiver, giveaways);
						giveaways = new ArrayList<Giveaway>(giveaways.subList(0, Math.min(5, giveaways.size())));
						ObjectOutputStream writer = new ObjectOutputStream(sock.getOutputStream());
						writer.writeObject(new FoodMessage(MessageType.RETURN, giveaways));
						writer.flush();
						//writer.close();
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
