package csci201;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ServerThread extends Thread{
	
	private PrintWriter pw;
	private BufferedReader br;
	private ThreadRoom cr;
	private Lock lock;
	private Condition con;
	public int num;
	private boolean first;
	private Game g;
	public int correctAnswers;
	private Lock commonlock;
	public ServerThread(Socket s, ThreadRoom cr, Lock lock, Condition con, int number, Game game, Lock lock2) {
		try {
			this.cr = cr;
			pw = new PrintWriter(s.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			g = game;
			correctAnswers = 0;
			g.setCurrPlayers(g.getCurrPlayers() + 1);
			this.lock = lock;
			this.con = con;
			this.num = number;
			this.commonlock = lock2;
			if(num == 1) {
				first = true;
			} else {
				first = false;
			}
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
		}
	}

	public void sendMessage(String message) {
		pw.println(message);
		pw.flush();
	}
	
	public void printBoard(String[][] board, int xSize, int ySize) {
		for(int i = 0; i < ySize; i++) {
			for(int j = 0; j < xSize; j++) {
				pw.print(board[i][j] + " ");
			}
			pw.println();
		}
		if(g.acrossWordsC.size() != 0) {
			pw.println("ACROSS");
		}	
		for(int i = 0; i < g.acrossWordsC.size(); i++) {
			pw.print(g.acrossWordsC.get(i).number + " ");
			pw.println(g.acrossWordsC.get(i).question);
		}
		pw.println();
		if(g.downWordsC.size() != 0) {
			pw.println("DOWN");
		}		
		for(int i = 0; i < g.downWordsC.size(); i++) {
			pw.print(g.downWordsC.get(i).number + " ");
			pw.println(g.downWordsC.get(i).question);
		}
		pw.println();
		pw.flush();
	}
	
	public void printFinal(String[][] board, int xSize, int ySize) {
		for(int i = 0; i < ySize; i++) {
			for(int j = 0; j < xSize; j++) {
				pw.print(board[i][j] + " ");
			}
			pw.println();
		}
		pw.println("ACROSS");
		for(int i = 0; i < g.acrossWords.length; i++) {
			pw.print(g.acrossWords[i].number + " ");
			pw.println(g.acrossWords[i].question);
		}
		pw.println();
		pw.println("DOWN");
		for(int i = 0; i < g.downWords.length; i++) {
			pw.print(g.downWords[i].number + " ");
			pw.println(g.downWords[i].question);
		}
		pw.println();
		pw.flush();
		
		this.sendMessage("Final Score: ");
		cr.printScores(this);
		
	}
	
	public void run() {
		try {	
				boolean isReady = g.isGameReady();
				System.out.println("game is ready : " + isReady);
				lock.lock();
						
				if(first) {
					commonlock.lock();
					boolean validNum = false;
					int numPlayers = 0;
					while(!validNum) {
						this.sendMessage("Please enter the number of players: ");
						String line = br.readLine();
						//verify number
						numPlayers = Integer.valueOf(line);
						if(numPlayers < 1 || numPlayers > 3) {
							//need to prompt the user again for the number of players
						} else {
							validNum = true;
						}
					}		
					g.setNumPlayers(numPlayers);
					first = false;
					commonlock.unlock();
					//unlock the lock until here
				} else {
					//make a common lock
					commonlock.lock();
					commonlock.unlock();
					//make the other people wait
					//just lock it and unlock it
					if(num > g.getNumPlayers()) {
						this.sendMessage("game is full");
						cr.deleteThread(this);
						while(true) {
							int hi = 0;
							hi += 1;
							hi -= 1;
						}
					}
					//tell them that the game is already full if the player num is maxed
				}
				
				System.out.println("game is ready" + g.isGameReady());
				if(!g.isGameReady() && !g.started) {
					//then we want to wait for the next people to join
					//code block at this line
					if(!isReady) {
						System.out.println("Number of players: " + g.getCurrPlayers());
						cr.broadcast("Waiting for player : " + (g.getCurrPlayers() + 1), this);
					}
					//make this an await and signal if the game is ready
					while(!isReady) {
						con.await();
						isReady = g.isGameReady();	
					}
				} else {
					//if youre player two you signal player 1 and player 3 signals player 1 and 2
					if(num == 2) {
						cr.signalClient(0);
					} else if (num == 3) {
						cr.signalClient(0);
						cr.signalClient(1);
					}
				}
				
				
				g.started = true;
				this.sendMessage("The game is beginning");
				boolean round1 = true;
			while(true) {
				//now we need to put an await back on one and lock one
				try {
					System.out.println("NUM: " + num + "getnumplayers: " + g.getNumPlayers());	
					if(num != 1) {
						con.await();
						System.out.println("made it past the await");
						//await here for the other stuff
					}		
					//waiting for the condition
				} catch(InterruptedException ie) {
					System.out.println(ie.getMessage());
				}
				
				System.out.println("made it here");
				this.playMove();
			}
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void playMove() {
		try {
			//return here when its the next players turn??
			boolean validAnswer = false;
			boolean stop = false;
			//use this to determine what user should be going
			boolean firstPass = true;
			cr.printBoardAll();
			while(!validAnswer) {
				//ask the current player to make a move and then parse it
				if(stop) {
					cr.clientUnlock();
					validAnswer = true;
					lock.lock();
					try {
						con.await();
						firstPass = true;
						cr.printBoardAll();
						stop = false;
						validAnswer = false;
					} catch(InterruptedException ie) {
						System.out.println(ie.getMessage());
					}
				}
				this.sendMessage("Would you like to answer a question across (a) or down (d) ?");
				if(firstPass) {
					cr.broadcastMinusCurr("Player's " + num + " turn", this);
					firstPass = false;
				}
				String line = br.readLine();
				line = line.toLowerCase();
				boolean numValid = false;
				int numm = 0;
				if(line.equals("d")) {
					int index = 0;
					while(!numValid) {
						this.sendMessage("Which number?");
						line = br.readLine();
						numm = Integer.valueOf(line);
						boolean found = false;
						for(int i = 0; i < g.downWords.length; i++) {
							if(g.downWords[i].number == numm) {
								found = true;
								index = i;
							}
						}
						if(!found) {
							this.sendMessage("That is not a valid option");
						} else {
							numValid = true;
						}
					}
					this.sendMessage("What is your guess for " + numm + " down?");
					line = br.readLine();
					line = line.toLowerCase();
					//now do some checking but rn just print a simple statement
					this.sendMessage("You guessed: " + line);
					cr.broadcast("Player " + num + " guessed '" + line + "' for "
							+ numm + " down", this);
					if(line.equals(g.downWords[index].word)) {
						this.sendMessage("Correct!");
						this.correctAnswers += 1;
						cr.broadcastMinusCurr("That is correct.", this);
						this.placeWordOnBoard(false, index, g.downWords[index]);
					} else {
						this.sendMessage("That is incorrect!");
						cr.broadcastMinusCurr("That is incorrect.", this);
						stop = true;
					}
				} else if(line.equals("a")){
					int index = 0;
					while(!numValid) {
						this.sendMessage("Which number?");
						line = br.readLine();
						numm = Integer.valueOf(line);
						boolean found = false;
						for(int i = 0; i < g.acrossWords.length; i++) {
							if(g.acrossWords[i].number == numm) {
								found = true;
								index = i;
							}
						}
						if(!found) {
							this.sendMessage("That is not a valid option");
						} else {
							numValid = true;
						}
					}
					this.sendMessage("What is your guess for " + numm + " across?");
					line = br.readLine();
					line = line.toLowerCase();
					//now do some checking but rn just print a simple statement
					this.sendMessage("You guessed: " + line);
					
					cr.broadcast("Player " + num + " guessed '" + line + "' for "
							+ numm + " across", this);
					if(line.equals(g.acrossWords[index].word)) {
						this.sendMessage("Correct!");
						this.correctAnswers += 1;
						cr.broadcastMinusCurr("That is correct.", this);
						this.placeWordOnBoard(true, index, g.acrossWords[index]);
					} else {
						this.sendMessage("That is incorrect!");
						cr.broadcastMinusCurr("That is incorrect.", this);
						stop = true;
						//now it is the next players turn!!
					}
				} else {
					this.sendMessage("That is not a valid option");
					continue;
				}
			}
		} catch(IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
	
	public void placeWordOnBoard(boolean across, int index, Word word) {
		//grab the word from the the answer and place it on the guessed board
		if(across) {
			//then place the word across starting at that index
			int x = (g.acrossWords[index].start[1] - g.minX) * 2 + 1;
			int y = g.acrossWords[index].start[0] - g.minY;
			for(int i = 0; i < g.acrossWords[index].word.length(); i++) {
				g.currentBoard[y][x + (i * 2)] = String.valueOf(g.acrossWords[index].word.charAt(i));
			}
			for(int i = 0; i < g.acrossWordsC.size(); i++) {
				if(g.acrossWordsC.get(i).equals(word)) {
					g.acrossWordsC.remove(i);
					break;
				}
			}
		} else {
			int x = (g.downWords[index].start[1] - g.minX) * 2 + 1;
			int y = g.downWords[index].start[0] - g.minY;
			for(int i = 0; i < g.downWords[index].word.length(); i++) {
				g.currentBoard[y + i][x] = String.valueOf(g.downWords[index].word.charAt(i));
			}
			for(int i = 0; i < g.downWordsC.size(); i++) {
				if(g.downWordsC.get(i).equals(word)) {
					g.downWordsC.remove(i);
					break;
				}
			}
		}		
		if(g.acrossWordsC.size() == 0 && g.downWordsC.size() == 0) {
			cr.printFinalAll();
			if(cr.playersRestarted != g.getNumPlayers()) {
				cr.playersRestarted += 1;
				//maybe make a while true loop to keep them here until they get deleted??
			} else {
				//then the last player has gotten here and we must restart the game
				cr.endGame();
			}
		} else {
			cr.printBoardAll();
		}	
	}	
}

