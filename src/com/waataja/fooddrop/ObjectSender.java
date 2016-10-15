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
