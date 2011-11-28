package de.hsrm.medieninf.mobcomp.ueb03.aufg01.game;

import java.math.BigInteger;

import de.hsrm.medieninf.mobcomp.ueb03.aufg01.entity.Guess;

/**
 * Verwaltet Hinweise zu der möglichen höchsten und niedrigsten Zahl in einem Spiel
 * 
 * @author Markus Tacker <m@coderbyheart.de>
 */
public class Hinter {
	private BigInteger maxHint;
	private BigInteger minHint;

	public Hinter(BigInteger min, BigInteger max) {
		minHint = min;
		maxHint = max;
	}

	public BigInteger getMaxHint() {
		return maxHint;
	}

	public void setMaxHint(BigInteger maxHint) {
		this.maxHint = maxHint;
	}

	public BigInteger getMinHint() {
		return minHint;
	}

	public void setMinHint(BigInteger minHint) {
		this.minHint = minHint;
	}

	public void update(Guess guess) {
		if (guess.isTooHigh()) {
			maxHint = guess.getNumber();
		}
		if (guess.isTooLow()) {
			minHint = guess.getNumber();
		}
	}
}
