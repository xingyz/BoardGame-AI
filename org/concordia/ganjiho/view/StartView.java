package org.concordia.ganjiho.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.concordia.ganjiho.common.PlayerMode;

public class StartView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel banner;
	private JPanel pane;
	private JRadioButton manualMode;
	private JRadioButton AIMode1;
	private JRadioButton AIMode2;
	private ButtonGroup modes;
	private JButton start;
	private final static String TITLE ="Ganji-Ho";
	
	public StartView(){
		pane = new JPanel();
		banner = new JLabel("Welcome to Ganji-Ho",SwingConstants.CENTER);
		banner.setFont(new Font("SanSerif",Font.BOLD,20));
		manualMode = new JRadioButton("Player VS Player");
		AIMode1 = new JRadioButton("Player(White) VS AI");
		AIMode2 = new JRadioButton("AI(White) VS Player");
		modes = new ButtonGroup();
		start = new JButton("START");
		modes.add(manualMode);
		modes.add(AIMode1);
		modes.add(AIMode2);
		pane.setLayout(new FlowLayout());
		pane.add(manualMode);
		pane.add(AIMode1);
		pane.add(AIMode2);
		setLayout(new BorderLayout());
		add(banner,BorderLayout.NORTH);
		add(pane,BorderLayout.CENTER);
		add(start,BorderLayout.SOUTH);
		
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();
				JFrame frame = new JFrame(TITLE);
				GameMain ganjiho;
				if(manualMode.isSelected()){
					ganjiho = new GameMain(PlayerMode.MANUAL);  
				}else if(AIMode1.isSelected()){
					ganjiho = new GameMain(PlayerMode.AI_AFTER);
				}else{
					ganjiho = new GameMain(PlayerMode.AI_BEFORE);
				}
				 // Set the content-pane of the JFrame to an instance of main JPanel
	            frame.setContentPane(ganjiho);
	            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            frame.pack();
	            frame.setLocationRelativeTo(null); // center the application window
	            frame.setVisible(true);            // show it
			}
		});
		
	}
}
