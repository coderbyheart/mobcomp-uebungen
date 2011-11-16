package de.hsrm.medieninf.mobcomp.ueb03.aufg01;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class NummernRatenActivity extends Activity {
	/** Called when the activity is first created. */

	private BigInteger userNumber;
	private BigInteger askNumber;
	private BigInteger limit = BigInteger.valueOf((long) 1000);
	private TextView number;
	private Button plus;
	private Button minus;
	private Button ok;
	private TableLayout table;
	private LayoutInflater inflater;
	private List<Guess> guesses = new ArrayList<Guess>();
	private Handler handler = new Handler();
	private int ms = 100;

	private class MinusListener implements OnTouchListener, OnClickListener,
			OnLongClickListener {

		Runnable updater;

		public MinusListener() {
			updater = decreaser;
		}

		@Override
		public void onClick(View v) {
			modify();
		}

		protected void modify() {
			decrease();
		}

		@Override
		public boolean onLongClick(View v) {
			handler.removeCallbacks(updater);
			handler.postAtTime(updater, SystemClock.uptimeMillis() + getMs());
			return true;
		}

		@Override
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

		userNumber = BigInteger.valueOf((long) (Math.random() * 1000));
		askNumber = BigInteger.valueOf((long) (Math.random() * 1000));
		minus = (Button) findViewById(R.id.minus_button);
		plus = (Button) findViewById(R.id.plus_button);
		ok = (Button) findViewById(R.id.ok_button);
		table = (TableLayout) findViewById(R.id.log_table);
		inflater = getLayoutInflater();
		
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

		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userNumber = BigInteger.valueOf(Long.parseLong(number.getText()
						.toString()));
				Log.v("ASK", "" + userNumber + " / " + askNumber);
				Guess guess = new Guess(userNumber, userNumber
						.compareTo(askNumber));
				guesses.add(guess);
				if (guess.isTooLow()) {
					Toast.makeText(NummernRatenActivity.this,
							R.string.label_result_too_low, Toast.LENGTH_SHORT)
							.show();
				} else if (guess.isTooHigh()) {
					Toast.makeText(NummernRatenActivity.this,
							R.string.label_result_too_high, Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(NummernRatenActivity.this,
							R.string.label_result_good, Toast.LENGTH_SHORT)
							.show();
				}
				updateGuessView(guess);
			}
		});

		updateTextView();
	}

	private void updateTextView() {
		number.setText(userNumber.toString());
	}

	synchronized private void decrease() {
		if (userNumber.compareTo(BigInteger.ZERO) <= 0)
			return;
		userNumber = userNumber.subtract(BigInteger.ONE);
		updateTextView();
	}

	synchronized private void increase() {
		if (userNumber.compareTo(limit) >= 0)
			return;
		userNumber = userNumber.add(BigInteger.ONE);
		updateTextView();
	}

	private int getMs() {
		if (ms > 10)
			ms = ms - 2;
		return ms;
	}

	private void updateGuessView(Guess guess) {
		TableRow logtableRow = (TableRow)inflater.inflate(R.layout.tablerow, table, false);
		
		TextView cntry = (TextView)logtableRow.findViewById(R.id.tablerow_ntry);
		TextView cguess = (TextView)logtableRow.findViewById(R.id.tablerow_guess);
		ImageView cicon = (ImageView)logtableRow.findViewById(R.id.tablerow_icon);
		
		if (guess.isGood()) {
			cicon.setImageDrawable(getResources().getDrawable(R.drawable.ok));
		} else if (guess.isTooHigh()) {
			cicon.setImageDrawable(getResources().getDrawable(R.drawable.up));
		} else if (guess.isTooLow()) {
			cicon.setImageDrawable(getResources().getDrawable(R.drawable.down));
		}
		
		cntry.setText(String.valueOf(table.getChildCount()));
		cguess.setText(guess.getNumber().toString());
		
		table.addView(logtableRow, 1);
	}
}