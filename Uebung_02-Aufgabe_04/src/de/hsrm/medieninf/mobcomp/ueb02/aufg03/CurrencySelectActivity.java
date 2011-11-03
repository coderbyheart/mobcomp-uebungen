package de.hsrm.medieninf.mobcomp.ueb02.aufg03;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class CurrencySelectActivity extends DbActivity {

	ListView currenciesListView;
	Cursor currencyList;
	int selectedCurrency;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.currency_list);

		currenciesListView = (ListView) findViewById(R.id.currencies_listview);
		currenciesListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int currencyId = (((Cursor) parent.getAdapter().getItem(
						position))).getInt(CurrencyDbAdapter.CURRENCY_COL_ID);
				startEditIntent(currencyId);
			}
		});
		registerForContextMenu(currenciesListView);

		((Button) findViewById(R.id.currency_add_button))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startNewIntent();
					}
				});

		((Button) findViewById(R.id.currency_done_button))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(CurrencySelectActivity.this,
								WaehrungsUmrechnerActivity.class));
					}
				});
	}

	private void startNewIntent() {
		startEditIntent(0);
	}

	private void startEditIntent(int currencyId) {
		Intent editIntent = new Intent(CurrencySelectActivity.this,
				CurrencyEditActivity.class);
		editIntent.putExtra(CurrencyDbAdapter.CURRENCY_KEY_ID, currencyId);
		startActivity(editIntent);
	}

	@Override
	public void onResume() {
		super.onResume();
		currencyList = dba.getCurrencies();
		startManagingCursor(currencyList);

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.currency_list_item, currencyList, new String[] {
						CurrencyDbAdapter.CURRENCY_KEY_NAME,
						CurrencyDbAdapter.CURRENCY_KEY_RATE,
						CurrencyDbAdapter.CURRENCY_KEY_SYMBOL }, new int[] {
						R.id.currency_list_item_name,
						R.id.currency_list_item_rate,
						R.id.currency_list_item_symbol });

		currenciesListView.setAdapter(adapter);
	}

	@Override
	public void onPause() {
		currencyList.close();
		super.onPause();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		Cursor c = (Cursor) currenciesListView.getAdapter().getItem(
				info.position);
		selectedCurrency = c.getInt(CurrencyDbAdapter.CURRENCY_COL_ID); 
		menu.setHeaderTitle(c.getString(CurrencyDbAdapter.CURRENCY_COL_NAME));
		getMenuInflater().inflate(R.menu.currency_menu, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ctx_delete:
			dba.remove(dba.getCurrency(selectedCurrency));
			currencyList.requery();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
}