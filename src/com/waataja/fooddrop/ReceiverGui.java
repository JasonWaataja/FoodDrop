package com.waataja.fooddrop;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import com.sun.xml.internal.bind.v2.runtime.Location;

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
	
