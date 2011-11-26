package de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity;

import java.util.ArrayList;

/**
 * Ein Spielblatt
 * 
 * @author Markus Tacker <m@coderbyheart.de>
 */
public class Sheet extends Entity {
	private int numberOfWords;
	private int numberOfheardWords;
	private ArrayList<Word> words;
	
	public ArrayList<Word> getWords() {
		return words;
	}

	public void setWords(ArrayList<Word> words) {
		this.words = words;
	}

	public int getNumberOfWords() {
		return numberOfWords;
	}

	public void setNumberOfWords(int nwords) {
		this.numberOfWords = nwords;
	}

	public int getNumberOfHeardWords() {
		return numberOfheardWords;
	}

	public void setNumberOfHeardWords(int numberOfCompletedWords) {
		this.numberOfheardWords = numberOfCompletedWords;
	}

	public boolean bingo() {
		return getNumberOfHeardWords() >= getNumberOfWords();
	}
}
