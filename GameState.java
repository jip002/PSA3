/*
 * Author: Jihun Park
 * CS8B Login: cs8bwasc
 * Date: 13 February 2019
 * File: GameState.java
 * Sources of Help: PSA3 Write up, Piazza answers
 * The main purpose of this file is to create necessary methods to make
 * the streamline game work.
 */

import java.util.*;

/**
 * This class has constructors that create a game board and methods
 * that allow the snake to move in certain direction.
 * All the necessary character symbols are declared.
 */
public class GameState {

	// Used to populate char[][] board below and to display the
	// current state of play.
	final static char TRAIL_CHAR = '.';
	final static char OBSTACLE_CHAR = 'X';
	final static char SPACE_CHAR = ' ';
	final static char CURRENT_CHAR = 'O';
	final static char GOAL_CHAR = '@';
	final static char NEWLINE_CHAR = '\n';

	final static char DASH_CHAR = '-';
	final static char VERTICAL_BAR = '|';
	//characters to construct the edge of the board.

	private static final int FACTOR_TWO = 2;
	//factor two is needed to calculate the length of the board
	private static final int EXTRA_THREE_DASH = 3;
	private static final int FOUR_DIRECTION = 4;

	// This represents a 2D map of the board
	char[][] board;

	// Location of the player
	int playerRow;
	int playerCol;

	// Location of the goal
	int goalRow;
	int goalCol;

	// true means the player completed this level
	boolean levelPassed;

	// initialize a board of given parameters,
	// fill the board with SPACE_CHAR
	// set corresponding fields to parameters.

	/**
	 * Constructor contains has board information to make a gaming board
	 * it also initializes the instance method (board information)
	 * @param  int the height, width, player and goal position
	 * @return No return, but it creates a board
	 */
	public GameState(int height, int width, int playerRow, int playerCol,
			int goalRow, int goalCol) {
		char[][] board = new char[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++){
				board[i][j] = SPACE_CHAR;
			}
		//fill space chars to the board
		this.board = board;
		this.playerRow = playerRow;
		this.playerCol = playerCol;
		this.goalRow = goalRow;
		this.goalCol = goalCol;
	}

	// copy constructor
	/**
	 * Constructor that makes a deep copy of the board created by the
	 * first constructor above
	 * @param  GameState the other GameState object
	 * @return No return, but it creates a deep copied board
	 */
	public GameState(GameState other) {
		char[][] original = other.board;
		char[][] copy = new char[original.length][original[0].length];
		for(int i = 0; i < original.length; i++)
			for(int j = 0; j < original[i].length; j++){
				copy[i][j] = original[i][j];
			}
		//deep copying the original board to the copy
		this.board = copy;
		this.levelPassed = other.levelPassed;
		this.playerRow = other.playerRow;
		this.playerCol = other.playerCol;
		this.goalCol = other.goalCol;
		this.goalRow = other.goalRow;
	}


	// add count random blocks into this.board
	// avoiding player position and goal position

	/**
	 * Method adds certain number of obstacles to the board
	 * @param  int count, the number of obstacles to create
	 * @return  void
	 */
	void addRandomObstacles(int count){
		Random rand = new Random();
		int random_Row, random_Col, count_space = 0;
		//int of random obstacle position and number of spaces
		char[][] board = this.board;

		for(int i = 0; i < board.length; i++)
			for(int j = 0; j < board[0].length; j++)
				if(board[i][j] == SPACE_CHAR) count_space++;
		//check the number of spaces in the board
		if(count < 0 || count > count_space) return;
		//check if there's more obstacles to create than the available spaces

		for(int i = 0; i < count; i++){
			//repeat until there are enough obstacles created
			do{
				random_Row = rand.nextInt(board.length);
				random_Col = rand.nextInt(board[0].length);
			} while((board[random_Row][random_Col] != SPACE_CHAR)
					|| (random_Row == this.playerRow && random_Col == this.playerCol)
					|| (random_Row == this.goalRow && random_Col == this.goalCol));
			board[random_Row][random_Col] = OBSTACLE_CHAR;
			//repeat until the random position is in the space char position
			//and the position is not in player or goal position
		}
	}


	// rotate clockwise once
	// rotation should account for all instance var including board, current
	// position, goal position

	/**
	 * Method rotates the board by switching the height and length of it
	 * @param  no parameters taken
	 * @return  nothing, but the board is rotated (including its content)
	 */
	void rotateClockwise() {
		char[][] board = this.board;
		int new_width = board.length;
		int new_height = board[0].length;
		//the height and width of new board is the reverse of the original
		char[][] temp_board = new char[new_height][new_width];
		//initializing the temporary board
		for(int i = 0; i < board.length; i++)
			for(int j = 0; j < board[0].length; j++)
				temp_board[j][board.length-1-i] = board[i][j];
		//copying the content of original board to the new board
		//with apropriate row and column positions
		this.board = temp_board;
		int playerRow_temp = this.playerRow;
		int playerCol_temp = this.playerCol;
		int goalRow_temp = this.goalRow;
		int goalCol_temp = this.goalCol;
		//switching the player and goal position with correct index positions
		this.playerRow = playerCol_temp;
		this.playerCol = board.length-1 - playerRow_temp;
		this.goalRow = goalCol_temp;
		this.goalCol = board.length-1 - goalRow_temp;
	}


	// move current position towards right until stopped by obstacle / edge
	// leave a trail of dots for all positions that we're walked through
	// before stopping

	/**
	 * Method that moves the player position to the right until it hits
	 * edge, obstacle, or trail char, and puts trail char in the path taken
	 * @param  no parameters taken
	 * @return  nothing
	 */
	void moveRight() {
		char[][] board = this.board;
		int stop_position = this.playerCol;
		for(int i = this.playerCol; i < board[0].length; i++){
			if((i != board[0].length-1 &&
						board[this.playerRow][i+1] == OBSTACLE_CHAR) ||
					(i != board[0].length-1 && board[this.playerRow][i+1] == TRAIL_CHAR))
			{
				//check horizontal path of the player position
				//this if let player position to stop before trail or obstacle
				stop_position = i; break;
				//let the stop_position to be where the player position should stop
			}
			else if(i == board[0].length-1) {stop_position = i; break;}
			//this if let player position to stop before at the edge
		}

		for(int i = this.playerCol; i < stop_position; i++)
			board[this.playerRow][i] = TRAIL_CHAR;
		//prints the trail char horizontally before the stop position
		this.playerCol = stop_position;
		if(this.playerCol == this.goalCol && this.playerRow == this.goalRow){
			levelPassed = true;
			//check if the game is passed
			return;
		}
	}


	// move towards any direction given
	// accomplish this by rotating, move right, rotating back

	/**
	 * Method rotates the board according to the direction
	 * and runs moveRight method
	 * @param  Direction the direction indicating the number to rotate
	 * @return  nothing, but the board is changed
	 */
	void move(Direction direction) {
		int count_rotation = direction.getRotationCount();
		for(int i = 0; i < count_rotation; i++){
			this.rotateClockwise();
		} //rotate the board with appropriate number of rotation
		this.moveRight();
		for(int i = 0; i < FOUR_DIRECTION - count_rotation; i++){
			this.rotateClockwise();
		}
		//rotates back to original state
	}


	@Override
	// compare two game state objects, returns true if all fields match

	/**
	 * Method compares two GameState board if they are exactly equal
	 * @param  Object other GameState object (supposed to be)
	 * @return  boolean returns true if the two object has same content
	 *          returns false if not
	 */
	public boolean equals(Object other) {
		if( other instanceof GameState == false) return false;
		//check if the input is GameState object, if not return false
		if( this.board == null || ((GameState)other).board == null)
			return false;
		//check if either of the object is invalid
		char[][] board = this.board;
		char[][] board_2 = ((GameState)other).board;
		if(board.length != board_2.length ||
				board[0].length != board_2[0].length) return false;
		//check if the height and width of the two board is same
		boolean equal = true;
		//temporary boolean variable to test equality
		if(board.length != board_2.length ||
				board[0].length != board_2[0].length) equal = false;
		if(this.playerRow != ((GameState)other).playerRow ||
				this.playerCol != ((GameState)other).playerCol) equal=false;
		if(this.goalRow != ((GameState)other).goalRow ||
				this.goalCol != ((GameState)other).goalCol) equal=false;
		//check if the player and goal position is same
		for(int i = 0; i < board.length; i++)
			for(int j = 0; j < board[0].length; j++)
				if(board[i][j] != board_2[i][j]) equal = false;
		//check if the board content is the same
		if(this.levelPassed != ((GameState)other).levelPassed)
			equal=false;
		//check if the instance variable levelpassed has same value
		if(equal == false) return false;
		else return true;
	}


	@Override
	/**
	 * Method prints the board in String with player and goal position chars
	 * @param  no parameters taken
	 * @return  String that will be printed to represent the board
	 */
	public String toString() {
		if(this.board == null) return null;
		//check if the board is null
		char[][] board = this.board;
		int height = board.length;
		int width = board[0].length;
		int length = width * FACTOR_TWO + EXTRA_THREE_DASH;
		//calculate the length of the board edge
		StringBuilder data = new StringBuilder(height*width);
		//makes StringBuilder with the size needed for making the whole string
		for(int i = 0; i < length; i++) data.append(DASH_CHAR);
		//print the upper edge
		data.append(NEWLINE_CHAR);
		for(int i = 0; i < height; i++){
			data.append(VERTICAL_BAR);
			data.append(SPACE_CHAR);
			//print left vertical adge and space for each row
			for(int j = 0; j < width; j++){
				if(i == this.playerRow && j == this.playerCol){
					data.append(CURRENT_CHAR);
					data.append(SPACE_CHAR);
					//if the position is the player position, print current char
				}
				else if(i == this.goalRow && j == this.goalCol){
					data.append(GOAL_CHAR);
					data.append(SPACE_CHAR);
					//if the position is the goal position, print goal char
				}
				else{
					data.append(board[i][j]);
					data.append(SPACE_CHAR);
					//else, just print content of the board (space or obstacle or trail)
				}
			}
			data.append(VERTICAL_BAR);
			data.append(NEWLINE_CHAR);
			//prints the right vertical edge and newline char after each row
		}
		for(int i = 0; i < length; i++){
			data.append(DASH_CHAR);
			//print bottom edge
		}
		data.append(NEWLINE_CHAR);
		return data.toString();
	}
}
