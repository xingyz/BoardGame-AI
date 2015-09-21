package org.concordia.ganjiho.model;
import java.awt.*;

import org.concordia.ganjiho.view.*;
import org.concordia.ganjiho.common.*;
/**
 * The Cell class models each individual cell of the game board.
 */
public class Cell {
   private Seed content; // content of this cell (Seed.EMPTY, Seed.CROSS, or Seed.NOUGHT)
   int row, col; // row and column of this cell
 
   /** Constructor to initialize this cell with the specified row and col */
   public Cell(int row, int col) {
      this.row = row;
      this.col = col;
      clear(); // clear content
   }
   public Seed getContent(){
	   return content;
   }
   public void setContent(Seed newContent){
	   this.content = newContent;
   }
   /** Clear this cell's content to EMPTY */
   public void clear() {
      content = Seed.EMPTY;
   }
 
   /** Paint itself on the graphics canvas, given the Graphics context */
   public void paint(Graphics g) {
      // Use Graphics2D which allows us to set the pen's stroke
      Graphics2D g2d = (Graphics2D)g;
      g2d.setStroke(new BasicStroke(GameMain.SYMBOL_STROKE_WIDTH,
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); // Graphics2D only
      
     
      // Draw the Seed if it is not empty
      int x1 = col * GameMain.CELL_SIZE + GameMain.CELL_PADDING;
      int y1 = row * GameMain.CELL_SIZE + GameMain.CELL_PADDING;
      
      if (content == Seed.EMPTY){
    	  g2d.drawString(Character.toString((char) (row+'A'))+(col+1),x1,y1);
      }
      if (content == Seed.WHITE) {
         g2d.setColor(Color.BLACK);
         g2d.drawOval(x1, y1, GameMain.SYMBOL_SIZE, GameMain.SYMBOL_SIZE);    
      } else if (content == Seed.BLACK) {
         g2d.setColor(Color.BLACK);
         g2d.fillOval(x1, y1, GameMain.SYMBOL_SIZE, GameMain.SYMBOL_SIZE);
      }
   }
}