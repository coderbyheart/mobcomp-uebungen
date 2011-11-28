package de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity;


public class BullshitWord {
	private int id;
	private BullshitSheet sheet;
	private String word;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BullshitSheet getSheet() {
		return sheet;
	}

	public void setSheet(BullshitSheet sheet) {
		this.sheet = sheet;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
}
