package csci201;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UserClient extends Thread{
	
private BufferedReader br;
private Socket s = null;
//^ want to access this socket and thread from different methods? idrk how to do this 
	
	public UserClient(String hostname, int port) {
		//Socket s = null;
		try {
			System.out.println("Connecting to :" + port);
			//we know were connected after this point or else an exception would get thrown
			s = new Socket(hostname, port);
			//^this could be wrong
			System.out.println("Connected to:" + port);
//			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//			this.start();
//			//now we need to add in reading from the console and sending to the server
//			PrintWriter pw = new PrintWriter(s.getOutputStream());
//			Scanner scan = new Scanner(System.in);
//			while(true) {
//				String line = scan.nextLine();
//				pw.println("Ginger: " + line);
//				pw.flush();
//				
//			}
		} catch(IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		} 
//		finally {
//			try {
//				if ( s != null) {
//					s.close();
//				}
//			} catch (IOException ioe) {
//				System.out.println("ioe closing socket: " + ioe.getMessage());
//			}
//		}
	}
	
	public int getNumPlayers() {
		int numPlayers = 0;
		
		try {
			//we know were connected after this point or else an exception would get thrown
			//s = new Socket(hostname, port);
			//System.out.println("Connected to " + hostname + ":" + port);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.start();
			//now start this tread and we want to read one number from the console and send it to the
			//server
			//now we need to add in reading from the console and sending to the server
			PrintWriter pw = new PrintWriter(s.getOutputStream());
			Scanner scan = new Scanner(System.in);
			//we just want to read in one line, we want to read in the number
			while(true) {
				String line = scan.nextLine();
				pw.println("Number: " + line);
				pw.flush();
				
			}
//			String line = scan.nextLine();
//			pw.println("Number: " + line);
			//^output it now to verify that the number is correct
			//pw.flush();
		} catch(IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		} finally {
			try {
				if ( s != null) {
					s.close();
				}
			} catch (IOException ioe) {
				System.out.println("ioe closing socket: " + ioe.getMessage());
			}
		}
		
		return numPlayers;
	}
	
	public void run() {
		try {
			while(true) {
				String line = br.readLine();
				System.out.println(line);
			}
		} catch (IOException ioe) {
			System.out.println("ioe reading lines: " + ioe.getMessage());
		}
	
	}

	public static void main(String[] args) {
		UserClient uc = new UserClient("localhost", 3456);
	}

}
