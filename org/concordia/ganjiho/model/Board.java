package org.concordia.ganjiho.model;

import java.awt.*;

import org.concordia.ganjiho.view.*;
import org.concordia.ganjiho.common.*;
/**
 * The Board class models the ROWS-by-COLS game-board.
 */
public class Board {
 
   private Cell[][] cells; // composes of 2D array of ROWS-by-COLS Cell instances
  
   /** Constructor to initialize the game board */
   public Board() {
      cells = new Cell[GameMain.ROWS][GameMain.COLS]; // allocate the array
      for (int row = 0; row < GameMain.ROWS; ++row) {
         for (int col = 0; col < GameMain.COLS; ++col) {
            cells[row][col] = new Cell(row, col); // allocate element of array
         }
      }
   }
   
   public Cell[][] getCells(){
	   return cells;
   }
 
   /** Initialize (or re-initialize) the game board */
   public void init() {
      for (int row = 0; row < GameMain.ROWS; ++row) {
         for (int col = 0; col < GameMain.COLS; ++col) {
            cells[row][col].clear(); // clear the cell content
         }
      }
   }
 
   /** Return true if it is a draw (i.e., no more EMPTY cell) */
   public boolean isDraw() {
      for (int row = 0; row < GameMain.ROWS; ++row) {
         for (int col = 0; col < GameMain.COLS; ++col) {
            if (cells[row][col].getContent() == Seed.EMPTY) {
               return false; // an empty seed found, not a draw, exit
            }
         }
      }
      return true; // no empty cell, it's a draw
   }
 
   /** Return true if the player with "seed" has won after placing at
       (seedRow, seedCol) */

 
   /** Paint itself on the graphics canvas, given the Graphics context */
   public void paint(Graphics g) {
      // Draw the grid-lines
      g.setColor(Color.GRAY);
      for (int row = 1; row < GameMain.ROWS; ++row) {
         g.fillRoundRect(0, GameMain.CELL_SIZE * row - GameMain.GRID_WIDHT_HALF,
               GameMain.CANVAS_WIDTH-1, GameMain.GRID_WIDTH,
               GameMain.GRID_WIDTH, GameMain.GRID_WIDTH);
      }
      for (int col = 1; col < GameMain.COLS; ++col) {
         g.fillRoundRect(GameMain.CELL_SIZE * col - GameMain.GRID_WIDHT_HALF, 0,
               GameMain.GRID_WIDTH, GameMain.CANVAS_HEIGHT - 1,
               GameMain.GRID_WIDTH, GameMain.GRID_WIDTH);
      }
 
      // Draw all the cells
      for (int row = 0; row < GameMain.ROWS; ++row) {
         for (int col = 0; col < GameMain.COLS; ++col) {
        	cells[row][col].paint(g);  // ask the cell to paint itself
         }
      }
   }
}