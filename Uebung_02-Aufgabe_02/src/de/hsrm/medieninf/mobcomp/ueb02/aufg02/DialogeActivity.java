package de.hsrm.medieninf.mobcomp.ueb02.aufg02;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

public class DialogeActivity extends Activity {
	private ListView mainListView;
	private TextView statusView;
	private ProgressBar progressbar;
	private int progressStatus = 0;
	private int progressSeconds = 10;
	private Handler handler = new Handler();

	private static final int DIALOG_WARNING = 1;
	private static final int DIALOG_TIME = 2;

	static final private int RESULT_DIRECTION_GET = 3;
	static final private int RESULT_TIME_GET = 4;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mainListView = (ListView) findViewById(R.id.main_listview);
		statusView = (TextView) findViewById(R.id.status_textview);

		mainListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = (String) parent.getAdapter().getItem(position);
				Log.v(getLocalClassName(), text);
				/* Beenden */
				if (text.equals(getResources().getString(
						R.string.exit_button_text))) {
					finish();
					return;
				}
				/* Warnung anzeigen */
				if (text.equals(getResources().getString(
						R.string.warning_button_text))) {
					showDialog(DIALOG_WARNING);
					return;
				}
				/* Auswahl-Activity starten */
				if (text.equals(getResources().getString(
						R.string.select_button_text))) {
					Intent intent = new Intent(DialogeActivity.this,
							DirectionActivity.class);
					// startActivity(intent);
					startActivityForResult(intent, RESULT_DIRECTION_GET);
					return;
				}
				/* Uhrzeit auswählen */
				if (text.equals(getResources().getString(
						R.string.time_button_text))) {
					showDialog(DIALOG_TIME);
					return;
				}
				/* Uhrzeit anzeigen */
				if (text.equals(getResources().getString(
						R.string.time_button_text2))) {
					Intent intent = new Intent(DialogeActivity.this,
							ShowTimeActivity.class);
					// startActivity(intent);
					startActivityForResult(intent, RESULT_TIME_GET);
					return;
				}
			}

		});

		List<String> activities = new ArrayList<String>();

		activities.add(getResources().getString(R.string.warning_button_text));
		activities.add(getResources().getString(R.string.select_button_text));
		activities.add(getResources().getString(R.string.time_button_text));
		activities.add(getResources().getString(R.string.time_button_text2));
		activities.add(getResources().getString(R.string.exit_button_text));

		ListAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, activities);
		mainListView.setAdapter(adapter);

		progressbar = (ProgressBar) findViewById(R.id.main_progressbar);
		
		class UpdateProgess extends TimerTask {
			@Override
			public void run() {
				progressbar.setVisibility(View.VISIBLE);
				while (progressStatus < 100) {
					doSomeWork();
					progressStatus++;
					// Progressbar aktualisieren
					handler.post(new Runnable() {
                        public void run() {
                        	progressbar.setProgress(progressStatus);
                        }
                    });
				}

				handler.post(new Runnable() {
					public void run() {
						progressbar.setVisibility(View.INVISIBLE);
					}
				});
			}

			private void doSomeWork() {
				try {
					Thread.sleep((progressSeconds * 1000) / 100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		Timer progressTimer = new Timer();
		progressTimer.schedule(new UpdateProgess(), 0);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_WARNING:
			return new AlertDialog.Builder(DialogeActivity.this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.warning_title)
					.setMessage(R.string.warning_text)
					.setPositiveButton(R.string.warning_button_positive_text,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									statusView.setText(Html.fromHtml(String
											.format(getResources()
													.getString(
															R.string.status_button_clicked_text),
													getResources()
															.getString(
																	R.string.warning_button_positive_text))));
								}
							})
					.setNeutralButton(R.string.warning_button_neutral_text,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									statusView.setText(Html.fromHtml(String
											.format(getResources()
													.getString(
															R.string.status_button_clicked_text),
													getResources()
															.getString(
																	R.string.warning_button_neutral_text))));
								}
							})
					.setNegativeButton(R.string.warning_button_negative_text,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									statusView.setText(Html.fromHtml(String
											.format(getResources()
													.getString(
															R.string.status_button_clicked_text),
													getResources()
															.getString(
																	R.string.warning_button_negative_text))));
								}
							}).create();
		case DIALOG_TIME:
			Date currDate = new Date();
			OnTimeSetListener otsl = new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					statusView.setText(Html.fromHtml(String.format(
							getResources().getString(
									R.string.status_time_selected_text),
							hourOfDay, minute)));
				}
			};
			TimePickerDialog tpd = new TimePickerDialog(DialogeActivity.this,
					otsl, currDate.getHours(), currDate.getMinutes(), true);
			return tpd;
		}
		return null;
	}

	/**
	 * This method is called when the sending activity has finished, with the
	 * result it supplied.
	 * 
	 * @param requestCode
	 *            The original request code as given to startActivity().
	 * @param resultCode
	 *            From sending activity as per setResult().
	 * @param data
	 *            From sending activity as per setResult().
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULT_DIRECTION_GET) {
			// This is a standard resultCode that is sent back if the
			// activity doesn't supply an explicit result. It will also
			// be returned if the activity failed to launch.
			if (resultCode == RESULT_CANCELED) {
				// Our protocol with the sending activity is that it will send
				// text in 'data' as its result.
			} else {
				statusView.setText(Html.fromHtml(String.format(getResources()
						.getString(R.string.status_direction_selected_text),
						data.getAction())));
			}
		} else if (requestCode == RESULT_TIME_GET) {
			if (resultCode == RESULT_OK) {
				statusView.setText(Html.fromHtml(String.format(getResources()
						.getString(R.string.status_time_showed_text), data
						.getAction())));
			}
		}
	}
}