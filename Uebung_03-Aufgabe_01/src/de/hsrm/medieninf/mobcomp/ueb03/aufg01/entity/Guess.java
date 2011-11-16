package de.hsrm.medieninf.mobcomp.ueb03.aufg01.entity;

import java.math.BigInteger;

import android.util.Log;

public class Guess {
	private BigInteger number;
	private int result; 
	
	public Guess(BigInteger number, int result)
	{
		this.setNumber(number);
		this.setResult(result);
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
}
