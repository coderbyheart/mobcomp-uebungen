package de.hsrm.medieninf.mobcomp.ueb03.aufg02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.entity.Sheet;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.persistence.DbAdapter;
import de.hsrm.medieninf.mobcomp.ueb03.aufg02.persistence.Provider;

public class BullshitBingoActivity extends Activity {

	public static final int FIELDS_ASK_DIALOG = 1;

	ArrayList<TextView> wordViews;
	Map<TextView, Uri> wordUris;
	int numberOfWords = 9;
	Uri currentSheet;
	OnClickListener textViewClickListener;
	private ContentResolver cr;
	private TableLayout grid;

	private TextView fieldsInput;

	private Button okButton;

	private Dialog numberOfFieldsDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		cr = getContentResolver();

		textViewClickListener = new OnClickListener() {
			public void onClick(View v) {
				v.setBackgroundColor(R.color.green);
				v.setClickable(false);
				cr.update(wordUris.get((TextView) v), null, null, null);
			}
		};

		grid = (TableLayout) findViewById(R.id.grid);
		startBingo();
	}

	private void startBingo() {
		// createBingo();
		showDialog(FIELDS_ASK_DIALOG);
	}

	private void createBingo() {
		grid.removeAllViews();
		wordViews = new ArrayList<TextView>();
		int rows = getRows(numberOfWords);
		numberOfWords = rows * rows;
		grid.setWeightSum(rows);
		for (int i = 0; i < rows; i++) {
			TableRow row = (TableRow) getLayoutInflater().inflate(
					R.layout.grid_row, grid, false);
			row.setWeightSum(rows);
			grid.addView(row);
			for (int j = 0; j < rows; j++) {
				TextView cell = (TextView) getLayoutInflater().inflate(
						R.layout.grid_cell, row, false);
				row.addView(cell);
				wordViews.add(cell);
				cell.setOnClickListener(textViewClickListener);
			}
		}

		Uri createSheetUri = Provider.CONTENT_URI.buildUpon()
				.appendPath("sheet").build();
		ContentValues cv = new ContentValues();
		cv.put(DbAdapter.SHEET_KEY_NWORDS, numberOfWords);
		currentSheet = cr.insert(createSheetUri, cv);

		// Folie holen
		Cursor sheetCursor = cr.query(currentSheet, null, null, null, null);
		Sheet sheet = new Sheet();
		sheet.setId(sheetCursor.getInt(DbAdapter.SHEET_COL_ID));
		sheet.setNumberOfWords(sheetCursor.getInt(DbAdapter.SHEET_COL_NWORDS));
		sheet.setCreationTime(sheetCursor
				.getString(DbAdapter.SHEET_COL_CREATION_TIME));

		// Wörter der Folie holen
		wordUris = new HashMap<TextView, Uri>();
		Uri wordUrl = currentSheet.buildUpon().appendPath("word").build();
		Log.v(getClass().getCanonicalName(), wordUrl.toString());
		Cursor wordCursor = cr.query(wordUrl, null, null, null, null);
		if (wordCursor.moveToFirst()) {
			int p = 0;
			do {
				TextView tv = wordViews.get(p++);
				tv.setText(wordCursor.getString(DbAdapter.WORD_COL_WORD));
				wordUris.put(
						tv,
						Provider.CONTENT_URI
								.buildUpon()
								.appendPath("word")
								.appendPath(
										""
												+ wordCursor
														.getInt(DbAdapter.WORD_COL_ID))
								.build());
			} while (wordCursor.moveToNext());
		}
	}

	private int getRows(int numberOfWords) {
		return Double.valueOf(Math.ceil(Math.sqrt(numberOfWords)))
				.intValue();
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
		case R.id.restart:
			startBingo();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Wenn ein Dialog angezeigt wird
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case FIELDS_ASK_DIALOG:
			numberOfFieldsDialog = new Dialog(this);
			numberOfFieldsDialog.setTitle(R.string.ask_fields);
			numberOfFieldsDialog.setContentView(R.layout.ask_fields_dialog);
			fieldsInput = (TextView) numberOfFieldsDialog.findViewById(R.id.fieldsText);
			fieldsInput.setText(""+numberOfWords);
			SeekBar fieldsSeek = (SeekBar) numberOfFieldsDialog.findViewById(R.id.fieldsSeekBar);
			fieldsSeek.setProgress(numberOfWords);
			Uri infoUri = Provider.CONTENT_URI.buildUpon()
					.appendPath("info").build();
			Cursor infoCursor = cr.query(infoUri, null, null, null, null);
			okButton = (Button) numberOfFieldsDialog.findViewById(R.id.okButton);
			if (infoCursor.moveToFirst()) {
				fieldsSeek.setMax(infoCursor.getInt(DbAdapter.INFO_COL_NUMWORDS));
			}
			fieldsSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					fieldsInput.setText(""+progress);
					numberOfWords = progress;
					okButton.setEnabled(
						numberOfWords > 0
						&& getRows(numberOfWords) * getRows(numberOfWords) == numberOfWords 
					);
				}
				public void onStartTrackingTouch(SeekBar seekBar) {
				}
				public void onStopTrackingTouch(SeekBar seekBar) {
				}
			});
			okButton.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					createBingo();
					numberOfFieldsDialog.dismiss();
				}
			});
			return numberOfFieldsDialog;
		default:
			return super.onCreateDialog(id);
		}
	}
}