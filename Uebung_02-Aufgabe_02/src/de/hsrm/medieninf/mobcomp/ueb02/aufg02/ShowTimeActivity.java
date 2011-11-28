package de.hsrm.medieninf.mobcomp.ueb02.aufg02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class ShowTimeActivity extends Activity {

	private TimePicker.OnTimeChangedListener nullListener = new TimePicker.OnTimeChangedListener() {
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			// Do nothing!
		}
	};

	private TimePicker.OnTimeChangedListener updateListener = new TimePicker.OnTimeChangedListener() {
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			Toast.makeText(view.getContext(), "Don't touch this!", Toast.LENGTH_SHORT).show();
			view.setOnTimeChangedListener(nullListener);
			view.setCurrentHour(currentHour);
			view.setCurrentMinute(currentMinute);
			view.setOnTimeChangedListener(updateListener);
		}
	};
	
	private int currentHour, currentMinute;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showtime);
		
		TimePicker tp = (TimePicker) findViewById(R.id.showtime_timePicker);
		tp.setIs24HourView(true);
		currentHour = tp.getCurrentHour();
		currentMinute = tp.getCurrentMinute();

		tp.setOnTimeChangedListener(updateListener);

		Button button = (Button) findViewById(R.id.showtime_button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_OK, (new Intent()).setAction(String.format("%02d:%02d", currentHour, currentMinute)));
				finish();
			}
		});
	}
}