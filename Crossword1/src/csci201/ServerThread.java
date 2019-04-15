package csci201;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


public class ServerThread extends Thread{
	
	private BufferedReader br;
	private PrintWriter pw;
	private ThreadRoom tr;

	
	//possibly add a boolean to flag if its the first player
	//instead of thread starting automatically, start in threadroom
	public ServerThread(Socket s, ThreadRoom _tr) {
		tr = _tr;
		try {
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream());
			//this.start();
			//^add this to threadroom instead
			//^we only want to start this thread and run it once were connect with the client
		} catch (IOException ioe) {
			System.out.println("ioe getting streams: " + ioe.getMessage());
		}
		
	}
	
	public void run() {
		try {
			while(true) {
				//^this will receive as many lines as the client wants to send to us
				String line = br.readLine();
				tr.broadcast(line, this);
			}
		} catch (IOException ioe) {
			System.out.println("ioe reading line: " + ioe.getMessage());
		}
		
	}
	
	public void sendMessage(String message) {
		pw.println(message);
		//^must use this because it has a new line character so then in the run method we can actually read something
		pw.flush();
	}
	
//	public static void main(String[] args) {
//		//try to obtain the file that will be used once the first user has connected to the port
//		//ServerThread s = new ServerThread();
//		//File file = this.selectFile();
//		//now you need to start parsing through this new file to select if it is properly formatted
//	}
	
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
		//you need to check that the file passed in is valid 
		//rules to check:
		//the words across and down exist on their own lines and are only one time in the file
		//the remaining parameters are formatted with 3 parameters - integer, answer, question
		// if an across and a down share a number, they start with the same letter
		//an answer doesnt have any whitespace
		
		
		//return true if the file is valid
		//lets just assume for now that it is a valid file
		return true;
	}
}

