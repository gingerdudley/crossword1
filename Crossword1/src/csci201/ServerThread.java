package csci201;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
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
	public ServerThread(Socket s, ThreadRoom cr, Lock lock, Condition con, int number) {
		try {
			this.cr = cr;
			pw = new PrintWriter(s.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			g = new Game();
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
	
	public void run() {
		try {
			while(true) {
				lock.lock();
				if(!first && !g.isGameReady()) {
					//then we want to wait for the next people to join
				}
				if(!first) {
					//signal the first player
					//function in thread room saying someone joined , tell the other players that they've joined
					//then we have a client and need to await
					g.setCurrPlayers((g.getCurrPlayers()) + 1);
					try {
						con.await();
						//waiting for the condition
					} catch(InterruptedException ie) {
						System.out.println(ie.getMessage());
					}
				} else {
					boolean playGame = false;
					int numPlayers = 0;
					while(!playGame) {
						this.sendMessage("Please enter the number of players: ");
						String line = br.readLine();
						//verify number
						numPlayers = Integer.valueOf(line);
						if(numPlayers < 1 || numPlayers > 3) {
							//need to prompt the user again for the number of players
						}
					}		
					g.setNumPlayers(numPlayers);
					g.setCurrPlayers((g.getCurrPlayers()) + 1);		
				}
				first = false;
				//now the condition is met and we can continue on
				boolean stop = false;
				while(!stop) {
					String line = br.readLine();
					if(line.contains("END_OF_MESSAGE")) {
						cr.broadcast("done sending messages", this);
						stop = true;
						lock.unlock();
						cr.clientUnlock();
					} else {
						cr.broadcast(line, this);
					}
				}
			}
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//this is for the file stuff, maybe move this to another class to clean up the server thread class
	public File selectFile() {
		
		//make sure to make this relative instead of what you have it as rn 
		File folder = new File("/Users/gingerdudley/git/crossword1/Crossword1/gamedata");
		File[] listOfFiles = folder.listFiles();
		//System.out.println(listOfFiles.length);
		if(listOfFiles.length == 1) {
			//then there is only one file and we need to return that file to the main game for game play
			return listOfFiles[0];
		} else {
			//randomly select a file from this array to return
			Random rand = new Random();
			int max = listOfFiles.length;
			int randomNum = rand.nextInt((max - 0));
			//System.out.println("Random number: " + randomNum);
			return listOfFiles[randomNum];
		}
		//System.out.println(listOfFiles[0].getName());
//		for (File file : listOfFiles) {
//		    if (file.isFile()) {
//		        System.out.println(file.getName());
//		    }
//		}
	}
	
public boolean verifyValidity(File f) {
		
		//actually maybe well return the file contents and return null if the file isnt valid ?
		//decide this later
		Vector<Word> acrossV = new Vector<Word>();
		Vector<Word> downV = new Vector<Word>();
		//need to keep count of the across and down words to add to the arrays at the end of this
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			int aNum = 0, dNum = 0;
			boolean acrossFound = false, downFound = false;
			line = br.readLine();
			line = line.toLowerCase();
			if(!line.equals("across") && !line.equals("down")) {
				return false;
			} else if(line.equals("across")){
				acrossFound = true;
				while(!downFound) {
					while ((line = br.readLine()) != null) {
						line = line.toLowerCase();
						//process all the contents for the across section
						if(line.equals("across")) {
							return false;
						} else if(line.equals("down") && aNum == 0) {
							return false;
							//there are no arguments for the across
						} else if(line.equals("down")) {
							downFound = true;
							break;
						} else {
							aNum++;
							//parse out the line and decide if its valid
							Word returnedWord = ParseData(line, true);
							if(returnedWord == null) {
								return false;
							}
							acrossV.add(returnedWord);
							//add this word to the across array
						}
					}
					if(downFound == false) {
						return false;
						//then youre at the end of the file but you have only had across words
					}
				}
				
			} 
			System.out.println("line value: " + line);
			if(line.equals("down")) {		
				downFound = true;
				while ((line = br.readLine()) != null) {
					line = line.toLowerCase();
					//process all the contents for the across section
					if(line.equals("across") && acrossFound == false && dNum != 0) {
						//return false;
						//then u need to parse out them words
						while ((line = br.readLine()) != null) {
							line = line.toLowerCase();
							if(line.equals("across")) {
								return false;
							} else if(line.equals("down") && aNum == 0) {
								return false;
								//there are no arguments for the across
							} else if(line.equals("down")) {
								return false;
							} else {
								aNum++;
								//parse out the line and decide if its valid
								Word returnedWord = ParseData(line, true);
								if(returnedWord == null) {
									return false;
								}
								acrossV.add(returnedWord);
								//add this word to the across array
							}
						}
						break;
//						aNum++;
//						Word returnedWord = ParseData(line, true);
//						acrossV.add(returnedWord);
						
					} else if((line.equals("across") && acrossFound == true) || (line.equals("across") && dNum == 0)){
						return false;
					} else if(line.equals("down")) {
						return false;
					} else {
						dNum++;
						//parse out the line and decide if its valid
						Word returnedWord = ParseData(line, false);
						if(returnedWord == null) {
							return false;
						}
						downV.add(returnedWord);
						//add this word to the across array
					}
				}
			}
			if(aNum == 0 || dNum == 0) {
				return false;
			}
			//check to make sure this 1st line is across or down
//			while ((line = br.readLine()) != null) {
//				//now process the line
//			}
			fr.close();
			br.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
		return true;
	}
	
	private Word ParseData(String line, boolean across) {
		String beginningLine = line;
		int parameterCount = 0;
		int paramNum = 0;
		Word word = new Word();
		StringTokenizer paramToken = new StringTokenizer(line, "|");
		while(paramToken.hasMoreTokens()) {
			paramToken.nextToken();
			paramNum++;
		}
		if(paramNum < 2) {
			//then we have a problem and need to return null
		} else {
			
			if(across) {
				word.across = true;
			} else {
				word.across = false;
			}
			//then we can actually start parsing the data
			StringTokenizer token = new StringTokenizer(line, "|");
			while(token.hasMoreTokens()) {
				//keep reading in the next thing and then see if its formatted correctly
				if(parameterCount == 0) {
					String attempt = token.nextToken();
					try {
						int num = Integer.parseInt(attempt);
						word.number = num;
						//city.setDayLow(low);		
					} catch(Exception e) {
						System.out.println("problem converting to an int");
						return null;
						//^change this to null once u fix that aspect of this to return the correct type
					}
				} else if(parameterCount == 1) {
					//make sure that the answer doesnt have any white space
					String attempt = token.nextToken();
					//make sure that this string doesnt have any white space
					Pattern pattern = Pattern.compile("\\s");
					Matcher matcher = pattern.matcher(attempt);
					boolean found = matcher.find();
					if(found) {
						return null;
						//then there is white space in the answer and this is illegalll
					} else {
						word.word = attempt;
					}
					
				} else if (parameterCount == 2) {
					String attempt = token.nextToken();
					//^this sting will contain the question
					word.question = attempt;	
				}
				parameterCount++;
			}
		}
		return word;
	}
}

