# BoardGame-AI
The GUI is implemented in Java Graphics, and the AI is implemented in Java SE7.
The main models of this program are Board.java for chess board, and Cell.java for each board cell. They are packaged in model.
The program consists of two views, a welcome page and a game page respectively. They are packaged in view.
The classes related to players are stored in package common. 
The alpha-beta pruning code is in algorithm package. The depth of search can be specified in method minimax().

The algorithm gets slower when the depth gets bigger than 4. In order to solve this problem, multithreads could be used.
