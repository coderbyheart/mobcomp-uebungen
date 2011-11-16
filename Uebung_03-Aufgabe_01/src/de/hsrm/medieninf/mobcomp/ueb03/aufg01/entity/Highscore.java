package de.hsrm.medieninf.mobcomp.ueb03.aufg01.entity;

public class Highscore {
	private int id;
	private Integer tries;
	private String name;
	private long time;
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
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
