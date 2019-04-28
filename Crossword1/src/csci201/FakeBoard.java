package csci201;

import java.util.Vector;

public class FakeBoard {
	public String[][] makeFakeB(){
		String[][] fake = new String[13][11];
		for(int i = 0; i < 13; i++) {
			for(int j = 0; j < 11; j++) {
				fake[i][j] = "@";
			}
		}
		String word = "dodgers";
		for(int i = 0; i < 7; i++) {
			fake[9][i] = String.valueOf(word.charAt(i));
		}
		word = "traveler";
		for(int i = 0; i < 8; i++) {
			fake[5 + i][4] = String.valueOf(word.charAt(i));
		}
		word = "trojans";
		for(int i = 0; i < 7; i++) {
			fake[5][4 + i] = String.valueOf(word.charAt(i));
		}
		word = "gold";
		for(int i = 0; i < 4; i++) {
			fake[4 + i][6] = String.valueOf(word.charAt(i));
		}
		word = "csci";
		for(int i = 0; i < 4; i++) {
			fake[3][7 + i] = String.valueOf(word.charAt(i));
		}
		word = "marshall";
		for(int i = 0; i < 8; i++) {
			fake[i][8] = String.valueOf(word.charAt(i));
		}
		return fake;
	}
	
	public String[][] makeFakeC(){
		String[][] fake = new String[13][11];
		for(int i = 0; i < 13; i++) {
			for(int j = 0; j < 11; j++) {
				fake[i][j] = "@";
			}
		}
		return fake;
	}
	
	public void setFakeGame(Game g) {
		Word[] words = new Word[6];
		for(int i = 0; i < words.length; i++) {
			words[i] = new Word();
		}
		
		//rewriting this to try for the down word and stuff
		words[0].word = "trojans";
		words[0].across = true;
		words[0].number = 1;
		words[0].match = true;
		words[0].question = "What is USC’s mascot?";
		words[0].start = new int[2];
		words[0].start[0] = 5;
		words[0].start[1] = 4;
		words[1].word = "dodgers";
		words[1].across = true;
		words[1].number = 2;
		words[1].match = false;
		words[1].question = "What professional baseball team is closest to USC?";
		words[1].start = new int[2];
		words[1].start[0] = 9;
		words[1].start[1] = 0;
		words[2].word = "csci";
		words[2].across = true;
		words[2].number = 3;
		words[2].match = false;
		words[2].question = "What is the four-letter prefix for Computer Science?";
		words[2].start = new int[2];
		words[2].start[0] = 3;
		words[2].start[1] = 7;
		words[3].word = "traveler";
		words[3].across = false;
		words[3].number = 1;
		words[3].match = true;
		words[3].question = "What is the name of USC’s white horse?";
		words[3].start = new int[2];
		words[3].start[0] = 5;
		words[3].start[1] = 4;
		words[4].word = "gold";
		words[4].across = false;
		words[4].number = 4;
		words[4].match = false;
		words[4].question = "What is one of USC’s colors?";
		words[4].start = new int[2];
		words[4].start[0] = 4;
		words[4].start[1] = 6;
		words[5].word = "marshall";
		words[5].across = false;
		words[5].number = 5;
		words[5].match = false;
		words[5].question = "Who is USC’s School of Business named after?";
		words[5].start = new int[2];
		words[5].start[0] = 0;
		words[5].start[1] = 8;
		
		Word[] acrossWords = new Word[3];
		Word[] downWords = new Word[3];
		acrossWords[0] = words[0];
		acrossWords[1] = words[1];
		acrossWords[2] = words[2];
		//acrossWords[3] = words[6];
		downWords[0] = words[3];
		downWords[1] = words[4];
		downWords[2] = words[5];
		g.acrossWords = new Word[3];
		g.downWords = new Word[3];
		g.acrossWords = acrossWords;
		g.downWords = downWords;
		g.acrossWordsC = new Vector<Word>();
		g.downWordsC = new Vector<Word>();
		
		//this might mess some things up with pointers and all that jazz
		//make sure to check up on this later
		for(int a = 0; a < acrossWords.length; a++) {
			g.acrossWordsC.add(acrossWords[a]);
		}
		for(int a = 0; a < downWords.length; a++) {
			g.downWordsC.add(downWords[a]);
		}
		g.words = new Word[6];
		for(int i = 0; i < 6; i ++) {
			g.words[i] = words[i];
		}
		
	}
}
