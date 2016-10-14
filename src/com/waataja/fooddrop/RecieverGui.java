package com.waataja.fooddrop;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class RecieverGui {
	private static JTextField tf;
	private static JTextField north;
	private static JButton b;
	public RecieverGui() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Panel p = new Panel();
		p.setLayout(new BorderLayout());
		north = new JTextField("North");
		p.add(north,BorderLayout.NORTH);
		tf = new JTextField("Middle");		
		p.add(tf, BorderLayout.CENTER);
		b = new JButton("South");
		b.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				tf.setText("");
				north.setText("");
				
			}
			
		});
		p.add(b, BorderLayout.SOUTH);
		JFrame j = new JFrame();
		j.setContentPane(p);
		j.setSize(1600, 1200);
		j.setVisible(true);
	}

}
	
