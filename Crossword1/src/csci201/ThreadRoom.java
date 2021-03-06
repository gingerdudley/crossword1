package csci201;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadRoom {
	
	private Vector<ServerThread> serverThreads;
	private Vector<Lock> lockVector;
	private Vector<Condition> conditionVector;
	private Vector<Condition> waitingVector;
	private Game game;
	private int num = 0;
	private int threadNum = 0;
	int playersRestarted = 1;
	public validFileContents vfc;
	FakeBoard fb;
	public ServerSocket ss;
	public Vector<Socket> socketVec; 
	public ThreadRoom(int port) {
		try {
			System.out.println("Binding to port " + port);
			ss = new ServerSocket(port);
			System.out.println("Bound to port " + port);
			serverThreads = new Vector<ServerThread>();
			lockVector = new Vector<Lock>();
			conditionVector = new Vector<Condition>();
			waitingVector = new Vector<Condition>();
			socketVec = new Vector<Socket>();
			game = new Game();
			
			boolean fileFound = false;
			vfc = new validFileContents();
			while(true) {
				Socket s = ss.accept(); // blocking
				System.out.println("Connection from: " + s.getInetAddress());
				socketVec.add(s);
				//now read the random game file
				if(fileFound == false) {
					System.out.println("Reading random game file.");
					vfc = vfc.setUpGame(game, vfc);
					if(vfc == null) {
						//then you need to pick another file and check its validity
					} else {
						fileFound = true;
					}
				}
				
				Lock lock = new ReentrantLock();
				Lock lock2 = new ReentrantLock();
				Condition con = lock.newCondition();
				Condition conn2 = lock.newCondition();
				lockVector.add(lock);
				conditionVector.add(con);
				waitingVector.add(conn2);
				num++;
				ServerThread st = new ServerThread(s, this, lock, con, num, vfc.g, lock2);
				serverThreads.add(st);
			}
		} catch (IOException ioe) {
			System.out.println("ioe in ChatRoom constructor: " + ioe.getMessage());
		}
	}
	
	public void broadcast(String message, ServerThread st) {
		if (message != null) {
			for(ServerThread threads : serverThreads) {
					threads.sendMessage(message);
			}
		}
	}
	
	public void broadcastMinusCurr(String message, ServerThread st) {
		//use this to send stuff to the other threads
		if (message != null) {
			for(ServerThread threads : serverThreads) {
				if (st != threads) {
					threads.sendMessage(message);
				}
			}
		}
	}
	
	public void printScores(ServerThread st) {
			int winnerNum = serverThreads.get(0).correctAnswers;
			int winnerID = 1;
			for(int j = 0; j < serverThreads.size(); j++) {
				if(winnerNum < serverThreads.get(j).correctAnswers) {
					winnerNum = serverThreads.get(j).correctAnswers;
					winnerID = serverThreads.get(j).num;
				} 
				st.sendMessage("Player " + 
						serverThreads.get(j).num + " - " + serverThreads.get(j).correctAnswers + 
						" correct answers.");
				}
			st.sendMessage("Player " + winnerID + " is the winner.");
	}
	
	public void deleteThread(ServerThread st) {
		for(int i = 0; i < serverThreads.size(); i++) {
			if(serverThreads.get(i) == st) {
				serverThreads.remove(i);
			}
		}
	}
	
	public void printBoard(ServerThread st) {
		st.printBoard(vfc.g.currentBoard, vfc.g.currX, vfc.g.currY);
	}
	
	public void printBoardAll() {
		//this will print the current gameboard
		for(ServerThread threads : serverThreads) {
			threads.printBoard(vfc.g.currentBoard, vfc.g.currX, vfc.g.currY);
		}
	}
	
	public void printFinalAll() {
		for(ServerThread threads : serverThreads) {
			threads.printFinal(vfc.g.currentBoard, vfc.g.currX, vfc.g.currY);
		}
	}
	
	public void endGame() {
		for(int i = 0; i < socketVec.size(); i++) {
			try {
				socketVec.get(i).close();
			} catch(IOException ioe) {
				System.out.println(ioe.getMessage());
			}
			
		}
		for(int i = 0; i < serverThreads.size(); i++) {
			this.deleteThread(serverThreads.get(0));
		}
	}
	
	public void clientUnlock(){
		//go to office hours and ask about this bc u r wrong
		threadNum = (threadNum + 1)%serverThreads.size();
		lockVector.get(threadNum).lock();
		conditionVector.get((threadNum)).signalAll();
		lockVector.get(threadNum).unlock();	
		//this will unlock the next client and accept messages from them
	}
	
	public void signalClient(int num) {
		lockVector.get(num).lock();
		conditionVector.get(num).signalAll();
		lockVector.get(num).unlock();
	}
	
	public void startingUnlock() {
		//we want to unlock the first thread
		threadNum = (threadNum)%serverThreads.size();
		lockVector.get(threadNum).lock();
		conditionVector.get((threadNum)).signalAll();
		lockVector.get(threadNum).unlock();	
	}
	
	public static void main(String [] args) {
		ThreadRoom cr = new ThreadRoom(3456);
	}
}
