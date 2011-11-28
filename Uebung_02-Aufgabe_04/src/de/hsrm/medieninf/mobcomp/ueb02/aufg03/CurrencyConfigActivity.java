package de.hsrm.medieninf.mobcomp.ueb02.aufg03;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CurrencyConfigActivity extends Activity {

	ListView currenciesListView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		CurrencyDbAdapter dba = new CurrencyDbAdapter(this);
		Cursor currencyList = dba.getCurrencies();
		startManagingCursor(currencyList);
		
		setContentView(R.layout.currencies);

		currenciesListView = (ListView) findViewById(R.id.currencies_listview);
		currenciesListView.setOnItemClickListener(new OnItemClickListener() {

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