package csci201;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class Crossword {
	
	
	//TAKE INTO ACCOUNT WHEN WORDS START W SAME NUMBER
	//^ they should be placed intersecting each other od this in the backtrack
	
	//use this file to write how to create the crossword board when given a set of words
	
	public void MakeBoardArray(Word[] acrossWords, Word[] downWords) {
		int acrossSize = 0;
		int downSize = 0;
		for(int i = 0; i < acrossWords.length; i++) {
			acrossSize += acrossWords[i].word.length();
		}
		for(int j = 0; j < downWords.length; j++) {
			downSize += downWords[j].word.length();
		}
		
		
		String[][] board = new String[acrossSize + 1][downSize + 1];
		//initializing all the board to empty characters to begin with 
		for(int i = 0; i < acrossSize + 1; i++) {
			for(int j = 0; j< downSize + 1; j++) {
				board[i][j] = "@";
			}
		}
		
		//putting all the word objects into one large array instead of 3 different ones
		Word [] words = new Word[acrossWords.length + downWords.length];
		for(int i = 0; i < acrossWords.length; i++) {
			words[i] = acrossWords[i];
		}
		for(int j = acrossWords.length; j < downWords.length + acrossWords.length; j++) {
			words[j] = downWords[j - acrossWords.length];
		}
		boolean allWordsPlaced = false;
		int currentWordIndex = 0;
		while(allWordsPlaced == false) {
			//try to place the next word
		}

//		Start with the first word in the list 
//		-In a nested for loop, try every single position on the board:
		
//			-If the current position is valid (please note, most of the positions will not be valid):
//				- use recursion call to place the next word
//		               		- if you cannot place the next word (or any of the subsequent words), take the word off the board, and try the next position 
//		             		- if you can place all the words, then congrats, you have found the solution! 
	}
	
	//start from the (0,0) index and try to place a word there, but were gonna leave the 0,0 index black, so try to place
	//the first across word in the 1,1 place, then try to place the next word (a down word), run through all the letters
	//of the row and see if any of them match the letter in the word that goes down
	
	//sort the words by length, descending 
	
	public boolean tryPlacingWord(Word[] words, int currentWordIndex, String[][] board, int aSize, int dSize,
			String[][] holderBoard, Vector<Word> wordsLeft) {
		//System.out.println("testing");
		//printBoard(holderBoard, 15, 15);
		if(currentWordIndex == words.length) {
			//then all words were able to be placed legally
			
			//fix this
			return true;
		}
		if(currentWordIndex == 0) {
			//placing the first word on the board
			for(int i = 0; i < aSize + 1; i++) {
				for(int j = 0; j < dSize + 1; j++) {
					//try to place the board at each spot on the board? if it works then skip out of the loop
					if(words[0].word.length() + i < aSize + 1) {
						//then you can possibly place the word there
						//place word starting at i 
						for(int k = 0; k < words[0].word.length(); k++) {
							holderBoard[i][j + k] = String.valueOf(words[0].word.charAt(k));
						}
						System.out.println("testing");
						printBoard(holderBoard, 15, 15);
						currentWordIndex++;
						Vector<Word> copyOfWordsLeft = new Vector<Word>();
						for(int g = 0; g < wordsLeft.size(); g++) {
							copyOfWordsLeft.add(wordsLeft.get(g));
						}
							//remove the first word and continue down
						copyOfWordsLeft.remove(0);
						boolean locationWorked = tryPlacingWord(words, currentWordIndex, board, aSize,
								dSize, holderBoard, copyOfWordsLeft);
						if(locationWorked) {
							//then execute this move on the real board to be returned later
						} else {
							//also will need to remove the things we put on the holder board
							for(int k = 0; k < words[0].word.length(); k++) {
								holderBoard[i][j] = "@";
							}
							currentWordIndex--;
							
						}
					}
				}
			}
			return true;
		}
		
		boolean nextWord = true;
		//run through every place on the board for a word, then if the word cannot be placed yet,
		//increment the word index and recurse again
		for(int i = 0; i < aSize;i++) {
			for(int j = 0; j < dSize; j++) {
				//try to place the word on every spot of the board
				//this isn't the first word so it has to touch a word
				if(holderBoard[i][j] != "@") {
					//once we find a letter on the board, test to see if we can place the new word
					//if we are trying to place a vertical word do different things than a horizontal place
					if(words[currentWordIndex].across == false) {
						//check if the space next to the word is occupied
						if((i + 1) < (aSize + 1)) {
							if(!holderBoard[i+1][j].equals("@")) {
								//if there is already a word there, continue
								continue;
							} 
						} 
						if(i - 1 >= 0) {
							if(!holderBoard[i-1][j].equals("@")) {
								//if there is already a word there, continue
								continue;
							} 
						}
						String charCheck = holderBoard[i][j];
						int letterIndex = 0;
						for(int x = 0; x < words[currentWordIndex].word.length(); x++) {
							String letter = String.valueOf(words[currentWordIndex].word.charAt(x));
							if(letter.equals(charCheck)){
								//try to place the word on the board at this place
								letterIndex = x;
								boolean wordLegal = true;
								if(i - letterIndex < 0) {
									//this could be wrong, check over this later
									wordLegal = false;
									break;
								} else if((words[currentWordIndex].word.length() - letterIndex) + i > (aSize + 1)) {
									//if the word is too long for the board also break this 
									wordLegal = false;
									break;
								}
								if(i - letterIndex - 1 > 0) {
									if(!holderBoard[i - letterIndex - 1][j].equals("@")) {
										//then we cant place the word here and we need to continue
										wordLegal = false;
										break;
									}
								}
								if(words[currentWordIndex].word.length() - letterIndex + i + 1 < (aSize + 1)){
									if(!holderBoard[words[currentWordIndex].word.length() - letterIndex + i + 1]
											[j].equals("@")) {
										//then we cant place the word here and we need to continue
										wordLegal = false;
										break;
									}
								}
								for(int y = 0; y < words[currentWordIndex].word.length(); y++) {

									if(j - 1 > 0) {
										if(!holderBoard[i - letterIndex + y][j-1].equals("@") && (letterIndex - y) != 0) {
											//then we cant place the word here and we need to continue
											wordLegal = false;
											break;
										}
									}
									if(j + 1 < dSize + 1) {
										if(!holderBoard[i - letterIndex + y][j+1].equals("@") && (letterIndex - y) != 0) {
											//then we cant place the word here and we need to continue
											wordLegal = false;
											break;
										}
									}
									if(!holderBoard[i - letterIndex + y][j].equals("@") && (letterIndex - y) != 0) {
										wordLegal = false;
										break;
									}
									
									//if none if these broke, then try to place the word here and then recurse again	
								}
								//well check if the word is legal and if it is we'll recurse
								for(int y = 0; y < words[currentWordIndex].word.length(); y++) {
									//place the word on the temp board
									char placeLetter = words[currentWordIndex].word.charAt(y);
									holderBoard[i - letterIndex + y][j] = String.valueOf(placeLetter);
								}
								
								//print out current board to test what the placement looked
								//like 
								printBoard(holderBoard, 15, 15);
								//help
								Vector<Word> copyOfWordsLeft = new Vector<Word>();
								for(int g = 0; g < wordsLeft.size(); g++) {
									copyOfWordsLeft.add(wordsLeft.get(g));
								}
//								//this is DEFINIETLY WRONG
//								int removeIndex = 0;
//									//remove the first word and continue down
//								String currentWord = words[currentWordIndex].word;
//								for(int c = 0; c < copyOfWordsLeft.size(); c++) {
//									String checker = copyOfWordsLeft.get(c).word;
//									if(checker.equals(currentWord));
//									removeIndex = c;
//								}
//								copyOfWordsLeft.remove(removeIndex);
								currentWordIndex++;
								boolean locationWorked = tryPlacingWord(words, currentWordIndex, board, aSize,
										dSize, holderBoard, copyOfWordsLeft);
								if(locationWorked) {
									//continue to the next word and recurse more
									nextWord = false;
								} else {
									//this word cant be placed yet, so continue on and undo what you just added to 
									//the holder board
									for(int y = 0; y < words[currentWordIndex].word.length(); y++) {
										//place the word on the temp board
										holderBoard[i - letterIndex + y][j] = "@";
									}
									holderBoard[i][j] = charCheck;
									nextWord = true;
								}
							}
						}
					} else {
						//this is for horizontal words
						//check if the space above to the word is occupied
						if((j + 1) < (dSize + 1)) {
							if(!holderBoard[i][j + 1].equals("@")) {
								//if there is already a word there, continue
								continue;
							} 
						} 
						if(j - 1 >= 0) {
							if(!holderBoard[i][j - 1].equals("@")) {
								//if there is already a word there, continue
								continue;
							} 
						}
						String charCheck = holderBoard[i][j];
						int letterIndex = 0;
						for(int x = 0; x < words[currentWordIndex].word.length(); x++) {
							String letter = String.valueOf(words[currentWordIndex].word.charAt(x));
							if(letter.equals(charCheck)){
								//try to place the word on the board at this place
								letterIndex = x;
								boolean wordLegal = true;
								if(i - letterIndex < 0) {
									//this could be wrong, check over this later
									wordLegal = false;
									continue;
								} else if((words[currentWordIndex].word.length() - letterIndex) + j > (dSize + 1)) {
									//if the word is too long for the board also break this 
									wordLegal = false;
									continue;
								}
								if(j - letterIndex - 1 > 0) {
									if(!holderBoard[i][j - letterIndex - 1].equals("@")) {
										//then we cant place the word here and we need to continue
										wordLegal = false;
										continue;
									}
								}
								if(words[currentWordIndex].word.length() - letterIndex + j + 1 < (dSize + 1)){
									if(!holderBoard[i]
											[words[currentWordIndex].word.length() - letterIndex + j + 1].equals("@")) {
										//then we cant place the word here and we need to continue
										wordLegal = false;
										continue;
									}
								}
								for(int y = 0; y < words[currentWordIndex].word.length(); y++) {

									if(i - 1 > 0 && j - letterIndex + y > 0) {
										if(!holderBoard[i - 1][j - letterIndex + y].equals("@") && (letterIndex - y) != 0) {
											//then we cant place the word here and we need to continue
											wordLegal = false;
											break;
										}
									}
									if(i + 1 < aSize + 1 && j - letterIndex + y > 0) {
										if(!holderBoard[i + 1][j - letterIndex + y].equals("@") && (letterIndex - y) != 0) {
											//then we cant place the word here and we need to continue
											wordLegal = false;
											break;
										}
									}
									if(j - letterIndex + y > 0) {
										if(!holderBoard[i][j  - letterIndex + y].equals("@") && (letterIndex - y) != 0) {
											wordLegal = false;
											break;
										}
									}
									if(j - letterIndex + y < 0) {
										wordLegal = false;
										//return false;
										break;
									}
									
									//if none if these broke, then try to place the word here and then recurse again	
								}
//								if(j - letterIndex + y < 0) {
//									wordLegal = false;
//									return false;
//								}
								//well check if the word is legal and if it is we'll recurse
								if(wordLegal == true) {
									for(int y = 0; y < words[currentWordIndex].word.length(); y++) {
										//place the word on the temp board
										char placeLetter = words[currentWordIndex].word.charAt(y);
										holderBoard[i][j  - letterIndex + y] = String.valueOf(placeLetter);
									}
								}
								
								
								//print out current board to test what the placement looked
								//like 
								printBoard(holderBoard, 15, 15);
								//help
								Vector<Word> copyOfWordsLeft = new Vector<Word>();
								for(int g = 0; g < wordsLeft.size(); g++) {
									copyOfWordsLeft.add(wordsLeft.get(g));
								}
//								//this is DEFINIETLY WRONG
//								int removeIndex = currentWordIndex;
//									//remove the first word and continue down
//								copyOfWordsLeft.remove(removeIndex);
								currentWordIndex++;
								boolean locationWorked = tryPlacingWord(words, currentWordIndex, board, aSize,
										dSize, holderBoard, copyOfWordsLeft);
								if(locationWorked) {
									//continue to the next word and recurse more
									nextWord = false;
								} else {
									//this word cant be placed yet, so continue on and undo what you just added to 
									//the holder board
									for(int y = 0; y < words[currentWordIndex].word.length(); y++) {
										//place the word on the temp board
										holderBoard[i][j - letterIndex + y] = "@";
									}
									holderBoard[i][j] = charCheck;
									nextWord = true;
								}
							}
						}
						
					}
				}
			}
		}
		if(nextWord == true && currentWordIndex == (words.length - 1)) {
			return false;
		} else if(nextWord == true) {
			//currentWordIndex++;
			//return false;
			Vector<Word> copyOfWordsLeft = new Vector<Word>();
			for(int g = 0; g < wordsLeft.size(); g++) {
				copyOfWordsLeft.add(wordsLeft.get(g));
			}
			//this is DEFINIETLY WRONG
			//you need to add the word that you just tried to the end of words left so its
			//not tried again till after the recursion
			currentWordIndex++;
			return tryPlacingWord(words, currentWordIndex, board, aSize,
					dSize, holderBoard, wordsLeft);
//			currentWordIndex++;
			//int removeIndex = currentWordIndex;
		} else {
			
			//recurse on this new word
			Vector<Word> copyOfWordsLeft = new Vector<Word>();
			for(int g = 0; g < wordsLeft.size(); g++) {
				copyOfWordsLeft.add(wordsLeft.get(g));
			}
			//moving the word that didnt work to the back of the vector that will be used next
			//int removeIndex = currentWordIndex;
//			Word move = copyOfWordsLeft.get(0);
//			copyOfWordsLeft.remove(0);
//			copyOfWordsLeft.add(move);
//			currentWordIndex++;
			return tryPlacingWord(words, currentWordIndex, board, aSize,
					dSize, holderBoard, copyOfWordsLeft);
		}
		
		//return true;
	}
	
	
	
	public String[][] tryWord(Word word, String[][] holderBoard, int aSize, int dSize){
		if(word.across == true) {
			
			//this is for horizontal words
			//check if the space above to the word is occupied
			for(int i = 0; i < aSize; i++) {
				for (int j = 0; j < dSize; j++) {
					if(holderBoard[i][j] != "@") {
						if((j + 1) < (dSize + 1)) {
							if(!holderBoard[i][j + 1].equals("@")) {
								//if there is already a word there, continue
								continue;
							} 
						} 
						if(j - 1 >= 0) {
							if(!holderBoard[i][j - 1].equals("@")) {
								//if there is already a word there, continue
								continue;
							} 
						}
						String charCheck = holderBoard[i][j];
						int letterIndex = 0;
						for(int x = 0; x < word.word.length(); x++) {
							String letter = String.valueOf(word.word.charAt(x));
							if(letter.equals(charCheck)){
								//try to place the word on the board at this place
								letterIndex = x;
								boolean wordLegal = true;
								if(i - letterIndex < 0) {
									//word wont fit on the board so go to the next letter
									//this could be wrong, check over this later
									wordLegal = false;
									continue;
								} else if((word.word.length() - letterIndex) + j > (dSize + 1)) {
									//if the word is too long for the board also break this 
									wordLegal = false;
									continue;
								}
								if(j - letterIndex - 1 > 0) {
									if(!holderBoard[i][j - letterIndex - 1].equals("@")) {
										//then we cant place the word here and we need to continue
										wordLegal = false;
										continue;
									}
								}
								if(word.word.length() - letterIndex + j + 1 < (dSize + 1)){
									if(!holderBoard[i]
											[word.word.length() - letterIndex + j + 1].equals("@")) {
										//then we cant place the word here and we need to continue
										wordLegal = false;
										continue;
									}
								}
								//if we get here then we will run through the word and try to place 
								//it on the board
								for(int y = 0; y < word.word.length(); y++) {

									if(i - 1 > 0 && j - letterIndex + y > 0) {
										if(!holderBoard[i - 1][j - letterIndex + y].equals("@") && (letterIndex - y) != 0) {
											//then we cant place the word here and we need to continue
											wordLegal = false;
											//word cannot go here and we need to try another place/ word
											break;
										}
									}
									if(i + 1 < aSize + 1 && j - letterIndex + y > 0) {
										if(!holderBoard[i + 1][j - letterIndex + y].equals("@") && (letterIndex - y) != 0) {
											//then we cant place the word here and we need to continue
											wordLegal = false;
											break;
										}
									}
									if(j - letterIndex + y > 0) {
										if(!holderBoard[i][j  - letterIndex + y].equals("@") && (letterIndex - y) != 0) {
											wordLegal = false;
											break;
										}
									}
									if(j - letterIndex + y < 0) {
										wordLegal = false;
										//return false;
										break;
									}
									
									
								}
								//well check if the word is legal and if it is we'll recurse
								if(wordLegal == true) {
									for(int y = 0; y < word.word.length(); y++) {
										//place the word on the temp board
										char placeLetter = word.word.charAt(y);
										holderBoard[i][j  - letterIndex + y] = String.valueOf(placeLetter);
										//then we need to recurse from there
									}
									//return the current board, and then well call the next word from there
									return holderBoard;
								} else {
									//then we need to return null and try the next word
									return null;
								}
							}
						}
					}			
				}
			}
		} else{
			
		}
		
		//this is the word can be placed
		return holderBoard;
	}
	
	
	public void tester() {
//		tryPlacingWord(Word[] words, int currentWordIndex, String[][] board, int aSize, int dSize,
//				String[][] holderBoard, Vector<Word> wordsLeft)
		Word[] words = new Word[3];
		for(int i = 0; i < words.length; i++) {
			words[i] = new Word();
		}
		words[0].word = "trojans";
		words[0].across = false;
		words[1].word = "dodgers";
		words[1].across = false;
		words[2].word = "csci";
		words[2].across = false;
		
		String[][] board = new String[15][15];
		//initializing all the board to empty characters to begin with 
		for(int i = 0; i < 14 + 1; i++) {
			for(int j = 0; j< 14 + 1; j++) {
				board[j][i] = "@";
			}
		}
		String[][] holderBoard = new String[15][15];
		//initializing all the board to empty characters to begin with 
		for(int i = 0; i < 14 + 1; i++) {
			for(int j = 0; j< 14 + 1; j++) {
				holderBoard[j][i] = "@";
			}
		}
		//fake place gold and then test the tryPlacingWord funciton to see what happens
		holderBoard[5][5] = "g";
		holderBoard[5][6] = "o";
		holderBoard[5][7] = "l";
		holderBoard[5][8] = "d";
		printBoard(holderBoard, 15, 15);
		Vector<Word> werdz = new Vector<Word>();
		werdz.add(words[0]);
		werdz.add(words[1]);
		werdz.add(words[2]);
		
		boolean worked = tryPlacingWord(words, 1, board, 15, 15, holderBoard, werdz);
		System.out.println(worked);
	}
	
	public void testerHoriz() {
//		tryPlacingWord(Word[] words, int currentWordIndex, String[][] board, int aSize, int dSize,
//		String[][] holderBoard, Vector<Word> wordsLeft)
		Word[] words = new Word[6];
		for(int i = 0; i < words.length; i++) {
			words[i] = new Word();
		}
		words[0].word = "trojans";
		words[0].across = true;
		words[1].word = "dodgers";
		words[1].across = true;
		words[2].word = "csci";
		words[2].across = true;
		words[3].word = "traveler";
		words[3].across = false;
		words[4].word = "gold";
		words[4].across = false;
		words[5].word = "marshall";
		words[5].across = true;
		
		String[][] board = new String[15][15];
		//initializing all the board to empty characters to begin with 
		for(int i = 0; i < 14 + 1; i++) {
			for(int j = 0; j< 14 + 1; j++) {
				board[j][i] = "@";
			}
		}
		String[][] holderBoard = new String[15][15];
		//initializing all the board to empty characters to begin with 
		for(int i = 0; i < 14 + 1; i++) {
			for(int j = 0; j< 14 + 1; j++) {
				holderBoard[j][i] = "@";
			}
		}
		//fake place gold and then test the tryPlacingWord funciton to see what happens
//		holderBoard[5][5] = "g";
//		holderBoard[6][5] = "o";
//		holderBoard[7][5] = "l";
//		holderBoard[8][5] = "d";
//		printBoard(holderBoard, 15, 15);
		Vector<Word> werdz = new Vector<Word>();
		werdz.add(words[0]);
		werdz.add(words[1]);
		werdz.add(words[2]);
		werdz.add(words[3]);
		werdz.add(words[4]);
		werdz.add(words[5]);
		
		
		boolean worked = tryPlacingWord(words, 0, board, 15, 15, holderBoard, werdz);
		System.out.println(worked);
	}
	
	public void printBoard(String[][] board, int xSize, int ySize) {
		for(int i = 0; i < xSize; i++) {
			for(int j = 0; j < ySize; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		
		System.out.println();
	}
	
	
	public static void main(String[] arg) {
		Crossword cr = new Crossword();
		//cr.tester();
		cr.testerHoriz();
		
		
		
		
		//testing to see if the words get sorted correctly
//		Word[] across = new Word[3];
//		Word[] down = new Word[3];
//		for(int i = 0; i < across.length; i++) {
//			across[i] = new Word();
//		}
//		for(int i = 0; i < down.length; i++) {
//			down[i] = new Word();
//		}
//		across[0].word = "trojans";
//		across[1].word = "dodgers";
//		across[2].word = "csci";
//		down[0].word = "traveler";
//		down[1].word = "gold";
//		down[2].word = "marshall";
//		for(int i = 0; i < tester.length; i++) {
//			System.out.print(i + " " + tester[i].word + " ");
//		}
//		System.out.println();
//		System.out.println();
		//mergeSort(across, across.length);
		//mergeSort(down, down.length);
//		Word[] result = new Word[6];
//		for(int i = 0; i < result.length; i++) {
//			result[i] = new Word();
//		}
//		
//		//merge(result, across, down, 3, 3);
//		
//		for(int i = 0; i < result.length; i++) {
//			System.out.print(i + " " + result[i].word + " ");
//		}
		
	}
	
//	public void placeWords() {
//		
//	}

}
