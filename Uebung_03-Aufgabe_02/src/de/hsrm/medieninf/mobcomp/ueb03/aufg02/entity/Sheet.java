package de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity;

import java.util.ArrayList;

public class Sheet extends Entity {
	private int numberOfWords;
	private int numberOfHeardWords;
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
		return numberOfHeardWords;
	}

	public void setNumberOfHeardWords(int numberOfHeardWords) {
		this.numberOfHeardWords = numberOfHeardWords;
	}
	
	public boolean bingo()
	{
		return getNumberOfHeardWords() == getNumberOfWords();
	}
}
