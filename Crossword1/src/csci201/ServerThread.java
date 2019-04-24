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
	private int num;
	private boolean first;
	private Game g;
	public ServerThread(Socket s, ThreadRoom cr, Lock lock, Condition con, int number, Game game) {
		try {
			this.cr = cr;
			pw = new PrintWriter(s.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			g = game;
			g.setCurrPlayers(g.getCurrPlayers() + 1);
			this.lock = lock;
			this.con = con;
			this.num = number;
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
		pw.println("ACROSS");
		for(int i = 0; i < g.acrossWordsC.size(); i++) {
			pw.print(g.acrossWordsC.get(i).number + " ");
			pw.println(g.acrossWordsC.get(i).question);
		}
		pw.println();
		pw.println("DOWN");
		for(int i = 0; i < g.downWordsC.size(); i++) {
			pw.print(g.downWordsC.get(i).number + " ");
			pw.println(g.downWordsC.get(i).question);
		}
		pw.println();
		pw.flush();
	}
	
	public void run() {
		try {	
				boolean isReady = g.isGameReady();
				System.out.println("game is ready : " + isReady);
				lock.lock();
						
				if(first) {
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
				} else {
					//signal the first player
					//function in thread room saying someone joined , tell the other players that they've joined
					//then we have a client and need to await
					//g.setCurrPlayers((g.getCurrPlayers()) + 1);
//					try {
//						con.await();
//						//waiting for the condition
//					} catch(InterruptedException ie) {
//						System.out.println(ie.getMessage());
//					}
				}
				
				System.out.println("game is ready" + g.isGameReady());
				if(!g.isGameReady() && !g.started) {
					//then we want to wait for the next people to join
					//code block at this line
					if(!isReady) {
						System.out.println("current players in game: " + g.getCurrPlayers());
						System.out.println("num players in game: " + g.getNumPlayers());
						cr.broadcast("Waiting for player : " + (g.getCurrPlayers() + 1), this);
					}
					//make this an await and signal if the game is ready
					while(!isReady) {
						isReady = g.isGameReady();
					}
				} 
				
				
				g.started = true;
				cr.broadcast("The game is beginning", this);
				//now we need to start the actual gameplay
				//now send the board to the players
				
				//maybe put the while true loop here
				boolean round1 = true;
			while(true) {
				cr.printBoard();
				try {
					if(num != g.getNumPlayers()) {
						con.await();
						System.out.println("made it past the await");
						//await here for the other stuff
					}				
					//waiting for the condition
				} catch(InterruptedException ie) {
					System.out.println(ie.getMessage());
				}
				
				//now add a condition if its the start of the game
				if(round1) {
					round1 = false;
					cr.clientUnlock();
					//test this out and see was up
				}
				System.out.println("made it here");
				this.playMove();
			}
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
		}
	}
	
	
	
	public void playMove() {
		try {
			//return here when its the next players turn??
			boolean validAnswer = false;
			boolean stop = false;
			//use this to determine what user should be going
			boolean firstPass = true;
			while(!validAnswer) {
				//ask the current player to make a move and then parse it
				if(stop) {
					lock.unlock();
					cr.clientUnlock();
					validAnswer = true;
					return;
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
					//now do some checking but rn just print a simple statement
					this.sendMessage("You guessed: " + line);
					cr.broadcast("Player " + num + " guessed '" + line + "' for "
							+ numm + " down", this);
					if(line.equals(g.downWords[index].word)) {
						this.sendMessage("Correct!");
						cr.broadcast("That is correct.", this);
						this.placeWordOnBoard(true, index, g.downWords[index]);
					} else {
						this.sendMessage("That is incorrect!");
						cr.broadcast("That is incorrect.", this);
						stop = true;
					}
				} else if(line.equals("a")){
					int index = 0;
					while(!numValid) {
						this.sendMessage("Which number?");
						line = br.readLine();
						numm = Integer.valueOf(line);
						boolean found = false;
	//					int index = 0;
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
					//now do some checking but rn just print a simple statement
					this.sendMessage("You guessed: " + line);
					
					cr.broadcast("Player " + num + " guessed '" + line + "' for "
							+ numm + " across", this);
					if(line.equals(g.acrossWords[index].word)) {
						this.sendMessage("Correct!");
						cr.broadcast("That is correct.", this);
						this.placeWordOnBoard(true, index, g.acrossWords[index]);
					} else {
						this.sendMessage("That is incorrect!");
						cr.broadcast("That is incorrect.", this);
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
			int x = g.acrossWords[index].start[1];
			int y = g.acrossWords[index].start[0];
			for(int i = 0; i < g.acrossWords[index].word.length(); i++) {
				g.currentBoard[y][x + i] = String.valueOf(g.acrossWords[index].word.charAt(i));
			}
			for(int i = 0; i < g.acrossWordsC.size(); i++) {
				if(g.acrossWordsC.get(i).equals(word)) {
					g.acrossWordsC.remove(i);
					break;
				}
			}
		} else {
			int x = g.downWords[index].start[1];
			int y = g.downWords[index].start[0];
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
		this.printBoard(g.currentBoard, g.xSize, g.ySize);
	}
	
}

