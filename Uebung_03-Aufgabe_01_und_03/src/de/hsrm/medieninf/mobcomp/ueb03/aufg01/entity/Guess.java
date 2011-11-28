package de.hsrm.medieninf.mobcomp.ueb03.aufg01.entity;

import java.math.BigInteger;

import android.util.Log;

/**
 * Repräsentiert einen abgegebenen Tipp
 * 
 * @author Markus Tacker <m@coderbyheart.de>
 */
public class Guess {
	private BigInteger number;
	private int result;
	private int numberOfTry;
	private long time;

	public Guess(BigInteger number, int result, int numberOfTry, long time) {
		this.setNumber(number);
		this.setResult(result);
		this.setNumberOfTry(numberOfTry);
		this.setTime(time);
		Log.v("Guess", "" + result);
	}

	public BigInteger getNumber() {
		return number;
	}

	public void setNumber(BigInteger number) {
		this.number = number;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public boolean isTooLow() {
		return result < 0;
	}

	public boolean isTooHigh() {
		return result > 0;
	}

	public boolean isGood() {
		return !isTooHigh() && !isTooLow();
	}

	public Integer getNumberOfTry() {
		return numberOfTry;
	}

	public void setNumberOfTry(int numberOfTry) {
		this.numberOfTry = numberOfTry;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
