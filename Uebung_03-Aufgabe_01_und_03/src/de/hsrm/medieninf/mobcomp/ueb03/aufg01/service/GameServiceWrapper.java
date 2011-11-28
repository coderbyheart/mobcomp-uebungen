package de.hsrm.medieninf.mobcomp.ueb03.aufg01.service;

import java.math.BigInteger;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.ParameterRunnable;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.entity.Guess;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.game.Hinter;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.game.IGame;

/**
 * Wrapper für ein Spiel, das den Zustand mit dem {@link GameService} verwaltet.
 * 
 * @author Markus Tacker <m@coderbyheart.de>
 */
public class GameServiceWrapper implements IGame {

	private Messenger messengerSend;
	private IncomingHandler incomingHandler = new IncomingHandler();
	private Messenger messengerReceive = new Messenger(incomingHandler);
	private ParameterRunnable<Guess> guessRunnable;
	private BigInteger userNumber;
	protected Hinter hinter;
	private BigInteger limit;
	private ParameterRunnable<IGame> gameRunnable;

	private class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GameService.MSG_START_GAME_RESULT:
				gameRunnable.setParameter(GameServiceWrapper.this);
				gameRunnable.run();
				break;
			case GameService.MSG_GUESS_RESULT:
				Guess guess = new Guess(
					userNumber, 
					msg.getData().getInt(GameService.KEY_GUESS_RESULT), 
					msg.getData().getInt(GameService.KEY_GUESS_NUMBER_OF_TRY), 
					msg.getData().getLong(GameService.KEY_GUESS_TIME)
				);
				hinter.update(guess);
				guessRunnable.setParameter(guess);
				guessRunnable.run();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	public GameServiceWrapper(int limit, Messenger messengerSend) {
		this.messengerSend = messengerSend;
		this.limit = BigInteger.valueOf(limit);
		hinter = new Hinter(BigInteger.ONE, this.limit);
	}

	/**
	 * Startet ein neues Spiel
	 * 
	 * @param limit
	 */
	public void createGame(ParameterRunnable<IGame> gameRunnable) {
		this.gameRunnable = gameRunnable;
		sendMessage(Message.obtain(null, GameService.MSG_START_GAME, getLimit().intValue(), 0));
	}

	/**
	 * Sendet eine Nachricht
	 * 
	 * @param message
	 */
	private void sendMessage(Message message) {
		message.replyTo = messengerReceive;
		try {
			messengerSend.send(message);
		} catch (RemoteException e) {
			Log.v(getClass().getCanonicalName(), "Sending message failed.");
		}
	}

	/**
	 * Erzeugt einen neuen Guess
	 */
	public void createGuess(BigInteger userNumber,
			ParameterRunnable<Guess> runnable) {
		this.userNumber = userNumber;
		guessRunnable = runnable;
		sendMessage(Message.obtain(null, GameService.MSG_GUESS,
				userNumber.intValue(), 0));
	}

	/**
	 * @see {@link IGame#getMinHint()}
	 */
	public BigInteger getMinHint() {
		return hinter.getMinHint();
	}

	/**
	 * @see {@link IGame#getMaxHint()}
	 */
	public BigInteger getMaxHint() {
		return hinter.getMaxHint();
	}

	/**
	 * @see {@link IGame#getLimit()}
	 */
	public BigInteger getLimit() {
		return limit;
	}

	/**
	 * @see {@link IGame#pause()}
	 */
	public void pause() {
		sendMessage(Message.obtain(null, GameService.MSG_PAUSE));
	}

	/**
	 * @see {@link IGame#resume()}
	 */
	public void resume() {
		sendMessage(Message.obtain(null, GameService.MSG_RESUME));
	}
}
