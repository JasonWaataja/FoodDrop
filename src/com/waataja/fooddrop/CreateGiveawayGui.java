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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CreateGiveawayGui implements ActionListener {

	private JFrame frame;
	private JTextField nameField;
	private JTextArea itemsArea;
	private JButton confirmButton;
	private DonatorGui donatorGui;
	private Giveaway giveawayToModify;

	public CreateGiveawayGui() {
		frame = new JFrame();
		frame.setSize(200, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Create Giveaway");
		frame.setVisible(true);

		nameField = new JTextField();
		nameField.setText("Name of giveaway");
		frame.add(BorderLayout.NORTH, nameField);
		itemsArea = new JTextArea();
		frame.add(BorderLayout.CENTER, itemsArea);
		confirmButton = new JButton("Confirm");
		frame.add(BorderLayout.SOUTH, confirmButton);
		confirmButton.addActionListener(this);
	}

	// Returns name.
	public static void createGiveawayDialog(Giveaway giveawayToModify, DonatorGui donatorGui) {
		CreateGiveawayGui gui = new CreateGiveawayGui();
		gui.giveawayToModify = giveawayToModify;
		gui.donatorGui = donatorGui;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String areaText = itemsArea.getText();
		String[] lines = areaText.split("\n");
		List<FoodItem> items = new ArrayList<FoodItem>();
		for (int i = 0; i < lines.length; i++) {
			if (!lines[i].equals("")) {
				String[] words = lines[i].split(" ");
				FoodItem item = new FoodItem(words[0], "", Integer.parseInt(words[1]));
				items.add(item);
			}
		}

		giveawayToModify.setItems(items);
		String name = nameField.getText();
		frame.setVisible(false);
		frame.dispose();
		
		donatorGui.getGiveaways().put(name, giveawayToModify);
		donatorGui.updateList();
	}
}
