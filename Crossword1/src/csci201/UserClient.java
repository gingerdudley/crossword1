package csci201;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UserClient extends Thread {

	private BufferedReader br;
	private PrintWriter pw;
	private Socket s;
	public UserClient(String hostname, int port) {
		try {
			System.out.println("Trying to connect to " + hostname + ":" + port);
			s = new Socket(hostname, port);
			System.out.println("Connected to " + hostname + ":" + port);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
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
		System.out.println("Welcome to Crossword!");
		System.out.println("Please enter a hostname");
		BufferedReader reader =  
                new BufferedReader(new InputStreamReader(System.in)); 
		String hostname = "@"; 
		int port = 0;
		try {
			hostname = reader.readLine(); 
			System.out.println("Now please enter a portnuber");
			port = Integer.valueOf(reader.readLine()); 
			
		} catch(IOException ioe) {
			System.out.println("IOException: " + ioe.getMessage());
		}
		
		
		UserClient cc = new UserClient(hostname, port);
	}
}