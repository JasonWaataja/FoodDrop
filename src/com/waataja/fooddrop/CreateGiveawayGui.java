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
