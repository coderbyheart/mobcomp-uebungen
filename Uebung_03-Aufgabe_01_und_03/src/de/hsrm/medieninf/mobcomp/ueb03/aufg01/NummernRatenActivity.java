package de.hsrm.medieninf.mobcomp.ueb03.aufg01;

import java.math.BigInteger;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.entity.Guess;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.entity.Highscore;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.game.Game;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.game.IGame;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.persistence.HighscoreDbAdapter;

/**
 * Verwendet direkt ein Game-Objekt zum speichern des Spielzustandes
 * 
 * Für Aufgabe 4 wurde das Verwenden von Runnables eingeführt
 * 
 * @author Markus Tacker <m@coderbyheart.de>
 */
public class NummernRatenActivity extends Activity {
	/** Called when the activity is first created. */

	public static final int NEW_HIGHSCORE_DIALOG = 1;

	private BigInteger userNumber;
	private TextView number;
	private Button plus;
	private Button minus;
	private Button ok;
	private TableLayout table;
	private LayoutInflater inflater;
	private Dialog highscoreEnterDialog;
	private EditText highscoreNameText;
	private Button highscoreOkButton;
	private Highscore hs;
	private Handler handler = new Handler();
	protected IGame game;
	private SeekBar seeker;
	private int ms = 100;
	private Guess lastGuess;

	private class MinusListener implements OnTouchListener, OnClickListener,
			OnLongClickListener {

		Runnable updater;

		public MinusListener() {
			updater = decreaser;
		}

		public void onClick(View v) {
			modify();
		}

		protected void modify() {
			decrease();
		}

		public boolean onLongClick(View v) {
			handler.removeCallbacks(updater);
			handler.postAtTime(updater, SystemClock.uptimeMillis() + getMs());
			return true;
		}

		public boolean onTouch(View view, MotionEvent motionevent) {
			int action = motionevent.getAction();
			if (action == MotionEvent.ACTION_DOWN) {
			} else if (action == MotionEvent.ACTION_UP) {
				handler.removeCallbacks(updater);
				ms = 100;
			}
			return false;
		}
	}

	private class PlusListener extends MinusListener {

		public PlusListener() {
			updater = increaser;
		}

		@Override
		protected void modify() {
			increase();
		}
	}

	private Runnable increaser = new Runnable() {
		public void run() {
			increase();
			handler.postAtTime(this, SystemClock.uptimeMillis() + getMs());
		}
	};

	private Runnable decreaser = new Runnable() {
		public void run() {
			decrease();
			handler.postAtTime(this, SystemClock.uptimeMillis() + getMs());
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		number = (TextView) findViewById(R.id.numberInput);

		minus = (Button) findViewById(R.id.minus_button);
		plus = (Button) findViewById(R.id.plus_button);
		ok = (Button) findViewById(R.id.ok_button);
		table = (TableLayout) findViewById(R.id.log_table);
		inflater = getLayoutInflater();
		seeker = (SeekBar) findViewById(R.id.seeker);

		MinusListener ml = new MinusListener();
		MinusListener pl = new PlusListener();
		minus.setOnTouchListener(ml);
		minus.setOnClickListener(ml);
		minus.setOnLongClickListener(ml);
		minus.setLongClickable(true);
		plus.setOnTouchListener(new PlusListener());
		plus.setOnClickListener(pl);
		plus.setOnLongClickListener(pl);
		plus.setLongClickable(true);

		// Einen neuen Versuch machen
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				readUserNumber();
				game.createGuess(userNumber, new ParameterRunnable<Guess>() {
					public void run() {
						int lastGuessNumberOfTry = lastGuess == null ? 0 : lastGuess.getNumberOfTry(); 
						if (lastGuessNumberOfTry + 1 != parameter.getNumberOfTry()) {
							// Strafe!
							Toast.makeText(NummernRatenActivity.this,
									R.string.label_result_time_penalty, Toast.LENGTH_SHORT)
									.show();
						}
						lastGuess = parameter;
						if (parameter.isTooLow()) {
							Toast.makeText(NummernRatenActivity.this,
									R.string.label_result_too_low, Toast.LENGTH_SHORT)
									.show();
							updateSeeker();
						} else if (parameter.isTooHigh()) {
							Toast.makeText(NummernRatenActivity.this,
									R.string.label_result_too_high, Toast.LENGTH_SHORT)
									.show();
							updateSeeker();
						} else {
							showDialog(NEW_HIGHSCORE_DIALOG);
						}
						updateGuessView(parameter);
					}
				});
			}
		});

		seeker.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				setUserNumber(game.getMinHint().add(BigInteger.valueOf(seekBar.getProgress())));
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				setUserNumber(game.getMinHint().add(BigInteger.valueOf(progress)));
			}
		});
		
		number.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			public void afterTextChanged(Editable s) {
				if (game == null) return;
				seeker.setProgress(Integer.valueOf(s.toString()) - game.getMinHint().intValue());
			}
		});
		
		reset();
	}

	private void updateTextView() {
		number.setText(userNumber.toString());
	}

	private void decrease() {
		if (userNumber.compareTo(BigInteger.ZERO) <= 0)
			return;
		setUserNumber(userNumber.subtract(BigInteger.ONE));
	}
	
	private void setUserNumber(BigInteger userNumber)
	{
		this.userNumber = userNumber;
		updateTextView();
	}
	
	private void readUserNumber()
	{
		this.userNumber = BigInteger.valueOf(Long.parseLong(number.getText()
				.toString()));
	}

	private void increase() {
		if (userNumber.compareTo(game.getLimit()) >= 0)
			return;
		setUserNumber(userNumber.add(BigInteger.ONE));
	}

	private int getMs() {
		if (ms > 10)
			ms = ms - 2;
		return ms;
	}

	private void updateGuessView(Guess guess) {
		TableRow logtableRow = (TableRow) inflater.inflate(R.layout.tablerow,
				table, false);

		TextView cntry = (TextView) logtableRow
				.findViewById(R.id.tablerow_ntry);
		TextView cguess = (TextView) logtableRow
				.findViewById(R.id.tablerow_guess);
		ImageView cicon = (ImageView) logtableRow
				.findViewById(R.id.tablerow_icon);

		if (guess.isGood()) {
			cicon.setImageDrawable(getResources().getDrawable(R.drawable.ok));
		} else if (guess.isTooHigh()) {
			cicon.setImageDrawable(getResources().getDrawable(R.drawable.up));
		} else if (guess.isTooLow()) {
			cicon.setImageDrawable(getResources().getDrawable(R.drawable.down));
		}

		cntry.setText(String.valueOf(guess.getNumberOfTry()));
		cguess.setText(guess.getNumber().toString());

		table.addView(logtableRow, 1);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case NEW_HIGHSCORE_DIALOG:
			highscoreEnterDialog = new Dialog(this);
			highscoreEnterDialog.setContentView(R.layout.newhighscoredialog);
			highscoreEnterDialog.setTitle(R.string.newhighscoredialog_title);

			highscoreOkButton = ((Button) highscoreEnterDialog
					.findViewById(R.id.ok_button));
			highscoreOkButton.setEnabled(false);

			highscoreNameText = (EditText) highscoreEnterDialog
					.findViewById(R.id.name_text);
			highscoreNameText.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void afterTextChanged(Editable s) {
					highscoreOkButton.setEnabled(s.length() > 0);
				}
			});

			highscoreOkButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					hs = new Highscore();
					hs.setName(highscoreNameText.getText().toString());
					hs.setTries(lastGuess.getNumberOfTry());
					hs.setTime(lastGuess.getTime());
					new Thread(new Runnable() {
						public void run() {
							HighscoreDbAdapter dba = new HighscoreDbAdapter(
									NummernRatenActivity.this).open();
							dba.persist(hs);
							dba.close();
							showHighscore();
						}
					}).start();
					highscoreEnterDialog.cancel();
					reset();
				}
			});

			((Button) highscoreEnterDialog.findViewById(R.id.abort_button))
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							highscoreEnterDialog.cancel();
						}
					});
			return highscoreEnterDialog;
		}
		return null;
	}

	/**
	 * Menü erzeugen
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Wenn ein Menüpunkt ausgewählt wurde ...
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_show_highscores:
			showHighscore();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showHighscore() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri
				.parse("highscore://de.hsrm.medieninf.mobcomp.ueb03.aufg01/");
		intent.setData(uri);
		startActivity(intent);
	}

	protected void reset() {
		game = new Game(1000);
		resetLayout();
	}

	protected void resetLayout() {
		table.removeViews(1, table.getChildCount() - 1);
		setUserNumber(BigInteger.valueOf(500));
		updateSeeker();
	}

	private void updateSeeker() {
		// Seeker geht von 0 bis x
		seeker.setMax(game.getMaxHint().subtract(game.getMinHint()).intValue());
		// Funktioniert komischerweise nur, wenn der Wert zu klein war 
		seeker.setProgress(userNumber.subtract(game.getMinHint()).intValue());
	}
	
	public void onPause()
	{
		super.onPause();
		if (game != null) game.pause();
	}
	
	public void onResume()
	{
		super.onResume();
		if (game != null) game.resume();
	}
}