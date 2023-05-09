/*
 * Author: Jihun Park
 * CS8B Login: cs8bwasc
 * Date: 13 February 2019
 * File: Streamline.java
 * Sources of Help: PSA3 Write up, Piazza answers, websites given by Write up
 * The main purpose of this file is to implement the method from GameState class
 * and actually let the player to play the Streamline game.
 */

import java.util.*;
import java.io.*;

/**
 * This class has constructors that create the Stremline game and play method
 * that lets player actually play it. This class also can let the player
 * to save and load game data from files.
 */
public class Streamline {

	final static int DEFAULT_HEIGHT = 6;
	final static int DEFAULT_WIDTH = 5;

	final static int LAST_ROW_INDEX = 5;
	final static int LAST_COL_INDEX = 4;
	final static int FIRST_INDEX = 0;
	//default player and goal position
	final static int DEFAULT_OBSTACLES = 3;

	final static String OUTFILE_NAME = "saved_streamline_game";
	final static String MIDDLE_SPACE = " ";

	GameState currentState;
	List<GameState> previousStates;

	/**
	 * Constructor that creates a GameState object with default settings
	 * it also creates an empty list of previousStates
	 * @param  this constructor doesn't take any parameters
	 * @return No return, but it creates a GameState object
	 */
	public Streamline() {
		this.currentState = new GameState(DEFAULT_HEIGHT,DEFAULT_WIDTH,
				LAST_ROW_INDEX,FIRST_INDEX,FIRST_INDEX,LAST_COL_INDEX);
		currentState.addRandomObstacles(DEFAULT_OBSTACLES);
		//add 3 obstacles
		this.previousStates = new ArrayList<GameState>();
		//creating emtpy arraylist
	}

	/**
	 * Constructor that creates a GameState object with the information
	 * loaded from a game data file through helper method
	 * @param  String the name of the file to load on
	 * @return No return, but it creates a GameState object
	 */
	public Streamline(String filename) {
		try {
			loadFromFile(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that reads the content of a file and creates a GameState
	 * object with the information provided
	 * it also initializes the instance variables with the data
	 * @param  String file name to read
	 * @return  void
	 */
	protected void loadFromFile(String filename) throws IOException {
		Scanner fileReader = new Scanner(new File(filename));

		int board_Rows = fileReader.nextInt();
		int board_Cols = fileReader.nextInt();
		//stores the height and width into variables
		fileReader.nextLine();
		int playerRow = fileReader.nextInt();
		int playerCol = fileReader.nextInt();
		//stores player position from file to the local variables
		fileReader.nextLine();
		int goalRow = fileReader.nextInt();
		int goalCol = fileReader.nextInt();
		//stores goal position from file to the local variables
		fileReader.nextLine();

		this.currentState = new GameState(board_Rows, board_Cols,
				playerRow, playerCol, goalRow, goalCol);
		//set the currentState to the GameState based on the data
		char[][] sample = currentState.board;
		for(int i = 0; i < board_Rows; i++){
			String line = fileReader.nextLine();
			for(int j = 0; j < board_Cols; j++){
				sample[i][j] = line.charAt(j);
				//place the board content into the array
			}
		}
		if(playerRow == goalRow && playerCol == goalCol)
			currentState.levelPassed = true;
		else currentState.levelPassed = false;
		//check if the level has passed
		this.previousStates = new ArrayList<GameState>();
	}

	/**
	 * Method that records the current GameState to the
	 * list of previousStates, then move in the direction provided
	 * @param  Direction the direction to move
	 * @return  void
	 */
	void recordAndMove(Direction direction) {
		GameState currentState_copy = new GameState(this.currentState);
		previousStates.add(currentState_copy);
		//append the copy of current gamestate into the arraylist
		if(direction != null)
			currentState.move(direction);
		else return;
		//if the direction is null, do nothing
	}

	/**
	 * Method that makes the currentState to the previous (undo)
	 * so the player can cancel his most recent step
	 * @param  void no parameter
	 * @return  void
	 */
	void undo() {
		int last_index = previousStates.size() - 1;
		if(previousStates == null) return;
		if(previousStates.isEmpty() == true) return;
		//check if the list is invalid
		if(last_index >= 0)
			this.currentState = previousStates.get(last_index);
		//set current state to the previous state of the game
		else this.currentState = previousStates.get(0);
		//in case there is no previousStates
		previousStates.remove(last_index);
		//remove the last previousStates so the player can
		//make multiple undo
	}

	/**
	 * Method that allow the user to play the Streamline game by
	 * typing in certain commands to move, save, or quit the game
	 * @param  void
	 * @return  void
	 */
	void play() {
		while(currentState.levelPassed == false){
			//the loop continues until the level is passed
			System.out.print(currentState.toString());
			System.out.print("> ");

			Scanner scan = new Scanner(System.in);
			String user_command = scan.next();
			if(user_command.equals("w"))
				this.recordAndMove(Direction.UP);
			else if(user_command.equals("a"))
				this.recordAndMove(Direction.LEFT);
			else if(user_command.equals("s"))
				this.recordAndMove(Direction.DOWN);
			else if(user_command.equals("d"))
				this.recordAndMove(Direction.RIGHT);
			//move in the direction according to user input
			else if(user_command.equals("u"))
				this.undo();
			//undo the last step
			else if(user_command.equals("o"))
				this.saveToFile();
			//save the currentState to a file
			else if(user_command.equals("q"))
				break;
			else;
			//if the input is invalid, the loop starts again
		}

		if(currentState.levelPassed == true) {
			System.out.print(currentState.toString());
			System.out.println("Level Passed!");
			//print message if the level has passed
		}
	}

	/**
	 * Method that saves the current state of the baord to a file
	 * and prints appropriate message
	 * @param  void
	 * @return  void
	 */
	void saveToFile() {
		try {
			PrintWriter writer = new PrintWriter(OUTFILE_NAME);
			char[][] board = currentState.board;
			int row = board.length;
			int col = board[0].length;
			int playerRow = currentState.playerRow;
			int playerCol = currentState.playerCol;
			int goalRow = currentState.goalRow;
			int goalCol = currentState.goalCol;
			//stores the currentstate's data into variables
			writer.println(row + MIDDLE_SPACE + col);
			writer.println(playerRow + MIDDLE_SPACE + playerCol);
			writer.println(goalRow + MIDDLE_SPACE + goalCol);
			//store the board information in the first 3 lines
			for(int i = 0; i < row; i++){
				for(int j = 0; j < col; j++){
					writer.print(board[i][j]);
				}
				writer.println();
			}
			//store the baord content and write it in the file
			writer.close();
			System.out.println("Saved current state to: " + OUTFILE_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
