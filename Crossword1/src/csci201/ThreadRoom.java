package csci201;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ThreadRoom {
	
	private int currentNumPlayers = 0;
	private int maxNumPlayers = 3;
	private Vector<ServerThread> serverThreads;
	boolean haveValidFile = false;
	
	public ThreadRoom() {
		ServerSocket ss = null;
		int port = 3456;
		try {
			//instantiate our server socket and send the operating system a request to bind to the port
			System.out.println("Trying to bind to port: " + port);
			ss = new ServerSocket(port);
			System.out.println("Bound to port " + port);
			serverThreads = new Vector<ServerThread>();
			while(true) {
				Socket s = ss.accept();
				//then we need to read a file and determine if it is valid or not
				//make this method in the serverThread class
				if(!haveValidFile) {
					//output this to the client and continue waiting for another thread
				}
				currentNumPlayers++;
				if(currentNumPlayers > maxNumPlayers) {
					//stop accepting future threads
				}
				if(currentNumPlayers == 1) {
					//ask for the number of players
					UserClient uc = new UserClient("localhost", 3456);
					int players = 0;
					while(true) {
						players = uc.getNumPlayers();
						if(players < 1 || players >3) {
							continue;
						} else {
							break;
							//^then we know that the entered number is between one and three
						}
					}
					
					System.out.println("this is the number of returned players: " + players);
					
				}
				//^this is a blocking call, were waiting for a client to connect with us
				System.out.println("Connection from " + s.getInetAddress());
				ServerThread st = new ServerThread(s, this);
				serverThreads.add(st);
				//st.start
			}
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		} finally {
			try {
				if( ss!= null) {
					ss.close();
				}
			} catch(IOException ioe) {
				System.out.println("ioe closing stuff: " + ioe.getMessage());
			}
		}
	}
	
	public void broadcast(String message, ServerThread currentST) {
		//in this we need to iterate through all the server threads and send the message back out to them
		if(message != null) {
			System.out.println(message);
			for(ServerThread st: serverThreads) {
				if(st != currentST) {
					st.sendMessage(message);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		ThreadRoom tr = new ThreadRoom();
	}
}
