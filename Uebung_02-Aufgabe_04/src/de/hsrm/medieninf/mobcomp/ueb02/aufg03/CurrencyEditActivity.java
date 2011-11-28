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
									Intent selectIntent = new Intent(
											CurrencyEditActivity.this,
											CurrencySelectActivity.class);
									/**
									 * If set, and the activity being launched is already running in the current task, 
									 * then instead of launching a new instance of that activity, all of the other 
									 * activities on top of it will be closed and this Intent will be delivered to 
									 * the (now on top) old activity as a new Intent.
									 * 
									 * For example, consider a task consisting of the activities: A, B, C, D.
									 * If D calls startActivity() with an Intent that resolves to the component of 
									 * activity B, then C and D will be finished and B receive the given Intent, 
									 * resulting in the stack now being: A, B.
									 * 
									 * The currently running instance of activity B in the above example will either 
									 * receive the new intent you are starting here in its onNewIntent() method, or 
									 * be itself finished and restarted with the new intent. If it has declared its 
									 * launch mode to be "multiple" (the default) and you have not set 
									 * FLAG_ACTIVITY_SINGLE_TOP in the same intent, then it will be finished and 
									 * re-created; for all other launch modes or if FLAG_ACTIVITY_SINGLE_TOP is set 
									 * then this Intent will be delivered to the current instance's onNewIntent().
									 * 
									 * This launch mode can also be used to good effect in conjunction with 
									 * FLAG_ACTIVITY_NEW_TASK: if used to start the root activity of a task, it will 
									 * bring any currently running instance of that task to the foreground, and then 
									 * clear it to its root state. This is especially useful, for example, when 
									 * launching an activity from the notification manager. 
									 */
									selectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(selectIntent);
									finish();
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