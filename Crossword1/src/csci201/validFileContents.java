package csci201;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class validFileContents {
	Word[] acrossWords;
	Word[] downWords;
	
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
		FileSearch fs = new FileSearch();
		File file = fs.selectFile();
		System.out.println(file);
		validFileContents vfc = fs.verifyValidity(file);
		vfc = fs.checkMatches(vfc);
		System.out.println("here");
		//now we need to build the board if everything is valid 
		if(vfc != null) {
			Crossword1 cw = new Crossword1();
			cw.MakeBoardArray(vfc.acrossWords, vfc.downWords);
		} else {
			//notify user that the board is invalid and stuff
		}
	}
}
