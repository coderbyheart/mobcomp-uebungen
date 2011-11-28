package de.hsrm.medieninf.mobcomp.ueb03.aufg01.game;

import java.math.BigInteger;
import java.util.Date;

import android.util.Log;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.ParameterRunnable;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.entity.Guess;

/**
 * Repräsentiert ein Spiel
 * 
 * @author Markus Tacker <m@coderbyheart.de>
 */
public class Game implements IGame {

	private BigInteger askNumber;
	protected BigInteger limit;
	protected Hinter hinter;
	protected long start;
	private long stop;
	private int tries = 0;
	private BigInteger lastGuess;
	private boolean finished = false;
	
	public Game(long limit) {
		this.limit = BigInteger.valueOf((long) limit);
		hinter = new Hinter(BigInteger.ONE, this.limit);
		this.start = new Date().getTime();
		setAskNumber(BigInteger.valueOf((long) (Math.random() * 1000)));
	}

	public BigInteger getAskNumber() {
		return askNumber;
	}

	public void setAskNumber(BigInteger askNumber) {
		this.askNumber = askNumber;
	}
	
	public Guess createGuess(BigInteger userNumber) {
		tries++;
		lastGuess = userNumber;
		Guess guess = new Guess(userNumber, userNumber.compareTo(askNumber), tries, new Date().getTime() - start);
		hinter.update(guess);
		if (guess.isGood()) {
			this.stop = new Date().getTime();
			finished = true;
			Log.v(this.getClass().getCanonicalName(), "Zeit: " + getTime());
		}
		return guess;
	}

	public void createGuess(BigInteger userNumber,
			ParameterRunnable<Guess> runnable) {
		Guess guess = createGuess(userNumber);
		runnable.setParameter(guess);
		runnable.run();
	}

	public BigInteger getLimit() {
		return limit;
	}

	public Integer getTries() {
		return tries;
	}

	public BigInteger getMaxHint() {
		return hinter.getMaxHint();
	}

	public BigInteger getMinHint() {
		return hinter.getMinHint();
	}

	/**
	 * Gibt die Zeit in Millisekunden zurück
	 * 
	 * @return
	 */
	public long getTime() {
		return stop - start;
	}

	public BigInteger getLastGuess() {
		return lastGuess;
	}

	public boolean isFinished() {
		return finished ;
	}

	public void pause() {
		// Tut hier nichts
	}

	public void resume() {
		// Tut hier nichts
	}

}
