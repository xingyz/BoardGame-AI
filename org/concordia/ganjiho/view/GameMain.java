package org.concordia.ganjiho.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.concordia.ganjiho.algorithm.*;
import org.concordia.ganjiho.model.*;
import org.concordia.ganjiho.common.*;
/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 */
@SuppressWarnings("serial")
public class GameMain extends JPanel {
   // Named-constants for the game board
   public static final int ROWS = 8;  // ROWS by COLS cells
   public static final int COLS = 8;
   public static final String TITLE = "Ganji-Ho";
 
   // Name-constants for the various dimensions used for graphics drawing
   public static final int CANVAS_WIDTH = 600;  // the drawing canvas
   public static final int CANVAS_HEIGHT = 600;
   public static final int CELL_SIZE = CANVAS_WIDTH/(ROWS); // cell width and height (square)
   public static final int GRID_WIDTH = 8;  // Grid-line's width
   public static final int GRID_WIDHT_HALF = GRID_WIDTH / 2; // Grid-line's half-width
   // Symbols (cross/nought) are displayed inside a cell, with padding from border
   public static final int CELL_PADDING = CELL_SIZE / 6;
   public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
   public static final int SYMBOL_STROKE_WIDTH = 8; // pen's stroke width
   
   // Player Mode (MANUAL,AI_AFTER,AI_BEFORE)
   public PlayerMode mode;
 
   private Board board;            // the game board
   private State currentState; // the current state of the game
   private Seed currentPlayer;     // the current player
   private JPanel notification;    // contains statusBar and nextMove
   private JLabel statusBar;       // for displaying status message
   private JPanel nextMove;		   // for specifying the next move's position
   private JTextField col;
   private JTextField row;
   private JButton button;
   private JButton restartButton;
 
   /** Constructor to setup the UI and game components */
   public GameMain(PlayerMode newMode) {
	   
	   mode = newMode;
      // This JPanel fires MouseEvent
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
            int mouseX = e.getX();
            int mouseY = e.getY();
            // Get the row and column clicked
            int rowSelected = mouseY / CELL_SIZE;
            int colSelected = mouseX / CELL_SIZE;
            displayCells(colSelected,rowSelected);
            repaint();
            // if mode is AI, then call minimax algorithm to decide next move for AI.
            // If two repaints are called closely, the first one will wait for the second one. So if the minimax
            // takes several seconds, we will not be able to see the placment of white tokens before minimax finishes
            // invokeLater() solves this problem
            SwingUtilities.invokeLater(new Runnable(){
            	public void run(){
            		if(currentState==State.PLAYING&&
                        	(mode== PlayerMode.AI_BEFORE&&currentPlayer==Seed.WHITE)||
                        	(mode== PlayerMode.AI_AFTER&&currentPlayer==Seed.BLACK)){
                    		int[] bestMove = GanjihoAlgorithms.minimax(board,mode,currentPlayer,6,Integer.MIN_VALUE,Integer.MAX_VALUE);
                    		displayCells(bestMove[2],bestMove[1]);
                        	repaint();
                        }
            	}
            });    	
         }
      });
 
      // Setup the status bar (JLabel) to display status message
      statusBar = new JLabel("         ");
      statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));
      statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
      statusBar.setOpaque(true);
      statusBar.setBackground(Color.LIGHT_GRAY);
      
      // Setup the bar for specifying the position for next move
      row = new JTextField(3);
      col = new JTextField(3);
      button = new JButton("PUT");
      restartButton = new JButton("RESTART");
      nextMove = new JPanel();
      nextMove.setLayout(new FlowLayout());
      nextMove.add(new JLabel("Row:"));
      nextMove.add(row);
      nextMove.add(new JLabel("Column:"));
      nextMove.add(col);
      nextMove.add(button);
      nextMove.add(restartButton);
      button.addActionListener(new ActionListener(){
    	  public void actionPerformed(ActionEvent e) {     
              String columnNumber = col.getText();
              String rowLetter = row.getText();
              // convert to char and int
              char letter = rowLetter.charAt(0);
              int r = letter - 'A';
              int c = Integer.valueOf(columnNumber)-1;
              if(r>=ROWS||r<0||c>=COLS||c<0){
            	  System.out.println("Error! Token placed out of board."+r+"   "+c);
            	  currentState = State.ERROR;
              }else{
              displayCells(c,r);
              }
              repaint();
              // If two repaints are called closely, the first one will wait for the second one. So if the minimax
              // takes several seconds, we will not be able to see the placment of white tokens before minimax finishes
              // invokeLater() solves this problem
              SwingUtilities.invokeLater(new Runnable(){
              	public void run(){
              		if(currentState==State.PLAYING&&
                          	(mode== PlayerMode.AI_BEFORE&&currentPlayer==Seed.WHITE)||
                          	(mode== PlayerMode.AI_AFTER&&currentPlayer==Seed.BLACK)){
                      		int[] bestMove = GanjihoAlgorithms.minimax(board,mode,currentPlayer,8,Integer.MIN_VALUE,Integer.MAX_VALUE);
                      		displayCells(bestMove[2],bestMove[1]);
                          	repaint();
                          }
              	}
              });    	
              
    	  }
      });
      restartButton.addActionListener(new ActionListener(){
    	  public void actionPerformed(ActionEvent e){
    		  initGame();
    		  repaint();
    	  }
      });
      
      // setup notification
      notification = new JPanel();
      notification.setLayout(new BorderLayout());
      notification.add(statusBar,BorderLayout.CENTER);
      notification.add(nextMove,BorderLayout.SOUTH);
      
      setLayout(new BorderLayout());
      add(notification,BorderLayout.PAGE_END);
      setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 30));
            // account for statusBar in height
 
      board = new Board();   // allocate the game-board
      initGame();  // Initialize the game variables
   }
 
   /**
    * set playermode
    */
   public void setPlayerMode(PlayerMode mode){
	   this.mode = mode;
   }
   /** Initialize the game-board contents and the current-state */
   public void initGame() {
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            board.getCells()[row][col].setContent(Seed.EMPTY); // all cells empty
         }
      }
      currentState = State.PLAYING;  // ready to play
      currentPlayer = Seed.WHITE;        // cross plays first
      if(mode == PlayerMode.AI_BEFORE){
    	  int[] bestMove = GanjihoAlgorithms.minimax(board,mode,currentPlayer,6,Integer.MIN_VALUE,Integer.MAX_VALUE);
  		  displayCells(bestMove[2],bestMove[1]);
  		  repaint();
      }
      
   }
 
   /** Update the currentState after the player with "theSeed" has placed on (row, col) */
   public void updateGame(Seed theSeed) {
	   currentState = GanjihoAlgorithms.findWinner(board, theSeed);
   }
   public void displayCells(int colSelected, int rowSelected){
	   if (currentState == State.PLAYING||currentState == State.ERROR) {
       	if(currentPlayer == Seed.WHITE){
       		if (rowSelected >= 0 && rowSelected+1 < ROWS
                       && colSelected >= 0 && colSelected < COLS
                       && board.getCells()[rowSelected][colSelected].getContent() == Seed.EMPTY
                       && board.getCells()[rowSelected+1][colSelected].getContent() == Seed.EMPTY) {
                    board.getCells()[rowSelected][colSelected].setContent(currentPlayer); // move
                    board.getCells()[rowSelected+1][colSelected].setContent(currentPlayer); 
                 }else{
               	  currentState = State.ERROR;
                 }
       	} 
       	else if(currentPlayer == Seed.BLACK){
       		if (rowSelected >= 0 && rowSelected < ROWS
                       && colSelected >= 0 && colSelected+1 < COLS
                       && board.getCells()[rowSelected][colSelected].getContent() == Seed.EMPTY
                       && board.getCells()[rowSelected][colSelected+1].getContent() == Seed.EMPTY) {
                    board.getCells()[rowSelected][colSelected].setContent(currentPlayer); // move
                    board.getCells()[rowSelected][colSelected+1].setContent(currentPlayer);
                 }else{
               	  currentState = State.ERROR;
                 }
       	}
       	if(currentState != State.ERROR){ 	
       		updateGame(currentPlayer);
       		
       	}System.out.println("!!!"+currentState);
           // Switch player
       	if(currentState==State.PLAYING)
       		currentPlayer = (currentPlayer == Seed.WHITE) ? Seed.BLACK : Seed.WHITE;	
       } else {        // game over
    	  initGame();
          System.out.println(currentState);
       }
	   
   }
   /** Custom painting codes on this JPanel */
   @Override
   public void paintComponent(Graphics g) {  // invoke via repaint()
      super.paintComponent(g);    // fill background
      setBackground(Color.WHITE); // set its background color
 
      board.paint(g);  // ask the game board to paint itself
 
      // Print status-bar message
      if (currentState == State.PLAYING) {
         statusBar.setForeground(Color.BLACK);
         if(mode == PlayerMode.AI_AFTER){
        	 if (currentPlayer == Seed.WHITE) {
                 statusBar.setText("Player's Turn (Vertical)");
              } else {
                 statusBar.setText("Computer's Turn (Horizontal). Wait...");
              }
         }else if(mode == PlayerMode.AI_BEFORE){
        	 if (currentPlayer == Seed.WHITE) {
                 statusBar.setText("Computer's Turn (Vertical). Wait...");
              } else {
                 statusBar.setText("Player's Turn (Horizontal).");
              }
         }else{
        	 if (currentPlayer == Seed.WHITE) {
                 statusBar.setText("White's Turn (Vertical)");
              } else {
                 statusBar.setText("Black's Turn (Horizontal)");
              }
         }
         
      } else if(currentState == State.ERROR){
    	  statusBar.setForeground(Color.RED);
    	  if(currentPlayer==Seed.WHITE)
    		  statusBar.setText("Error! Token Position Not Allowed. White Try Again");
    	  else if(currentPlayer ==Seed.BLACK)
    		  statusBar.setText("Error! Token Position Not Allowed. Black Try Again");
    	  currentState = State.PLAYING;
      } 
      else if (currentState == State.DRAW) {
         statusBar.setForeground(Color.RED);
         statusBar.setText("It's a Draw! Click to play again.");
      } else if (currentState == State.WHITE_WON) {
         statusBar.setForeground(Color.RED);
         if(mode == PlayerMode.AI_AFTER)
        	 statusBar.setText("'Player' Won! Click RESTART to play again.");
         else if(mode == PlayerMode.AI_BEFORE)
        	 statusBar.setText("'Computer' Won! Click RESTART to play again.");
         else 
        	 statusBar.setText("'White' Won! Click RESTART to play again.");
      } else if (currentState == State.BLACK_WON) {
         statusBar.setForeground(Color.RED);
         if(mode == PlayerMode.AI_AFTER)
        	 statusBar.setText("'Computer' Won! Click RESTART to play again.");
         else if(mode == PlayerMode.AI_BEFORE)
        	 statusBar.setText("'Player' Won! Click RESTART to play again.");
         else 
        	 statusBar.setText("'Black' Won! Click RESTART to play again.");
      }
   }
 
   /** The entry "main" method */
   public static void main(String[] args) {
      // Run GUI construction codes in Event-Dispatching thread for thread safety
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
        	 StartView welcomeFrame = new StartView();
        	 welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	 welcomeFrame.pack();
        	 welcomeFrame.setLocationRelativeTo(null); // center the application window
        	 welcomeFrame.setVisible(true);            // show it
        	
         }
      });
   }
}