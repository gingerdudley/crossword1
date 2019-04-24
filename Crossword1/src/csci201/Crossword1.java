package csci201;

import java.util.Vector;
//THE MAIN ARRAY IS Y AND THEN X!!

public class Crossword1 {
	public void MakeBoardArray(Word[] acrossWords, Word[] downWords, Game game, validFileContents vf) {
		int acrossSize = 0;
		int downSize = 0;
		for(int i = 0; i < acrossWords.length; i++) {
			acrossSize += acrossWords[i].word.length();
		}
		for(int j = 0; j < downWords.length; j++) {
			downSize += downWords[j].word.length();
		}
		
		//creating a board twice the size that would ever be used, then we know we can start in the middle
		//and not run out of room
		String[][] board = new String[(downSize * 2) + 1][(acrossSize * 2) + 1];
		//initializing all the board to empty characters to begin with 
		for(int i = 0; i < downSize * 2 + 1; i++) {
			for(int j = 0; j< acrossSize * 2+ 1; j++) {
				board[i][j] = "@";
			}
		}
		Word [] words = new Word[acrossWords.length + downWords.length];
		for(int i = 0; i < acrossWords.length; i++) {
			words[i] = acrossWords[i];
		}
		for(int j = acrossWords.length; j < downWords.length + acrossWords.length; j++) {
			words[j] = downWords[j - acrossWords.length];
		}
		//automatically place the first word in the middle of the board, then recurse until find a solution
		int middleX = acrossSize;
		int middleY = downSize;
		int firstWordLength = words[0].word.length();
		String firstWord = words[0].word;
		for(int i = 0; i < firstWordLength; i++) {
			//place the first word on the board and then recurse
			String chara = String.valueOf(firstWord.charAt(i));
			board[middleY][middleX + i] = chara;
		}		
		words[0].placed = true;
		words[0].start = new int[2];
		words[0].start[0] = middleY;
		words[0].start[1] = middleX;
		if(words[0].match) {
			//then we need to place the word that it aligns with it on the board
			//first find the word that is the match
			int matchIndex = 0;
			for(int g = 1; g < words.length; g++) {
				if(words[g].number == words[0].number) {
					matchIndex = g;
				}
			}
			Word firstMatch = words[matchIndex];
			int matchLength = firstMatch.word.length();
			for(int i = 0; i < matchLength; i++) {
				//place the first word on the board and then recurse
				String chara = String.valueOf(firstMatch.word.charAt(i));
				board[middleY + i][middleX] = chara;
			}
			words[matchIndex].placed = true;
			words[matchIndex].start = new int[2];
			words[matchIndex].start[0] = middleY;
			words[matchIndex].start[1] = middleX;			
		}		
		printBoard(board, (acrossSize * 2 + 1), (downSize * 2 + 1));
		//boolean worked = findNextWord(acrossWords.length, downWords.length, words, board);
		boolean worked = findNextWord(acrossWords.length, downWords.length, words, board, true, game, vf);
	}
	
	//this is the function that will backtrack over all the words and try to find the correct placement
	
	public boolean findNextWord(int acrossCount, int downCount, Word[] words, String[][] board, boolean firstRound, Game g, 
			validFileContents vf) {
		//this function will find the next word that can be placed on the board and then call the placeWord function
		//will start by going through the across array to try to place all words
		//vector of word indexes that still need to be placed
		
		//*******************
		//***testing to see if this prints and what it does / how it works 
		int acrossSize = 0;
		int downSize = 0;
		for(int i = 0; i < acrossCount; i++) {
			acrossSize += words[i].word.length();
		}
		for(int j = 0; j < downCount; j++) {
			downSize += words[j + acrossCount].word.length();
		}
		printBoard(board, (acrossSize * 2 + 1), (downSize * 2 + 1));
		//*********************
		//*********************
		
		Vector<Integer> acrossWordIndex = new Vector<Integer>();
		for(int i = 0; i < acrossCount; i++) {
			if(words[i].placed == true) {
				acrossWordIndex.add(i);
			}	
		}
		
		Vector<Integer> downWordIndex = new Vector<Integer>();
		for(int j = 0; j < downCount; j++) {
			if(words[j + acrossCount].placed == true) {
				downWordIndex.add(j + acrossCount);
				//we will check these words and see if we can place them with the across word
			}
		}
		
		if(downWordIndex.size() == downCount && acrossWordIndex.size() == acrossCount) {
			printBoard(board, (acrossSize * 2 + 1), (downSize * 2 + 1));
//			return true;
			vf.g.acrossWords = new Word[acrossCount];
			vf.g.downWords = new Word[downCount];
			vf.g.xSize = (acrossSize * 2 + 1);
			vf.g.ySize = (downSize * 2 + 1);
			for(int l = 0; l < acrossCount; l++) {
				vf.g.acrossWords[l] = words[l];
			}
			for(int l = 0; l < downCount; l++) {
				vf.g.downWords[l] = words[acrossCount + l];
			}
			vf.g.board = board;
			return true;
			
		} else {
			Vector<PossiblePlay> possiblePlays = new Vector<PossiblePlay>();
			//need to do certian things for down words and certian for across words
			if(downWordIndex.size() == 0) {
				//then no down words are currently placed and we need to place one with the first word
				Word firstAcross = words[0];
				for(int ii = 0; ii < downCount; ii++) {
					//get all the possible down moves
					Word downn = words[acrossCount + ii];
					for(int jj = 0; jj < firstAcross.word.length(); jj++) {
						String acrossChar = String.valueOf(firstAcross.word.charAt(jj));
						for(int gg = 0; gg < downn.word.length(); gg++) {
							String downChar = String.valueOf(downn.word.charAt(gg));
							if(downChar.equals(acrossChar)) {
								//then we need to see if this would be a legal move and then continue from there
								//check if this is a legal move
								//the start of the word would be the intersection - the # of chars before
								int startX = firstAcross.start[1] + jj;
								//something is up with this, shouldnt matter where the word starts
								//cut should be where they intersect
								int startY = firstAcross.start[0] - gg;
								int intersection = gg;
								//^these are the coordinates of where the word would be placed if legal
								boolean legal = legalDownMove(board, downn, startX, startY, intersection);
								//if this word is legal then we need to add it to the vector
								if(legal) {
									PossiblePlay play = new PossiblePlay();
									play.word = downn;
									play.xStart = startX;
									play.yStart = startY;
									play.intersection = intersection;
									possiblePlays.add(play);
									//^ this vector should hold all of the possible across plays 
									//from the current board
								}
							}
							
						}
					}
				}
			} else {
				if(downWordIndex.size() != downCount){
					//this is for words that we want to place down 
					
					//if there are down words that still need to be played
					//then we can try to place a word on this word
					//so get a vector os possible positions that the new word can go
					//create an object of these possible words
					
					//go through the vector of placed down words and compare them with the across words
					for(int a = 0; a < acrossWordIndex.size(); a++) {
						Word acrosssWord = words[acrossWordIndex.get(a)];
						for(int b = 0; b < downCount; b++) {
							//now go through the words and see if any of the letters are the same
							if(words[b + acrossCount].placed == true) {
								continue;
								//we don't want to replace this word on the board so we need to continue on
							}
							String acrossW = acrosssWord.word;
							String downW = words[b + acrossCount].word;
							Word wordDown = words[b + acrossCount];
							for(int c = 0; c < acrossW.length(); c++) {
								String acrossChar = String.valueOf(acrossW.charAt(c));
								for(int d = 0; d < downW.length(); d++) {
									String downChar = String.valueOf(downW.charAt(d));
									if(downChar.equals(acrossChar)) {
										//check if this is a legal move
										//the start of the word would be the intersection - the # of chars before
										int startX = acrosssWord.start[1] + c;
										//something is up with this, shouldnt matter where the word starts
										//cut should be where they intersect
										int startY = acrosssWord.start[0] - d;
										int intersection = d;
										//^these are the coordinates of where the word would be placed if legal
										boolean legal = legalDownMove(board, wordDown, startX, startY, intersection);
										//if this word is legal then we need to add it to the vector
										if(legal) {
											PossiblePlay play = new PossiblePlay();
											play.word = wordDown;
											play.xStart = startX;
											play.yStart = startY;
											play.intersection = intersection;
											possiblePlays.add(play);
											//^ this vector should hold all of the possible across plays 
											//from the current board
										}
										//then this is a possible play and add it to the vector of possible plays
									}
									
								}
							}
							//then check if this is a legal move
						}
					}
					
				} 
				
				if(acrossWordIndex.size() != acrossCount) {
					//then we can try to place a word on this word
					//so get a vector os possible positions that the new word can go
					//create an object of these possible words
					
					//go through the vector of placed down words and compare them with the across words
					//Vector<PossiblePlay> possiblePlays = new Vector<PossiblePlay>();
					for(int a = 0; a < downWordIndex.size(); a++) {
						Word downWord = words[downWordIndex.get(a)];
						for(int b = 0; b < acrossCount; b++) {
							//now go through the words and see if any of the letters are the same
							if(words[b].placed == true) {
								continue;
								//we don't want to replace this word on the board so we need to continue on
							}
							String downW = downWord.word;
							String acrossW = words[b].word;
							Word wordAcross = words[b];
							for(int c = 0; c < downW.length(); c++) {
								String downChar = String.valueOf(downW.charAt(c));
								for(int d = 0; d < acrossW.length(); d++) {
									String acrossChar = String.valueOf(acrossW.charAt(d));
									if(downChar.equals(acrossChar)) {
										//check if this is a legal move
										//the start of the word would be the intersection - the # of chars before
										int startX = downWord.start[1] - d;
										//something is up with this, shouldnt matter where the word starts
										//cut should be where they intersect
										int startY = downWord.start[0] + c;
										int intersection = d;
										//^these are the coordinates of where the word would be placed if legal
										boolean legal = legalAcrossMove(board, wordAcross, startX, startY, intersection);
										//if this word is legal then we need to add it to the vector
										if(legal) {
											PossiblePlay play = new PossiblePlay();
											play.word = wordAcross;
											play.xStart = startX;
											play.yStart = startY;
											play.intersection = intersection;
											possiblePlays.add(play);
											//^ this vector should hold all of the possible across plays 
											//from the current board
										}
										//then this is a possible play and add it to the vector of possible plays
									}
									
								}
							}
						}
					}
				}
			}
			
			if(possiblePlays.size() == 0) {
				return false;
			} else {
				boolean success = false;
				for(int aa = 0; aa < possiblePlays.size(); aa++) {
					//place the word on the board
					//recurse
					//if false is returned, then remove the word and try the next word in possiblePlays
					if(possiblePlays.get(aa).word.across) {
						placeWordAcross(board, possiblePlays.get(aa).word, possiblePlays.get(aa).xStart,
								possiblePlays.get(aa).yStart, possiblePlays.get(aa).intersection);
					} else {
						placeWordDown(board, possiblePlays.get(aa).word, possiblePlays.get(aa).xStart,
								possiblePlays.get(aa).yStart, possiblePlays.get(aa).intersection);
					}
					//need to mark that this word has been placed and update its start and stuff
					//Word justPlaced
					for(int zz = 0; zz < words.length; zz++) {
						//need to change some stuff on the word that you just passed in
						if(words[zz].equals(possiblePlays.get(aa).word)) {
							words[zz].placed = true;
							words[zz].start = new int[2];
							words[zz].start[0] = possiblePlays.get(aa).yStart;
							words[zz].start[1] = possiblePlays.get(aa).xStart;
							
						}
					}
						
					//then recurse and if its false then u have some problems
					boolean worked = findNextWord(acrossCount, downCount, words, board, false, g, vf);
					//if it didn't work u need to undo all of the 
					//need some marker for if its the first word placed cause then
					//you should continue up instead of returning false all the way up
					//have a boolean thats like first placed word
					if(!worked) {
						//now mark that that word hasn't been placed
						for(int zz = 0; zz < words.length; zz++) {
							//need to change some stuff on the word that you just passed in
							if(words[zz].equals(possiblePlays.get(aa).word)) {
								words[zz].placed = false;
								continue;
							}
						}
						if(possiblePlays.get(aa).word.across) {
							removeWordAcross(board, possiblePlays.get(aa).word, possiblePlays.get(aa).xStart,
									possiblePlays.get(aa).yStart, possiblePlays.get(aa).intersection);
						} else {
							removeWordDown(board, possiblePlays.get(aa).word, possiblePlays.get(aa).xStart,
									possiblePlays.get(aa).yStart, possiblePlays.get(aa).intersection);
						}
						if(!firstRound) {
							return false;
						}
						//return false;
						//^need to return up that this move didn't work
					} else {
						//if this recursive call worked than you can return true 
						//might need to return the board at some point but we'll deal w later
						success = true;
						return true;
					}
				}
				if(success) {
					return true;
				} else {
					return false;
				}
			}	
		}
	}
	
	
	//this method needs to take a word and run through the board and see if it would be a legal move
	public boolean legalAcrossMove(String[][] board, Word word, int xStart, int yStart, int intersection) {
		int length = word.word.length();
		for(int i = 0; i < length; i++) {
			//if we aren't at the intersection place, check the areas around the word and make sure theyre empty
			if(i != intersection) {
				if(i == 0) {
					//then we also need to check the letter to the left
					//might need to add some code to this to prevent a seg fault
					if(!board[yStart][xStart - 1].equals("@")) {
						//then there is a word there and it will b illegal 
						return false;
					}
				} else if(i == length - 1) {
					//we need to check the letter to the right of it
					if(!board[yStart][xStart + length + 1].equals("@")) {
						//then there is a word there and it will b illegal 
						return false;
					}
				} 
				//check the current space for a letter, check below it, and check above it
				if(!board[yStart + 1][xStart + i].equals("@")) {
					return false;
				}
				if(!board[yStart - 1][xStart + i].equals("@")) {
					return false;
				}
				if(!board[yStart][xStart + i].equals("@")) {
					return false;
				}		
			}
		}
		//if we have reached this point, then the word is legal and we can return true
		return true;
	}
	
	public boolean legalDownMove(String[][] board, Word word, int xStart, int yStart, int intersection) {
		int length = word.word.length();
		for(int i = 0; i < length; i++) {
			//if we aren't at the intersection place, check the areas around the word and make sure theyre empty
			if(i != intersection) {
				if(i == 0) {
					//then we also need to check the letter to the left
					//might need to add some code to this to prevent a seg fault
					if(!board[yStart - 1][xStart].equals("@")) {
						//then there is a word there and it will b illegal 
						return false;
					}
				} else if(i == length - 1) {
					//we need to check the letter to the right of it
					if(!board[yStart  + length + 1][xStart].equals("@")) {
						//then there is a word there and it will b illegal 
						return false;
					}
				} 
				//check the current space for a letter, check below it, and check above it
				if(!board[yStart + i][xStart + 1].equals("@")) {
					return false;
				}
				if(!board[yStart + i][xStart - 1].equals("@")) {
					return false;
				}
				if(!board[yStart + i][xStart].equals("@")) {
					return false;
				}		
			}
		}
		//if we have reached this point, then the word is legal and we can return true
		return true;
	}
	
	public void placeWordAcross(String[][] board, Word word, int xStart, int yStart, int intersection) {
		int length = word.word.length();
		for(int i = 0; i < length; i ++) {
			board[yStart][xStart + i] = String.valueOf(word.word.charAt(i));
			//adding the word to the board Temporarily
		}
	}
	
	public void placeWordDown(String[][] board, Word word, int xStart, int yStart, int intersection) {
		int length = word.word.length();
		for(int i = 0; i < length; i ++) {
			board[yStart + i][xStart] = String.valueOf(word.word.charAt(i));
			//adding the word to the board Temporarily
		}
	}

	
	public void removeWordAcross(String[][] board, Word word, int xStart, int yStart, int intersection) {
		int length = word.word.length();
		for(int i = 0; i < length; i ++) {
			if(i != intersection) {
				board[yStart][xStart + i] = "@";
			}
		}
	}
	
	public void removeWordDown(String[][] board, Word word, int xStart, int yStart, int intersection) {
		int length = word.word.length();
		for(int i = 0; i < length; i ++) {
			if(i != intersection) {
				board[yStart + i][xStart] = "@";
			}
		}
	}
	
	public void printBoard(String[][] board, int xSize, int ySize) {
		for(int i = 0; i < ySize; i++) {
			for(int j = 0; j < xSize; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		
		System.out.println();
	}
	
	public void resizeBoard() {
		
	}
	
//	public void testerHoriz() {
//		Word[] words = new Word[6];
//		for(int i = 0; i < words.length; i++) {
//			words[i] = new Word();
//		}
//		
//		//rewriting this to try for the down word and stuff
//		words[0].word = "trojans";
//		words[0].across = true;
//		words[0].number = 1;
//		words[0].match = true;
//		words[1].word = "dodgers";
//		words[1].across = true;
//		words[1].number = 2;
//		words[1].match = false;
//		words[2].word = "csci";
//		words[2].across = true;
//		words[2].number = 3;
//		words[2].match = false;
//		words[3].word = "traveler";
//		words[3].across = false;
//		words[3].number = 1;
//		words[3].match = true;
//		words[4].word = "gold";
//		words[4].across = false;
//		words[4].number = 4;
//		words[4].match = false;
//		words[5].word = "marshall";
//		words[5].across = false;
//		words[5].number = 5;
//		words[5].match = false;
//		
//		Word[] acrossWords = new Word[3];
//		Word[] downWords = new Word[3];
//		acrossWords[0] = words[0];
//		acrossWords[1] = words[1];
//		acrossWords[2] = words[2];
//		//acrossWords[3] = words[6];
//		downWords[0] = words[3];
//		downWords[1] = words[4];
//		downWords[2] = words[5];
//		
//		MakeBoardArray(acrossWords, downWords);
//	}
	
//	public static void main(String[] arg) {
//		Crossword1 cr = new Crossword1();
//		cr.testerHoriz();
//	}

}
