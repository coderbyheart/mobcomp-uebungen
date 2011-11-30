package de.hsrm.medieninf.mobcomp.ueb03.aufg01;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.game.IGame;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.service.GameService;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.service.GameServiceWrapper;

/**
 * Verwendet der {@link GameService} zum speichern des Spielzustandes.
 * 
 * @author Markus Tacker <m@coderbyheart.de>
 */
public class NummernRatenActivityService extends NummernRatenActivity {

	private GameServiceWrapper gsw;
	private boolean serviceBound = false;
	private Messenger messengerSend;
	private Runnable afterConnect;
	private ServiceConnection sConn = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.v(getClass().getCanonicalName(), "... verbunden.");
			messengerSend = new Messenger(service);
			if (afterConnect != null) {
				Runnable r = afterConnect;
				afterConnect = null;
				r.run();
			}
		}

		public void onServiceDisconnected(ComponentName name) {
			messengerSend = null;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void reset() {
		afterConnect = new Runnable() {
			public void run() {
				gsw = new GameServiceWrapper(1000, messengerSend);
				gsw.createGame(new ParameterRunnable<IGame>() {
					public void run() {
						game = getParameter();
						resetLayout();
					}
				});
				gsw.setPenaltyRunnable(new Runnable() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(NummernRatenActivityService.this,
										R.string.label_result_time_penalty,
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				});
			}
		};
		connect();
	}

	public void onResume() {
		super.onResume();
		connect();
	}

	private void connect() {
		if (!serviceBound) {
			Log.v(getClass().getCanonicalName(), "Verbinde mit Service ...");
			Intent intent = new Intent(this, GameService.class);
			serviceBound = bindService(intent, sConn, Context.BIND_AUTO_CREATE);
		}
	}

	public void onPause() {
		super.onPause();
		disconnect();
	}

	private void disconnect() {
		if (serviceBound) {
			Log.v(getClass().getCanonicalName(), "Trenne vom Service ...");
			unbindService(sConn);
			serviceBound = false;
		}
	}
}
