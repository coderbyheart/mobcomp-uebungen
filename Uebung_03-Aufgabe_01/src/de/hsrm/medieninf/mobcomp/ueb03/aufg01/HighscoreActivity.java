package de.hsrm.medieninf.mobcomp.ueb03.aufg01;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.entity.Highscore;
import de.hsrm.medieninf.mobcomp.ueb03.aufg01.persistence.HighscoreDbAdapter;

public class HighscoreActivity extends Activity {

	private TableLayout table;
	private LayoutInflater inflater;
	private String sort = HighscoreDbAdapter.KEY_TRIES;
	private LinearLayout sortTriesCell, sortTimeCell; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscorelist);

		table = (TableLayout) findViewById(R.id.highscore_table);
		inflater = getLayoutInflater();
		
		sortTriesCell = (LinearLayout)findViewById(R.id.tablerow_sort_tries_cell);
		sortTimeCell = (LinearLayout)findViewById(R.id.tablerow_sort_time_cell);
		
		sortTriesCell.setClickable(true);
		sortTimeCell.setClickable(true);
		
		sortTriesCell.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setSort(HighscoreDbAdapter.KEY_TRIES);
				sortTriesCell.findViewById(R.id.tablerow_sort_tries_icon).setVisibility(View.VISIBLE);
				sortTimeCell.findViewById(R.id.tablerow_sort_time_icon).setVisibility(View.INVISIBLE);
			}
		});
		
		sortTimeCell.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setSort(HighscoreDbAdapter.KEY_TIME);
				sortTimeCell.findViewById(R.id.tablerow_sort_time_icon).setVisibility(View.VISIBLE);
				sortTriesCell.findViewById(R.id.tablerow_sort_tries_icon).setVisibility(View.INVISIBLE);
			}
		});
	}
	
	private void setSort(String sort)
	{
		if (sort.equals(this.sort)) return;
		this.sort = sort;
		updateHighscore();
	}

	@Override
	public void onResume() {
		super.onResume();
		updateHighscore();
	}
	
	private void updateHighscore()
	{
		HighscoreDbAdapter dba = new HighscoreDbAdapter(this);
		dba.open();
		
		table.removeViews(1, table.getChildCount() - 1);

		for (Highscore hs : dba.getHighscores(sort)) {
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
			cdate.setText(String.valueOf(Math.round(hs.getTime() / 1000.0)) + "s");

			table.addView(hsTableRow);
		}

		dba.close();
	}
}