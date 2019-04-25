package csci201;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class validFileContents {
	public Word[] acrossWords;
	public Word[] downWords;
	public Game g;
	
//	public void test() {
//		URL url = getClass().getResource("gamedata");
//		System.out.println(url);
//		File file = new File("gamedata");
//		try {
//			System.out.println(file.getCanonicalPath());
//		} catch(IOException ioe) {
//			System.out.println(ioe);
//		}
//		
//	}
	
	public static void main(String[] args) {
		validFileContents vf = new validFileContents();
		vf.setUpGame();
	}
	
	public void setUpGame() {
		FileSearch fs = new FileSearch();
		File file = fs.selectFile();
		System.out.println(file);
		validFileContents vfc = fs.verifyValidity(file);
		vfc = fs.checkMatches(vfc);
		System.out.println("here");
		//now we need to build the board if everything is valid 
		if(vfc != null) {
			vfc.g = new Game();
			Crossword1 cw = new Crossword1();
			cw.MakeBoardArray(vfc.acrossWords, vfc.downWords, vfc.g, vfc);
			//now test the game and see whats working 
			System.out.println("Testing");
			
			//test to see if this prints the correct board !!
			cw.printBoard(vfc.g.board, vfc.g.xSize, vfc.g.ySize);
			String[][] holderB = vfc.g.resizeBoard();
			cw.printBoard(holderB, 11, 13);
		} else {
			//notify user that the board is invalid and stuff
			System.out.println("nullll bitchhhh");
			//we need to notify the client of this and act accordingly
		}
	}
}
