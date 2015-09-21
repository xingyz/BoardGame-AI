package org.concordia.ganjiho.algorithm;

import java.util.ArrayList;
import java.util.List;

import org.concordia.ganjiho.model.*;
import org.concordia.ganjiho.view.GameMain;
import org.concordia.ganjiho.common.*;
public class GanjihoAlgorithms {

	public static State findWinner(Board board,Seed seed){
		Cell[][] cells = board.getCells();
		if(seed==Seed.WHITE){
			for(int i=0; i!=cells.length-1; ++i){
				for(int j=0; j!=cells[0].length;++j){
					if(cells[i][j].getContent() == Seed.EMPTY 
					   &&cells[i+1][j].getContent() == Seed.EMPTY)
						return State.PLAYING;
					
				}
			}
			return State.BLACK_WON;
		}else if(seed==Seed.BLACK){
			for(int i=0; i!=cells.length; ++i){
				for(int j=0; j!=cells[0].length-1;++j){
					if(cells[i][j].getContent() == Seed.EMPTY 
					   &&cells[i][j+1].getContent() == Seed.EMPTY)
						return State.PLAYING;
				}
			}
			return State.WHITE_WON;
		}
		return State.PLAYING;
	}
	
	public static int[] minimax(Board board,PlayerMode mode, Seed player,int depth,int alpha, int beta){
		 List<int[]> nextMoves;
		  if(depth!=0)
			  nextMoves = generateMoves(board,player);
		  else
			  nextMoves = null;
	    
	      // mySeed is maximizing; while oppSeed is minimizing
	      int bestScore;
	      if(mode==PlayerMode.AI_BEFORE){ // AI is White, so white maximizes and black minimizes
	    	  bestScore = (player == Seed.WHITE) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
	      }else{  // AI plays second, so AI is black, black maximizes and white minimizes
	    	  bestScore = (player == Seed.BLACK) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
	      }
	      
	      int score;
	      int bestRow = -1;
	      int bestCol = -1;
	      // stop condition for the recursion: either at the end of game or reaches the specified depth
	      if (depth==0||nextMoves.isEmpty()) {
	         // Gameover or depth reached, evaluate score
	         score = evaluate(board,player,mode);
	         return new int[] {score,bestRow,bestCol};
	      } else {
	         for (int[] move : nextMoves) {
	            // Try this move for the current "player"
	            board.getCells()[move[0]][move[1]].setContent(player);
	            if(mode == PlayerMode.AI_BEFORE){
	            	if (player == Seed.WHITE) {  // (computer) is maximizing player
	 	               score = minimax(board,mode, Seed.BLACK,depth-1,alpha,beta)[0];
	 	               if (score > alpha) {
	 	                  alpha = score;
	 	                  bestRow = move[0];
	 	                  bestCol = move[1];
	 	               }
	 	            } else {  // player is minimizing player
	 	               score = minimax(board,mode,Seed.WHITE,depth - 1,alpha,beta)[0];
	 	               if (score < beta) {
	 	                  beta = score;
	 	                  bestRow = move[0];
	 	                  bestCol = move[1];
	 	               }
	 	            }
	            }else if(mode == PlayerMode.AI_AFTER){
	            	if (player == Seed.BLACK) {  //  (computer) is maximizing player
		 	               score = minimax(board,mode, Seed.WHITE,depth-1,alpha,beta)[0];
		 	              //System.out.println("[depth:"+(depth-1)+",score:"+currentScore+",move:"+move[0]+","+move[1]+"]");
		 	               if (score > alpha) {
		 	                  alpha = score;
		 	                  bestRow = move[0];
		 	                  bestCol = move[1];
		 	               }
		 	            } else {  // player is minimizing player
		 	               score = minimax(board,mode,Seed.BLACK,depth - 1,alpha,beta)[0];
		 	             // System.out.println("[depth:"+(depth-1)+",score:"+currentScore+",move:"+move[0]+","+move[1]+"]");
		 	               if (score < beta) {
		 	                  beta = score;
		 	                  bestRow = move[0];
		 	                  bestCol = move[1];
		 	               }
		 	            }
	            }     
	            // Undo move
	            board.getCells()[move[0]][move[1]].setContent(Seed.EMPTY);
	            // cut-off
	            if(alpha>=beta) break;
	         }
	      }
	     //if(depth==2)
	           //System.out.println("[depth:"+depth+",bestScore:"+bestScore+",move:"+bestRow+","+bestCol+"]");
	      int val = 0;
	      if(mode==PlayerMode.AI_AFTER){
	    	  if(player == Seed.BLACK)
	    		  val = alpha;
	    	  else
	    		  val = beta;
	      }else if(mode == PlayerMode.AI_BEFORE){
	    	  if(player == Seed.WHITE)
	    		  val = alpha;
	    	  else
	    		  val = beta;
	      }
	      return new int[] {val, bestRow, bestCol};
	}
	
	 private static List<int[]> generateMoves(Board board,Seed player) {
	      List<int[]> nextMoves = new ArrayList<int[]>(); // allocate List
	 
	      // Search for empty cells and add to the List
	      if(player == Seed.WHITE){
		      for (int row = 0; row < GameMain.ROWS-1; ++row) {
		          for (int col = 0; col < GameMain.COLS; ++col) {
		            if (board.getCells()[row][col].getContent() == Seed.EMPTY
		            	&&board.getCells()[row+1][col].getContent() == Seed.EMPTY) {
		               nextMoves.add(new int[] {row, col});
		            }
		         }
		      }
	      }else if(player == Seed.BLACK){
	    	  for (int row = 0; row < GameMain.ROWS; ++row) {
		          for (int col = 0; col < GameMain.COLS-1; ++col) {
		            if (board.getCells()[row][col].getContent() == Seed.EMPTY
		            	&&board.getCells()[row][col+1].getContent() == Seed.EMPTY) {
		               nextMoves.add(new int[] {row, col});
		            }
		         }
		      }
	      }
	    
	      return nextMoves;
	   }
	 
	 /*compute heuristic*/
	 private static int evaluate(Board board,Seed player,PlayerMode mode){
		 int score = 0;
		 int numVertical = 0;
		 int numHorizontal = 0;
		 
		 for (int row = 0; row < GameMain.ROWS-1; ++row) {
	          for (int col = 0; col < GameMain.COLS-1; ++col) {
	            if (board.getCells()[row][col].getContent() == Seed.EMPTY
	            	&&board.getCells()[row+1][col].getContent() == Seed.EMPTY) {
	            		numVertical++;
	            }
	         }
	      }
		 for (int row = 0; row < GameMain.ROWS-1; ++row) {
	          for (int col = 0; col < GameMain.COLS-1; ++col) {
	            if (board.getCells()[row][col].getContent() == Seed.EMPTY
	            	&&board.getCells()[row][col+1].getContent() == Seed.EMPTY) {
	            		numHorizontal++;
	            }
	         }
	      }
		 if(mode == PlayerMode.AI_BEFORE)
		 //if(player == Seed.WHITE)
			 score = numVertical-numHorizontal;
		 else if(mode == PlayerMode.AI_AFTER)
		// else
			 score = numHorizontal-numVertical;
	  
		 return score;
	 }
	
}
