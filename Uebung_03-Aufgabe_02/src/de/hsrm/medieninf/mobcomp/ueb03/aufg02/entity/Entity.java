package de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity;

public abstract class Entity {

	private int id;
	private String creationTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String time) {
		this.creationTime = time;
	}

}
