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
	private int num = 0;
	private int threadNum = 0;
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
				ServerThread st = new ServerThread(s, this, lock, con, num);
				serverThreads.add(st);
			}
		} catch (IOException ioe) {
			System.out.println("ioe in ChatRoom constructor: " + ioe.getMessage());
		}
	}
	
	public void broadcast(String message, ServerThread st) {
		if (message != null) {
			System.out.println(message);
			for(ServerThread threads : serverThreads) {
				if (st != threads) {
					threads.sendMessage(message);
				}
			}
		}
	}
	
	public void clientUnlock(){
		threadNum = (threadNum + 1)%serverThreads.size();
		lockVector.get(threadNum).lock();
		conditionVector.get((threadNum)).signalAll();
		lockVector.get(threadNum).unlock();	
		//this will unlock the next client and accept messages from them
	}
	
	public static void main(String [] args) {
		ThreadRoom cr = new ThreadRoom(6789);
	}
}
