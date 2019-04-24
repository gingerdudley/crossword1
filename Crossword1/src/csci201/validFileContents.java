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
		//now we need to build the board from this information
	}
}
