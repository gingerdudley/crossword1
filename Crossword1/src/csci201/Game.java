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
	
	public void resizeBoard() {
		int newXSize = 0;
		int newYSize = 0;
		int minY = acrossWords[0].start[0];
		int minX = acrossWords[0].start[1];
		int maxY = downWords[0].start[0] + downWords[0].word.length();
		//^add the length of the word to the y position
		int maxX = acrossWords[0].start[1] + acrossWords[0].word.length();
		//run through all the words and find their starting x and y position
		for(int i = 1; i < acrossWords.length; i++) {
			if(acrossWords[i].start[0] < minY) {
				minY = acrossWords[i].start[0];
			}
			if(acrossWords[i].start[1] < minX) {
				minX = acrossWords[i].start[1];
			}
			if(acrossWords[i].start[1] + acrossWords[i].word.length() > maxX) {
				maxX = acrossWords[i].start[1] + acrossWords[i].word.length();
			}
		}
		for(int i = 1; i < downWords.length; i++) {
			if(downWords[i].start[0] < minY) {
				minY = downWords[i].start[0];
			}
			if(downWords[i].start[1] < minX) {
				minX = downWords[i].start[1];
			}
			if(downWords[i].start[0] + downWords[i].word.length() > maxY) {
				maxY = downWords[i].start[0] + downWords[i].word.length();
			}
		}
		System.out.println("xMin: " + minX);
		System.out.println("yMin: " + minY);
		System.out.println("xMax: " + maxX);
		System.out.println("yMax: " + maxY);
		//now we can chop the board down to this new little size for the x
		//do the same thing but add the length of the word
		
	}
	
	public void initializeCurrentBoard() {
		//we need to set the current board to the correct syntax with the spaceing and stuff
		//take the x position of each word and multiply it, first we need to resize the board
	}
}
