package de.hsrm.medieninf.mobcomp.ueb03.aufg01;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.persistence.Highscore;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.persistence.HighscoreDbAdapter;

public class HighscoreActivity extends Activity {

	private TableLayout table;
	private LayoutInflater inflater;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscorelist);

		table = (TableLayout) findViewById(R.id.highscore_table);
		inflater = getLayoutInflater();
	}

	@Override
	public void onResume() {
		super.onResume();
		
		HighscoreDbAdapter dba = new HighscoreDbAdapter(this);
		dba.open();

		for (Highscore hs : dba.getHighscores()) {
			TableRow hsTableRow = (TableRow) inflater.inflate(
					R.layout.highscorerow, table, false);

			TextView cntry = (TextView) hsTableRow
					.findViewById(R.id.highscore_ntries_textview);
			TextView cname = (TextView) hsTableRow
					.findViewById(R.id.highscore_name_textview);
			TextView cdate = (TextView) hsTableRow
					.findViewById(R.id.highscore_date_textview);

			cntry.setText(String.valueOf(hs.getTries()));
			cname.setText(hs.getName());
			cdate.setText(hs.getTimestamp());

			table.addView(hsTableRow);
		}

		dba.close();
	}
}