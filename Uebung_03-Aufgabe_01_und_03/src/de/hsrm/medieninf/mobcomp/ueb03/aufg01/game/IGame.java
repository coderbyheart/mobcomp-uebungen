package de.hsrm.medieninf.mobcomp.ueb03.aufg01.game;

import java.math.BigInteger;

import de.hsrm.medieninf.mobcomp.ueb03.aufg01.ParameterRunnable;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.entity.Guess;

/**
 * Interface für ein Spiel
 * 
 * @author Markus Tacker <m@coderbyheart.de>
 */
public interface IGame {

	/**
	 * Die auch asynchrone Services unterstützt werden, bekommt das runnable den erzeugten Guess übergeben
	 * 
	 * @param userNumber
	 * @param runnable
	 */
	void createGuess(BigInteger userNumber, ParameterRunnable<Guess> runnable);

	/**
	 * Gibt den Hinweis zum maximal möglichen Wert zurück
	 * 
	 * @return
	 */
	BigInteger getMinHint();

	/**
	 * Gibt den Hinweis zum minimal möglichen Wert zurück
	 * 
	 * @return
	 */
	BigInteger getMaxHint();

	/**
	 * Gibt den Maximalen Wert im Spiel zurück
	 * @return
	 */
	BigInteger getLimit();

	/**
	 * Pausiert das Spiel (und damit beim Service den Timer)
	 */
	void pause();

	/**
	 * Führt das Spiel weiter
	 */
	void resume();
}
