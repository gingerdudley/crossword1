package csci201;

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
	
//	public void printBoard() {
//		String[][] board = this.makeFakeB();
//		//this will print the current gameboard
//		for(int i = 0; i < 13; i++) {
//			for(int j = 0; j < 11; j++) {
//				System.out.print(board[i][j] + " ");
//			}
//			System.out.println();
//		}
//		
//		System.out.println();
//	}
	
//	public static void main(String[] args) {
//		FakeBoard fb = new FakeBoard();
//		fb.printBoard();
//	}
}
