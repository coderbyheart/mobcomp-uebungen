package de.hsrm.medieninf.mobcomp.ueb02.aufg03;

import java.security.InvalidParameterException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CurrencyEditActivity extends DbActivity {
	/** Called when the activity is first created. */

	EditText name, symbol, rate;
	int currencyId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.currency_edit);
		currencyId = getIntent().getExtras().getInt(
				CurrencyDbAdapter.CURRENCY_KEY_ID);

		name = (EditText) findViewById(R.id.currency_name_edittext);
		rate = (EditText) findViewById(R.id.currency_rate_edittext);
		symbol = (EditText) findViewById(R.id.currency_symbol_edittext);

		((Button) findViewById(R.id.currency_edit_button))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									Currency c = new Currency();
									c.setId(currencyId);
									c.setName(name.getText().toString());
									c.setRate(Double.valueOf(rate.getText()
											.toString()));
									c.setSymbol(symbol.getText().toString());
									c = dba.persist(c);
									startActivity(new Intent(
											CurrencyEditActivity.this,
											CurrencySelectActivity.class));
								} catch(InvalidParameterException e) {
									Toast.makeText(CurrencyEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
								}
							}
						});
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
		Currency c = dba.getCurrency(currencyId);
		if (c == null) {
			c = new Currency();
		}
		name.setText(c.getName());
		rate.setText(String.valueOf(c.getRate()));
		symbol.setText(c.getSymbol());
	}
}