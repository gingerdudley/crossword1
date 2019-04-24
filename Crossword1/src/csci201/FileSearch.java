package csci201;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSearch {
	
	
	public File selectFile() {
		
		URL url = getClass().getResource("gamedata");
		File file = new File("gamedata");
		String path = "";
		try {
			path = file.getCanonicalPath();
		} catch(IOException ioe) {
			System.out.println(ioe);
		}
		//File folder = new File("/Users/gingerdudley/git/crossword1/Crossword1/gamedata");
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		if(listOfFiles.length == 1) {
			//then there is only one file and we need to return that file to the main game for game play
			return listOfFiles[0];
		} else {
			//randomly select a file from this array to return
			Random rand = new Random();
			int max = listOfFiles.length;
			int randomNum = rand.nextInt((max - 0));
			return listOfFiles[randomNum];
		}
	}
	
	public validFileContents verifyValidity(File f) {
		
		//actually maybe well return the file contents and return null if the file isnt valid ?
		//decide this later
		Vector<Word> acrossV = new Vector<Word>();
		Vector<Word> downV = new Vector<Word>();
		validFileContents vfc = new validFileContents();

		//need to keep count of the across and down words to add to the arrays at the end of this
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			int aNum = 0, dNum = 0;
			boolean acrossFound = false, downFound = false;
			line = br.readLine();
			line = line.toLowerCase();
			if(!line.equals("across") && !line.equals("down")) {
				return null;
			} else if(line.equals("across")){
				acrossFound = true;
				while(!downFound) {
					while ((line = br.readLine()) != null) {
						line = line.toLowerCase();
						//process all the contents for the across section
						if(line.equals("across")) {
							return null;
						} else if(line.equals("down") && aNum == 0) {
							return null;
							//there are no arguments for the across
						} else if(line.equals("down")) {
							downFound = true;
							break;
						} else {
							aNum++;
							//parse out the line and decide if its valid
							Word returnedWord = ParseData(line, true);
							if(returnedWord == null) {
								return null;
							}
							acrossV.add(returnedWord);
							//add this word to the across array
						}
					}
					if(downFound == false) {
						return null;
						//then youre at the end of the file but you have only had across words
					}
				}
				
			} 
			//System.out.println("line value: " + line);
			if(line.equals("down")) {		
				downFound = true;
				while ((line = br.readLine()) != null) {
					line = line.toLowerCase();
					//process all the contents for the across section
					if(line.equals("across") && acrossFound == false && dNum != 0) {
						//return false;
						//then u need to parse out them words
						while ((line = br.readLine()) != null) {
							line = line.toLowerCase();
							if(line.equals("across")) {
								return null;
							} else if(line.equals("down") && aNum == 0) {
								return null;
								//there are no arguments for the across
							} else if(line.equals("down")) {
								return null;
							} else {
								aNum++;
								//parse out the line and decide if its valid
								Word returnedWord = ParseData(line, true);
								if(returnedWord == null) {
									return null;
								}
								acrossV.add(returnedWord);
								//add this word to the across array
							}
						}
						break;
//						aNum++;
//						Word returnedWord = ParseData(line, true);
//						acrossV.add(returnedWord);
						
					} else if((line.equals("across") && acrossFound == true) || (line.equals("across") && dNum == 0)){
						return null;
					} else if(line.equals("down")) {
						return null;
					} else {
						dNum++;
						//parse out the line and decide if its valid
						Word returnedWord = ParseData(line, false);
						if(returnedWord == null) {
							return null;
						}
						downV.add(returnedWord);
						//add this word to the across array
					}
				}
			}
			if(aNum == 0 || dNum == 0) {
				return null;
			}
			fr.close();
			br.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}

		vfc.acrossWords = new Word[acrossV.size()];
		vfc.downWords = new Word[downV.size()];
		for(int i = 0; i < acrossV.size(); i++) {
			vfc.acrossWords[i] = acrossV.get(i);
		}
		for(int i = 0; i < downV.size(); i++) {
			vfc.downWords[i] = downV.get(i);
		}
		return vfc;
	}
	


	private Word ParseData(String line, boolean across) {
		//String beginningLine = line;
		int parameterCount = 0;
		int paramNum = 0;
		Word word = new Word();
		StringTokenizer paramToken = new StringTokenizer(line, "|");
		while(paramToken.hasMoreTokens()) {
			paramToken.nextToken();
			paramNum++;
		}
		if(paramNum < 2) {
			return null;
		} else {
			
			if(across) {
				word.across = true;
			} else {
				word.across = false;
			}
			//then we can actually start parsing the data
			StringTokenizer token = new StringTokenizer(line, "|");
			while(token.hasMoreTokens()) {
				//keep reading in the next thing and then see if its formatted correctly
				if(parameterCount == 0) {
					String attempt = token.nextToken();
					try {
						int num = Integer.parseInt(attempt);
						word.number = num;
						//city.setDayLow(low);		
					} catch(Exception e) {
						System.out.println("problem converting to an int");
						return null;
					}
				} else if(parameterCount == 1) {
					//make sure that the answer doesnt have any white space
					String attempt = token.nextToken();
					//make sure that this string doesnt have any white space
					Pattern pattern = Pattern.compile("\\s");
					Matcher matcher = pattern.matcher(attempt);
					boolean found = matcher.find();
					if(found) {
						return null;
						//then there is white space in the answer and this is illegalll
					} else {
						word.word = attempt;
					}
					
				} else if (parameterCount == 2) {
					String attempt = token.nextToken();
					//^this sting will contain the question
					word.question = attempt;	
				}
				parameterCount++;
			}
		}
		return word;
	}
	
	public validFileContents checkMatches(validFileContents vfc) {
		for(int i = 0; i < vfc.acrossWords.length; i++) {
			for(int j = 0; j < vfc.downWords.length; j++) {
				if(vfc.acrossWords[i].number == vfc.downWords[j].number) {
					if(!String.valueOf(vfc.acrossWords[i].word.charAt(0)).equals(
							String.valueOf(vfc.downWords[j].word.charAt(j)))){
						return null;
					} else {
						vfc.acrossWords[i].match = true;
						vfc.downWords[j].match = true;
					}
				}
			}
		}
		return vfc;
	}
}
