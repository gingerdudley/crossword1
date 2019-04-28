package csci201;

import java.util.Vector;

public class Game {
	private int numPlayers;
	private int currPlayers; 
	private boolean gameReady;
	public String[][] board;
	public String[][] currentBoard;
	public String[][] smallBoard;
	public int xSize;
	public int ySize;
	public int currX;
	public int currY;
	public int minX;
	public int minY;
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
		acrossWordsC = new Vector<Word>();
		downWordsC = new Vector<Word>();
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
	
	public String[][] resizeBoard() {
		int newXSize = 0;
		int newYSize = 0;
		minY = acrossWords[0].start[0];
		minX = acrossWords[0].start[1];
		int maxY = downWords[0].start[0] + downWords[0].word.length();
		//^add the length of the word to the y position
		int maxX = acrossWords[0].start[1] + acrossWords[0].word.length();
		//run through all the words and find their starting x and y position
		for(int i = 0; i < acrossWords.length; i++) {
			acrossWordsC.add(acrossWords[i]);
		}
		for(int j = 0; j < downWords.length; j++) {
			downWordsC.add(downWords[j]);
		}
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
		newXSize = maxX - minX;
		newYSize = maxY - minY;
		currX = newXSize * 2;
		currY = newYSize + 1;
		smallBoard = new String[newYSize + 1][newXSize * 2];
		for(int i = 0; i < newYSize + 1; i++) {
			for(int j = 0; j < newXSize * 2; j++) {
				if(j%2 == 1) {
					smallBoard[i][j] = board[minY + i][minX + (j / 2)];
				} else {
					smallBoard[i][j] = "!";
				}			
			}
		}
		for(int i = 0; i < acrossWords.length; i++) {
			int xPos = acrossWords[i].start[1];
			int xS = (xPos - minX) * 2 + 2;
			int yS = acrossWords[i].start[0] - minY;
			smallBoard[yS][xS - 2] = Integer.toString(acrossWords[i].number);
		}
		for(int i = 0; i < downWords.length; i++) {
			int xPos = downWords[i].start[1];
			int xS = (xPos - minX) * 2;
			int yS = downWords[i].start[0] - minY;
			smallBoard[yS][xS] = Integer.toString(downWords[i].number);
		}
		//now we need to multiply the board x side by two and make the places where
		//the letters are blanks
		currentBoard = new String[newYSize + 1][newXSize * 2];
		for(int i = 0; i < newYSize + 1; i++) {
			for(int j = 0; j < newXSize * 2; j++) {
				if(smallBoard[i][j].equals("!") || smallBoard[i][j].equals("@")) {
					currentBoard[i][j] = " ";
				} else if(smallBoard[i][j].equals("1") || smallBoard[i][j].equals("2") ||
						smallBoard[i][j].equals("3") || smallBoard[i][j].equals("4") || 
						smallBoard[i][j].equals("5") || smallBoard[i][j].equals("6") ||
						smallBoard[i][j].equals("7") || smallBoard[i][j].equals("8") ||
						smallBoard[i][j].equals("9")) {
					currentBoard[i][j] = smallBoard[i][j];
				} else {
					currentBoard[i][j] = "_";
				}
			}
		}
		
		return currentBoard;
	}
}
