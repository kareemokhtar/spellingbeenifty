/*
Abd El Kareem Gabareen Mokhtar
Before anything, I just want to say that writing anything with char is the most tedious experience ever, MAKE IT SIMPLE, BRING BACK SIMPLY SCHEME
 */
import java.util.HashSet;
import edu.willamette.cs1.spellingbee.SpellingBeeGraphics;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.Arrays;
public class SpellingBee {
	private int currentScore = 0; //tracks the TOTAL player score, there's a different variable in the calculateScore method for how much to award
	private Set<String> enteredWords = new HashSet<>(); //so that no words are repeated, and the game isn't cheesed

	public void run() {
		sbg = new SpellingBeeGraphics();
		sbg.addField("Puzzle", (s) -> puzzleAction(s));
		sbg.addButton("Solve", (s) -> {
			try {
				solveAction();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}


	private void puzzleAction(String puzzle) {
		if (isValidPuzzle(puzzle)) {
			puzzle = puzzle.toUpperCase(); //to convert to uppercase (easier to look at)
			sbg.setBeehiveLetters(puzzle);
			sbg.showMessage("Puzzle updated successfully!", Color.BLACK);
		} else {
			sbg.showMessage("invalid puzzle: must be 7 unique letters (a-z)", Color.RED);
		}
	}

	private boolean isValidPuzzle(String puzzle) {
		if (puzzle.length() != 7 || !puzzle.matches("[a-z]+")) {
			return false;
		}
		return new HashSet<>(Arrays.asList(puzzle.split(""))).size() == 7;
		//I was so glad this worked, I looked everywhere for a solution, and apparently I was supposed to use HashSet, 
		//which I didn't know was a thing so I played around with it forEVER, and it finally created the array
		//still don't really understand why this line works, though
	}



	private void solveAction() throws IOException {
		String enteredWord = sbg.getField("Puzzle").toUpperCase(); 
		//the reason I had to use toUpperCase so much was that if I didn't, you didn't get the luxury of 
		//capital letters in the puzzle, since the puzzle ended up being case sensitive
		String puzzle = sbg.getBeehiveLetters().toUpperCase();

		if (enteredWords.contains(enteredWord)) { //self-explanatory
			sbg.showMessage("Word already entered: " + enteredWord, Color.RED);
			return; //allows the method to act as normal-order procedure
		}

		if (!isValidWord(enteredWord, puzzle)) {
			sbg.showMessage("Invalid word: " + enteredWord, Color.RED);
			return;
		}

		int wordScore = calculateScore(enteredWord, puzzle);
		currentScore += wordScore; //the score tracker
		enteredWords.add(enteredWord);

		sbg.addWord(enteredWord, Color.GREEN);
		sbg.showMessage("Found word: " + enteredWord + " - Score: " + wordScore + " (Total Score: " + currentScore + ")", Color.BLACK);
	}

	private int calculateScore(String word, String puzzle) {
		if (word.length() == 4) {
			return 1;  // this gives 1 point for 4 letter words, so no matter what, if the word is over 4 letters and fits criteria, it's at least 1 point
		} else {
			int baseScore = word.length();  // this makes points equal to number of letters for other words
			int extraPoints = (usesEveryLetter(word, puzzle)) ? 7 : 0; //this basically just references usesEveryLetter and returns 7 if it does
			return baseScore + extraPoints;
		}
	}

	private boolean usesEveryLetter(String word, String puzzle) { //the easiest part of this whole project lol
		for (char c : puzzle.toUpperCase().toCharArray()) {
			if (word.indexOf(c) == -1) {
				return false;
			}
		}
		return true;
	}


	private boolean isValidWord(String word, String puzzle) {
		// makes sur that word is 4+ letters
		if (word.length() < 4) {
			return false;
		}

		// basically, this checks if the first letter of the word is used literally anywhere in the word.
		char firstLetter = Character.toUpperCase(word.charAt(0));
		if (puzzle.toUpperCase().indexOf(firstLetter) == -1) {
			return false;
		}

		// this checks if the word is in the English dictionary (I still don't really understand the try/catch stuff
		try {
			ArrayList<String> dictionary = readWordsFromFile(ENGLISH_DICTIONARY);
			boolean found = false;

			for (String dictWord : dictionary) {
				if (dictWord.equalsIgnoreCase(word)) {
					found = true;
					break;
				}
			}

			if (!found) {
				return false;
			}
		} catch (IOException e) {
			// Print the error message for debugging purposes
			e.printStackTrace();
			return false;
		}

		// this checks for only puzzle letters/center letter
		char centerLetter = Character.toUpperCase(puzzle.charAt(0)); //since the first letter is always the center letter

		// Check each letter in the word (not the center letter)
		for (char c : word.toUpperCase().toCharArray()) {
			if (c != centerLetter && puzzle.toUpperCase().indexOf(c) == -1) {
				return false;
			}
		}

		return true;
	}






	public ArrayList<String> readWordsFromFile(String filename) throws IOException {
		//biggest pain in the ass ever, I stayed up so much to get this to work with isValidWord
		ArrayList<String> words = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String trimmedLine = line.trim();
				if (trimmedLine.length() >= 4) {
					words.add(trimmedLine);
				}
			}
		} catch (IOException e) {
			// Print the error message for debugging purposes
			e.printStackTrace();
			throw e;  // Rethrow the exception to indicate the failure
		}
		return words;
	}






	/* Constants */

	private static final String ENGLISH_DICTIONARY = "EnglishWords.txt";
	/* Private instance variables */

	private SpellingBeeGraphics sbg;

	/* Startup code */

	public static void main(String[] args) {
		new SpellingBee().run();
	}

}




//Honestly, this project was both fun and an absolute nightmare. I spent so long trying to do the readWordsFromTextFile method
//That I stayed up for like 3 hours while in DC some nights just tinkering with it. I still don't reall understand HashSet, Set, and the IOExceptions,
//But everything else wasn't awful. Took some Stack Overflow, some GPT, some coding rooms (for refresher), and some w3
//as well as some quick google searches, but we got there. (:     (ALSO MR.PALEY IF YOU SEE THIS, DON'T THROW ME OFF THE SECOND FLOOR)