package csci201;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class validFileContents {
	public Word[] acrossWords;
	public Word[] downWords;
	public Game g;
	
	public validFileContents setUpGame(Game game, validFileContents vfc) {
		FileSearch fs = new FileSearch();
		File file = fs.selectFile();
		System.out.println(file);
		vfc = fs.verifyValidity(file);
		vfc = fs.checkMatches(vfc);
		System.out.println("here");
		//now we need to build the board if everything is valid 
		if(vfc != null) {
			vfc.g = new Game();
			Crossword1 cw = new Crossword1();
			cw.MakeBoardArray(vfc.acrossWords, vfc.downWords, vfc.g, vfc);
			cw.printBoard(vfc.g.board, vfc.g.xSize, vfc.g.ySize);
			String[][] holderB = vfc.g.resizeBoard();
			return vfc;
		} else {
			return null;
			//we need to notify the client of this and act accordingly
		}
	}
}
