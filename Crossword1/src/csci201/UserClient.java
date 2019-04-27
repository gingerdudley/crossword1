package csci201;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserClient extends Thread {

	private BufferedReader br;
	private PrintWriter pw;
	private Socket s;
	private Lock lock;
	private Condition con;
	private boolean stopPlay;
	public UserClient(String hostname, int port) {
		try {
			System.out.println("Trying to connect to " + hostname + ":" + port);
			s = new Socket(hostname, port);
			System.out.println("Connected to " + hostname + ":" + port);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			Lock lock = new ReentrantLock();
			Condition con = lock.newCondition();
			stopPlay = false;
			if(stopPlay) {
				try {
					con.await();
				} catch(InterruptedException ie) {
					System.out.println(ie.getMessage());
				}
				
			}
			pw = new PrintWriter(s.getOutputStream());
			this.start();
			Scanner scan = new Scanner(System.in);
			while(true) {
				String line = scan.nextLine();
				pw.println(line);
				pw.flush();
			}
			
		} catch (IOException ioe) {
			System.out.println("ioe in ChatClient constructor: " + ioe.getMessage());
		}
	}
	public void run() {
		try {
			while(true) {
				String line = br.readLine();
				System.out.println(line);
			}
		} catch (IOException ioe) {
			System.out.println("ioe in ChatClient.run(): " + ioe.getMessage());
		}
	}
	
	
	
	public static void main(String [] args) {
		System.out.println("Welcome to 201 Crossword!");
		System.out.println("Enter the server hostname: ");
		BufferedReader reader =  
                new BufferedReader(new InputStreamReader(System.in)); 
		String hostname = "@"; 
		int port = 0;
		try {
			hostname = reader.readLine(); 
			System.out.println("Enter the server port: ");
			port = Integer.valueOf(reader.readLine()); 
			int i = 0;
			
		} catch(IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
		}
		
		
		UserClient cc = new UserClient(hostname, port);
	}
}