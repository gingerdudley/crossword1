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
	FakeBoard fb;
	//private int 
	public ThreadRoom(int port) {
		try {
			System.out.println("Binding to port " + port);
			ServerSocket ss = new ServerSocket(port);
			System.out.println("Bound to port " + port);
			serverThreads = new Vector<ServerThread>();
			lockVector = new Vector<Lock>();
			conditionVector = new Vector<Condition>();
			waitingVector = new Vector<Condition>();
			game = new Game();
			fb = new FakeBoard();
			game.board = fb.makeFakeB();
			game.currentBoard = fb.makeFakeC();
			fb.setFakeGame(game);
			//also setting up some fake stuff with the board
			game.ySize = 13;
			game.xSize = 11;
			//^^change this when you actually start integrating the real functional board
			//set the board rn to the fake board but make note to change this later
			while(true) {
				Socket s = ss.accept(); // blocking
				System.out.println("Connection from: " + s.getInetAddress());
				Lock lock = new ReentrantLock();
				Condition con = lock.newCondition();
				Condition conn2 = lock.newCondition();
				lockVector.add(lock);
				conditionVector.add(con);
				waitingVector.add(conn2);
				num++;
				ServerThread st = new ServerThread(s, this, lock, con, num, game);
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
	
	public void printBoard() {
		//this will print the current gameboard
		for(ServerThread threads : serverThreads) {
			//if (st != threads) {
				threads.printBoard(game.board, game.xSize, game.ySize);
			//}
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
