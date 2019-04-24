package csci201;

import java.util.Vector;

public class Game {
	private int numPlayers;
	private int currPlayers; 
	private boolean gameReady;
	public String[][] board;
	//gonna need a different array for the current board vs the correct board
	public String[][] currentBoard;
	public int xSize;
	public int ySize;
	public boolean started;
	Word[] acrossWords;
	Word[] downWords;
	Vector<Word> acrossWordsC;
	Vector<Word> downWordsC;
	Word[] words;
	
	public Game() {
		currPlayers = 0;
		numPlayers = 0;
		started = false;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	public int getCurrPlayers() {
		return currPlayers;
	}

	public void setCurrPlayers(int currPlayers) {
		this.currPlayers = currPlayers;
	}

	public boolean isGameReady() {
		//need to check how many players is ready
		if(currPlayers != numPlayers) {
			gameReady = false;
		} else {
			gameReady = true;
		}
		return gameReady;
	}

	public void setGameReady(boolean gameReady) {
		this.gameReady = gameReady;
	}
}
