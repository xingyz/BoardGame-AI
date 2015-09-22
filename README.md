# BoardGame-AI
The GUI is implemented in <b>Java Graphics</b>, and the AI is implemented in <b>Java SE7</b>.


The main models of this program are Board.java for chess board, and Cell.java for each board cell. They are packaged in model.
The program consists of two views, a welcome page and a game page respectively. They are packaged in view.
The classes related to players are stored in package common. 
The alpha-beta pruning code is in algorithm package. The depth of search can be specified in method minimax().

To play this game, you have to choose a mode first, either play against a real player, or our AI. 
![alt tag](https://raw.githubusercontent.com/perceptron-XYZ/BoardGame-AI/master/welcome%20page.png)

When placing a token, you can specify by entering the location, or click a location on the board.
![alt tag](https://raw.githubusercontent.com/perceptron-XYZ/BoardGame-AI/master/game%20page.png)

The algorithm gets slower when the depth gets bigger than 4. In order to solve this problem, multithreads could be used.
