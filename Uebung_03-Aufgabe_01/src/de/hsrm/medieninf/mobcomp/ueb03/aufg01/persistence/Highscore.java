package de.hsrm.medieninf.mobcomp.ueb03.aufg01.persistence;

public class Highscore {
	private int id;
	private Integer tries;
	private String name;
	private String timestamp;
	public Integer getTries() {
		return tries;
	}
	public void setTries(Integer tries) {
		this.tries = tries;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
