package de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity;

import java.util.ArrayList;

public class BullshitSheet {
	private int id;
	private String time;
	private ArrayList<BullshitWord> words;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<BullshitWord> getWords() {
		return words;
	}

	public void setWords(ArrayList<BullshitWord> words) {
		this.words = words;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
