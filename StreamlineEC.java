/*
 * Author: Jihun Park
 * CS8B Login: cs8bwasc
 * Date: 13 February 2019
 * File: StreamlineEC.java
 * Sources of Help: PSA3 Write up, Piazza answers, websites given by Write up
 * The main purpose of this file is to add extra function that we can
 * type the opposite direction ket to undo
 */
import java.util.*;
import java.io.*;
public class StreamlineEC extends Streamline{

	List<Direction> previousDirections;
	//the arraylist of direction objects to record direction

	public StreamlineEC(String filename) {
		super(filename); //inheirts from parent class
		this.previousDirections = new ArrayList<Direction>();
	}
	public StreamlineEC() {
		super();
		this.previousDirections = new ArrayList<Direction>();
	}

	void recordAndMove(Direction direction) {
		GameState currentState_copy = new GameState(this.currentState);
		if(previousDirections.isEmpty() == true){
			previousDirections.add(direction);
			previousStates.add(currentState_copy);
			//check if the list is empty
		}
		else { //not empty
			int last = previousDirections.size()-1;
			Direction previousOne = previousDirections.get(last);
			//previous direction
			int previousD_num = previousOne.getRotationCount();
			int currentD_num  = direction.getRotationCount();
			//int variables to store rotarion counts

			if((currentD_num < 2) && (previousD_num == currentD_num+2)){
				this.undo();
				previousStates.add(currentState_copy);
				previousDirections.remove(last);
				//if the input is the opposite direction of the previous one
				//undo, and remove the last direction from list
			}
			else if((currentD_num >= 2) && (previousD_num+2 == currentD_num)){
				this.undo();
				previousStates.add(currentState_copy);
				previousDirections.remove(last);
			}
			else {
				previousDirections.add(direction);
				previousStates.add(currentState_copy);
				//append the copy of current gamestate into the arraylist
			}
		}
		if(direction != null)
			currentState.move(direction);
		else return;
		//if the direction is null, do nothing
		int last_index = previousStates.size() - 1;
		if(currentState.equals(currentState_copy))
			previousStates.remove(last_index);
	}

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
}
