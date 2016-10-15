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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class DonatorGui {
	
	public static final int FIELD_LENGTH = 20;
	
	// Map of names to Giveaway.
	private HashMap<String, Giveaway> giveaways;
	
	public static void main(String[] args) {
		DonatorGui gui = new DonatorGui();
		gui.show(); 
		/*String name = CreateGiveawayGui.createGiveawayDialog(new Giveaway(null, null, null, null, null));
		System.out.println(name);*/
	}
	
	public static final String WINDOW_TITLE = "Donate Food";
	
	private JFrame mainWindow;
	private DefaultListModel<String> listModel;
	private JList<String> giveawayView;
	private JScrollPane scrollPane;
	
	private JPanel buttonsPanel;
	private JButton addButton;
	
	private JPanel infoPanel;
	private JTextField nameField;
	private JTextField addressField;
	private JCheckBox foodbankBox;
	private JCheckBox peopleBox;
	
	private FoodDonator donator;
	
	private JButton sendButton;
	
	public DonatorGui() {
		giveaways = new HashMap<String, Giveaway>();
		donator = new FoodDonator("Test Name", "Test Address", "Test Description");
		
		mainWindow = new JFrame();
		mainWindow.setTitle(WINDOW_TITLE);
		mainWindow.setSize(960, 540);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		listModel = new DefaultListModel<String>();
		
		giveawayView = new JList<String>(listModel);
		giveawayView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		giveawayView.setLayoutOrientation(JList.VERTICAL);
		
		scrollPane = new JScrollPane(giveawayView);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mainWindow.add(BorderLayout.CENTER, scrollPane);
		
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		//mainWindow.add(BorderLayout.SOUTH, buttonsPanel);
		DonatorGui self = this;
		
		addButton = new JButton("Add Giveaway");
		addButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean acceptPeople = peopleBox.isSelected();
				boolean acceptFoodbank = foodbankBox.isSelected();
				Giveaway.GiveawayType type = Giveaway.GiveawayType.ANY;
				if (!(acceptPeople ^ acceptFoodbank)) {
					type = Giveaway.GiveawayType.ANY;
				} else {
					if (acceptPeople)
						type = Giveaway.GiveawayType.PEOPLE;
					if (acceptFoodbank)
						type = Giveaway.GiveawayType.FOODBANK;
				}
				
				updateDonator();
				Giveaway giveaway = new Giveaway(self.donator, new GregorianCalendar(2016, 10, 14), new GregorianCalendar(2016, 10, 15), type, "Currently Available");
				CreateGiveawayGui.createGiveawayDialog(giveaway, self);
				self.updateList();
			}});
		//buttonsPanel.add(addButton);
		mainWindow.add(BorderLayout.SOUTH, addButton);
		
		infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		nameField = new JTextField(FIELD_LENGTH);
		nameField.setText("name");
		addressField = new JTextField(FIELD_LENGTH);
		addressField.setText("address");
		foodbankBox = new JCheckBox("Accept food banks");
		peopleBox = new JCheckBox("Accept people");
		infoPanel.add(nameField);
		infoPanel.add(addressField);
		infoPanel.add(foodbankBox);
		infoPanel.add(peopleBox);
		mainWindow.add(BorderLayout.EAST, infoPanel);
		
		sendButton = new JButton("Send to server");
		sendButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				sendGiveawaysToServer();
			}
			
		});
		mainWindow.add(BorderLayout.WEST, sendButton);
	}
	
	public void updateList() {
		listModel.clear();
		for (String name : giveaways.keySet()) {
			listModel.addElement(name);
		}
	}
	
	public void show() {
		mainWindow.setVisible(true);
	}

	public HashMap<String, Giveaway> getGiveaways() {
		return giveaways;
	}

	public void setGiveaways(HashMap<String, Giveaway> giveaways) {
		this.giveaways = giveaways;
	}
	
	public void updateDonator() {
		donator.setName(nameField.getText());
		donator.setAddress(addressField.getText());
	}
	
	private void sendGiveawaysToServer() {
		for (Giveaway giveaway : giveaways.values()) {
			System.out.println(giveaway);
			boolean success = ObjectSender.addGiveaway(giveaway);
			System.out.println(success);
		}
	}
}
