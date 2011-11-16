package de.hsrm.medieninf.mobcomp.ueb03.aufg01.game;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.entity.Guess;

public class Game {
	private List<Guess> guesses;
	private BigInteger askNumber;
	private BigInteger limit;
	private BigInteger maxHint;
	private BigInteger minHint;

	public Game(long limit) {
		this.limit = this.maxHint = BigInteger.valueOf((long) limit);
		this.minHint = BigInteger.ONE; 
		setGuesses(new ArrayList<Guess>());
		setAskNumber(BigInteger.valueOf((long) (Math.random() * 1000)));
	}

	public BigInteger getAskNumber() {
		return askNumber;
	}

	public void setAskNumber(BigInteger askNumber) {
		this.askNumber = askNumber;
	}

	public List<Guess> getGuesses() {
		return guesses;
	}

	public void setGuesses(List<Guess> guesses) {
		this.guesses = guesses;
	}

	public Guess createGuess(BigInteger userNumber) {
		Guess guess = new Guess(userNumber, userNumber.compareTo(askNumber));
		guesses.add(guess);
		if (guess.isTooHigh()) {
			maxHint = userNumber;
		}
		if (guess.isTooLow()) {
			minHint = userNumber;
		}
		return guess;
	}

	public BigInteger getLimit() {
		return limit;
	}

	public Integer getTries() {
		return guesses.size();
	}

	public BigInteger getMaxHint() {
		return maxHint;
	}
	
	public BigInteger getMinHint() {
		return minHint;
	}
}
