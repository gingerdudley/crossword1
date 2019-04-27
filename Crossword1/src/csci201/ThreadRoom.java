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
	public validFileContents vfc;
	FakeBoard fb;
	public ServerSocket ss;
	//private int 
	public ThreadRoom(int port) {
		try {
			System.out.println("Binding to port " + port);
			ss = new ServerSocket(port);
			System.out.println("Bound to port " + port);
			serverThreads = new Vector<ServerThread>();
			lockVector = new Vector<Lock>();
			conditionVector = new Vector<Condition>();
			waitingVector = new Vector<Condition>();
			game = new Game();
			
			//changing everything here
			//fb = new FakeBoard();
			//game.board = fb.makeFakeB();
			//game.currentBoard = fb.makeFakeC();
			//fb.setFakeGame(game);
			
			//need to change this it is WRONGGGGGGGGGGG
			
			//also setting up some fake stuff with the board
			//game.ySize = 13;
			//game.xSize = 11;
			//^^change this when you actually start integrating the real functional board
			//set the board rn to the fake board but make note to change this later
			boolean fileFound = false;
			vfc = new validFileContents();
			while(true) {
				Socket s = ss.accept(); // blocking
				System.out.println("Connection from: " + s.getInetAddress());
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
			//System.out.println(message);
			for(ServerThread threads : serverThreads) {
				//if (st != threads) {
					threads.sendMessage(message);
				//}
			}
		}
	}
	
	public void broadcastMinusCurr(String message, ServerThread st) {
		//use this to send stuff to the other threads
		if (message != null) {
			//System.out.println(message);
			for(ServerThread threads : serverThreads) {
				if (st != threads) {
					threads.sendMessage(message);
				}
			}
		}
	}
	
	public void printScores(ServerThread st) {
		for(ServerThread threads : serverThreads) {
			threads.sendMessage("Player " + threads.num + " - " + threads.correctAnswers + 
					" correct answers.");
		}
	}
	
	public void deleteThread(ServerThread st) {
		for(int i = 0; i < serverThreads.size(); i++) {
			if(serverThreads.get(i) == st) {
				serverThreads.remove(i);
			}
		}
	}
	
	public void printBoard(ServerThread st) {
		//st.printBoard(game.board, game.xSize, game.ySize);
		st.printBoard(vfc.g.currentBoard, vfc.g.currX, vfc.g.currY);
	}
	
	public void printBoardAll() {
		//this will print the current gameboard
		for(ServerThread threads : serverThreads) {
			//threads.printBoard(game.board, game.xSize, game.ySize);
			threads.printBoard(vfc.g.currentBoard, vfc.g.currX, vfc.g.currY);
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
