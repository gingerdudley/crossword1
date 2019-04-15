package csci201;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UserClient extends Thread{
	
private BufferedReader br;
	
	public UserClient(String hostname, int port) {
		Socket s = null;
		try {
			System.out.println("Connecting to " + hostname + ":" + port);
			//we know were connected after this point or else an exception would get thrown
			s = new Socket(hostname, port);
			System.out.println("Connected to " + hostname + ":" + port);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.start();
			//now we need to add in reading from the console and sending to the server
			PrintWriter pw = new PrintWriter(s.getOutputStream());
			Scanner scan = new Scanner(System.in);
			while(true) {
				String line = scan.nextLine();
				pw.println("Ginger: " + line);
				pw.flush();
				
			}
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
