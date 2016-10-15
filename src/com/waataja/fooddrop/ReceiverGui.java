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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

public class ReceiverGui {
	private JTextArea tf;
	private JTextField north;
	private JButton b;
	private JComboBox<String> comboBox;
	private FoodReceiver receiver;
	
	public ReceiverGui() {
		receiver = new FoodReceiver(FoodReceiver.ReceiverType.PERSON, 0, 0);
		Panel p = new Panel();
		p.setLayout(new BorderLayout());
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		north = new JTextField("My location");
		p.add(northPanel,BorderLayout.NORTH);
		northPanel.add(north);
		tf = new JTextArea("");		
		tf.setEditable(false);
		p.add(tf, BorderLayout.CENTER);
		b = new JButton("Get giveaways");
		b.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateReceiver();
				List<Giveaway> giveaways = ObjectSender.getGiveaways(receiver);
				String giveawayText = "";
				for (Giveaway giveaway : giveaways) {
					String itemsText = giveaway.getDonator().getName() + "\n";
					itemsText += giveaway.getDonator().getAddress() + "\n";
					List<FoodItem> items = giveaway.getItems();
					for (FoodItem item : items) {
						itemsText += "\t" + item.getName() + ": " + item.getAmount() + "\n";
					}
					giveawayText += itemsText;
				}
				tf.setText(giveawayText);
				
			}
			
		});
		
		comboBox = new JComboBox<String>();
		comboBox.addItem("I am a person");
		comboBox.addItem("I am a foodbank");
		//p.add(BorderLayout.WEST, comboBox);
		northPanel.add(comboBox);
		p.add(b, BorderLayout.SOUTH);
		JFrame j = new JFrame();
		j.setContentPane(p);
		j.setSize(960, 540);
		j.setVisible(true);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ReceiverGui gui = new ReceiverGui();
	}
	
	private void updateReceiver() {
		int index = comboBox.getSelectedIndex();
		FoodReceiver.ReceiverType type = FoodReceiver.ReceiverType.PERSON;
		if (index == 1) {
			type = FoodReceiver.ReceiverType.FOODBANK;
		}
		receiver.setType(type);
		LatLong location = new LatLong(north.getText());
		receiver.setLatitude(location.getLat());
		receiver.setLongitude(location.getLon());
		
	}

}
	
