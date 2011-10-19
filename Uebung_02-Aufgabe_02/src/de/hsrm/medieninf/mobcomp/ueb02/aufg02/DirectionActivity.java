package de.hsrm.medieninf.mobcomp.ueb02.aufg02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DirectionActivity extends Activity {

	ListView directionListView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.direction);

		directionListView = (ListView) findViewById(R.id.direction_listview);
		directionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				setResult(RESULT_OK, (new Intent()).setAction((String) parent
						.getAdapter().getItem(position)));
				finish();
			}
		});
	}
}