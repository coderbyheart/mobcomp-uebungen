package de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity;

/**
 * Ein Wort eines Spielblattes
 * 
 * @author Markus Tacker <m@coderbyheart.de>
 */
public class Word extends Entity {
	private Entity sheet;
	private Integer sheetId;
	private String word;
	private String heardTime;

	public Entity getSheet() {
		return sheet;
	}

	public void setSheet(Entity sheet) {
		this.sheet = sheet;
		sheetId = sheet.getId();
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getHeardTime() {
		return heardTime;
	}

	public void setHeardTime(String heardTime) {
		this.heardTime = heardTime;
	}

	public Integer getSheetId() {
		return sheetId;
	}

	public void setSheetId(Integer sheetId) {
		this.sheetId = sheetId;
	}
}
