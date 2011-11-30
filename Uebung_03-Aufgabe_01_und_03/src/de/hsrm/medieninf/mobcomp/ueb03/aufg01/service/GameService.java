package de.hsrm.medieninf.mobcomp.ueb03.aufg01.service;

import java.math.BigInteger;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.entity.Guess;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.game.Game;

/**
 * Service für das Spiel:
 * 
 * Stellt die Spiellogik als Service zur Verfügung. Zudem werden hier Strafen
 * vergeben, wenn man autoGuess Sekunden lang nichts macht.
 * 
 * @author Markus Tacker <m@coderbyheart.de>
 */
public class GameService extends Service {

	public static final int MSG_START_GAME = 1;
	public static final int MSG_START_GAME_RESULT = 2;
	public static final int MSG_GUESS = 3;
	public static final int MSG_GUESS_RESULT = 4;
	public static final int MSG_TRIES = 5;
	public static final int MSG_TRIES_RESULT = 6;
	public static final int MSG_PAUSE = 7;
	public static final int MSG_PAUSE_RESULT = 8;
	public static final int MSG_RESUME = 9;
	public static final int MSG_RESUME_RESULT = 10;
	public static final int MSG_PENALTY = 11;

	public static final String KEY_GUESS_RESULT = "guess.result";
	public static final String KEY_GUESS_TIME = "guess.time";
	public static final String KEY_GUESS_NUMBER_OF_TRY = "guess.numberOfTry";

	private final Messenger messenger = new Messenger(new IncomingHandler());
	private Game game;
	private Timer timer;
	private Messenger gameStarter;
	
	/**
	 * Anzahl der Sekunden, nach denen automatisch ein Versuch hinzu kommt
	 */
	private int autoGuess = 10;

	private class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message request) {
			switch (request.what) {
			case MSG_START_GAME:
				doStartGame(request.arg1);
				gameStarter = request.replyTo;
				sendMessage(request.replyTo,
						Message.obtain(null, MSG_START_GAME_RESULT));
				break;
			case MSG_GUESS:
				Message message = Message.obtain(null, MSG_GUESS_RESULT);
				Guess guess = doAddGuess(request.arg1);
				Bundle data = new Bundle();
				data.putInt(KEY_GUESS_RESULT, guess.getResult());
				data.putLong(KEY_GUESS_TIME, guess.getTime());
				data.putInt(KEY_GUESS_NUMBER_OF_TRY, guess.getNumberOfTry());
				message.setData(data);
				sendMessage(request.replyTo, message);
				break;
			case MSG_TRIES:
				sendMessage(request.replyTo,
						Message.obtain(null, MSG_TRIES_RESULT, doGetTries(), 0));
				break;
			case MSG_PAUSE:
				sendMessage(request.replyTo,
						Message.obtain(null, MSG_PAUSE_RESULT, doPause(), 0));
				break;
			case MSG_RESUME:
				sendMessage(request.replyTo,
						Message.obtain(null, MSG_RESUME_RESULT, doResume(), 0));
				break;
			default:
				super.handleMessage(request);
			}
		}

		private void sendMessage(Messenger rcpt, Message response) {
			if (rcpt == null)
				return;
			try {
				rcpt.send(response);
			} catch (RemoteException e) {
				Log.v(getClass().getCanonicalName(),
						"Failed to send game message.");
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return messenger.getBinder();
	}

	public void onDestroy() {
		stopTimer();
	}

	/**
	 * Startet ein neues Spiel
	 * 
	 * @param limit
	 *            Maximale Zahl
	 */
	public void doStartGame(long limit) {
		game = new Game(limit);
		startTimer();
	}

	/**
	 * Fügt einen Versuch hinzu
	 * 
	 * @param guessValue
	 */
	public Guess doAddGuess(long guessValue) {
		stopTimer();
		Guess guess = game.createGuess(BigInteger.valueOf(guessValue));
		startTimer();
		return guess;
	}

	/**
	 * Gibt die Anzahl der abgegeben Versuche zurück
	 */
	public int doGetTries() {
		return game.getTries();
	}

	/**
	 * Startet den Timer
	 */
	private boolean startTimer() {
		if (game.isFinished())
			return false;
		if (timer != null)
			return false;
		Log.v(getClass().getCanonicalName(), "Starte Timer.");
		timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				game.createGuess(game.getLastGuess());
				Message penaltyMessage = Message.obtain(null, MSG_PENALTY, game.getTries(), 0);
				try {
					gameStarter.send(penaltyMessage);
				} catch (RemoteException e) {
					Log.v(getClass().getCanonicalName(),
							"Failed to send penalty.");
				}
			}
		}, autoGuess * 1000, autoGuess * 1000);
		return true;
	}

	/**
	 * Hält den Straf-Timer an
	 */
	private boolean stopTimer() {
		if (timer == null)
			return false;
		timer.cancel();
		timer = null;
		return true;
	}

	/**
	 * Methode zum Pausieren des Timers von Außen
	 */
	private int doResume() {
		return startTimer() ? 1 : 0;
	}

	/**
	 * Methode zum Pausieren des Timers von Außen
	 */
	private int doPause() {
		return stopTimer() ? 1 : 0;
	}

}
